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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.sf.eos.Experimental;


/**
 * <p><a name='Inject_eos_class_documentation'>Marks a method as injectable</a>.</p>
 * <p>It's convention that a method marked as injectable must follow the rules:</p>
 * <ul>
 *   <li>the method must b {@code public}</li>
 *   <li>the method must have one parameter which refers to a reference type. The reference type
 *     must follow the rules:
 *     <ul>
 *       <li>not to be an enumeration</li>
 *       <li>not to be an annotation</li>
 *       <li>not to be an array type</li>
 *     </ul>
 *   </li>
 *   <li>the return type must be {@code void}</li>
 *   <li>the method name must be prefixed with {@code set}.</li>
 * </ul>
 * <p>The referenced implementation of the {@code className} must follow the reference type rules
 * of the parameter an must be also</p>
 * <ul>
 *   <li>not an interface</li>
 *   <li>not an abstract class</li>
 *   <li>support at least a constructor with no arguments</li>
 * </ul>
 * <p>The value of {@code className} must be a real class, reaching over the classpath or
 * a name of a file in {@code META-INF/services/}. If the file doesn't exists in
 * the injector must try to create a file from {@code className}.</p>
 * @author Sascha Kohlmann
 * @since 0.2.0
 */
@Retention(value = RUNTIME)
@Target(value = METHOD)
@Experimental
public @interface Inject {
    /** The {@link InjectionSupport} needs the full qualified name of the class to inject. See
     * <a href='#Inject_eos_class_documentation'>for further rule</a> information. */
    String className();

    /** Describes the injection. */
    String description() default "";
}
