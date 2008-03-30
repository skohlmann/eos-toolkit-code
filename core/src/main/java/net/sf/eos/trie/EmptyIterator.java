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

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Provides an unmodifiable empty iterator. <code>EmptyIterator</code> always
 * returns that there aren't any more items and throws a 
 * {@link NoSuchElementException} when attempting to move to the next item.
 * 
 <pre>
    try{
        EmptyIterator ei = new EmptyIterator();     
        ei.next();      
    } catch (Exception e) {
        System.out.println("Expected to get NoSuchElementException exception: " + e.toString());
    }

    Output:
        Expected to get NoSuchElementException exception: java.util.NoSuchElementException
 </pre>
 */
public class EmptyIterator extends UnmodifiableIterator {
    /** A constant EmptyIterator. */
    public final static Iterator EMPTY_ITERATOR = new EmptyIterator();

    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    // inherits javadoc comment
    public boolean hasNext() {
        return false;
    }

    // inherits javadoc comment
    public Object next() {
        throw new NoSuchElementException();
    }
}
