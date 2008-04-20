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

import static net.sf.eos.entity.DictionaryBasedEntityRecognizer.ENTITY_ID_KEY;
import static net.sf.eos.entity.EntityRecognizer.ENTITY_TYPE;
import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.document.EosDocument;
import net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer;
import net.sf.eos.entity.DictionaryBasedEntityRecognizer;
import net.sf.eos.hadoop.mapred.AbstractKeyGenerator;
import net.sf.eos.hadoop.mapred.KeyGenerator;
import net.sf.eos.trie.Trie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;


public class DictionaryBasedEntityIdKeyGenerator extends Configured
/*        implements KeyGenerator<Text> */ {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DictionaryBasedEntityIdKeyGenerator.class.getName());

    private Trie<CharSequence, Set<CharSequence>> trie;

    public Map<Text, EosDocument> createKeysForDocument(final EosDocument doc)
            throws EosException {

        final CharSequence text = doc.getText();
        final DictionaryBasedEntityRecognizer dber =
            getDictionaryBasedEntityRecognizerForText(text);

        List<Token> tokens = tokens = identifiyToken(dber);

        final Map<String, List<Token>> mapToTokenList =
            new HashMap<String, List<Token>>();

        // Map for entity ID to all tokens with addition meta information.
        for (final Token token : tokens) {
            assert token != null;
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

        final Map<Text, EosDocument> mapToDocument =
            new HashMap<Text, EosDocument>();

        final Configuration lconf = getConfiguration();
        if (lconf.get(AbstractKeyGenerator.ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME)
                == null) {
            lconf.set(AbstractKeyGenerator.ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME,
                      IdMetadataKeyGenerator.class.getName());
        }

        final KeyGenerator<Text> generator =
            (KeyGenerator<Text>) AbstractKeyGenerator.newInstance(lconf);

        // Create new document for each entity ID. Remove enity ID from
        // document and replace charater sequence of the entity common- or
        // other name by the entity ID.
        for (final Entry<String, List<Token>> entry : mapToTokenList.entrySet())
        {
            final String key = entry.getKey();
            if (! mapToDocument.containsKey(key)) {
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

                final Map<Text, EosDocument> toStore =
                    generator.createKeysForDocument(newDoc);
                for (final Entry<Text, EosDocument> toStoreEntry
                        : toStore.entrySet()) {
                    final Text keyAsText = toStoreEntry.getKey();
                    final EosDocument toStoreDoc = toStoreEntry.getValue();
                    mapToDocument.put(keyAsText, toStoreDoc);
                }
            }
        }

        return mapToDocument;
    }

    final List<Token> identifiyToken(final DictionaryBasedEntityRecognizer dber)
            throws TokenizerException {

        final List<Token> tokens = new ArrayList<Token>();

        Token t = null;
        while ((t = dber.next()) != null) {
            tokens.add(t);
        }

        return tokens;
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
            LOG.debug("Initialize DictionaryBasedEntityRecognizer");

            final Configuration lconf = getConfiguration();
            final ResettableTokenizer tokenizer = getTokenizer();
            tokenizer.reset(text);

            final DictionaryBasedEntityRecognizer regconizer =
                AbstractDictionaryBasedEntityRecognizer.newInstance(tokenizer,
                                                                    lconf);
            final Trie<CharSequence, Set<CharSequence>> ltrie = getTrie();
            regconizer.setEntityMap(ltrie);
            final TextBuilder textBuilder = TextBuilder.newInstance(lconf);
            regconizer.setTextBuilder(textBuilder);

            return regconizer;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a <code>Tokenizer</code> as <em>source</em> for the
     * recognizer.
     * @return the <em>source</em> for the recognizer
     * @throws TokenizerException if an error occurs
     */
    protected ResettableTokenizer getTokenizer() throws TokenizerException {

        final Configuration conf = getConfiguration();

        final TokenizerBuilder tokenBuilder =
            TokenizerBuilder.newInstance(conf);
        final ResettableTokenizer tokenizer =
            tokenBuilder.newResettableTokenizer();

        return tokenizer;
    }

    public Trie<CharSequence, Set<CharSequence>> getTrie() {
        return this.trie;
    }

    public void setTrie(final Trie<CharSequence, Set<CharSequence>> trie) {
        this.trie = trie;
    }
}
