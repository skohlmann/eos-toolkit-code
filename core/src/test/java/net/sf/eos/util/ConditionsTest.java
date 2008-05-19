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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConditionsTest {

    @Test(expected=IllegalArgumentException.class)
    public final void testCheckArgumentNotNullWithNull() {
        Conditions.checkArgumentNotNull(null);
    }

    @Test
    public final void testCheckArgumentNotNull() {
        assertEquals("cond", Conditions.checkArgumentNotNull("cond"));
    }

    @Test
    public final void testCheckArgumentNotNullWithMessageAndNullValue() {
        try {
            Conditions.checkArgumentNotNull(null, "dummy");
            fail("Oops... Missing IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            assertEquals("dummy", e.getMessage());
        }
    }

    @Test
    public final void testCheckArgumentNotNullWithMessageWithValue() {
        assertEquals("cond", Conditions.checkArgumentNotNull("cond", "dummy"));
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testCheckArgumentWithFalse() {
        Conditions.checkArgument(false);
    }

    @Test
    public final void testCheckArgumentWithTrue() {
        Conditions.checkArgument(true);
    }

    @Test
    public final void testCheckArgumentWithFalseWithMessage() {
        try {
            Conditions.checkArgument(false, "expr");
        } catch (final IllegalArgumentException e) {
             assertEquals("expr", e.getMessage());
        }
    }

    @Test(expected=IllegalStateException.class)
    public final void testCheckStateWithFalse() {
        Conditions.checkState(false);
    }

    @Test
    public final void testCheckStateWithTrue() {
        Conditions.checkState(true);
    }

    @Test
    public final void testCheckStateWithFalseWithMessage() {
        try {
            Conditions.checkState(false, "expr");
        } catch (final IllegalStateException e) {
             assertEquals("expr", e.getMessage());
        }
    }
}
