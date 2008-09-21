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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class ByteArrayKeyAnalyzerTest {

    @Test
    public void byteArrayTest() {
        final PatriciaTrie<byte[], Integer> trie =
            new PatriciaTrie<byte[], Integer>(new ByteArrayKeyAnalyzer());
        final Map<Integer, byte[]>  testMap = new HashMap<Integer, byte[]>();
        int i = 0;
        for (byte b1 = Byte.MIN_VALUE; b1 < Byte.MAX_VALUE; b1++) {
            for (byte b2 = Byte.MIN_VALUE; b2 < Byte.MAX_VALUE; b2++, i++) {
                final byte[] array = new byte[] {b1, b2};
                trie.put(array, i);
                testMap.put(i, array);
            }
            i++;
            final byte[] array = new byte[] {b1, Byte.MAX_VALUE};
            trie.put(array, i);
            testMap.put(i, array);
        }
        for (byte b2 = Byte.MIN_VALUE; b2 < Byte.MAX_VALUE; b2++, i++) {
            final byte[] array = new byte[] {Byte.MAX_VALUE, b2};
            trie.put(array, i);
            testMap.put(i, array);
        }
        i++;
        final byte[] array = new byte[] {Byte.MAX_VALUE, Byte.MAX_VALUE};
        trie.put(array, i);
        testMap.put(i, array);

//        assertEquals(trie.size(), testMap.size());

        int count = 0;
        for (final Entry<Integer, byte[]> e : testMap.entrySet()) {
            final byte[] key = e.getValue();
            final Integer value = e.getKey();
//            System.out.println("count: " + count);
            count++;
//            assertEquals(value, trie.get(key));
        }
    }
}
