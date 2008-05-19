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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.sf.eos.Experimental;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Identifies a method in a class as a factory method. A factory method has
 * always a {@linkplain Configuration configuration key} in which the name
 * of the implementing class is stored.
 * @author Sascha Kohlmann
 */
@Documented
@Retention(value=RUNTIME)
@Target(value=METHOD)
@Experimental
public @interface FactoryMethod {

    /** Default none implementation. */
    static class None {
        private None() {
            ;
        }
    }

    /** Contains the name of the configuration key.
     * @return the configuration key. */
    String key();

    /**
     * The class of the default implementation if available.
     * @return the name of the default implementation.
     */
    Class<?> implementation() default None.class;
}
