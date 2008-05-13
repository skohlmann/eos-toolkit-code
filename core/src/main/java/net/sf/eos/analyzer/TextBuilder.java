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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation creates new text sequences from {@link Token}- or
 * {@link CharSequence}-lists. Use {@link #newInstance(Configuration)} to
 * create a new instance.
 * @author Sascha Kohlmann
 */
public abstract class TextBuilder extends Configured {

    /** For logging. */
    private static final Log LOG = LogFactory.getLog(TextBuilder.class);

    /** The configuration key name for the classname of the builder.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                            description="Instances are used to create a new text "
                                        + "thru Token concationation.")
    public final static String TEXT_BUILDER_IMPL_CONFIG_NAME =
        "net.sf.eos.analyzer.TextBuilder.impl";

    /**
     * Simple implementation concats all texts from the tokens delimited
     * by space (ASCII <tt>0x20</tt>).
     */
    @SuppressWarnings("nls")
    public static final TextBuilder SPACE_BUILDER = new SpaceBuilder();

    /**
     * Simple implementation concats all texts from the tokens delimited
     * by space (ASCII <tt>0x20</tt>).
     */
    public static final class SpaceBuilder extends TextBuilder {
        @SuppressWarnings("nls")
        public final static String SPACE = " ";
        @Override
        public CharSequence buildText(final List<Token> tokens) {
            final StringBuilder sb = new StringBuilder();
            for (final Token token : tokens) {
                final CharSequence text = token.getTokenText();
                sb.append(text);
                sb.append(SPACE);
            }
            return sb.length() > 0 ? sb.subSequence(0, sb.length() - 1) : "";
        }
        @Override
        public CharSequence buildText(final Token... tokens) {
            return buildText(Arrays.asList(tokens));
        }
        @Override
        public CharSequence buildText(final CharSequence... seq) {
            final StringBuilder sb = new StringBuilder();
            for (final CharSequence cs : seq) {
                sb.append(cs);
                sb.append(SPACE);
            }
            return sb.length() > 0 ? sb.subSequence(0, sb.length() - 1) : "";
        }
    };

    /**
     * Creates a new instance of a of the builder. If the
     * {@code Configuration} contains a key
     * {@link #TEXT_BUILDER_IMPL_CONFIG_NAME} a new instance of the
     * classname of the value will instantiate. The
     * {@link #SPACE_BUILDER} will instantiate if there is no
     * value setted.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    @SuppressWarnings("nls")
    @FactoryMethod(key=TEXT_BUILDER_IMPL_CONFIG_NAME,
                   implementation=SpaceBuilder.class)
    public final static TextBuilder newInstance(final Configuration config)
            throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = TextBuilder.class.getClassLoader();
        }

        final String clazzName = config.get(TEXT_BUILDER_IMPL_CONFIG_NAME,
                                            SPACE_BUILDER.getClass().getName());
        if (clazzName.equals(SPACE_BUILDER.getClass().getName())) {
            return SPACE_BUILDER;
        }

        try {
            final Class<? extends TextBuilder> clazz = 
                (Class<? extends TextBuilder>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final TextBuilder textBuilder = clazz.newInstance();
                textBuilder.configure(config);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("TextBuilder instance: "
                               + textBuilder.getClass().getName());
                }
                return textBuilder;

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
     * Creates a new text from the given token.
     * @param tokens a list of token. If <em>tokens</em> is {@code null}
     *               an exception will raise.
     * @return a new text, never {@code null}
     */
    public abstract CharSequence buildText(final List<Token> tokens);

    /**
     * Creates a new text from the given token.
     * @param tokens a list of token If <em>tokens</em> is {@code null}
     *               an exception will raise.
     * @return a new text, never {@code null}
     */
    public abstract CharSequence buildText(final Token... tokens);

    /**
     * Creates a new text from the given {@code CharSequence}.
     * @param seq a list of {@code CharSequence} If <em>tokens</em> is
     *            {@code null} an exception will raise.
     * @return a new text, never {@code null}
     */
    public abstract CharSequence buildText(final CharSequence... seq);
}
