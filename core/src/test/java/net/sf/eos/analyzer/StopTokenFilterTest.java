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
import static org.junit.Assert.assertNull;

import net.sf.eos.analyzer.StopTokenFilter;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.WhitespaceTokenizer;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class StopTokenFilterTest {

    @Test
    public void simpleStopWords() throws Exception {
        final Tokenizer source = new WhitespaceTokenizer("the quick brown");
        final Set<CharSequence> stopWords = 
            new HashSet<CharSequence>(Arrays.asList("the"));
        final Tokenizer stop = new StopTokenFilter(source, stopWords);

        assertEquals("quick", stop.next().getTokenText());
        assertEquals("brown", stop.next().getTokenText());
        assertNull(stop.next());
    }

    @Test
    public void simpleStopWordsAtEnd() throws Exception {
        final Tokenizer source = new WhitespaceTokenizer("the quick brown");
        final Set<CharSequence> stopWords = 
            new HashSet<CharSequence>(Arrays.asList("brown"));
        final Tokenizer stop = new StopTokenFilter(source, stopWords);

        assertEquals("the", stop.next().getTokenText());
        assertEquals("quick", stop.next().getTokenText());
        assertNull(stop.next());
    }

    @Test
    public void simpleStopWordsNoWords() throws Exception {
        final Tokenizer source = new WhitespaceTokenizer("the quick brown");
        final Set<CharSequence> stopWords = 
            new HashSet<CharSequence>(Arrays.asList("brown", "quick", "the"));
        final Tokenizer stop = new StopTokenFilter(source, stopWords);

        assertNull(stop.next());
    }
}
