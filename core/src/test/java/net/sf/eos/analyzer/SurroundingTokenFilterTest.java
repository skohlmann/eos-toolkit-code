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

import static org.junit.Assert.assertEquals;

import net.sf.eos.analyzer.SurroundingTokenFilter;

import org.junit.Before;
import org.junit.Test;


public class SurroundingTokenFilterTest {

    private SurroundingTokenFilter tokenizer;

    @Test
    public void correctSurroundingCharsWithEndingDoubleQuote() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("test\"");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingDoubleQuote() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("\"test");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingAndEndingDoubleQuote() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("\"test\"");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLeftParenthesis() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("(test");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLeftParenthesisAndEndingRightParenthesis() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("(test)");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithEndingRightParenthesis() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("test)");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLessThanSign() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("<test");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithTwoStartingLessThanSign() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("<<test");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLessThanSignAndEndingGreaterThanSign() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("<test>");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLessThanSignAndTwoEndingGreaterThanSign() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("<test>>");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithEndingGreaterThanSign() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("test>");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLeftSquareBracket() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("[test");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLeftSquareBracketAndEndingRightSquareBracket() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("[test]");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithEndingRightSquareBracket() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("test]");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLeftCurlyBracket() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("{test");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingLeftCurlyBracketAndEndingRightCurlyBracket() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("{test}");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithEndingRightCurlyBracket() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("test}");
        assertEquals("test", s);
    }

    @Test
    public void correctSurroundingCharsWithStartingAndMiddleRoundBrace() {
        final CharSequence s = this.tokenizer.correctSurroundingChars("(t)-est");
        assertEquals("(t)-est", s);
    }

    @Test(expected=IllegalArgumentException.class)
    public void correctSurroundingCharsWithNull() {
        this.tokenizer.correctSurroundingChars(null);
    }

    @Before
    public void creatMapper() {
        this.tokenizer = new SurroundingTokenFilter(new NullTokenizer());
    }
}
