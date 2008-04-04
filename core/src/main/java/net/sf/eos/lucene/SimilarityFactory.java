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
 * Implementations must have a default constructor and must implement
 * {@link #newSimilarity()}.
 * @author Sascha Kohlmann
 */
public abstract class SimilarityFactory {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(SimilarityFactory.class.getName());

    @SuppressWarnings("nls")
    public final static String SIMILARITY_FACTORY_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.SimilarityFactory.impl";

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
                throw new TokenizerException(e);
            } catch (final IllegalAccessException e) {
                throw new TokenizerException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new TokenizerException(e);
        }
    }

    /**
     * Returns a new <code>Similarity</code> instance.
     * @return a new <code>Similarity</code> instance
     */
    public abstract Similarity newSimilarity();
}
