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

import java.text.BreakIterator;
import java.util.Locale;

import static net.sf.eos.util.Conditions.checkArgumentNotNull;

/**
 * Tokenizes a text into sentences.
 * <p>Based on {@link BreakIterator#getLineInstance(Locale)}.</p>
 * @author Sascha Kohlmann
 */
public class SentenceTokenizer /*extends Configured*/
                               implements ResettableTokenizer {

    public final static String SENTENCE_TYPE = "sentence";

    private BreakIterator itr;
//    private Locale locale;
    private int start;
    private String text;

    public SentenceTokenizer() {
        this("");
    }

    /**
     * Creates a new tokenizer. Uses {@link Locale#getDefault() default Locale}.
     * @param text the text to tokenize into sentences.
     */
    public SentenceTokenizer(
            @SuppressWarnings("hiding") final CharSequence text) {
        this(text, Locale.getDefault());
    }

    /**
     * Creates a new tokenizer.
     * @param text the text to tokenize into sentences.
     * @param locale
     */
    public SentenceTokenizer(
            @SuppressWarnings("hiding") final CharSequence text,
            @SuppressWarnings("hiding") final Locale locale) {
        checkArgumentNotNull(text, "text is null");
        checkArgumentNotNull(locale, "locale is null");

        this.itr = BreakIterator.getSentenceInstance(locale);
        final String toTokenize = text.toString();
        this.itr.setText(toTokenize);
//        this.locale = locale;
        this.start = this.itr.first();
        this.text = toTokenize;
    }

    /*
     * @see net.sf.eos.analyzer.Tokenizer#next()
     */
    public Token next() throws TokenizerException {
        final CharSequence sentence = nextSentence();
        if (sentence != null) {
            return new SentenceToken(sentence, SENTENCE_TYPE);
        }
        return null;
    }

    /*
     * @see net.sf.eos.analyzer.Tokenizer#reset(java.lang.CharSequence)
     */
    public void reset(final CharSequence input) throws TokenizerException {
        final String t = input.toString();
        this.itr.setText(t);
        this.start = this.itr.first();
        this.text = t;
    }

    /**
     * Override this method to implement a different sentence tokenizer.
     * @return a sentence or {@code null} if no next sentence available.
     * @throws TokenizerException if an error occurs
     */
    protected CharSequence nextSentence() throws TokenizerException {
        final int end = this.itr.next();
        if (end == BreakIterator.DONE) {
            return null;
        }
        final String sentence = this.text.substring(this.start, end).trim();
        this.start = end;

        return sentence;
    }

    /** Token represents sentence as token. */
    private final static class SentenceToken extends AbstractToken {
        /** Creates a new token representing a sentence.
         * @param value a sentence */
        public SentenceToken(final CharSequence value, final String type) {
            super(value, type);
        }
    }
}
