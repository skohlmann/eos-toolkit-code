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


import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.document.EosDocument;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Sentencer extends Configured {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(Sentencer.class.getName());

    @SuppressWarnings("nls")
    public static final String SENTENCER_HASH_CONFIG_NAME =
        "net.sf.eos.sentence.Sentencer.hash";
    @SuppressWarnings("nls")
    public static final String DEFAULT_HASH = "md5";

    @SuppressWarnings("nls")
    public final static String SENTENCER_IMPL_CONFIG_NAME =
        "net.sf.eos.sentence.Sentencer.impl";

    public final static Sentencer newInstance() throws EosException
    {
        final Configuration config = new Configuration();
        return newInstance(config);
    }

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

    protected Sentencer() {
        super();
    }

    protected MessageDigest createDigester() throws EosException {
        try {
            final Configuration config = getConfiguration();
            String algorithm = DEFAULT_HASH;
            if (config != null) {
                algorithm = config.get(SENTENCER_HASH_CONFIG_NAME,
                                       DEFAULT_HASH);
            }
            MessageDigest md;
            md = MessageDigest.getInstance(algorithm);
            return md;
        } catch (final NoSuchAlgorithmException e) {
            throw new EosException(e);
        }
    }

    public abstract Map<String, EosDocument> 
        toSentenceDocuments(final EosDocument doc,
                            final SentenceTokenizer sentencer,
                            final ResettableTokenizer tokenizer,
                            final TextBuilder builder)
            throws EosException;
}
