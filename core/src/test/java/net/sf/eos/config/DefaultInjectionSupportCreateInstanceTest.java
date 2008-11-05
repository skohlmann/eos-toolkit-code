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

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport;

/**
 * @author Sascha Kohlmann
 */
public class DefaultInjectionSupportCreateInstanceTest {


    @Test
    public void injectInSimpleInjectable() {
        final DefaultInjectionSupport<SimpleInjectable> injector =
                new DefaultInjectionSupport<SimpleInjectable>();
        final SimpleInjectable simple = new SimpleInjectable();
        injector.inject(simple);
        assertEquals(simple.getO().getClass(), InnerToInject.class);
    }

    @Test
    public void injectInGenericSimpleInjectable() {
        final DefaultInjectionSupport<GenericSimpleInjectable<? extends Object>> injector =
                new DefaultInjectionSupport<GenericSimpleInjectable<? extends Object>>();
        @SuppressWarnings("unchecked")
        final GenericSimpleInjectable<? extends Object> simple = new GenericSimpleInjectable();
        injector.inject(simple);
        assertEquals(simple.getT().getClass(), ToInject.class);
    }

    @Test
    public void injectInExtSimpleInjectable() {
        final DefaultInjectionSupport<ExtSimpleInjectable> injector =
                new DefaultInjectionSupport<ExtSimpleInjectable>();
        final ExtSimpleInjectable simple = new ExtSimpleInjectable();
        injector.inject(simple);
        assertEquals(simple.getO().getClass(), InnerToInject.class);
    }

    @Test
    public void injectInOverriddenExtSimpleInjectable() {
        final DefaultInjectionSupport<OverriddenExtSimpleInjectable> injector =
                new DefaultInjectionSupport<OverriddenExtSimpleInjectable>();
        final OverriddenExtSimpleInjectable simple = new OverriddenExtSimpleInjectable();
        injector.inject(simple);
        assertNull(simple.getO());
    }

    public static class SimpleInjectable {
        private InnerToInject o;
        @Inject(className="net.sf.eos.config.DefaultInjectionSupportCreateInstanceTest$InnerToInject")
        public void setO(final InnerToInject o) {
            this.o = o;
        }
        public InnerToInject getO() {
            return o;
        }
    }

    public static class ExtSimpleInjectable extends SimpleInjectable {}
    public static class OverriddenExtSimpleInjectable extends ExtSimpleInjectable {
        @Override
        public void setO(final InnerToInject o) {
            super.setO(o);
        }
    }
    public static class InnerToInject {}

    public static class GenericSimpleInjectable<T> {
        private T o;
        @Inject(className="net.sf.eos.config.ToInject")
        public void setT(final T o) {
            this.o = o;
        }
        public T getT() {
            return o;
        }
    }
}
