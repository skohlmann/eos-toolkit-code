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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.sf.eos.Experimental;

/**
 * Indicates the internal use of a service in an instance. Use {@link Services}
 * if the implementation supports more than one {@code Service}.
 * Internal services may create thru a method, annotated with
 * {@link FactoryMethod}.
 * <p>{@code Service} annotations should be volatile between releases.</p>
 * @author Sascha Kohlmann
 * @see Services
 */
@Experimental
@Retention(value=RUNTIME)
@Target(value=TYPE)
public @interface Service {

    /**
     * {@link java.lang.Class} instance of the an internally used service.
     * @return instance of the an internally used service
      */
    Class<?> factory();

    /** The used implementation. */
    Class<?> implementation() default FactoryMethod.None.class;

    /**
     * An optional description.
     * @return a description
     */
    String description() default "";
}
