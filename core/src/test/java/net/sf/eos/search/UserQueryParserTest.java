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
package net.sf.eos.search;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

/**
 * @author Sascha Kohlmann
 */
public class UserQueryParserTest {

    private UserQueryParser processor;

    @Test
    public void realWorldSearchTermsFromMedline() {
        final String test = "shark, oscillation, noise, sensory cells";
        final List<String> terms = this.processor.parseUserQuery(test);
        assertEquals(terms.size(), 5);
    }

    @Test
    public void removeNewLinesWithNewline() {
        final String test = "test \n test";
        final String adjusted = this.processor.removeNewLines(test);
        assertEquals("test  test", adjusted);
    }

    @Test
    public void removeNewLinesWithCarriageReturn() {
        final String test = "test \r test";
        final String adjusted = this.processor.removeNewLines(test);
        assertEquals("test  test", adjusted);
    }

    @Test
    public void removeNewLinesWithFormFeed() {
        final String test = "test \f test";
        final String adjusted = this.processor.removeNewLines(test);
        assertEquals("test  test", adjusted);
    }

    @Test
    public void splitSimpleTermsWithNoSpace() {
        final List<String> l = this.processor.splitSimpleTerms("test");
        assertEquals(1, l.size());
    }

    @Test
    public void splitSimpleTermsWithTrailingSpace() {
        final List<String> l = this.processor.splitSimpleTerms("test ");
        assertEquals(1, l.size());
    }

    @Test
    public void splitSimpleTermsWithLeadingAndTrailingSpace() {
        final List<String> l = this.processor.splitSimpleTerms(" test ");
        assertEquals(1, l.size());
    }

    @Test
    public void splitSimpleTermsWithTwoTermTabDelimited() {
        final List<String> l =
            this.processor.splitSimpleTerms(" test \t zulu");
        assertEquals("test", l.get(0));
        assertEquals("zulu", l.get(1));
    }

    @Test
    public void simpleSearchTerm() throws Exception {
        final String query = "test";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals(query, result.get(0));
    }

    @Test
    public void simpleWithTwoSearchTermWithspaceSeparated() throws Exception
    {
        final String query = "test help";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test", result.get(0));
        assertEquals("help", result.get(1));
    }

    @Test
    public void simpleWithOneCompundSearchTerm() throws Exception {
        final String query = "\"test, help\"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void simpleWithOneCompundSearchTermWithLeadingSpace()
            throws Exception {
        final String query = "\" test, help\"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void simpleWithOneCompundSearchTermWithTrailingSpace()
            throws Exception {
        final String query = "\"test, help \"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void simpleWithOneCompundSearchTermWithWhitespace()
            throws Exception {
        final String query = "\"test, \t  help \"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void simpleWithOneCompundSearchTermWithWhitespaceAndNewline()
            throws Exception {
        final String query = "\"\n test, \t \n help \"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void simpleWithTwoSearchTermsOneIsACompound() throws Exception {
        final String query = "gabi \"test, help\"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("gabi", result.get(0));
        assertEquals("test, help", result.get(1));
    }

    @Test
    public void simpleWithTwoSearchTermsAndTrailingCompound()
            throws Exception {
        final String query = " \"test, help\"gabi";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("gabi", result.get(1));
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void simpleWithTwoSearchTermCompounds() throws Exception {
        final String query = " \"test, help\" \"test, doof\"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, doof", result.get(1));
        assertEquals("test, help", result.get(0));
    }

    public void testSimpleWithComplexMismatch() throws Exception {
        final String query = "test, help\" \"test, doof\"";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals(4, result.size());
    }

    @Test
    public void simpleWithTwoSearchTermCompoundsNotClosing()
            throws Exception {
        final String query = " \"test, help\" \"test, doof";
        final List<String> result = this.processor.parseUserQuery(query);
        assertEquals("test, doof", result.get(1));
        assertEquals("test, help", result.get(0));
    }

    @Test
    public void withNull() throws Exception {
        final List<String> result = this.processor.parseUserQuery(null);
        assertTrue(0 == result.size());
    }

    @Before
    public void createProcessor() {
        this.processor = new UserQueryParser();
    }
}
