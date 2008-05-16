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


import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// import net.sf.eos.Provider;
import net.sf.eos.Provider;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;
import net.sf.eos.medline.MedlineTokenizerProvider;

/**
 * Support class for {@link ResettableTokenizer}.
 * @author Sascha Kohlmann
 * @see ResettableTokenFilter
 */
public abstract class TokenizerProvider extends Configured
        implements Provider<ResettableTokenizer> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(TokenizerProvider.class.getName());

    /** The configuration key name for the classname of the provider.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Provider supports the creation of stacked "
                                        + "Tokenizers following the decoration "
                                        + "pattern.")
    public final static String TOKENIZER_PROVIDER_IMPL_CONFIG_NAME =
        "net.sf.eos.analyzer.TokenizerProvider.impl";

    /**
     * Creates a new instance. The
     * {@code Configuration} must contain a key
     * {@link #TOKENIZER_PROVIDER_IMPL_CONFIG_NAME} of a provider implementation. There 
     * is no default implementation.
     * @param config the configuration
     * @return a new instance
     * @throws TokenizerException if it is not possible to instantiate an instance
     */
    @SuppressWarnings("nls")
    @FactoryMethod(key=TOKENIZER_PROVIDER_IMPL_CONFIG_NAME,
                   implementation=MedlineTokenizerProvider.class)
    public final static TokenizerProvider newInstance(final Configuration config)
            throws TokenizerException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = TokenizerProvider.class.getClassLoader();
        }

        final String clazzName = config.get(TOKENIZER_PROVIDER_IMPL_CONFIG_NAME,
                                            MedlineTokenizerProvider.class.getName());

        try {
            final Class<? extends TokenizerProvider> clazz = 
                (Class<? extends TokenizerProvider>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final TokenizerProvider builder = clazz.newInstance();
                builder.configure(config);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("TokenizerProvider instance: "
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
    public abstract ResettableTokenizer get();
}
