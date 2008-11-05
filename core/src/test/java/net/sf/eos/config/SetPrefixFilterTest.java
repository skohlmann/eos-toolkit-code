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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Test;

import net.sf.eos.Function;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IndentifyInjectAnnotatedMethods;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.SetPrefixFilter;
import net.sf.eos.util.Compositions;



public class SetPrefixFilterTest {

    @Test
    public void creation() {
        new SetPrefixFilter();
    }

    @Test
    public void handlesNull() {
        final SetPrefixFilter optifm = new SetPrefixFilter();
        assertTrue(optifm.apply(null).size() == 0);
    }

    @Test
    public void indentityMethods() {
        final Function<WithNoSetPrefixedMethodName, Collection<Method>> composition =
            Compositions.compose(new IndentifyInjectAnnotatedMethods<WithNoSetPrefixedMethodName>(),
                                 new SetPrefixFilter());
        assertEquals(composition.apply(new WithNoSetPrefixedMethodName()).size(), 2);
    }

    public static class WithSetPrefixedMethodName
                extends IndentifyInjectAnnotatedMethodsTest.ExtInjected {
        @Inject(className="")
        public void setParameter(final String s) {}
        @Inject(className="")
        public void set(final String s) {}
    }

    public static class WithNoSetPrefixedMethodName extends WithSetPrefixedMethodName {
        @Inject(className="")
        public void seParameter(final String first, final String second) {}
        @Inject(className="")
        public void SET(final String s) {}
    }
}
