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
package net.sf.eos.lucene;

import net.sf.eos.EosException;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class DefaultEosQueryTest {

    @Test
    public void creation() {
        new DefaultEosQuery();
    }

    @Test
    public void simpleContent() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.and("test");
        final String qs = q.executableQuery();
        assertEquals("CONTENT:\"test\" ", qs);
    }

    @Test
    public void simpleNotContent() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andNot("te\"st");
        final String qs = q.executableQuery();
        assertEquals("-CONTENT:\"te\\\"st\" ", qs);
    }

    @Test
    public void andContent() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.and("test");
        q.and("boxi");
        final String qs = q.executableQuery();
        assertEquals("CONTENT:\"test\" AND CONTENT:\"boxi\" ", qs);
    }

    @Test
    public void andNotContent() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.and("test");
        q.andNot("boxi");
        final String qs = q.executableQuery();
        assertEquals("CONTENT:\"test\" AND -CONTENT:\"boxi\" ", qs);
    }

    @Test
    public void orContent() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.and("test");
        q.or("boxi");
        final String qs = q.executableQuery();
        assertEquals("CONTENT:\"test\" OR CONTENT:\"boxi\" ", qs);
    }

    @Test
    public void simpleMeta() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andMeta("FIELD", "test");
        final String qs = q.executableQuery();
        assertEquals("FIELD:\"test\" ", qs);
    }

    @Test
    public void mndMeta() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andMeta("FIELD", "test");
        q.andMeta("YEAR", "2006");
        final String qs = q.executableQuery();
        assertEquals("FIELD:\"test\" AND YEAR:\"2006\" ", qs);
    }

    @Test
    public void orMeta() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andMeta("FIELD", "test");
        q.orMeta("YEAR", "2006");
        final String qs = q.executableQuery();
        assertEquals("FIELD:\"test\" OR YEAR:\"2006\" ", qs);
    }

    @Test
    public void andNotMeta() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andMeta("FIELD", "test");
        q.andNotMeta("YEAR", "2006");
        final String qs = q.executableQuery();
        assertEquals("FIELD:\"test\" AND -YEAR:\"2006\" ", qs);
    }

    @Test
    public void andMetaRange() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andMeta("FIELD", "test");
        q.andMetaRange("YEAR", "2006", "2007");
        final String qs = q.executableQuery();
        assertEquals("FIELD:\"test\" AND YEAR:[\"2006\" TO \"2007\"] ", qs);
    }

    @Test
    public void orMetaRange() throws EosException {
        final DefaultEosQuery q = new DefaultEosQuery();
        q.andMeta("FIELD", "test");
        q.orMetaRange("YEAR", "2006", "2007");
        final String qs = q.executableQuery();
        assertEquals("FIELD:\"test\" OR YEAR:[\"2006\" TO \"2007\"] ", qs);
    }
}
