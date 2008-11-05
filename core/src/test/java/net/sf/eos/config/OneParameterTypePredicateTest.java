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
package net.sf.eos.config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.Test;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.OneParameterTypePredicate;


/**
 * @author Sascha Kohlmann
 */
public class OneParameterTypePredicateTest {

    @Test
    public void creation() {
        new OneParameterTypePredicate();
    }

    @Test
    public void handlesNull() {
        final OneParameterTypePredicate optp = new OneParameterTypePredicate();
        try {
            optp.evaluate(null);
            fail("Oops... must throw NullPointerException");
        } catch (final NullPointerException e) { }
    }

    @Test
    public void oneParameter() {
        final OneParameterTypePredicate optp = new OneParameterTypePredicate();
        final Method[] methods = OneParameterMethodHolder.class.getDeclaredMethods();
        Method m = null;
        for (final Method method : methods) {
            if ("oneParameter".equals(method.getName())) {
                m = method;
            }
        }
        
        assertTrue(optp.evaluate(m));
    }

    @Test
    public void notOneParameter() {
        final OneParameterTypePredicate optp = new OneParameterTypePredicate();
        final Method[] methods = TwoParameterMethodHolder.class.getDeclaredMethods();
        Method m = null;
        for (final Method method : methods) {
            if ("twoParameter".equals(method.getName())) {
                m = method;
            }
        }
        
        assertFalse(optp.evaluate(m));
    }

    public static class OneParameterMethodHolder {
        public void oneParameter(final Object o) {}
    }

    public static class TwoParameterMethodHolder {
        public void twoParameter(final Object o1, final Object o2) {}
    }
}
