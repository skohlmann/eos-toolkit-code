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
}
