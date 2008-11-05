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
package net.sf.eos.util;

/**
 * Provides static methods to simpler condition handling. This implies less
 * code.
 * <p><strong>Example</strong></p>
 * <pre>
 *     if (reference == null) {
 *         throw new IllegalArgumentException("reference is null");
 *     }
 *     this.value = reference;
 * </pre>
 * <p>will change to a simple single line:</p>
 * <pre>
 *     this.value = <em>checkArgumentNotNull</em>(reference, "reference is null");
 * </pre>
 * @author Sascha Kohlmann
 */
public final class Conditions {

    private Conditions() { }

    /**
     * Checks that the given reference is {@code null} and throws an
     * {@code IllegalArgumentException} if so.
     *
     * @param ref an object reference
     * @return the non-{@code null} reference
     * @throws IllegalArgumentException if {@code ref} is {@code null}
     */
    public static <T> T checkArgumentNotNull(final T ref) {
        if (ref == null) {
             throw new IllegalArgumentException();
        }
        return ref;
    }

    /**
     * Checks that the given reference is {@code null} and throws an
     * {@code IllegalArgumentException} if so.
     *
     * @param ref an object reference
     * @return the non-{@code null} reference
     * @throws IllegalArgumentException if {@code ref} is {@code null}
     */
    public static <T> T checkArgumentNotNull(final T ref,
                                             final Object errorMessage) {
        if (ref == null) {
             throw new IllegalArgumentException("" + errorMessage);
        }
        return ref;
    }

    /**
     * Checks the expression of an argument and throws an
     * {@code IllegalArgumentException} if the expression is {@code false}.
     *
     * @param expr the expression to test
     * @throws IllegalArgumentException if {@code expr} is {@code false}
     */
    public static void checkArgument(final boolean expr) {
        if (! expr) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks the expression of an argument and throws an
     * {@code IllegalArgumentException} if the expression is {@code false}.
     *
     * @param expr the expression to test
     * @throws IllegalArgumentException if {@code expr} is {@code false}
     */
    public static void checkArgument(final boolean expr,
                                     final Object errorMessage) {
        if (! expr) {
            throw new IllegalArgumentException("" + errorMessage);
        }
    }

    /**
     * Checks the expression of an argument and throws an
     * {@code IllegalStateException} if the expression is {@code false}.
     *
     * @param expr the expression to test
     * @throws IllegalStateException if {@code expr} is {@code false}
     */
    public static void checkState(final boolean expr) {
        if (! expr) {
            throw new IllegalStateException();
        }
    }

    /**
     * Checks the expression of an argument and throws an
     * {@code IllegalStateException} if the expression is {@code false}.
     *
     * @param expr the expression to test
     * @throws IllegalStateException if {@code expr} is {@code false}
     */
    public static void checkState(final boolean expr,
                                  final Object errorMessage) {
        if (! expr) {
            throw new IllegalStateException("" + errorMessage);
        }
    }

    /**
     * Checks that any of the values of the {@link Iterable} are <em>not</em>
     * {@code null}. Throws an {@code NullPointerException} for the first
     * found {@code null} reference.
     *
     * @param <T> the generic type of the reference parameter.
     * @param iterable any {@code Iterable} object
     * @return an {@code iterable} with no {@code null} reference
     * @throws NullPointerException if {@code iterable} self is {@code null} 
     *                              or contains at least one {@code null}
     *                              reference
     */
    public static <T extends Iterable<?>> T checkContainsNoNull(T iterable) {
        checkNotNull(iterable);
        for (final Object ref : iterable) {
            checkNotNull(ref);
        }
        return iterable;
    }

    /**
     * Checks that any of the values of the {@link Iterable} are <em>not</em>
     * {@code null}. Throws an {@code NullPointerException} for the first
     * found {@code null} reference.
     *
     * @param <T> the generic type of the reference parameter.
     * @param iterable any {@code Iterable} object
     * @param errorMessage the error message to be included into the
     *                     exception message.
     * @return and {@code iterable} with no {@code null} reference
     * @throws NullPointerException if {@code iterable} self is {@code null} 
     *                              or contains at least one {@code null}
     *                              reference
     */
    @SuppressWarnings("nls")
    public static <T extends Iterable<?>> T checkContainsNoNull(final T iterable,
                                                                final Object errorMessage) {
        checkNotNull(iterable, errorMessage);
        for (Object element : iterable) {
            checkNotNull(element, errorMessage);
        }
        return iterable;
    }

    /**
     * Checks that the given reference is {@code null} and throws an
     * {@code NullPointerException} if so.
     *
     * @param <T> the generic type of the reference parameter.
     * @param ref an object reference
     * @return the non-{@code null} reference
     * @throws NullPointerException if {@code ref} is {@code null}
     */
    public static <T> T checkNotNull(final T ref) {
        if (ref == null) {
            throw new NullPointerException();
        }
        return ref;
    }

    /**
     * Checks that the given reference is {@code null} and throws an
     * {@code NullPointerException} if so.
     *
     * @param <T> the generic type of the reference parameter.
     * @param ref an object reference
     * @param errorMessage the error message to be included into the exception
     *                     message.
     * @return the non-{@code null} reference
     * @throws NullPointerException if {@code ref} is {@code null}
     */
    @SuppressWarnings("nls")
    public static <T> T checkNotNull(final T ref,
                                     final Object errorMessage) {
        if (ref == null) {
            throw new NullPointerException("" + errorMessage);
        }
        return ref;
    }

}
