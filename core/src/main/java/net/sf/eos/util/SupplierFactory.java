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
package net.sf.eos.util;

import net.sf.eos.Supplier;
import net.sf.eos.config.ConfigurableSupplier;
import net.sf.eos.config.Configuration;

import static net.sf.eos.util.Conditions.checkArgumentNotNull;

public final class SupplierFactory<T> {

    /**
     * Creates a new instance of the {@link Supplier} class. The
     * {@code supplier} must support a default Constructor.
     * @param <T> the type of the {@code supplier}
     * @param clazz the class instance of the {@code supplier}
     * @return the {@code supplier}
     * @throw IllegalArgumentException if it is not possible to create
     *                                 a new {@code supplier}
     */
    public static final <T> Supplier<T>
                newSupplier(final Class<? extends Supplier<T>> clazz) {
        checkArgumentNotNull(clazz, "clazz is null");
        try {
            final Supplier<T> p = clazz.newInstance();
            return p;
        } catch (final InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Creates a new instance of the {@link ConfigurableSupplier} class.
     * The {@code supplier} must support a default Constructor.
     * @param <T> the type of the {@code supplier}
     * @param clazz the class instance of the {@code supplier}
     * @return the {@code supplier}
     * @throw IllegalArgumentException if it is not possible to create
     *                                 a new {@code supplier}
     */
    public static final <T> ConfigurableSupplier<T>
            newSupplier(final Class<? extends ConfigurableSupplier<T>> clazz,
                        final Configuration config) {
        checkArgumentNotNull(config, "configuration is null");
        final ConfigurableSupplier<T> cp =
             (ConfigurableSupplier<T>) newSupplier(clazz);
        cp.configure(config);
        return cp;
    }

    /**
     * Creates a new instance of the {@link Supplier} class. The
     * {@code supplier} must support a default Constructor.
     * @param <T> the type of the {@code supplier}
     * @param configName the name of the key that value in {@code config}
     *                   is the name of a supplier class
     * @param config the configuration containing the class name
     * @return the {@code supplier}
     * @throw IllegalArgumentException if it is impossible to create
     *                                 a new {@code supplier}
     */
    public static final <T> Supplier<T> newSupplier(
                 final String configName,
                 final Configuration config) {
        checkArgumentNotNull(configName, "config name is null");
        checkArgumentNotNull(config, "configuration is null");
        final String clazzName = config.get(configName);
        return newSupplier(configName, config, clazzName);
    }

    /**
     * Creates a new instance of the {@link Supplier} class. The
     * {@code supplier} must support a default Constructor.
     * @param <T> the type of the {@code supplier}
     * @param configName the name of the key that value in {@code config}
     *                   is the name of a supplier class
     * @param config the configuration containing the class name
     * @param defaultClazzName the default supplier class name
     * @return the {@code supplier}
     * @throw IllegalArgumentException if it is impossible to create
     *                                 a new {@code supplier}
     */
    public static final <T> Supplier<T> newSupplier(
                final String configName,
                final Configuration config,
                final String defaultClazzName) {
        checkArgumentNotNull(configName, "config name is null");
        checkArgumentNotNull(config, "configuration is null");
        final String clazzName = config.get(configName, defaultClazzName);
        try {
            final Class<Supplier<T>> clazz =
                (Class<Supplier<T>>) Class.forName(clazzName);

            final Supplier<T> p = (Supplier<T>) newSupplier(clazz);
            if (p instanceof ConfigurableSupplier) {
                final ConfigurableSupplier<T> cp =
                    (ConfigurableSupplier<T>) p;
                cp.configure(config);
            }
            return p;
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
