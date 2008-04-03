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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import static net.sf.eos.entity.DictionaryBasedEntityRecognizer.ENTITY_ID_KEY;
import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.document.EosDocument;
import net.sf.eos.document.Serializer;
import net.sf.eos.document.XmlSerializer;
import net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer;
import net.sf.eos.entity.DictionaryBasedEntityRecognizer;
import net.sf.eos.entity.EntityRecognizer;
import net.sf.eos.entity.SimpleLongestMatchDictionaryBasedEntityRecognizer;
import net.sf.eos.hadoop.DistributedCacheStrategy;
import net.sf.eos.hadoop.TestDistributedCacheStrategy;
import net.sf.eos.medline.MedlineTokenizerBuilder;
import net.sf.eos.trie.AbstractTrieLoader;
import net.sf.eos.trie.Trie;
import net.sf.eos.trie.XmlTrieLoader;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SimpleDictionaryBasedEntityRecognizerMapperTest {

    private ExtDictionaryBasedEntityRecognizerMapper mapper = null;

//    @Test
//    public void testToEntityIdMap() throws Exception {
//        final String text = "a simple text with a first word and a "
//            + "second word end first word ";
//        final EosDocument doc = new EosDocument();
//        doc.setText(text);
//        doc.setTitle("irgendwas");
//
//        final Map<String, EosDocument> mapped = this.mapper.toEntityIdMap(doc);
//        assertEquals(2, mapped.size());
//
//        final EosDocument newDoc1 = mapped.get("urn:id:1");
//        assertNotNull(newDoc1);
//        assertEquals("irgendwas", newDoc1.getTitle());
//        assertEquals("a simple text with a  and a urn:id:2 end ",
//                     newDoc1.getText());
//        assertEquals("urn:id:1",
//                     newDoc1.getMeta().get(EosDocument.ID_META_KEY).get(0));
//
//        final EosDocument newDoc2 = mapped.get("urn:id:2");
//        assertNotNull(newDoc2);
//        assertEquals("irgendwas", newDoc2.getTitle());
//        assertEquals("a simple text with a urn:id:1 and a  end urn:id:1",
//                     newDoc2.getText());
//        assertEquals("urn:id:2",
//                     newDoc2.getMeta().get(EosDocument.ID_META_KEY).get(0));
//    }

//    @Test
//    public void checkGetDictionaryBasedEntityRecognizerForText() throws Exception
//    {
//        final String first = "a simple text with a first word and a "
//                             + "second word end ";
//        final DictionaryBasedEntityRecognizer recognizer =
//            this.mapper.getDictionaryBasedEntityRecognizerForText(first);
//        assertTrue(recognizer instanceof SimpleLongestMatchDictionaryBasedEntityRecognizer);
//
//        final List<Token> tokens = new ArrayList<Token>();
//        Token t = null;
//        while ((t = recognizer.next()) != null) {
//            tokens.add(t);
//        }
//
//        final Token firstToken = tokens.get(1);
//        assertFalse(EntityRecognizer.ENTITY_TYPE.equals(firstToken.getType()));
//        
//        final Token fivthToken = tokens.get(5);
//        assertEquals("first word", fivthToken.getTokenText());
//        assertTrue(EntityRecognizer.ENTITY_TYPE.equals(fivthToken.getType()));
//        final Map<String, List<String>> fivthMeta = fivthToken.getMeta();
//        assertEquals("urn:id:1", fivthMeta.get(ENTITY_ID_KEY).get(0));
//
//        final Token eigthToken = tokens.get(8);
//        assertEquals("second word", eigthToken.getTokenText());
//        assertTrue(EntityRecognizer.ENTITY_TYPE.equals(eigthToken.getType()));
//        final Map<String, List<String>> eigthMeta = eigthToken.getMeta();
//        assertEquals("urn:id:2", eigthMeta.get(ENTITY_ID_KEY).get(0));
//
//        final Token ninethToken = tokens.get(9);
//        assertFalse(EntityRecognizer.ENTITY_TYPE.equals(ninethToken.getType()));
//    }

    @Test
    public void validTrie() {
        final Trie<CharSequence, Set<CharSequence>> trie =
            this.mapper.getTrie();
        assertEquals(2, trie.size());
    }

    @Test
    public void validResettableTokenizer() throws TokenizerException {
        final Tokenizer tokenizer = this.mapper.getTokenizer();
        assertTrue(tokenizer instanceof ResettableTokenizer);
    }

    @Test
    public void validSerializer() throws EosException {
        final Serializer serializer = this.mapper.getSerializer();
        assertTrue(serializer.getClass() == XmlSerializer.class);
    }

    @Test
    public void validDeserialize() throws Exception {
        final String s = "<d><m><k>1</k><v>a</v><v>b</v></m><ti>title</ti><te>text</te></d>";
        final Text text = new Text(s);
        final EosDocument doc = this.mapper.textToEosDocument(text);

        assertEquals("title", doc.getTitle());
        assertEquals("text", doc.getText());
        assertEquals("b", doc.getMeta().get("1").get(1));
    }

    @Test
    public void validSerialize() throws Exception {
        final EosDocument doc = new EosDocument();
        doc.setText("Text");
        doc.setTitle("Title");
        final List<String> metaValue = new ArrayList<String>();
        metaValue.add("value1");
        metaValue.add("value2");
        final Map<String, List<String>> meta =
            new HashMap<String, List<String>>();
        meta.put("key1", metaValue);
        doc.setMeta(meta);

        final Text text = this.mapper.eosDocumentToText(doc);
        final EosDocument newDoc = this.mapper.textToEosDocument(text);

        assertEquals(doc, newDoc);
    }

    @Before
    public void createDictionaryBasedEntityRecognizerMapperInstace() {
        final String TRIEX_DAT = "simple.triex";

        final Thread currentThread = Thread.currentThread();
        final ClassLoader loader = currentThread.getContextClassLoader();
        final URL resource = loader.getResource(TRIEX_DAT);

        final String path = resource.getPath();
        final int lastIndexOf = path.lastIndexOf("/");
        final String LOCAL_PATH = path.substring(0, lastIndexOf);

        final JobConf conf = new JobConf();
        conf.set(Serializer.SERIALIZER_IMPL_CONFIG_NAME,
                 XmlSerializer.class.getName());
        conf.set(TokenizerBuilder.BUILDER_IMPL_CONFIG_NAME,
                 MedlineTokenizerBuilder.class.getName());
        conf.set(AbstractTrieLoader.TRIE_LOADER_IMPL_CONFIG_NAME,
                 XmlTrieLoader.class.getName());
        conf.set(AbstractDictionaryBasedEntityRecognizer
                    .ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME,
                 SimpleLongestMatchDictionaryBasedEntityRecognizer
                     .class.getName());

        DistributedCache.addCacheFile(new Path(LOCAL_PATH, TRIEX_DAT).toUri(),
                                      conf);
        conf.set(DistributedCacheStrategy.STRATEGY_CONFIG_NAME,
                 TestDistributedCacheStrategy.class.getName());

        final ExtDictionaryBasedEntityRecognizerMapper mapper =
            new ExtDictionaryBasedEntityRecognizerMapper();
        mapper.configure(conf);

        this.mapper = mapper;
    }

    private static final class ExtDictionaryBasedEntityRecognizerMapper
        extends DictionaryBasedEntityRecognizerMapper {
        @Override
        public Trie<CharSequence, Set<CharSequence>> getTrie() {
            return super.getTrie();
        }
        @Override
        public ResettableTokenizer getTokenizer() throws TokenizerException {
            return super.getTokenizer();
        }
        @Override
        public Serializer getSerializer() throws EosException {
            return super.getSerializer();
        }
        @Override
        public EosDocument textToEosDocument(final Text eosDoc)
            throws Exception, IOException {
            return super.textToEosDocument(eosDoc);
        }
        @Override
        public Text eosDocumentToText(final EosDocument doc)
            throws IOException, Exception {
            return super.eosDocumentToText(doc);
        }
//        @Override
//        public DictionaryBasedEntityRecognizer
//                getDictionaryBasedEntityRecognizerForText(final CharSequence cs)
//        {
//            return super.getDictionaryBasedEntityRecognizerForText(cs);
//        }
    }
}
