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
import static org.junit.Assert.assertTrue;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IndentifyInjectAnnotatedMethods;

public class IndentifyInjectAnnotatedMethodsTest {

    @Test
    public void creation() {
        new IndentifyInjectAnnotatedMethods<String>();
    }

    @Test
    public void handlesNull() {
        final IndentifyInjectAnnotatedMethods<String> iiam =
            new IndentifyInjectAnnotatedMethods<String>();
        assertTrue(iiam.apply(null).size() == 0);
    }

    @Test
    public void indentityMethods() {
        final IndentifyInjectAnnotatedMethods<ExtInjected> iiam =
            new IndentifyInjectAnnotatedMethods<ExtInjected>();
        assertTrue(iiam.apply(new ExtInjected()).size() == 2);
    }

    @Test
    public void indentityMethodsInAClassWithNoInjectAnnotaedMethod() {
        final IndentifyInjectAnnotatedMethods<NoInjectAnnotation> iiam =
            new IndentifyInjectAnnotatedMethods<NoInjectAnnotation>();
        assertTrue(iiam.apply(new NoInjectAnnotation()).size() == 0);
    }

    public static class Injected {
        public void notAnnotated() {}
        @Inject(className="")
        public void annotated() {}
    }

    public static class ExtInjected extends Injected {
        public void notAnnotatedExt() {}
        @Inject(className="")
        public void annotatedExt() {}
    }

    public static final class NoInjectAnnotation {
        public void notAnnotated() {}
    }
}

