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
import net.sf.eos.EosException;
import net.sf.eos.Supplier;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;


/**
 * To support different strategies of Lucene analyzers this
 * factory decoupled the creation of the analyzer from hard coded classnames.
 * Set the classname of a factory different from
 * <i>{@linkplain WhitespaceAnalyzerSupplier default}</i> implementation.
 * {@link #ANALYZER_PROVIDER_IMPL_CONFIG_NAME} contains the name of the
 * configuration key.
 *
 * <p>Implementations must have a default constructor and must implement
 * {@link #get()}.</p>
 * @author Sascha Kohlmann
 */
public abstract class AnalyzerSupplier implements Supplier<Analyzer> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(AnalyzerSupplier.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Configuration key of the Lucene analyzer "
                                        + " factory.")
    public final static String ANALYZER_PROVIDER_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.AnalyzerProvider.impl";

    /**
     * Creates a new instance of a of the provider. If the
     * {@code Configuration} contains a key
     * {@link #ANALYZER_PROVIDER_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link WhitespaceAnalyzerSupplier} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    public final static AnalyzerSupplier newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = AnalyzerSupplier.class.getClassLoader();
        }

        final String clazzName =
            config.get(ANALYZER_PROVIDER_IMPL_CONFIG_NAME,
                       WhitespaceAnalyzerSupplier.class.getName());

        try {
            final Class<? extends AnalyzerSupplier> clazz =
                (Class<? extends AnalyzerSupplier>) Class
                    .forName(clazzName, true, classLoader);
            try {

                final AnalyzerSupplier factory = clazz.newInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("AnalyzerProvider instance: "
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
     * Returns a new {@code Analyzer} instance.
     * @return a new {@code Analyzer} instance
     */
    public abstract Analyzer get();
}
