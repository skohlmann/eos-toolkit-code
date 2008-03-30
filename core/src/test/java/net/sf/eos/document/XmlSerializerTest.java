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
import net.sf.eos.document.Serializer;
import net.sf.eos.document.XmlSerializer;

import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class XmlSerializerTest {

    @Test
    public void serializeComplexDocument() throws Exception {
        final EosDocument doc = new EosDocument();
        doc.setTitle("title");
        doc.setText("text");
        final Map<String, List<String>> meta =
            new HashMap<String, List<String>>();
        final List<String> values = new ArrayList<String>();
        values.add("v&1");
        values.add("v<2");
        meta.put("k1", values);
        doc.setMeta(meta);

        final StringWriter sw = new StringWriter();
        final Serializer serializer = new XmlSerializer();

        serializer.serialize(doc, sw);
        sw.flush();

        assertEquals("<d><m><k>k1</k><v>v&amp;1</v><v>v&lt;2</v></m><ti>title"
                     + "</ti><te>text</te></d>",
                     sw.toString());
    }

    @Test
    public void serializeTextAndTitleWithNewline() throws Exception {
        final EosDocument doc = new EosDocument();
        doc.setText("te\rxt");
        doc.setTitle("ti\ntle");

        final StringWriter sw = new StringWriter();
        final Serializer serializer = new XmlSerializer();

        serializer.serialize(doc, sw);
        sw.flush();

        assertEquals("<d><ti>ti tle</ti><te>te xt</te></d>", sw.toString());
    }

    @Test
    public void serializeTextWithNewline() throws Exception {
        final EosDocument doc = new EosDocument();
        doc.setText("te\rxt");

        final StringWriter sw = new StringWriter();
        final Serializer serializer = new XmlSerializer();

        serializer.serialize(doc, sw);
        sw.flush();

        assertEquals("<d><te>te xt</te></d>", sw.toString());
    }

    @Test
    public void serializeTitleWithNewline() throws Exception {
        final EosDocument doc = new EosDocument();
        doc.setTitle("ti\ntle");

        final StringWriter sw = new StringWriter();
        final Serializer serializer = new XmlSerializer();

        serializer.serialize(doc, sw);
        sw.flush();

        assertEquals("<d><ti>ti tle</ti></d>", sw.toString());
    }


    @Test
    public void serializeOnlyMeta() throws Exception {
        final EosDocument doc = new EosDocument();
        final Map<String, List<String>> meta =
            new HashMap<String, List<String>>();
        final List<String> values = new ArrayList<String>();
        values.add("v&1");
        values.add("v<2");
        meta.put("k>1", values);
        doc.setMeta(meta);

        final StringWriter sw = new StringWriter();
        final Serializer serializer = new XmlSerializer();

        serializer.serialize(doc, sw);
        sw.flush();

        assertEquals("<d><m><k>k&gt;1</k><v>v&amp;1</v><v>v&lt;2</v></m></d>",
                     sw.toString());
    }

    @Test
    public void deserializeTitle() throws Exception {
        final String s = "<d><ti>title</ti></d>";
        final StringReader reader = new StringReader(s);

        final Serializer serializer = new XmlSerializer();
        final EosDocument doc = serializer.deserialize(reader);

        assertEquals("title", doc.getTitle());
        assertNull(doc.getText());
        assertEquals(0, doc.getMeta().size());
    }

    @Test
    public void deserializeText() throws Exception {
        final String s = "<d><te>text</te></d>";
        final StringReader reader = new StringReader(s);

        final Serializer serializer = new XmlSerializer();
        final EosDocument doc = serializer.deserialize(reader);

        assertEquals("text", doc.getText());
        assertNull(doc.getTitle());
        assertEquals(0, doc.getMeta().size());
    }

    @Test
    public void deserializeTextAndTitle() throws Exception {
        final String s = "<d><te>text</te><ti>gabi</ti></d>";
        final StringReader reader = new StringReader(s);

        final Serializer serializer = new XmlSerializer();
        final EosDocument doc = serializer.deserialize(reader);

        assertEquals("text", doc.getText());
        assertEquals("gabi", doc.getTitle());
    }


    @Test
    public void deserializeTextAndTitleAnd2Meta() throws Exception {
        final String s = "<d><te>text</te><m><v>v1</v><k>k</k></m><ti>gabi</ti>"
                         + "<m><k>s</k><v>1</v><v>2</v></m></d>";
        final StringReader reader = new StringReader(s);

        final Serializer serializer = new XmlSerializer();
        final EosDocument doc = serializer.deserialize(reader);

        assertEquals("text", doc.getText());
        assertEquals("gabi", doc.getTitle());
        assertEquals(2, doc.getMeta().size());
        assertTrue(doc.getMeta().containsKey("k"));
        assertEquals("v1", doc.getMeta().get("k").get(0));
        assertTrue(doc.getMeta().containsKey("s"));
        assertEquals("1", doc.getMeta().get("s").get(0));
        assertEquals("2", doc.getMeta().get("s").get(1));
    }
}
