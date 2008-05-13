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

import net.sf.eos.trie.PatriciaTrie.KeyAnalyzer;

public class ByteArrayKeyAnalyzer implements KeyAnalyzer<byte[]> {
    
    private static final int LENGTH = 8;
    private static final int[] BITS = createIntBitMask(LENGTH);

    private static final int[] createIntBitMask(int bitCount) {
        final int[] bits = new int[bitCount];
        for(int i = 0; i < bitCount; i++) {
            bits[i] = 1 << (bitCount - i - 1);
        }
        return bits;
    } 

    public int bitsPerElement() {
        return LENGTH;
    }

    public boolean isBitSet(final byte[] key,
                            final int keyLength,
                            final int bitIndex) {
        if (key == null || bitIndex >= keyLength) {
            return false;
        }

        int index = bitIndex / BITS.length;
        int bit = bitIndex - index * BITS.length;
        return (key[index] & BITS[bit]) != 0;
    }

    public boolean isPrefix(final byte[] prefix,
                            final int offset,
                            final int length,
                            final byte[] key) {
        if(offset % LENGTH != 0 || length % LENGTH != 0) {
            throw new IllegalArgumentException("Cannot determine prefix outside"
                                               + " of character boundaries");
        }
//        String s1 = prefix.subSequence(offset / 16, length / 16).toString();
//        String s2 = key.toString();
//        return s2.startsWith(s1);

        final int start = offset / LENGTH;
        final int end = start + length / LENGTH;

        for (int i = start, j = 0; i < end; i++, j++) {
            if (prefix[i] != key[j]) {
                return false;
            }
        }
        return true;
    }

    public int length(final byte[] key) {
        return (key != null ? key.length * LENGTH : 0);
    }

    public int compare(final byte[] o1, final byte[] o2) {
        if (o1.length != o2.length) {
            return o1.length - o2.length;
        }
        assert o1.length == o2.length;
        for (int i = 0; i < o1.length; i++) {
            if (o1[i] != o2[1]) {
                return o1[i] - o2[i];
            }
        }
        return 0;
    }

    public int bitIndex(final byte[] key,    final int keyOff,
                        final int keyLength, final byte[] found,
                        final int foundOff,  final int foundKeyLength) {
        boolean allNull = true;
        
        if(keyOff % LENGTH != 0 || foundOff % LENGTH != 0
                || keyLength % LENGTH != 0 || foundKeyLength % LENGTH != 0)
            throw new IllegalArgumentException("offsets & lengths must be at "
                                               + "character boundaries");

//        try {
//            System.out.println("\nkey: .......... " + new String(key, "UTF-8"));
//            System.out.println("found: ........ " + (found != null 
//                                                    ? new String(found, "UTF-8")
//                                                    : null));
//        } catch (final UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.println("key.length: ... " + key.length);
//        System.out.println("found.length: . " + (found != null ? found.length
//                                                               : null));
//        System.out.println("keyOff: ....... " + keyOff);
//        System.out.println("keyLength: .... " + keyLength);
//        System.out.println("foundOff: ..... " + foundOff);
//        System.out.println("foundKeyLength: " + foundKeyLength);

        final int off1 = keyOff / LENGTH;
        final int off2 = foundOff / LENGTH;
        final int len1 = keyLength / LENGTH + off1;
        final int len2 = foundKeyLength / LENGTH + off2;
        final int length = Math.max(len1, len2);

//        System.out.println("off1: ......... " + off1);
//        System.out.println("off2: ......... " + off2);
//        System.out.println("len1: ......... " + len1);
//        System.out.println("len2: ......... " + len2);
//        System.out.println("length: ....... " + length);

        // Look at each character, and if they're different
        // then figure out which bit makes the difference
        // and return it.
        int k = 0, f = 0;
        for(int i = 0; i < length; i++) {
            final int kOff = i + off1;
            final int fOff = i + off2;
//            System.out.println(i + ". kOff: ......... " + kOff);
//            System.out.println(i + ". fOff: ......... " + fOff);

            if(kOff >= len1) {
                k = 0;
            } else {
                k = key[kOff];
                if (k < 0) {
                    k = Math.abs(k) * 2;
                }
            }
//            System.out.println(i + ". k: ............ " + k);

            if(found == null || fOff >= len2) {
                f = 0;
            } else {
                f = found[fOff];
                if (f < 0) {
                    f = Math.abs(f) * 2;
                }
            }
//            System.out.println(i + ". f: ............ " + f);

            if(k != f) {
                final int x = k ^ f;
//                System.out.println(i + ". x: ............ " + x);
                final int retval = 
                    i * LENGTH + (Integer.numberOfLeadingZeros(x) - LENGTH);
//                System.out.println(i + ". retval: ....... " + retval);
                return retval;
            }

            if(k != 0) {
                allNull = false;
            }
        }

        if (allNull) {
            return KeyAnalyzer.NULL_BIT_KEY;
        }

        return KeyAnalyzer.EQUAL_BIT_KEY;
    }
}
