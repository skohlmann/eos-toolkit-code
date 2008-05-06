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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Searchable;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;

public abstract class SearcherProvider extends Configured {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(SearcherProvider.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    public final static String SEARCHER_PROVIDER_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.SearcherProvider.impl";

    /**
     * Creates a new instance of a of the factory. If the
     * <code>Configuration</code> contains a key
     * {@link #SEARCHER_PROVIDER_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link IndexSearcherProvider} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     * @see IndexSearcherProvider
     */
    @FactoryMethod(key=SEARCHER_PROVIDER_IMPL_CONFIG_NAME,
                   implementation=IndexSearcherProvider.class)
    public final static SearcherProvider newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = AnalyzerFactory.class.getClassLoader();
        }

        final String clazzName =
            config.get(SEARCHER_PROVIDER_IMPL_CONFIG_NAME,
                       IndexSearcherProvider.class.getName());

        try {
            final Class<? extends SearcherProvider> clazz =
                (Class<? extends SearcherProvider>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final SearcherProvider factory = clazz.newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("SearcherProvider instance: "
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
     * @throws EosException if an error occurs.
     */
    public Searcher newSearcher() throws EosException {
        final Configuration conf = this.getConfiguration();
        return newSearcher(conf);
    }

    /**
     * Use the given configuration to create a new {@code Searchable} instance.
     * @param conf the configuration to use for {@code Searchable} creating
     * @return a new Lucence {@code Searchable}
     * @throws EosException if an error occurs.
     */
    public abstract Searcher newSearcher(final Configuration conf)
            throws EosException;
}
