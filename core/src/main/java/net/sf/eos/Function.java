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
package net.sf.eos;


/**
 * A Function provides a transformation on an object and returns the resulting
 * object. For example, a {@code StringToIntegerFunction} may implement
 * {@literal Function<String,Integer>} and transform integers in {@link String}
 * format to {@link Integer} format.</p>
 *
 * <p>The transformation on the source object does not necessarily result in
 * an object of a different type. For example, a
 * {@code MeterToFeetFunction} may implement
 * {@literal Function<Integer, Integer>}.</p>
 *
 * <p>Implementations which may cause side effects upon evaluation are strongly
 * encouraged to state this fact clearly in their API documentation.</p>
 *
 * <p><strong>Note:</strong> experimental - inspired by <em>guice</em></p>
 *
 * @author Sascha Kohlmann
 * @since 0.1.0
 * @param <F> the <em>from</em> type to handle in the function
 * @param <T> the <em>to</em> type results from {@link #apply}
 */
@Experimental
public interface Function<F, T> {

    /**
     * Applies the function to an object of type {@code F}, resulting in an object
     * of type {@code T}. Note that types {@code F} and {@code T} may or may not
     * be the same.
     * @param from The source object.
     * @return The resulting object.
     */
    T apply(@Nullable F from);

    /**
     * Indicates whether some other object is equal to this {@code Function}.
     * This method can return {@code true} <em>only</em> if the specified object
     * is also a {@code Function} and, for every input object {@code o}, it
     * returns exactly the same value.  Thus, {@code function1.equals(function2)}
     * implies that either {@code function1.apply(o)} and
     * {@code function1.apply(o)} are both {@code null}, or
     * {@code function1.apply(o).equals(function2.apply(o))}.</p>
     *
     * <p>Note that it is <em>always</em> safe <em>not</em> to override
     * {@code Object.equals(Object)}.</p>
     */
    boolean equals(Object obj);
}
