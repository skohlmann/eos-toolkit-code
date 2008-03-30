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
package net.sf.eos.entity;

import static net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer.ENTITY_ID_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;



import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.WhitespaceTokenizer;
import net.sf.eos.entity.EntityRecognizer;
import net.sf.eos.entity.SimpleLongestMatchDictionaryBasedEntityRecognizer;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SimpleLongestMatchDictionaryBasedEntityRecognizerTest {

    @Test
    public void simple() throws Exception {
        final Map<CharSequence, Set<CharSequence>> map =
            new HashMap<CharSequence, Set<CharSequence>>();
        map.put("f", Collections.EMPTY_SET);
        map.put("a b", Collections.EMPTY_SET);
        map.put("a b c d", Collections.EMPTY_SET);

        final WhitespaceTokenizer tokenizer =
            new WhitespaceTokenizer("a b e a b c d e f g");
        final SimpleLongestMatchDictionaryBasedEntityRecognizer rc =
            new SimpleLongestMatchDictionaryBasedEntityRecognizer(tokenizer);

        rc.setEntityMap(map);

        Token t = rc.next();
        assertEquals("a b", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());
        
        t = rc.next();
        assertEquals("e", t.getTokenText());
        assertEquals(Token.DEFAULT_TYPE, t.getType());

        t = rc.next();
        assertEquals("a b c d", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());

        t = rc.next();
        assertEquals("e", t.getTokenText());
        assertEquals(Token.DEFAULT_TYPE, t.getType());

        t = rc.next();
        assertEquals("f", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());

        t = rc.next();
        assertEquals("g", t.getTokenText());
        assertEquals(Token.DEFAULT_TYPE, t.getType());

        assertNull(rc.next());
    }

    @Test
    public void longestMatch() throws Exception {
        final Map<CharSequence, Set<CharSequence>> map =
            new HashMap<CharSequence, Set<CharSequence>>();
        map.put("f", Collections.EMPTY_SET);
        map.put("a b", Collections.EMPTY_SET);
        map.put("a b c d", Collections.EMPTY_SET);

        final WhitespaceTokenizer tokenizer =
            new WhitespaceTokenizer("f a b a b c d");
        final SimpleLongestMatchDictionaryBasedEntityRecognizer rc =
            new SimpleLongestMatchDictionaryBasedEntityRecognizer(tokenizer);

        rc.setEntityMap(map);

        Token t = rc.next();
        assertEquals("f", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());

        t = rc.next();
        assertEquals("a b", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());

        t = rc.next();
        assertEquals("a b c d", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());
    }

    @Test
    public void longestMatch2() throws Exception {
        final Map<CharSequence, Set<CharSequence>> map =
            new HashMap<CharSequence, Set<CharSequence>>();
        map.put("f", Collections.EMPTY_SET);
        map.put("a b", Collections.EMPTY_SET);
        map.put("a b c d", Collections.EMPTY_SET);

        final WhitespaceTokenizer tokenizer =
            new WhitespaceTokenizer("f a b c d a b");
        final SimpleLongestMatchDictionaryBasedEntityRecognizer rc =
            new SimpleLongestMatchDictionaryBasedEntityRecognizer(tokenizer);

        rc.setEntityMap(map);

        Token t = rc.next();
        assertEquals("f", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());

        t = rc.next();
        assertEquals("a b c d", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());

        t = rc.next();
        assertEquals("a b", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());
    }

    @Test(expected=IllegalStateException.class)
    public void simpleNextWithIllegalStateException() throws Exception {
        final WhitespaceTokenizer tokenizer =
            new WhitespaceTokenizer("f a b a b c d");
        final SimpleLongestMatchDictionaryBasedEntityRecognizer rc =
            new SimpleLongestMatchDictionaryBasedEntityRecognizer(tokenizer);
        rc.next();
    }

    @Test
    public void matchWithId() throws Exception {
        final Map<CharSequence, Set<CharSequence>> map =
            new HashMap<CharSequence, Set<CharSequence>>();
        final Set<CharSequence> id1 = new HashSet<CharSequence>();
        id1.add("id1");
        map.put("f", id1);
        final Set<CharSequence> id2 = new HashSet<CharSequence>();
        id2.add("id2");
        map.put("a b", id2);

        final WhitespaceTokenizer tokenizer =
            new WhitespaceTokenizer("f a b");
        final SimpleLongestMatchDictionaryBasedEntityRecognizer rc =
            new SimpleLongestMatchDictionaryBasedEntityRecognizer(tokenizer);

        rc.setEntityMap(map);

        Token t = rc.next();
        assertEquals("f", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());
        assertTrue(t.getMeta().containsKey(ENTITY_ID_KEY));
        assertEquals("id1", t.getMeta().get(ENTITY_ID_KEY).get(0));

        t = rc.next();
        assertEquals("a b", t.getTokenText());
        assertEquals(EntityRecognizer.ENTITY_TYPE, t.getType());
        assertTrue(t.getMeta().containsKey(ENTITY_ID_KEY));
        assertEquals("id2", t.getMeta().get(ENTITY_ID_KEY).get(0));
    }
}
