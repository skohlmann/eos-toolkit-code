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

import org.apache.lucene.analysis.Analyzer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementations must have a default constructor and must implement
 * {@link #newAnalyzer()}.
 * @author Sascha Kohlmann
 */
public abstract class AnalyzerFactory {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(AnalyzerFactory.class.getName());

    @SuppressWarnings("nls")
    public final static String ANALYZER_FACTORY_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.AnalyzerFactory.impl";

    public final static AnalyzerFactory newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = AnalyzerFactory.class.getClassLoader();
        }

        final String clazzName =
            config.get(ANALYZER_FACTORY_IMPL_CONFIG_NAME,
                       WhitespaceAnalyzerFactory.class.getName());

        try {
            final Class<? extends AnalyzerFactory> clazz =
                (Class<? extends AnalyzerFactory>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final AnalyzerFactory factory = clazz.newInstance();
                if (LOG.isLoggable(Level.CONFIG)) {
                    LOG.config("AnalyzerFactory instance: "
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
     * Returns a new <code>Analyzer</code> instance.
     * @return a new <code>Analyzer</code> instance
     */
    public abstract Analyzer newAnalyzer();
}
