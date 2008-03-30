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
package net.sf.eos.trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SaxTrieLoaderTest {

//    @Test
//    public void simpleBuilder() throws Exception {
//        final String xml = "<?xml version='1.0' encoding='UTF-8'?>"
//                           + "<trie><entry><key>key</key><value>value</value></entry></trie>";
//        final InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//
//        final TrieLoader builder = new XmlTrieLoader();
//        final Trie<byte[], Set<byte[]>> trie =
//            new PatriciaTrie<byte[], Set<byte[]>>(new ByteArrayKeyAnalyzer());
//        builder.loadTrie(in, trie);
//
//        assertTrue(1 == trie.get("key".getBytes()).size());
//    }
//
//    @Test
//    public void twoKeys() throws Exception {
//        final String xml = "<?xml version='1.0' encoding='UTF-8'?>"
//                           + "<trie><entry><key>key</key><value>value</value></entry>" 
//                           + "<entry><key>key2</key><value>value2</value></entry></trie>";
//        final InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//
//        final TrieLoader builder = new XmlTrieLoader();
//        final Trie<byte[], Set<byte[]>> trie =
//            new PatriciaTrie<byte[], Set<byte[]>>(new ByteArrayKeyAnalyzer()); 
//        builder.loadTrie(in, trie);
//
//        assertTrue(1 == trie.get("key".getBytes()).size());
//        assertTrue(1 == trie.get("key2".getBytes()).size());
//    }
//
//    @Test
//    public void twoSameKeysWithDifferentValue() throws Exception {
//        final String xml = "<?xml version='1.0' encoding='UTF-8'?>"
//                           + "<trie><entry><key>key</key><value>value</value></entry>"
//                           + "<entry><key>key</key><value>value2</value></entry></trie>";
//        final InputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
//
//        final TrieLoader builder = new XmlTrieLoader();
//        final Trie<byte[], Set<byte[]>> trie =
//            new PatriciaTrie<byte[], Set<byte[]>>(new ByteArrayKeyAnalyzer()); 
//        builder.loadTrie(in, trie);
//
//        assertEquals(2, trie.get("key".getBytes()).size());
//        final List<byte[]> list = 
//            new ArrayList<byte[]>(trie.get("key".getBytes()));
//        final List<String> sList = new ArrayList<String>();
//        for (final byte[] seq : list) {
//            sList.add("" + seq);
//        }
//        Collections.sort(sList);
//        assertEquals("value".getBytes(), list.get(0));
//        assertEquals("value2".getBytes(), list.get(1));
//    }
    
//    @Test
//    public void simpleLoadTest() throws Exception {
//        final InputStream in = new FileInputStream("/Users/sk/eos.triex"); 
//        final TrieLoader builder = new XmlTrieLoader();
//        final Trie<byte[], Set<byte[]>> trie =
//            new PatriciaTrie<byte[], Set<byte[]>>(new ByteArrayKeyAnalyzer());
//        try {
//            builder.loadTrie(in, trie);
//        } catch(final OutOfMemoryError e) {
//            e.printStackTrace();
//        }
//    }
    
    @Test
    public void dummy() { }
}
