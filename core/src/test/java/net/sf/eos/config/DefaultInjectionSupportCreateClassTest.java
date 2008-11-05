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


import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.InjectAnnotationFetcher;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IndentifyInjectAnnotatedMethods;

public class DefaultInjectionSupportCreateClassTest {

    private DefaultInjectionSupport<Object> support;

    @Test
    public void exitingClassFromAnnotation() {
        final Collection<Method> methods = this.support.getFilter().apply(new Injected());
        final Method m = methods.iterator().next();
        final InjectAnnotationFetcher iaf = new InjectAnnotationFetcher();
        final Inject inject = iaf.apply(m);
        final String className = inject.className();
        assertEquals(this.support.createClass(className, m), this.getClass());
    }

    @Test
    public void nonExitingClassFromAnnotationWithIllegalParameterLengthTwo() {
        final Collection<Method> methods =
            new IndentifyInjectAnnotatedMethods<IllegalParameterLengthInjectedTwo>()
                .apply(new IllegalParameterLengthInjectedTwo());
        final Method m = methods.iterator().next();
        final InjectAnnotationFetcher iaf = new InjectAnnotationFetcher();
        final Inject inject = iaf.apply(m);
        final String className = inject.className();
        try {
            this.support.createClass(className, m);
            fail("Oops... Violating contract.");
        } catch (final InjectionException e) {
            
        }
    }

    @Test
    public void nonExitingClassFromAnnotationWithIllegalParameterLengthZero() {
        final Collection<Method> methods =
            new IndentifyInjectAnnotatedMethods<IllegalParameterLengthInjectedZero>()
                .apply(new IllegalParameterLengthInjectedZero());
        final Method m = methods.iterator().next();
        final InjectAnnotationFetcher iaf = new InjectAnnotationFetcher();
        final Inject inject = iaf.apply(m);
        final String className = inject.className();
        try {
            this.support.createClass(className, m);
            fail("Oops... Violating contract.");
        } catch (final InjectionException e) {
            
        }
    }

    @Test
    public void nonExitingClassFromAnnotation() {
        final Collection<Method> methods = this.support.getFilter().apply(new Injected());
        final Method m = methods.iterator().next();
        assertEquals(this.support.createClass("", m), Object.class);
    }

    @Before
    public void setUp() throws Exception {
        this.support = new DefaultInjectionSupport<Object>();
    }

    public static class Injected {
        private Object object;
        final Object getObject() {
            return this.object;
        }
        @Inject(className="net.sf.eos.config.DefaultInjectionSupportCreateClassTest")
        public final void setObject(final Object object) {
            this.object = object;
        }
    }

    public static class IllegalParameterLengthInjectedTwo {
        @Inject(className="com")
        public final void setObject(final Object object, final Object o) {}
    }

    public static class IllegalParameterLengthInjectedZero {
        @Inject(className="com")
        public final void setObject() {}
    }
}
