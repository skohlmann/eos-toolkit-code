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
package net.sf.eos.document;

import net.sf.eos.document.EosDocument;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class EosDocumentEqualsTest {

    @Test
    public void differentText() throws Exception {
        final EosDocument doc1 = new EosDocument();
        doc1.setText("a");
        doc1.setTitle("1");
        doc1.setMeta(Collections.EMPTY_MAP);

        final EosDocument doc2 = new EosDocument();
        doc2.setText("b");
        doc2.setTitle("1");
        doc2.setMeta(Collections.EMPTY_MAP);

        assertFalse(doc1.equals(doc2));
    }

    @Test
    public void differentTitle() throws Exception {
        final EosDocument doc1 = new EosDocument();
        doc1.setText("a");
        doc1.setTitle("1");
        doc1.setMeta(Collections.EMPTY_MAP);

        final EosDocument doc2 = new EosDocument();
        doc2.setText("a");
        doc2.setTitle("2");
        doc2.setMeta(Collections.EMPTY_MAP);

        assertFalse(doc1.equals(doc2));
    }

    @Test
    public void differentMeta() throws Exception {
        final EosDocument doc1 = new EosDocument();
        doc1.setText("a");
        doc1.setTitle("1");
        doc1.setMeta(Collections.EMPTY_MAP);

        final EosDocument doc2 = new EosDocument();
        doc2.setText("a");
        doc2.setTitle("1");
        final Map<String, List<String>> meta =
            new HashMap<String, List<String>>();
        meta.put("u", new ArrayList<String>());
        doc2.setMeta(meta);

        assertFalse(doc1.equals(doc2));
    }

    @Test
    public void nullOther() throws Exception {
        final EosDocument doc1 = new EosDocument();
        assertFalse(doc1.equals(null));
        
    }

    @Test
    public void otherType() throws Exception {
        final EosDocument doc1 = new EosDocument();
        assertFalse(doc1.equals("1"));
    }

    @Test
    public void same() throws Exception {
        final EosDocument doc1 = new EosDocument();
        doc1.setText("a");
        doc1.setTitle("1");
        final Map<String, List<String>> meta1 =
            new HashMap<String, List<String>>();
        final List<String> value1 = new ArrayList<String>();
        value1.add("gabi");
        meta1.put("u", value1);
        doc1.setMeta(meta1);

        final EosDocument doc2 = new EosDocument();
        doc2.setText("a");
        doc2.setTitle("1");
        final Map<String, List<String>> meta2 =
            new HashMap<String, List<String>>();
        final List<String> value2 = new LinkedList<String>();
        value2.add("gabi");
        meta2.put("u", value2);
        doc2.setMeta(meta2);

        assertEquals(doc1, doc2);
    }
}
