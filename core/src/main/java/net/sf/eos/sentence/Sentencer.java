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
package net.sf.eos.sentence;

import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;
import net.sf.eos.document.EosDocument;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * The implementation fragmented {@link EosDocument} with more then one sentence
 * in a lot of sentences with maybe only one sentence. Each sentence is also
 * represented by a hashcode. The hashcode is able to support removing double
 * sentences from a corpus.
 * @author Sascha Kohlmann
 */
public abstract class Sentencer extends Configured {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(Sentencer.class.getName());

    /** The default message digest algorithm. */
    @SuppressWarnings("nls")
    public static final String DEFAULT_MESSAGE_DIGEST = "md5";

    /** The name of the algorithm of the message digest. */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            defaultValue=DEFAULT_MESSAGE_DIGEST,
                            description="The message digest.")
    public static final String MESSAGE_DIGEST_CONFIG_NAME =
        "net.sf.eos.sentence.Sentencer.messageDigest";

    /** The configuration key name for the classname of the implementation.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Configuration key of the sentencer.")
    public final static String SENTENCER_IMPL_CONFIG_NAME =
        "net.sf.eos.sentence.Sentencer.impl";

    /**
     * Creates a new instance of a of the implementation. If the
     * {@code Configuration} contains a key
     * {@link #SENTENCER_IMPL_CONFIG_NAME} a new instance of the
     * classname in the value will instantiate. The 
     * {@link DefaultSentencer} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    @SuppressWarnings("nls")
    @FactoryMethod(key=SENTENCER_IMPL_CONFIG_NAME,
                   implementation=DefaultSentencer.class)
    public final static Sentencer newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = Sentencer.class.getClassLoader();
        }

        final String clazzName = config.get(SENTENCER_IMPL_CONFIG_NAME,
                                            DefaultSentencer.class.getName());

        try {
            final Class<? extends Sentencer> clazz = 
                (Class<? extends Sentencer>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final Sentencer sentencer = clazz.newInstance();
                sentencer.configure(config);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sentencer instance: "
                              + sentencer.getClass().getName());
                }
                return sentencer;

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
     */
    protected Sentencer() {
        super();
    }

    /**
     * Returns the message digest implementation. If the
     * {@link #configure(Configuration) configuration} contains no value
     * for the key {@link #MESSAGE_DIGEST_CONFIG_NAME} the
     * <em>{@linkplain #DEFAULT_MESSAGE_DIGEST default}</em> digest will be
     * used.
     * @return the message digest
     * @throws EosException if it is not possible to create the message digest
     */
    protected MessageDigest createDigester() throws EosException {
        try {
            final Configuration config = getConfiguration();
            String algorithm = DEFAULT_MESSAGE_DIGEST;
            if (config != null) {
                algorithm = config.get(MESSAGE_DIGEST_CONFIG_NAME,
                                       DEFAULT_MESSAGE_DIGEST);
            }
            MessageDigest md;
            md = MessageDigest.getInstance(algorithm);
            return md;
        } catch (final NoSuchAlgorithmException e) {
            throw new EosException(e);
        }
    }

    /**
     * Fragments a document into documents of sentences. The return value is
     * a map of {@linkplain #createDigester() message digests} and sentenced
     * document. The documents of the return value has all metada data of the
     * original document and maybe additional metadata.
     * 
     * @param doc the document to fragment
     * @param sentencer a sentencer instance
     * @param tokenizer a tokenizer instance to tokenize the result of the
     *                  <em>sentencer</em>
     * @param builder the builder supports the rebuilding of the
     *                <em>tokenizer</em>
     * @return a map of message digest <tt>-></tt> document relations
     * @throws EosException if an error occurs
     */
    public abstract Map<String, EosDocument> 
        toSentenceDocuments(final EosDocument doc,
                            final SentenceTokenizer sentencer,
                            final ResettableTokenizer tokenizer,
                            final TextBuilder builder)
            throws EosException;
}
