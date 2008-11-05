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

import net.sf.eos.Experimental;
import net.sf.eos.Function;

/**
 * A {@code function} which is {@code #configure(Configuration) configurable}.
 *
 * @author Sascha Kohlmann
 * @since 0.2.0
 * @param <F> the <em>from</em> type to handle in the function
 * @param <T> the <em>to</em> type results from {@link #apply}
 */
@Experimental
public interface ConfigurableFunction<F, T> extends Function<F, T>, Configurable { }
