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
package net.sf.eos.hadoop.mapred.cooccurrence;

import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.TokenizerSupplier;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.config.Service;
import net.sf.eos.config.Services;
import net.sf.eos.document.EosDocument;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@Services(
    services={
        @Service(
            factory=TokenizerSupplier.class,
            description="Tokenizer for coocurence analyzing."
        ),
        @Service(
            factory=AbstractTrieLoader.class,
            description="Trie contains the look up data for cooccurance analyzis."
        )
    }
)
public class DictionaryBasedEntityRecognizerMapper
        extends EosDocumentSupportMapReduceBase
        implements Mapper<LongWritable, Text, Text, Text> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DictionaryBasedEntityRecognizerMapper.class.getName());

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
            final DictionaryBasedEntityIdKeyGenerator generator =
                new DictionaryBasedEntityIdKeyGenerator();
            final Configuration lConf = new Configuration();
            HadoopConfigurationAdapter.addHadoopConfigToEosConfig(conf, lConf);
            generator.configure(lConf);

            final Trie<CharSequence, Set<CharSequence>> lTrie = getTrie();
            generator.setTrie(lTrie);

            final Map<Text, EosDocument> idMap =
                generator.createKeysForDocument(doc); 

            for (final Entry<Text, EosDocument> entry : idMap.entrySet()) {
                final Text key = entry.getKey();
                final EosDocument newdoc = entry.getValue();
                final Text newTextDoc = this.eosDocumentToText(newdoc);
                outputCollector.collect(key, newTextDoc);
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

    /**
     * Configures the trie. After finishing the method {@link #getTrie()}.
     * Uses the value of {@link DistributedCacheStrategy#STRATEGY_IMPL_CONFIG_NAME}
     * if setted to get the distributed cache strategy.
     */
    protected void configureTrie() {
        synchronized(DictionaryBasedEntityRecognizerMapper.class) {
            try {
                assert this.conf != null;

                final String strageyClassName =
                    this.conf.get(DistributedCacheStrategy.STRATEGY_IMPL_CONFIG_NAME);
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
     * Returns a {@code Tokenizer} as <em>source</em> for the
     * recognizer.
     * @return the <em>source</em> for the recognizer
     * @throws TokenizerException if an error occurs
     */
    protected ResettableTokenizer getTokenizer() throws TokenizerException {

        assert this.conf != null;

        try {
            final Configuration lconf = new HadoopConfigurationAdapter(this.conf);
            final TokenizerSupplier tokenBuilder =
                TokenizerSupplier.newInstance(lconf);
            final ResettableTokenizer tokenizer = tokenBuilder.get();

            return tokenizer;
        } catch (final Exception e) {
            throw new TokenizerException(e);
        }
    }

    /**
     * Returns a {@code Trie} instance. See contract in
     * {@link #configureTrie()}
     * @return a {@code Trie} instance
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
