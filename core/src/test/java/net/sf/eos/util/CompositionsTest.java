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

import net.sf.eos.Function;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompositionsTest {

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void functionCompositionWithNullAsFromEntity() {
        Compositions.compose((Function) null, new Function() {
            public Object apply(Object from) {
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void functionCompositionWithNullAsToEntity() {
        Compositions.compose(new Function() {
            public Object apply(Object from) {
                return null;
            }
        }, (Function) null);
    }

    @Test
    public void validFunctionComposition() {
        final Function<String, String> f =
            Compositions.compose(new Function<String, StringBuilder>() {
                public StringBuilder apply(final String from) {
                    return new StringBuilder(from + "-");
                }
            }, new Function<StringBuilder, String>() {
                public String apply(final StringBuilder i) {
                    return i.append("to").toString();
                }
            });
        final String result = f.apply("from");
        assertEquals("from-to", result);
    }
}
