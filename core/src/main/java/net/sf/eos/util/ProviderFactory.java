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

import net.sf.eos.Provider;
import net.sf.eos.config.ConfigurableProvider;
import net.sf.eos.config.Configuration;

import static net.sf.eos.util.Conditions.checkArgumentNotNull;

public final class ProviderFactory<T> {

    /**
     * Creates a new instance of the {@link Provider} class. The
     * {@code provider} must support a default Constructor.
     * @param <T> the type of the {@code provider}
     * @param clazz the class instance of the {@code provider}
     * @return the {@code provider}
     * @throw IllegalArgumentException if it is not possible to create
     *                                 a new {@code provider}
     */
    public static final <T> Provider<T>
                newProvider(final Class<? extends Provider<T>> clazz) {
        checkArgumentNotNull(clazz, "clazz is null");
        try {
            final Provider<T> p = clazz.newInstance();
            return p;
        } catch (final InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Creates a new instance of the {@link ConfigurableProvider} class.
     * The {@code provider} must support a default Constructor.
     * @param <T> the type of the {@code provider}
     * @param clazz the class instance of the {@code provider}
     * @return the {@code provider}
     * @throw IllegalArgumentException if it is not possible to create
     *                                 a new {@code provider}
     */
    public static final <T> ConfigurableProvider<T>
            newProvider(final Class<? extends ConfigurableProvider<T>> clazz,
                        final Configuration config) {
        checkArgumentNotNull(config, "configuration is null");
        final ConfigurableProvider<T> cp =
             (ConfigurableProvider<T>) newProvider(clazz);
        cp.configure(config);
        return cp;
    }

    /**
     * Creates a new instance of the {@link Provider} class. The
     * {@code provider} must support a default Constructor.
     * @param <T> the type of the {@code provider}
     * @param configName the name of the key that value in {@code config}
     *                   is the name of a provider class
     * @param config the configuration containing the class name
     * @return the {@code provider}
     * @throw IllegalArgumentException if it is impossible to create
     *                                 a new {@code provider}
     */
    public static final <T> Provider<T> newProvider(
                 final String configName,
                 final Configuration config) {
        checkArgumentNotNull(configName, "config name is null");
        checkArgumentNotNull(config, "configuration is null");
        final String clazzName = config.get(configName);
        return newProvider(configName, config, clazzName);
    }

    /**
     * Creates a new instance of the {@link Provider} class. The
     * {@code provider} must support a default Constructor.
     * @param <T> the type of the {@code provider}
     * @param configName the name of the key that value in {@code config}
     *                   is the name of a provider class
     * @param config the configuration containing the class name
     * @param defaultClazzName the default provider class name
     * @return the {@code provider}
     * @throw IllegalArgumentException if it is impossible to create
     *                                 a new {@code provider}
     */
    public static final <T> Provider<T> newProvider(
                final String configName,
                final Configuration config,
                final String defaultClazzName) {
        checkArgumentNotNull(configName, "config name is null");
        checkArgumentNotNull(config, "configuration is null");
        final String clazzName = config.get(configName, defaultClazzName);
        try {
            final Class<Provider<T>> clazz =
                (Class<Provider<T>>) Class.forName(clazzName);

            final Provider<T> p = (Provider<T>) newProvider(clazz);
            if (p instanceof ConfigurableProvider) {
                final ConfigurableProvider<T> cp =
                    (ConfigurableProvider<T>) p;
                cp.configure(config);
            }
            return p;
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
