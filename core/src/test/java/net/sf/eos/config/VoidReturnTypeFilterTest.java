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
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.VoidReturnTypeFilter;
import net.sf.eos.util.Compositions;


public class VoidReturnTypeFilterTest {

    @Test
    public void creation() {
        new VoidReturnTypeFilter();
    }

    @Test
    public void handlesNull() {
        final VoidReturnTypeFilter optifm = new VoidReturnTypeFilter();
        assertTrue(optifm.apply(null).size() == 0);
    }

    @Test
    public void indentityMethods() {
        final Function<WithStringReturnType, Collection<Method>> composition =
            Compositions.compose(new IndentifyInjectAnnotatedMethods<WithStringReturnType>(),
                                 new VoidReturnTypeFilter());
        assertEquals(composition.apply(new WithStringReturnType()).size(), 2);
    }

    public static class WithStringReturnType extends IndentifyInjectAnnotatedMethodsTest.ExtInjected
    {
        @Inject(className="")
        public String withStringReturnType(final String first) {
            return null;
        }
    }
}
