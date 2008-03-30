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

import net.sf.eos.analyzer.AbstractToken;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.analyzer.WhitespaceTokenizer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class WhitespaceTokenizerTest {

    @Test
    public void simpleWhitespaceTest() throws Exception {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer("a b\tc");
        assertEquals("a", tokenizer.next().getTokenText());
        assertEquals("b", tokenizer.next().getTokenText());
        assertEquals("c", tokenizer.next().getTokenText());
        assertNull(tokenizer.next());
    }

    @Test
    public void simpleResetWhitespaceTest() throws Exception {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer("a b\tc");
        assertEquals("a", tokenizer.next().getTokenText());
        assertEquals("b", tokenizer.next().getTokenText());

        tokenizer.reset("d e");
        assertEquals("d", tokenizer.next().getTokenText());
        assertEquals("e", tokenizer.next().getTokenText());

        tokenizer.reset("f");
        assertEquals("f", tokenizer.next().getTokenText());
        assertNull(tokenizer.next());
    }

    @Test
    public void simpleResetWhitespaceWithDefaultConstructorTest()
            throws Exception {
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();
        assertTrue(null == tokenizer.next());
    }

    @Test
    public void fromSourceTokenizer() throws Exception {
        final Tokenizer source = new DefaultTokenizer("h i j");
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(source);
        assertEquals("h", tokenizer.next().getTokenText());
        assertEquals("i", tokenizer.next().getTokenText());
        assertEquals("j", tokenizer.next().getTokenText());
        assertNull(tokenizer.next());
    }

    @Test
    public void fromResettableSourceTokenizer() throws Exception {
        final ResettableTokenizer source = new DefaultTokenizer("h i j");
        final WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(source);
        assertEquals("h", tokenizer.next().getTokenText());
        assertEquals("i", tokenizer.next().getTokenText());
        assertEquals("j", tokenizer.next().getTokenText());
        assertNull(tokenizer.next());

        source.reset("1 2");
        assertEquals("1", tokenizer.next().getTokenText());
        assertEquals("2", tokenizer.next().getTokenText());
        assertNull(tokenizer.next());
    }

    final static class DefaultTokenizer implements ResettableTokenizer {

        private Token token = null;

        public DefaultTokenizer(final CharSequence seq) {
            this.token = new AbstractToken(seq) {};
        }

        public Token next() throws TokenizerException {
            final Token retval = this.token;
            this.token = null;
            return retval;
        }

        public void reset(final CharSequence input) throws TokenizerException {
            this.token = new AbstractToken(input) {};
        }
    }
}
