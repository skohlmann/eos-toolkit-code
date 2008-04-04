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


import net.sf.eos.analyzer.lucene.LuceneTokenizerWrapper;
import net.sf.eos.config.Configured;

import org.apache.commons.io.input.CharSequenceReader;

import java.io.IOException;
import java.io.Reader;

/**
 * Tokenized a sequence of chars at whitespaces. Wrapper around Lucenes
 * <code>WhitespaceTokenizer</code>.
 * @author Sascha Kohlmann
 */
public final class WhitespaceTokenizer extends TokenFilter
                                       implements ResettableTokenizer{

    private final static Tokenizer NULL = new NullTokenizer();

    private LuceneTokenizerWrapper tokenizer;
    private org.apache.lucene.analysis.WhitespaceTokenizer wrapped;

    /**
     * Creates a new instance.
     * @param source a source filter
     */
    public WhitespaceTokenizer(final Tokenizer source) {
        super(source);
        final Reader reader = new CharSequenceReader("");
        this.wrapped = 
            new org.apache.lucene.analysis.WhitespaceTokenizer(reader);
        this.tokenizer = new LuceneTokenizerWrapper(this.wrapped);
    }

    /**
     * Creates a new instance.
     */
    public WhitespaceTokenizer() {
        this("");
    }

    /**
     * Creates a new instance for a char sequence.
     * @param text the sequence to tokenize
     */
    public WhitespaceTokenizer(final CharSequence text) {
        super(NULL);
        final Reader reader = new CharSequenceReader(text);
        this.wrapped = 
            new org.apache.lucene.analysis.WhitespaceTokenizer(reader);
        this.tokenizer = new LuceneTokenizerWrapper(this.wrapped);
    }

    /*
     * @see net.sf.eos.analyzer.Tokenizer#next()
     */
    @Override
    public Token next() throws TokenizerException {
        Token retval = this.tokenizer.next();
        if (retval == null) {
            final Tokenizer source = getSource();
            if (source.getClass() != NULL.getClass()) {
                final Token sourceToken = source.next();
                if (sourceToken != null) {
                    final CharSequence seq = sourceToken.getTokenText();
                    final Reader reader = new CharSequenceReader(seq);
                    try {
                        this.wrapped.reset(reader);
                    } catch (final IOException e) {
                        throw new TokenizerException(e);
                    }
                    retval = this.tokenizer.next();
                }
            }
        }
        return retval;
    }

    /*
     * @see net.sf.eos.analyzer.ResettableTokenizer#reset(java.lang.CharSequence)
     */
    public void reset(final CharSequence input) throws TokenizerException {
        assert this.wrapped != null;
        final Tokenizer source = getSource();
        if (source.getClass() != NULL.getClass() 
                && source instanceof ResettableTokenizer) {
            final ResettableTokenizer resettable = (ResettableTokenizer) source;
            resettable.reset(input);
        } else {
            final CharSequenceReader reader = new CharSequenceReader(input);
            try {
                this.wrapped.reset(reader);
            } catch (final IOException e) {
                throw new TokenizerException(e);
            }
        }
    }

    /**
     * Return value may be <code>null</code>.
     * @return may be <code>null</code>
     */
    @Override
    protected Tokenizer getSource() {
        return super.getSource();
    }

    private final static class NullTokenizer extends Configured
                                             implements Tokenizer {
        public Token next() throws TokenizerException {
            return null;
        }
    }
}
