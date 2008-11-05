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

import org.junit.Test;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.InjectAnnotationFetcher;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class InjectAnnotationFetcherTest {

    @Test
    public void creation() {
        new InjectAnnotationFetcher();
    }

    @Test
    public void handlesNull() {
        final InjectAnnotationFetcher iiam = new InjectAnnotationFetcher();
        assertNull(iiam.apply(null));
    }

    @Test
    public void injectAnnotated() {
        final InjectAnnotationFetcher iiam = new InjectAnnotationFetcher();
        final Method[] methodList = Injected.class.getMethods();
        assertNotNull(iiam.apply(methodList[0]));
    }

    @Test
    public void noInjectAnnotated() {
        final InjectAnnotationFetcher iiam = new InjectAnnotationFetcher();
        final Method[] methodList =
            IndentifyInjectAnnotatedMethodsTest.NoInjectAnnotation.class.getMethods();
        assertNull(iiam.apply(methodList[0]));
    }

    public static class Injected {
        @Inject(className="")
        public void annotated() {}
    }
}
