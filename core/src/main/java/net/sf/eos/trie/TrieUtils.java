/* Taken 2008 from Limewire -project under the following terms: 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos.trie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.sf.eos.trie.Trie.Cursor;



/**
 * Miscellaneous utilities for Tries. See <a 
 * href="http://en.wikipedia.org/wiki/Trie">Trie</a> for more information.
 * <p><strong>Note:</strong> Taken from 
 * <a href='http://www.limewire.org/'>Limewire</a> sourcecode and repackaged by
 * Sascha Kohlmann.</p>
 */
public final class TrieUtils {
    
    private TrieUtils() {}
    
    public static <K,V> List<V> select(Trie<K,V> trie, K key, int count) {
        return select(trie, key, count, null);
    }
    
    public static <K,V> List<V> select(Trie<K,V> trie, K key, 
                int count, final Cursor<K,V> cursor) {
        
        final int size = Math.min(trie.size(), count);
        final List<V> values = new ArrayList<V>(size);
        
        trie.select(key, new Cursor<K,V>() {
            public Cursor.SelectStatus select(Entry<? extends K, ? extends V> entry) {
                if (cursor == null || cursor.select(entry) == Cursor.SelectStatus.EXIT) {
                    values.add(entry.getValue());
                }
                
                return values.size() >= size ? Cursor.SelectStatus.EXIT : Cursor.SelectStatus.CONTINUE;
            }
        });
        
        return values;
    }
}
