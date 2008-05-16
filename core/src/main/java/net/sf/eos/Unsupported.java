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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.sf.eos.config.ConfigurableProvider;

/**
 * Annotates a method as unsupported. Useful for example
 * {@link ConfigurableProvider} instances which doesn't support the
 * {@link Provider#get()} method.
 *
 * <p>Such an annotated method should throw a
 * {@link UnsupportedOperationException}.</p>
 *
 * <p><strong>Note:</strong> experimental</p>
 *
 * @since 0.1.0
 * @author Sascha Kohlmann
 */
@Experimental
@Documented
@Retention(value=RUNTIME)
@Target(value=METHOD)
public @interface Unsupported { }
