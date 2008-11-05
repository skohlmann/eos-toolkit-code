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

import java.util.Arrays;

import net.sf.eos.Nullable;

/**
 * Utility class to support {@link Object#equals(Object)}
 * and {@link Object#hashCode()} method implementation.
 * @author Sascha Kohlmann
 * @since 0.1.0
 */
public final class EqualsAndHashUtil {

    /** Magic hash multi value. */
    private static final int HASH_MULTI = 37;

    /** Magic hash shift value. */
    private static final int HASH_SHIFT = 32;

    /** Only {@code static} methods available. */
    private EqualsAndHashUtil() {
        super();
    }

    /**
     * {@code boolean} equality check.
     * @param own the first {@code boolean}
     * @param other the second <{@code boolean}
     * @return {@code true} if the {@code boolean}s are equal,
     *         <{@code false} otherwise
     */
    public static boolean isEqual(final boolean own, final boolean other) {
        return own == other;
    }

    /**
     * {@code char} equality check.
     * @param own the first {@code char}
     * @param other the second {@code char}
     * @return {@code true} if the {@code char}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final char own, final char other) {
        return own == other;
    }

    /**
     * {@code byte} equality check.
     * @param own the first {@code byte}
     * @param other the second {@code byte}
     * @return {@code true} if the {@code byte}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final byte own, final byte other) {
        return own == other;
    }

    /**
     * {@code short} equality check.
     * @param own the first {@code short}
     * @param other the second {@code short}
     * @return {@code true} if the {@code short}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final short own, final short other) {
        return own == other;
    }

    /**
     * {@code int} equality check.
     * @param own the first {@code int}
     * @param other the second {@code int}
     * @return {@code true} if the {@code int}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final int own, final int other) {
        return own == other;
    }

    /**
     * {@code long} equality check.
     * @param own the first {@code long}
     * @param other the second {@code long}
     * @return {@code true} if the {@code long}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final long own, final long other) {
        return own == other;
    }

    /**
     * {@code float} equality check.
     * @param own the first {@code float}
     * @param other the second {@code float}
     * @return {@code true} if the {@code float}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final float own, final float other) {
        return Float.floatToIntBits(own) == Float.floatToIntBits(other);
    }

    /**
     * {@code double} equality check.
     * @param own the first {@code double}
     * @param other the second {@code double}
     * @return {@code true} if the {@code double}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(final double own, final double other) {
        return Double.doubleToLongBits(own) == Double.doubleToLongBits(other);
    }

    /**
     * {@code Object} equality check.
     * @param own the first {@code Object}
     * @param other the second {@code Object}
     * @return {@code true} if the {@code Object}s are equal,
     *         {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final Object own,
                                  @Nullable final Object other) {
        return own == null ? other == null : own.equals(other);
    }

    /**
     * {@code boolean} array equality check.
     * @param own the first array of {@code boolean}s
     * @param other the second array of {@code boolean}s
     * @return {@code true} if the two {@code boolean} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final boolean[] own,
                                  @Nullable final boolean[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code char} array equality check.
     * @param own the first array of {@code char}s
     * @param other the second array of {@code char}s
     * @return {@code true} if the two {@code char} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final char[] own,
                                  @Nullable final char[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code byte} array equality check.
     * @param own the first array of {@code byte}s
     * @param other the second array of {@code byte}s
     * @return {@code true} if the two {@code byte} arrays are
     *         equal, {@code false} otherwise
      */
    public static boolean isEqual(@Nullable final byte[] own,
                                  @Nullable final byte[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code short} array equality check.
     * @param own the first array of {@code short}s
     * @param other the second array of {@code short}s
     * @return {@code true} if the two {@code short} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final short[] own,
                                  @Nullable final short[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code int} array equality check.
     * @param own the first array of {@code int}s
     * @param other the second array of {@code int}s
     * @return {@code true} if the two {@code int} arrays are
     *         equal, {@code false} otherwise
    */
    public static boolean isEqual(@Nullable final int[] own,
                                  @Nullable final int[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code long} array equality check.
     * @param own the first array of {@code long}s
     * @param other the second array of {@code long}s
     * @return {@code true} if the two {@code long} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final long[] own,
                                  @Nullable final long[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code float} array equality check.
     * @param own the first array of {@code float}s
     * @param other the second array of {@code float}s
     * @return {@code true} if the two {@code float} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final float[] own,
                                  @Nullable final float[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code double} array equality check.
     * @param own the first array of {@code double}s
     * @param other the second array of {@code double}s
     * @return {@code true} if the two {@code double} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final double[] own,
                                  @Nullable final double[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code Object} array equality check.
     * @param own the first array of {@code Object}s
     * @param other the second array of {@code Object}s
     * @return {@code true} if the two {@code Object} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean isEqual(@Nullable final Object[] own,
                                  @Nullable final Object[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code Object} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param obj the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code boolean} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param bool the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final boolean bool) {
        return bool ? 0 : HASH_MULTI * 1;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code byte} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param b the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final byte b) {
        return HASH_MULTI * b;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code char} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param c the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final char c) {
        return HASH_MULTI * c;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code short} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param s the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final short s) {
        return HASH_MULTI * s;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code int} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param i the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final int i) {
        return HASH_MULTI * i;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code long} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param l the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final long l) {
        return (int) (HASH_MULTI * (l ^ (l >>> HASH_SHIFT)));
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code float} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param f the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final float f) {
        final int i = Float.floatToIntBits(f);
        return EqualsAndHashUtil.hash(i);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given {@code double} according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param d the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final double d) {
        final long l = Double.doubleToLongBits(d);
        return EqualsAndHashUtil.hash(l);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code Object}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code Object}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final Object[] objarr) {
        return Arrays.hashCode(objarr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code boolean}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code boolean}s to compute the hash code
     * @return a hash code
     */
    public static int hash(@Nullable final boolean[] boolarr) {
        return Arrays.hashCode(boolarr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code byte}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code byte}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final byte[] barr) {
        return Arrays.hashCode(barr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code char}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code char}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final char[] carr) {
        return Arrays.hashCode(carr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code short}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code short}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final short[] sarr) {
        return Arrays.hashCode(sarr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code int}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code int}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final int[] iarr) {
        return Arrays.hashCode(iarr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code long}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code long}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final long[] larr) {
        return Arrays.hashCode(larr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code float}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code float}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final float[] farr) {
        return Arrays.hashCode(farr);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of {@code double}s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of {@code double}s to compute the hash code for
     * @return a hash code
     */
    public static int hash(@Nullable final double[] darr) {
        return Arrays.hashCode(darr);
    }
}
