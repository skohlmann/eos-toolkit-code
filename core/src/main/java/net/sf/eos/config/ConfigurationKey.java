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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.sf.eos.Experimental;


/**
 * Indicates that a field name is a key of the {@link Configuration} data.
 * The annotation describes the usage of the configuration parameter.
 * <p>The {@code ConfigurationKey} annotation is an convention. Only
 * {@code fields} with</p>
 * <ul>
 *   <li>{@code public}, {@code static} and {@code final} modifiers</p>
 *   <li>which are initialized at compile time</li>
 *   <li>are of type {@link String}
 * </ul>
 * <p>should be annotated. Note, that the compiler couldn't check this
 * conventions. Other tools like {@code apt} implementation should do the
 * checking job.</p>
 *
 * @author Sascha Kohlmann
 */
@Documented
@Retention(value=RUNTIME)
@Target(value=FIELD)
@Experimental
public @interface ConfigurationKey {

    /** The possible type of the configuration parameter.
     * @author Sascha Kohlmann
     * @see ConfigurationKey#type()
     * @see Configuration */
    public enum Type {
        /** Value should be transformable into a {@code boolean} value.
         * @see Boolean#parseBoolean(String)
         * @see Configuration#getBoolean(String, boolean) */
        BOOLEAN,
        /** Value should be transformable into an {@code int} value.
         * @see Integer#parseInt(String)
         * @see Configuration#getInt(String, int) */
        INTEGER,
        /** Value should be transformable into an {@code float} value.
         * @see Float#parseFloat(String)
         * @see Configuration#getFloat(String, float) */
        FLOAT,
        /** Default type. */
        STRING,
        /** The value of the configuration parameter must be the fully
         * qualified name of the desired class.
         * @see Class#forName(String) */
        CLASSNAME
    }

    /** The description of the configuration parameter. */
    String description() default "";

    /** The default value of the configuration parameter. */
    String defaultValue() default "";

    /** The type of the configuration parameter. */
    Type type() default Type.STRING;
}
