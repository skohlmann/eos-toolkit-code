/* Copyright (c) 2008 Sascha Kohlmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos.hadoop.mapred.entity;

import static net.sf.eos.entity.DictionaryBasedEntityRecognizer.ENTITY_ID_KEY;
import static net.sf.eos.entity.EntityRecognizer.ENTITY_TYPE;
import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.document.EosDocument;
import net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer;
import net.sf.eos.entity.DictionaryBasedEntityRecognizer;
import net.sf.eos.hadoop.DistributedCacheStrategy;
import net.sf.eos.hadoop.FullyDistributedCacheStrategy;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;
import net.sf.eos.trie.AbstractTrieLoader;
import net.sf.eos.trie.CharSequenceKeyAnalyzer;
import net.sf.eos.trie.PatriciaTrie;
import net.sf.eos.trie.Trie;
import net.sf.eos.trie.TrieLoader;
import net.sf.eos.trie.PatriciaTrie.KeyAnalyzer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class DictionaryBasedEntityRecognizerMapper
        extends EosDocumentSupportMapReduceBase
        implements Mapper<LongWritable, Text, Text, Text> {

    /** For logging. */
    private static final Logger LOG =
        Logger.getLogger(DictionaryBasedEntityRecognizerMapper.class.getName());

    private JobConf conf;

    private Trie<CharSequence, Set<CharSequence>> entities = null;
    private DistributedCacheStrategy strategy =
        new FullyDistributedCacheStrategy();


    public void map(final LongWritable positionInFile,
                    final Text eosDoc,
                    final OutputCollector<Text, Text> outputCollector,
                    final Reporter reporter) throws IOException {

        try {
            final EosDocument doc = textToEosDocument(eosDoc);

            final Map<String, EosDocument> idMap =
                toEntityIdMap(doc);

            for (final Entry<String, EosDocument> entry : idMap.entrySet()) {
                final String key = entry.getKey();
                final EosDocument newdoc = entry.getValue();
                final Text newTextDoc = this.eosDocumentToText(newdoc);
                final Text keyText = new Text(key);
                outputCollector.collect(keyText, newTextDoc);
                reporter.incrCounter(Index.MAP, 1);
            }

        } catch (final EosException e) {
            reporter.incrCounter(Index.EOS_EXCEPTION, 1);
            final IOException te = new IOException(e.getMessage());
            te.initCause(e);
            throw te;
        } catch (final IOException e) {
            reporter.incrCounter(Index.IO_EXCEPTION, 1);
            throw e;
        } catch (final Exception e) {
            reporter.incrCounter(Index.OTHER_EXCEPTION, 1);
            final IOException te = new IOException(e.getMessage());
            te.initCause(e);
            throw te;
        }
    }

    final Map<String, EosDocument> toEntityIdMap(final EosDocument doc) 
            throws EosException {

        final CharSequence text = doc.getText();
        final DictionaryBasedEntityRecognizer dber =
            getDictionaryBasedEntityRecognizerForText(text);

        final List<Token> tokens = new ArrayList<Token>();
        Token t = null;
        while ((t = dber.next()) != null) {
            tokens.add(t);
        }

        final Map<String, List<Token>> mapToTokenList =
            new HashMap<String, List<Token>>();

        for (final Token token : tokens) {
            final String type = token.getType();
            if (ENTITY_TYPE.equals(type)) {
                final Map<String, List<String>> meta = token.getMeta();
                final List<String> ids = meta.get(ENTITY_ID_KEY);
                for (final String id :ids) {
                    if (! mapToTokenList.containsKey(id)) {
                        mapToTokenList.put(id, tokens);
                    }
                }
            }
        }

        final Map<String, EosDocument> mapToDocument =
            new HashMap<String, EosDocument>();

        for (final Entry<String, List<Token>> entry : mapToTokenList.entrySet())
        {
            final String key = entry.getKey();
            if (! mapToDocument.containsKey(key)) {
                assert this.conf != null;
                final Configuration lconf =
                    new HadoopConfigurationAdapter(this.conf);
                final TextBuilder builder =
                    TextBuilder.newInstance(lconf);

                final List<CharSequence> l = new ArrayList<CharSequence>();
                final List<Token> value = entry.getValue();
                for (final Token token : value) {
                    final String type = token.getType();
                    if (ENTITY_TYPE.equals(type)) {
                        final Map<String, List<String>> meta = token.getMeta();
                        final List<String> ids = meta.get(ENTITY_ID_KEY);
                        final List<CharSequence> idList =
                            new ArrayList<CharSequence>();
                        for (final String id :ids) {
                            if (! key.equals(id)) {
                                idList.add(id);
                            }
                        }
                        final int size = idList.size();
                        CharSequence[] css = new CharSequence[size];
                        css = idList.toArray(css);

                        final CharSequence in = builder.buildText(css);

                        l.add(in);
                    } else {
                        final CharSequence in = token.getTokenText();
                        l.add(in);
                    }
                }

                final int size = l.size();
                CharSequence[] css = new CharSequence[size];
                css = l.toArray(css);
                final CharSequence newText = builder.buildText(css);

                final EosDocument newDoc = new EosDocument();
                newDoc.setText(newText);
                final CharSequence title = doc.getTitle();
                newDoc.setTitle(title);
                
                final Map<String, List<String>> oldMeta = doc.getMeta();
                final Map<String, List<String>> newMap =
                    new HashMap<String, List<String>>(oldMeta);
                final List<String> newIdList = new ArrayList<String>();
                newIdList.add(key);
                newMap.put(EosDocument.ID_META_KEY, newIdList);
                newDoc.setMeta(newMap);
                mapToDocument.put(key, newDoc);
            }
        }

        return mapToDocument;
    }

    /**
     * Creates a new <code>DictionaryBasedEntityRecognizer</code> for the
     * given text. Uses the factory method of
     * {@link AbstractDictionaryBasedEntityRecognizer#newInstance(net.sf.eos.analyzer.Tokenizer, Configuration)}
     * to create a new instance. Use {@link #getTokenizer()} for the
     * <em>source</em>.
     * @param text the text to tokenize
     * @return a new instance
     */
    protected DictionaryBasedEntityRecognizer
            getDictionaryBasedEntityRecognizerForText(final CharSequence text) {
        try {
            LOG.fine("Initialize DictionaryBasedEntityRecognizer");

            assert this.conf != null;
            final Configuration lconf =
                new HadoopConfigurationAdapter(this.conf);
            final ResettableTokenizer tokenizer = getTokenizer();
            tokenizer.reset(text);

            final DictionaryBasedEntityRecognizer regconizer =
                AbstractDictionaryBasedEntityRecognizer.newInstance(tokenizer,
                                                                    lconf);
            final Trie<CharSequence, Set<CharSequence>> trie = getTrie();
            regconizer.setEntityMap(trie);
            final TextBuilder textBuilder = TextBuilder.newInstance(lconf);
            regconizer.setTextBuilder(textBuilder);

            return regconizer;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Configures the trie. After finishing the method {@link #getTrie()}.
     * Uses the value of {@link DistributedCacheStrategy#STRATEGY_CONFIG_NAME}
     * if setted to get the distributed cache strategy.
     * never returns <code>null</code>.
     */
    protected void configureTrie() {
        synchronized(DictionaryBasedEntityRecognizerMapper.class) {
            try {
                assert this.conf != null;

                final String strageyClassName =
                    this.conf.get(DistributedCacheStrategy.STRATEGY_CONFIG_NAME);
                if (strageyClassName != null) {
                    try {
                        this.strategy = (DistributedCacheStrategy) 
                            Class.forName(strageyClassName).newInstance();
                    } catch (final InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (final IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (final ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }

                final Path[] recognizerDataFile =
                    this.strategy.distributedCachePathes((this.conf));
                LOG.info("strategy: "
                         + this.strategy.getClass().getCanonicalName());
                LOG.info("path: " + recognizerDataFile[0]);
                final InputStream in =
                    recognizerDataFile[0].toUri().toURL().openStream();

                final Configuration lconf =
                    new HadoopConfigurationAdapter(this.conf);
                final TrieLoader newInstance =
                    AbstractTrieLoader.newInstance(lconf);
                final TrieLoader<CharSequence, Set<CharSequence>> loader = 
                    newInstance;

                final KeyAnalyzer<CharSequence> analyzer =
                    new CharSequenceKeyAnalyzer();
                this.entities = 
                    new PatriciaTrie<CharSequence, Set<CharSequence>>(analyzer);
                loader.loadTrie(in, this.entities);
            } catch (final Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Returns a <code>Tokenizer</code> as <em>source</em> for the
     * recognizer.
     * @return the <em>source</em> for the recognizer
     * @throws TokenizerException if an error occurs
     */
    protected ResettableTokenizer getTokenizer() throws TokenizerException {

        assert this.conf != null;

        final Configuration lconf = new HadoopConfigurationAdapter(this.conf);
        final TokenizerBuilder tokenBuilder =
            TokenizerBuilder.newInstance(lconf);
        final ResettableTokenizer tokenizer =
            tokenBuilder.newResettableTokenizer();

        return tokenizer;
    }

    /**
     * Returns a <code>Trie</code> instance. See contract in
     * {@link #configureTrie()}
     * @return a <code>Trie</code> instance
     */
    protected Trie<CharSequence, Set<CharSequence>> getTrie() {
        assert this.entities != null;
        return this.entities;
    }

    /**
     * Sets the configuration and calls {@link #configureTrie()}
     * @param conf the configuration
     */
    @Override
    public void configure(@SuppressWarnings("hiding") final JobConf conf) {
        super.configure(conf);
        this.conf = conf;
        configureTrie();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
