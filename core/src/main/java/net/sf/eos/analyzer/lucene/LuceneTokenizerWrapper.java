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
package net.sf.eos.analyzer.lucene;

import net.sf.eos.analyzer.AbstractToken;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.TokenFilter;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.TokenizerException;

import java.io.IOException;

/**
 * Wraps the functionality of the &#949;&#959;s {@link Tokenizer} for the
 * reuse of Lucene tokenizer.
 * @author Sascha Kohlmann
 */
public final class LuceneTokenizerWrapper extends TokenFilter {

    private final org.apache.lucene.analysis.Tokenizer delagate;
    private org.apache.lucene.analysis.Token luceneToken;

    /** Creates a new wrapper.
     * @param tokenizer a Lucene tokenizer to reuse in the &#949;&#959;s
     *                  environment
     */
    public LuceneTokenizerWrapper(final org.apache.lucene.analysis.Tokenizer tokenizer) {
        this(new NullTokenizer(), tokenizer);
    }

    /**
     * Creates a new wrapper.
     * @param source a &#949;&#959;s tokenizer
     * @param tokenizer a Lucene tokenizer to reuse in the &#949;&#959;s
     *                  environment
     */
    public LuceneTokenizerWrapper(
            final Tokenizer source,
            final org.apache.lucene.analysis.Tokenizer tokenizer) {
        super(source);
        this.delagate = tokenizer;
    }

    /*
     * @see net.sf.eos.analyzer.TokenFilter#next()
     */
    @Override
    public Token next() throws TokenizerException {
        if (this.luceneToken == null) {
            nextLuceneToken();
            if (this.luceneToken != null) {
                final org.apache.lucene.analysis.Token retval =
                    (org.apache.lucene.analysis.Token) this.luceneToken.clone();
                 return new LuceneTokenWrapper(retval);
                
            }
        }

        if (this.luceneToken == null) {
            return null;
        }

        try {
            this.luceneToken = this.delagate.next(this.luceneToken);
            if (this.luceneToken == null) {
                nextLuceneToken();
                if (this.luceneToken != null) {
                    final org.apache.lucene.analysis.Token retval =
                        (org.apache.lucene.analysis.Token) this.luceneToken.clone();
                    return new LuceneTokenWrapper(retval);
                }
                return null;
            }
        } catch (final IOException e) {
            throw new TokenizerException(e);
        }

        final org.apache.lucene.analysis.Token retval =
           (org.apache.lucene.analysis.Token) this.luceneToken.clone();
        return new LuceneTokenWrapper(retval);
    }

    void nextLuceneToken() throws TokenizerException {
        final Tokenizer tokenizer = getSource();
        if (tokenizer.getClass() == NullTokenizer.class) {
            try {
                this.luceneToken = this.delagate.next();
            } catch (final IOException e) {
                throw new TokenizerException(e);
            }
        } else {
            final Token token = tokenizer.next();
            if (token == null) {
                return;
            }
            final CharSequence seq = token.getTokenText();
            this.luceneToken = new org.apache.lucene.analysis.Token("" + seq,
                    0, seq.length());
        }
    }

    private final static class LuceneTokenWrapper extends AbstractToken {

        private final org.apache.lucene.analysis.Token token;
        private final String type;

        public LuceneTokenWrapper(final org.apache.lucene.analysis.Token token)
        {
            super("");
            this.token = token;
            this.type = token.type();
        }

        @Override
        public CharSequence getTokenText() {
            final char[] termBuffer = this.token.termBuffer();
            final int termLength = this.token.termLength();
            return new String(termBuffer, 0, termLength);
        }

        @Override
        public String getType() {
            return this.type;
        }
    }
}
