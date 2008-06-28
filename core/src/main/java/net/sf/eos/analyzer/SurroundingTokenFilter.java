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
 * <p>The filter removes surrounding braces and other characters around a
 * token text.</p>
 * <p>The open a close characters are:</p>
 * <table border='1'>
 *   <tr align='center'><th>opening</th><th>closing</th></tr>
 *   <tr align='center'><td><tt>(</tt></td><td><tt>)</tt></td></tr>
 *   <tr align='center'><td><tt>[</tt></td><td><tt>]</tt></td></tr>
 *   <tr align='center'><td><tt>&lt;</tt></td><td><tt>&gt;</tt></td></tr>
 *   <tr align='center'><td><tt>{</tt></td><td><tt>}</tt></td></tr>
 *   <tr align='center'><td><tt>&#xab;</tt></td><td><tt>&#xbb;</tt></td></tr>
 *   <tr align='center'><td><tt>&#x201c;</tt></td><td><tt>&#x201d;</tt></td></tr>
 *   <tr align='center'><td><tt>&#x2018;</tt></td><td><tt>&#x2019;</tt></td></tr>
 * </table>
 *
 * @author Sascha Kohlmann
 */
public class SurroundingTokenFilter extends TokenFilter {

    static final char[] OPEN_CHARS =
        new char[] {'"', '\'', '\u00ab', '\u201c', '\u2018', '(', '[', '<', '{'};
    static final char[] CLOSE_CHARS =
        new char[] {'"', '\'', '\u00bb', '\u201d', '\u2019', ')', ']', '>', '}'};

    private static final String EMPTY = "";

    public SurroundingTokenFilter(final Tokenizer tokenizer) {
        super(tokenizer);
    }

    /**
     * Removes surrounding characters from the token text.
     */
    @Override
    public Token next() throws TokenizerException {
        final Tokenizer source = getSource();
        final Token t = source.next();
        if (t == null) {
            return null;
        }
        final CharSequence tokenText = t.getTokenText();
        final CharSequence seq = this.correctSurroundingChars(tokenText);
        return new SurroundingToken(seq);
    }

    /**
     * Remove the surrounding characters.
     * @param term the term to remove
     * @return the cleaned term
     */
    final CharSequence correctSurroundingChars(final CharSequence term) {
        if (term == null) {
            throw new IllegalArgumentException("term is null");
        }
        if (term.length() == 0) {
            return EMPTY;
        }
        
        final char[] chars = term.toString().toCharArray();
        int start = 0;
        int length = chars.length;
        for (int i = 0; i < (chars.length - 1); i++) {
            for (int j = 0; j < OPEN_CHARS.length; j++) {
                if (chars[i] == OPEN_CHARS[j]) {
                    if (chars[chars.length - 1] == CLOSE_CHARS[j]) {
                        if ((start + 1) >= length || (length - 2) <= 0) {
                            return EMPTY;
                        }
                        final String s  = new String(chars, 1, length - 2);
                        return correctSurroundingChars(s);
                    }
                    final int open = countCharInTerm(chars, OPEN_CHARS[j]);
                    final char[] second = new char[chars.length - 1];
                    System.arraycopy(chars, 1, second, 0, chars.length - 1);
                    final int close = countCharInTerm(second, CLOSE_CHARS[j]);
                    if (open != close) {
                        final String s  = new String(chars, 1, length - 1);
                        return correctSurroundingChars(s);
                    }
                }
            }
        }

        for (int i = chars.length - 1 ; i >= 0; i--) {
            boolean found = false;
            for (int j = 0; j < CLOSE_CHARS.length; j++) {
                if (chars[i] == CLOSE_CHARS[j]) {
                    final String s = new String(chars, 0, chars.length - 1);
                    return correctSurroundingChars(s);
                }
            }
            if (!found) {
                break;
            }
        }
        
        return term;
    }

    final int countCharInTerm(final char[] chars, final char c) {
        if (chars == null) {
            throw new IllegalArgumentException("chars is null");
        }
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                count++;
            }
        }
        
        return count;
    }

    /** Token represents a surrounding freed token. */
    private final static class SurroundingToken extends AbstractToken {
        /** Creates a new token.
         * @param value the freed value */
        public SurroundingToken(final CharSequence value) {
            super(value);
        }
    }
}
