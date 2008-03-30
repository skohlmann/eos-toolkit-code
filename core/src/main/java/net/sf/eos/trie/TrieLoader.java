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

import java.io.InputStream;

/**
 * Implementations creats new tries.
 * @author Sascha Kohlmann
 */
public interface TrieLoader<K, V> {

    /**
     * Creates a <code>Trie</code> from the <code>InputStream</code>.
     * @param trieData stream pointing to the trie structure data.
     * @param trie the trie to fill with the <em>trieData</em>
     * @throws Exception if the builder is unable to create the trie.
     */
    void loadTrie(final InputStream trieData,
                  final Trie<K, V> trie) throws Exception;
}
