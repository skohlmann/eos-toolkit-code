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

import java.util.Locale;

import net.sf.eos.Nullable;

/**
 * Transforms the input token to a upper or lower cased format for a given
 * {@link Locale}. The default is to change in lower case format. If the
 * user doesn't set a locale, the implementation uses the
 * {@link Locale#getDefault() default} {@code Locale}.
 * @author Sascha Kohlmann
 */
public class CaseTokenFilter extends TokenFilter {

    /** The locale for lowercase transformation. */
    private Locale locale;
    /** Indicates if to lower- or to upper-case. */
    private boolean upper;

    /** Creates a new instance for lowercase with the default {@code Locale}.
     * @param source the source tokenizer.
     */
    public CaseTokenFilter(final Tokenizer source) {
        this(source, null);
    }

    /** Creates a new instance with the default {@code Locale}.
     * @param source the source tokenizer
     * @param upper {@code true} to uppercase the token. For lowercase
     *              {@code false}.
     */
    public CaseTokenFilter(final Tokenizer source,
                           @SuppressWarnings("hiding") final boolean upper) {
        this(source, null, upper);
    }

    /** Creates a new instance for lowercase with the given {@code Locale}.
     * @param source the source tokenizer
     * @param locale the {@code Locale} to use for lowercase handling.
     */
    public CaseTokenFilter(final Tokenizer source,
                           @Nullable final Locale locale) {
        this(source, locale, false);
    }

    /** Creates a new instance.
     * @param source the source tokenizer
     * @param locale the {@code Locale} to use for case handling.
     * @param upper {@code true} to uppercase the token. For lowercase
     *              {@code false}.
     */
    public CaseTokenFilter(final Tokenizer source,
                           @Nullable final Locale locale,
                           final boolean upper) {
        super(source);
        this.locale = locale;
        this.upper = upper;
    }

    /*
     * @see net.sf.eos.analyzer.Tokenizer#next()
     */
    @Override
    public Token next() throws TokenizerException {
        final Tokenizer source = getSource();
        final Token t = source.next();
        if (t == null) {
            return null;
        }
        final CharSequence tokenText = t.getTokenText();

        final Locale l = this.locale;
        CharSequence cased = null;
        if (l != null) {
            if (this.upper) {
                cased = ("" + tokenText).toUpperCase(l);
            } else {
                cased = ("" + tokenText).toLowerCase(l);
            }
        } else {
            if (this.upper) {
                cased = ("" + tokenText).toUpperCase(Locale.getDefault());
            } else {
                cased = ("" + tokenText).toLowerCase(Locale.getDefault());
            }
        }

        assert cased != null;
        return new CasedToken(cased);
    }

    /** Token represents a cased token. */
    private final static class CasedToken extends AbstractToken {
        /** Creates a new token.
         * @param value the cased value */
        public CasedToken(final CharSequence value) {
            super(value);
        }
    }
}
