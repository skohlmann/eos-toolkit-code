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


import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation creats new text sequences.
 * @author Sascha Kohlmann
 */
public abstract class TextBuilder {

    public final static String TEXT_BUILDER_IMPL_CONFIG_NAME =
        "net.sf.eos.analyzer.TextBuilder.impl";

    /**
     * Simple implementation concats all texts from the tokens delimited
     * by space (ASCII <tt>0x20</tt>).
     */
    public static final TextBuilder SPACE_BUILDER = new TextBuilder() {
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

    public final static TextBuilder newInstance() throws EosException {
        final Configuration config = new Configuration();
        return newInstance(config);
    }

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
     * @param tokens a list of token. If <em>tokens</em> is <code>null</code>
     *               an exception will raise.
     * @return a new text, never <code>null</code>
     */
    public abstract CharSequence buildText(final List<Token> tokens);

    /**
     * Creates a new text from the given token.
     * @param tokens a list of token If <em>tokens</em> is <code>null</code>
     *               an exception will raise.
     * @return a new text, never <code>null</code>
     */
    public abstract CharSequence buildText(final Token... tokens);

    /**
     * Creates a new text from the given <code><CharSequence/code>.
     * @param seq a list of <code><CharSequence/code> If <em>tokens</em> is
     *            <code>null</code> an exception will raise.
     * @return a new text, never <code>null</code>
     */
    public abstract CharSequence buildText(final CharSequence... seq);
}
