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
package net.sf.eos.util.functions;

import net.sf.eos.Experimental;
import net.sf.eos.Function;
import net.sf.eos.Nullable;
import net.sf.eos.config.ConfigurationKey;

/**
 * The function returns the default value of a {@link ConfigurationKey}.
 * @author Sascha Kohlmann
 * @since 0.1.0
 * @see ConfigurationKeyIntrospectorFunction
 */
@Experimental
public class DefaultValueConfigurationKeyFunction
        implements Function<ConfigurationKey, String> {

    /**
     * Returns the {@linkplain ConfigurationKey#defaultValue() default value}
     * of a {@code ConfigurationKey}.
     * @param from the configuration key instance
     * @return the default value. Maybe {@code null}
     */
    public String apply(@Nullable ConfigurationKey from) {
        return from == null ? null : from.defaultValue();
    }
}
