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
import net.sf.eos.config.ConfigurableSupplier;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationException;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;

public abstract class DirectorySupplier extends Configured
        implements ConfigurableSupplier<Directory> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DirectorySupplier.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Configuration key of the Lucene directory "
                                        + " provider factory.")
    public final static String DIRECTORY_SUPPLIER_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.DirectorySupplier.impl";

    /**
     * Creates a new instance of a of the factory. If the
     * {@code Configuration} contains a key
     * {@link #DIRECTORY_SUPPLIER_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link LocalFsDirectorySupplier} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     * @see LocalFsDirectorySupplier
     */
    @FactoryMethod(key=DIRECTORY_SUPPLIER_IMPL_CONFIG_NAME,
                   implementation=LocalFsDirectorySupplier.class)
    public final static DirectorySupplier newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = SearcherSupplier.class.getClassLoader();
        }

        final String clazzName =
            config.get(DIRECTORY_SUPPLIER_IMPL_CONFIG_NAME,
                    LocalFsDirectorySupplier.class.getName());

        try {
            final Class<? extends DirectorySupplier> clazz =
                (Class<? extends DirectorySupplier>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final DirectorySupplier factory = clazz.newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("DirectorySupplier instance: "
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
     * @throws ConfigurationException may thrown if misconfigured
     */
    public Directory get() {
        final Configuration conf = this.getConfiguration();
        return get(conf);
    }

    /**
     * Use the given configuration to create a new {@code Directory} instance.
     * @param conf the configuration to use for {@code Directory} creating
     * @return a new Lucence {@code Directory}
     * @throws ConfigurationException may thrown if misconfigured
     */
    public abstract Directory get(final Configuration conf);
}
