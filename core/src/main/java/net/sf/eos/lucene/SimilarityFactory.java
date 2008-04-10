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

import net.sf.eos.EosException;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;

import org.apache.lucene.search.Similarity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * To support different strategies of Similarity in a Lucene index this
 * factory decoupled the creation of the Similarity from hard coded classnames.
 * Set the classname of a factory different from
 * <i>{@linkplain NormedLengthSimilarityFactory default}</i> implementation.
 * {@link #SIMILARITY_FACTORY_IMPL_CONFIG_NAME} contains the name of the
 * configuration key.
 *
 * <p>Implementations must have a default constructor and must implement
 * {@link #newSimilarity()}.</p>
 * @author Sascha Kohlmann
 */
public abstract class SimilarityFactory {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(SimilarityFactory.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    public final static String SIMILARITY_FACTORY_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.SimilarityFactory.impl";

    /**
     * Creates a new instance of a of the factory. If the
     * <code>Configuration</code> contains a key
     * {@link #SIMILARITY_FACTORY_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link NormedLengthSimilarityFactory} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    public final static SimilarityFactory 
            newInstance(final Configuration config) throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = SimilarityFactory.class.getClassLoader();
        }

        final String clazzName =
            config.get(SIMILARITY_FACTORY_IMPL_CONFIG_NAME,
                       NormedLengthSimilarityFactory.class.getName());

        try {
            final Class<? extends SimilarityFactory> clazz =
                (Class<? extends SimilarityFactory>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final SimilarityFactory factory =
                    (SimilarityFactory) clazz.newInstance();
                if (LOG.isLoggable(Level.CONFIG)) {
                    LOG.config("SimilarityFactory instance: "
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
     * Returns a new <code>Similarity</code> instance.
     * @return a new <code>Similarity</code> instance
     */
    public abstract Similarity newSimilarity();
}
