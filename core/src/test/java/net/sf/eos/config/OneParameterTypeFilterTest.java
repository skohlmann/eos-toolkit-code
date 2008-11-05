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
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.OneParameterTypeFilter;
import net.sf.eos.util.Compositions;

public class OneParameterTypeFilterTest {

    @Test
    public void creation() {
        new OneParameterTypeFilter();
    }

    @Test
    public void handlesNull() {
        final OneParameterTypeFilter optifm = new OneParameterTypeFilter();
        assertTrue(optifm.apply(null).size() == 0);
    }

    @Test
    public void indentityMethods() {
        final Function<WithTwoParameter, Collection<Method>> composition =
            Compositions.compose(new IndentifyInjectAnnotatedMethods<WithTwoParameter>(),
                                 new OneParameterTypeFilter());
        assertEquals(composition.apply(new WithTwoParameter()).size(), 1);
    }

    public static class WithOneParameter extends IndentifyInjectAnnotatedMethodsTest.ExtInjected {
        @Inject(className="")
        public void withOneParameter(final String s) {}
    }

    public static class WithTwoParameter extends WithOneParameter {
        @Inject(className="")
        public void withTwoParameter(final String first, final String second) {}
    }
}

