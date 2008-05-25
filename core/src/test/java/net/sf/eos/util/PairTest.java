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
 */
package net.sf.eos.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

public class PairTest {

    @SuppressWarnings({ "nls", "boxing" })
    @Test
    public void creation() {
        final Pair<String, Integer> p = new Pair<String, Integer>("test", 2);
        assertEquals("test", p.getFirst());
        assertEquals(new Integer(2), p.getSecond());
    }

    @Test
    public void createWithNull() {
        final Pair<String, Integer> p = new Pair<String, Integer>(null, null);
        assertNull(p.getFirst());
        assertNull(p.getSecond());
    }

    @Test
    public void equalsWithSameInstance() {
        final Pair<String, Integer> p = new Pair<String, Integer>("test", 2);
        assertEquals(p, p);
    }

    @Test
    public void equalsWithSameValue() {
        final Pair<String, Integer> p1 = new Pair<String, Integer>("test", 2);
        final Pair<String, Integer> p2 = new Pair<String, Integer>("test", 2);
        assertEquals(p1, p2);
    }

    @Test
    public void equalsWithNull() {
        final Pair<String, Integer> p1 = new Pair<String, Integer>("test", 2);
        assertFalse(p1.equals(null));
    }

    @Test
    public void equalsWithNullValue1() {
        final Pair<String, Integer> p1 = new Pair<String, Integer>("test", null);
        final Pair<String, Integer> p2 = new Pair<String, Integer>(null, 3);
        assertFalse(p1.equals(p2));
    }
}
