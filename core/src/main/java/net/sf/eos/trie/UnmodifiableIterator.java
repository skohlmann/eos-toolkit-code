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
 */package net.sf.eos.trie;

import java.util.Iterator;

/** 
 * A convenience class to aid in developing iterators that cannot be modified.
 */
public abstract class UnmodifiableIterator<E> implements Iterator<E> {
    /** Throws UnsupportedOperationException */
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
