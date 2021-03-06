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

import static net.sf.eos.util.Conditions.checkArgumentNotNull;

/**
 * Main class to support {@code Tokenizer} chaining, also known as
 * <a href='http://en.wikipedia.org/wiki/Decorator_pattern'>decorator pattern</a>.
 * An implementation holds a <em>source</em> {@code Tokenizer}, available
 * thru {@link #getSource()}. Implementations must implement
 * {@link #next()} to process a {@link Token} from the <em>source</em>.
 * It is highly recommended that the implementation supports the metadata
 * handling.
 * @author Sascha Kohlmann
 * @see ResettableTokenFilter
 */
public abstract class TokenFilter /*extends Configured*/
                                  implements Tokenizer {

    private final Tokenizer source;

    /**
     * Creates a new instance.
     * @param source the source {@code Tokenizer}.
     * @see #getSource()
     */
    @SuppressWarnings("nls")
    public TokenFilter(@SuppressWarnings("hiding") final Tokenizer source) {
        this.source = checkArgumentNotNull(source, "source is null");
    }

    /**
     * Implementations should use {@link #getSource()} to fetch the source
     * {@code Tokenizer} an handle the resulting {@code Token}.
     * @return the next token or {@code null}
     * @throws TokenizerException if an error occurs
     */
    public abstract Token next() throws TokenizerException;

    /**
     * Returns the source {@code Tokenizer}.
     * @return the source {@code Tokenizer}
     */
    protected Tokenizer getSource() {
        assert this.source != null;
        return this.source;
    }
}
