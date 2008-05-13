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
package net.sf.eos.lucene;

import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.store.Directory;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;

public abstract class DirectoryProvider extends Configured {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DirectoryProvider.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Configuration key of the Lucene directory "
                                        + " provider factory.")
    public final static String DIRECTORY_PROVIDER_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.DirectoryProvider.impl";

    /**
     * Creates a new instance of a of the factory. If the
     * {@code Configuration} contains a key
     * {@link #DIRECTORY_PROVIDER_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link LocalFsDirectoryProvider} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     * @see LocalFsDirectoryProvider
     */
    @FactoryMethod(key=DIRECTORY_PROVIDER_IMPL_CONFIG_NAME,
                   implementation=LocalFsDirectoryProvider.class)
    public final static DirectoryProvider newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = AnalyzerFactory.class.getClassLoader();
        }

        final String clazzName =
            config.get(DIRECTORY_PROVIDER_IMPL_CONFIG_NAME,
                    LocalFsDirectoryProvider.class.getName());

        try {
            final Class<? extends DirectoryProvider> clazz =
                (Class<? extends DirectoryProvider>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final DirectoryProvider factory = clazz.newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("DirectoryProvider instance: "
                              + factory.getClass().getName());
                }
                return factory;

            } catch (final InstantiationException e) {
                throw new EosException(e);
            } catch (final IllegalAccessException e) {
                throw new EosException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new EosException(e);
        }
    }

    /**
     * Creates a new directory for the configuration at creation time.
     * @return a new Lucene {@code Directory} instance.
     * @throws EosException if an error occurs.
     */
    public Directory newDirectory() throws EosException {
        final Configuration conf = this.getConfiguration();
        return newDirectory(conf);
    }

    /**
     * Use the given configuration to create a new {@code Directory} instance.
     * @param conf the configuration to use for {@code Directory} creating
     * @return a new Lucence {@code Directory}
     * @throws EosException if an error occurs.
     */
    public abstract Directory newDirectory(final Configuration conf)
            throws EosException;
}
