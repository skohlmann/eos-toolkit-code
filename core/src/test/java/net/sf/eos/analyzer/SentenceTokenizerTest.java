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

import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.Tokenizer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;


public class SentenceTokenizerTest {

    @Test
    public void simple() throws Exception {
        final String sentence1 = "the quick brown fox!";
        final String sentence2 = "jumps over the lazy dog.";
        final String twoSentences = sentence1 + " " + sentence2;

        final Tokenizer tokenizer = new SentenceTokenizer(twoSentences);
        assertEquals(sentence1, tokenizer.next().getTokenText());
        assertEquals(sentence2, tokenizer.next().getTokenText());
        assertTrue(null == tokenizer.next());
    }

    @Test
    public void simpleEmpty() throws Exception {
        final Tokenizer tokenizer = new SentenceTokenizer();
        assertNull(tokenizer.next());
    }
}
