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
import org.apache.lucene.search.Searcher;

import net.sf.eos.EosException;
import net.sf.eos.Supplier;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationException;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.Configured;

public abstract class SearcherSupplier extends Configured
        implements Supplier<Searcher> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(SearcherSupplier.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Configuration key of the search supplier.")
    public final static String SEARCHER_SUPPLIER_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.SearcherSupplier.impl";

    /**
     * Creates a new instance of a of the factory. If the
     * {@code Configuration} contains a key
     * {@link #SEARCHER_SUPPLIER_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link IndexSearcherSupplier} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     * @see IndexSearcherSupplier
     */
    public final static SearcherSupplier newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = SearcherSupplier.class.getClassLoader();
        }

        final String clazzName =
            config.get(SEARCHER_SUPPLIER_IMPL_CONFIG_NAME,
                       IndexSearcherSupplier.class.getName());

        try {
            final Class<? extends SearcherSupplier> clazz =
                (Class<? extends SearcherSupplier>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final SearcherSupplier factory = clazz.newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("SearcherSupplier instance: "
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
     * Creates a new searchable for the configuration at creation time.
     * @return a new Lucene {@code Searchable} instance.
     * @throws ConfigurationException may thrown if misconfigured
     */
    public Searcher get() {
        final Configuration conf = this.getConfiguration();
        return get(conf);
    }

    /**
     * Use the given configuration to create a new {@code Searchable} instance.
     * @param conf the configuration to use for {@code Searchable} creating
     * @return a new Lucence {@code Searchable}
     * @throws ConfigurationException may thrown if misconfigured
     */
    public abstract Searcher get(final Configuration conf);
}
