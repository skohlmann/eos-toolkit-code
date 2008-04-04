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
package net.sf.eos.analyzer;


import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.medline.MedlineTokenizerBuilder;

/**
 * Support class for {@link ResettableTokenizer}.
 * @author Sascha Kohlmann
 * @see ResettableTokenFilter
 */
public abstract class TokenizerBuilder extends Configured {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(TokenizerBuilder.class.getName());

    /** The configuration key name for the classname of the builder.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    public final static String BUILDER_IMPL_CONFIG_NAME =
        "net.sf.eos.analyzer.TokenizerBuilder.impl";

    /**
     * Creates a new instance. The
     * <code>Configuration</code> must contain a key
     * {@link #BUILDER_IMPL_CONFIG_NAME} of a builder implementation. There 
     * is no default implementation.
     * @param config the configuration
     * @return a new instance
     * @throws TokenizerException if it is not possible to instantiate an instance
     */
    @SuppressWarnings("nls")
    public final static TokenizerBuilder newInstance(final Configuration config)
            throws TokenizerException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = TokenizerBuilder.class.getClassLoader();
        }

        final String clazzName = config.get(BUILDER_IMPL_CONFIG_NAME,
                                      MedlineTokenizerBuilder.class.getName());

        try {
            final Class<? extends TokenizerBuilder> clazz = 
                (Class<? extends TokenizerBuilder>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final TokenizerBuilder builder = clazz.newInstance();
                builder.configure(config);
                if (LOG.isLoggable(Level.CONFIG)) {
                    LOG.config("TokenizerBuilder instance: "
                               + builder.getClass().getName());
                }
                return builder;

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
     * Creates a new instance.
     * @return a new instance
     * @throws TokenizerException if an error occurs
     */
    public abstract ResettableTokenizer newResettableTokenizer()
        throws TokenizerException;
}
