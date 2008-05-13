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
package net.sf.eos.hadoop.mapred;

import net.sf.eos.EosException;
import net.sf.eos.document.EosDocument;

import org.apache.hadoop.io.WritableComparable;

import java.util.Map;

/**
 * The key generator creates a key for the given document. The advantage is
 * the support for replaceable strategies in key generation.
 * <p>Implementations must be stateless.</p>
 * @author Sascha Kohlmann
 * @param <K> the type of the key in a mapreduce environment
 */
public interface KeyGenerator<K extends WritableComparable> {

    /**
     * Creates a new key.
     * @param doc the document to create a key for
     * @return the key
     * @throws EosException if an error occurs
     */
    Map<K, EosDocument> createKeysForDocument(final EosDocument doc)
        throws EosException;
}
