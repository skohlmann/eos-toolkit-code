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


/**
 * <p>Contains the base structure for memory based entity recognition.
 * The trie based on an <a href='http://en.wikipedia.org/wiki/PATRICIA_Trie' title='Wikipedia'>PATRICIA</a>
 * implementation of the <a href='http://www.limewire.org/' title='Homepage'>Limewire</a>
 * project. The implementation comes under the terms of version 3 of the
 * <a href='http://www.gnu.org/licenses/gpl.txt' title='License Homepage'>GNU General Public License (GPL)</a>.
 * </p>
 *
 * <p>The main benefit for a memory based implementation for entity
 * recognition ist the cluster structure of the
 * <a href='http://hadoop.apache.org/' title='Homepage'>Hadoop</a> system.
 * In such a system it is contra productive to have a central instance for
 * entity recognition. Such a central system is always the bottleneck if it is
 * under fire of a few hundrets of cluster node, each with X running instances.
 * A PATRICIA trie structure consumes not as much main memory as other
 * implementations.</p>
 *
 * <p>To work with the trie in a cluster environment, use the service offered by
 * {@link net.sf.eos.trie.AbstractTrieLoader}. The default serialization format
 * is defined in {@link net.sf.eos.trie.XmlTrieLoader}. At this time the tries
 * key structure is based on {@link net.sf.eos.trie.CharSequenceKeyAnalyzer CharSequences}.
 * This implementation is not as memory optimized as the
 * {@linkplain net.sf.eos.trie.ByteArrayKeyAnalyzer byte array}
 * implementation. The byte array oriented key analyzer may use
 * {@link java.lang.CharSequence CharSequences} transformed in UTF-8 bytes.
 * This safes memory for latin based languages.</p>
 *
 * <p>For Hadoop use the distributed cache mechanism of Hadoop. See
 * {@link net.sf.eos.hadoop} for further information.</p>
 *
 * @since 0.1.0
 * @see net.sf.eos.hadoop
 * @see net.sf.eos.entity
 * @see net.sf.eos.hadoop.mapred.cooccurrence
 * @author Sascha Kohlmann
 */
package net.sf.eos.trie;
