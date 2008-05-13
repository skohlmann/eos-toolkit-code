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

/**
 * Utility class to support {@link Object#equals(Object)}
 * and {@link Object#hashCode()} method implementation.
 * @author Sascha Kohlmann
 */
public final class EqualsAndHashUtil {

    /** Magic hash multi value. */
    private static final int HASH_MULTI = 37;

    /** Magic hash shift value. */
    private static final int HASH_SHIFT = 32;

    /** Only <code>static</code> methods available. */
    private EqualsAndHashUtil() {
        super();
    }

    /**
     * <code>boolean</code> equality check.
     * @param own the first <code>boolean</code>
     * @param other the second <code>boolean</code>
     * @return {@code true} if the <code>boolean</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final boolean own, final boolean other) {
        return own == other;
    }

    /**
     * <code>char</code> equality check.
     * @param own the first <code>char</code>
     * @param other the second <code>char</code>
     * @return {@code true} if the <code>char</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final char own, final char other) {
        return own == other;
    }

    /**
     * <code>byte</code> equality check.
     * @param own the first <code>byte</code>
     * @param other the second <code>byte</code>
     * @return {@code true} if the <code>byte</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final byte own, final byte other) {
        return own == other;
    }

    /**
     * <code>short</code> equality check.
     * @param own the first <code>short</code>
     * @param other the second <code>short</code>
     * @return {@code true} if the <code>short</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final short own, final short other) {
        return own == other;
    }

    /**
     * <code>int</code> equality check.
     * @param own the first <code>int</code>
     * @param other the second <code>int</code>
     * @return {@code true} if the <code>int</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final int own, final int other) {
        return own == other;
    }

    /**
     * <code>long</code> equality check.
     * @param own the first <code>long</code>
     * @param other the second <code>long</code>
     * @return {@code true} if the <code>long</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final long own, final long other) {
        return own == other;
    }

    /**
     * <code>float</code> equality check.
     * @param own the first <code>float</code>
     * @param other the second <code>float</code>
     * @return {@code true} if the <code>float</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final float own, final float other) {
        return Float.floatToIntBits(own) == Float.floatToIntBits(other);
    }

    /**
     * <code>double</code> equality check.
     * @param own the first <code>double</code>
     * @param other the second <code>double</code>
     * @return {@code true} if the <code>double</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final double own, final double other) {
        return Double.doubleToLongBits(own) == Double.doubleToLongBits(other);
    }

    /**
     * <code>Object</code> equality check.
     * @param own the first <code>Object</code>
     * @param other the second <code>Object</code>
     * @return {@code true} if the <code>Object</code>s are equal,
     *         <code>false</code> otherwise
     */
    public static boolean equals(final Object own, final Object other) {
        return own == null ? other == null : own.equals(other);
    }

    /**
     * <code>boolean</code> array equality check.
     * @param own the first array of <code>boolean</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>boolean</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>boolean</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final boolean[] own, final boolean[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * <code>char</code> array equality check.
     * @param own the first array of <code>char</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>char</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>char</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final char[] own, final char[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * <code>byte</code> array equality check.
     * @param own the first array of <code>byte</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>byte</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>byte</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final byte[] own, final byte[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * <code>short</code> array equality check.
     * @param own the first array of <code>short</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>short</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>short</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final short[] own, final short[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * <code>int</code> array equality check.
     * @param own the first array of <code>int</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>int</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>int</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final int[] own, final int[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * <code>long</code> array equality check.
     * @param own the first array of <code>long</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>long</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>long</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final long[] own, final long[] other) {
        return Arrays.equals(own, other);
    }


    /**
     * <code>float</code> array equality check.
     * @param own the first array of <code>float</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>float</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>float</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final float[] own, final float[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * <code>double</code> array equality check.
     * @param own the first array of <code>double</code>s
     *            (may be <code>null</code>)
     * @param other the second array of <code>double</code>s
     *              (may be <code>null</code>)
     * @return {@code true} if the two <code>double</code> arrays are
     *         equal, <code>false</code> otherwise
     */
    public static boolean equals(final double[] own, final double[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * {@code Object} array equality check.
     * @param own the first array of {@code Object}s
     *            (may be {@code null})
     * @param other the second array of {@code Object}s
     *              (may be {@code null})
     * @return {@code true} if the two {@code Object} arrays are
     *         equal, {@code false} otherwise
     */
    public static boolean equals(final Object[] own, final Object[] other) {
        return Arrays.equals(own, other);
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>Object</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param obj the object to compute the hash code for (may be
     *            <code>null</code>)
     * @return a hash code
     */
    public static int hash(final Object obj) {
        return obj == null ? 0 : obj.hashCode();
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>boolean</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param bool the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final boolean bool) {
        return bool ? 0 : HASH_MULTI * 1;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>byte</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param b the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final byte b) {
        return HASH_MULTI * b;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>char</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param c the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final char c) {
        return HASH_MULTI * c;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>short</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param s the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final short s) {
        return HASH_MULTI * s;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>int</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param i the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final int i) {
        return HASH_MULTI * i;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>long</code> according to the rules stated in "Effective
     * Java" by Joshua Bloch.
     * @param l the object to compute the hash code for
     * @return a hash code
     */
    public static int hash(final long l) {
        return (int) (HASH_MULTI * (l ^ (l >>> HASH_SHIFT)));
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given <code>float</code> according to the rules stated in "Effective
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
     * given <code>double</code> according to the rules stated in "Effective
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
     * given array of <code>Object</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>Object</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final Object[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>boolean</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>boolean</code>s to compute the hash code
     *            for (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final boolean[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>byte</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>byte</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final byte[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>char</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>char</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final char[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>short</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>short</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final short[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>int</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>int</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final int[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>long</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>long</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final long[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>float</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>float</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final float[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }

    /**
     * Creates a {@linkplain Object#hashCode() hash code} for the
     * given array of <code>double</code>s according to the rules stated in
     * "Effective Java" by Joshua Bloch.
     * @param arr the array of <code>double</code>s to compute the hash code for
     *            (may be <code>null</code>)
     * @return a hash code
     */
    public static int hash(final double[] arr) {
        int hashCode = 0;
        if (arr != null) {
            for (int i = arr.length; i >= 0; i--) {
                hashCode += EqualsAndHashUtil.hash(arr[i]);
            }
        }
        return hashCode;
    }
}
