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

import java.util.HashSet;
import java.util.Set;

/**
 * Filter for stop words out of the {@linkplain Token token} stream.
 * @author Sascha Kohlmann
 */
public class StopTokenFilter extends TokenFilter {

    private final Set<CharSequence> stopWords = new HashSet<CharSequence>();

    /**
     * Creates a new instance.
     * @param source the source filter.
     * @param stopWords a collection of stop words
     */
    public StopTokenFilter(final Tokenizer source,
                @SuppressWarnings("hiding") final Set<CharSequence> stopWords) {
        super(source);
        this.stopWords.addAll(stopWords);
    }

    @Override
    public Token next() throws TokenizerException {
        final Tokenizer tokenizer = getSource();
        Token retval = null;

        do {
            final Token inner = tokenizer.next();
            if (inner == null) {
                return null;
            }
            final CharSequence seq = inner.getTokenText();
            if (! this.stopWords.contains(seq)) {
                retval = inner;
            }
        } while (retval == null);

        return retval;
    }
}
