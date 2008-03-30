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

/**
 * <p>A token filter that supports handling with resettable tokenizer. Use case
 * is a tokenizer chain where the last tokenizer in the chain is a resettable
 * tokenizer. The implementation fetch the next token from the <em>source</em>
 * token filter and resets the <code>CharSequence</code> to the 
 * <em>resettable</em> tokenizer as bypass. With this construct it's possible
 * to reuse a complete tokenizer chain.</p>
 * <p>Avoid using a <code>ResettableTokenFilter</code> if the tokenizer chain
 * isn't empty e.g. {@link #next()} returns <code>null</code>.</p>
 * @author Sascha Kohlmann
 */
public final class ResettableTokenFilter extends TokenFilter
                                         implements ResettableTokenizer {

    /** The resettable instance. */
    private ResettableTokenizer resettable;

    /**
     * Creates a new instance with the source using described in 
     * {@link TokenFilter}.
     * <p>The <em>source</em> tokenizer is called by {@link #next()}. The
     * <em>resettable</em> tokenizer is used if the {@link #reset(CharSequence)}
     * method is called.</p>
     * @param source the source tokenizer
     * @param resettable normaly the last tokenizer in the chain.
     */
    @SuppressWarnings("nls")
    public ResettableTokenFilter(final Tokenizer source,
            @SuppressWarnings("hiding") final ResettableTokenizer resettable) {
        super(source);
        if (resettable == null) {
            throw new IllegalStateException("resettable is null");
        }
        this.resettable = resettable;
    }

    /**
     * Usefull if the tokenizer are the same.
     * @param resettableSource the source tokenizer
     */
    public ResettableTokenFilter(final ResettableTokenizer resettableSource) {
        this(resettableSource, resettableSource);
    }

    /*
     * @see net.sf.eos.analyzer.ResettableTokenizer#reset(java.lang.CharSequence)
     */
    public void reset(final CharSequence input) throws TokenizerException {
        this.resettable.reset(input);
    }

    /*
     * @see net.sf.eos.analyzer.Tokenizer#next()
     */
    @Override
    public Token next() throws TokenizerException {
        final Tokenizer t = getSource();
        assert t != null;
        return t.next();
    }
}
