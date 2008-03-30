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
package net.sf.eos.hadoop.mapred.entity;


import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.document.EosDocument;

import org.apache.hadoop.mapred.JobConf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDictionaryBasedEntityRecognizerReducerTest {

    private DictionaryBasedEntityRecognizerReducer reducer = null;

    @Test
    public void combineDocumentWithFromAndToHasTextNull() throws Exception {
        final EosDocument from = new EosDocument();
        from.setText(null);

        final EosDocument to = new EosDocument();
        to.setText(null);

        this.reducer.combineDocuments(from, to);

        assertNull(to.getText());
    }

    @Test
    public void combineDocumentWithToHasTextNull() throws Exception {
        final EosDocument from = new EosDocument();
        from.setText("1");

        final EosDocument to = new EosDocument();
        to.setText(null);

        this.reducer.combineDocuments(from, to);

        assertEquals("1", to.getText());
    }

    @Test
    public void combineDocumentWithFromHasTextNull() throws Exception {
        final EosDocument from = new EosDocument();
        from.setText(null);

        final EosDocument to = new EosDocument();
        to.setText("2");

        this.reducer.combineDocuments(from, to);

        assertEquals("2", to.getText());
    }

    @Test
    public void combineDocumentWithFromAndToHasTitleNull() throws Exception {
        final EosDocument from = new EosDocument();
        from.setTitle(null);

        final EosDocument to = new EosDocument();
        to.setTitle(null);

        this.reducer.combineDocuments(from, to);

        assertNull(to.getTitle());
    }

    @Test
    public void combineDocumentWithToHasTitleNull() throws Exception {
        final EosDocument from = new EosDocument();
        from.setTitle("1");

        final EosDocument to = new EosDocument();
        to.setTitle(null);

        this.reducer.combineDocuments(from, to);

        assertEquals("1", to.getTitle());
    }

    @Test
    public void combineDocumentWithFromHasTitleNull() throws Exception {
        final EosDocument from = new EosDocument();
        from.setTitle(null);

        final EosDocument to = new EosDocument();
        to.setTitle("2");

        this.reducer.combineDocuments(from, to);

        assertEquals("2", to.getTitle());
    }

    @Test
    public void simpleCombineDocumentWithNullFrom() throws Exception {
        final EosDocument to = new EosDocument();
        to.setText("b");
        to.setTitle("2");

        final Map<String, List<String>> toMeta =
            new HashMap<String, List<String>>();
        final List<String> toList1 = new ArrayList<String>();
        toList1.add("u");
        toMeta.put("k1", toList1);

        to.setMeta(toMeta);

        this.reducer.combineDocuments(null, to);

        assertEquals("2", to.getTitle());
        assertEquals("b", to.getText());
        assertEquals("u", to.getMeta().get("k1").get(0));
    }

    @Test
    public void simpleCombineDocument() throws Exception {
        final EosDocument from = new EosDocument();
        from.setText("a");
        from.setTitle("1");
        final Map<String, List<String>> fromMeta =
            new HashMap<String, List<String>>();
        final List<String> fromList1 = new ArrayList<String>();
        fromList1.add("z");
        fromMeta.put("k1", fromList1);

        final List<String> fromList2 = new ArrayList<String>();
        fromList2.add("r");
        fromMeta.put("k2", fromList2);

        from.setMeta(fromMeta);

        final EosDocument to = new EosDocument();
        to.setText("b");
        to.setTitle("2");

        final Map<String, List<String>> toMeta =
            new HashMap<String, List<String>>();
        final List<String> toList1 = new ArrayList<String>();
        toList1.add("u");
        toMeta.put("k1", toList1);

        to.setMeta(toMeta);

        this.reducer.combineDocuments(from, to);

        assertEquals("2 1", to.getTitle());
        assertEquals("b a", to.getText());

        assertEquals("u", to.getMeta().get("k1").get(0));
        assertEquals("z", to.getMeta().get("k1").get(1));

        assertEquals("r", to.getMeta().get("k2").get(0));
    }

    @Before
    public void createInstance() throws Exception {
        final JobConf conf = new JobConf();
        conf.set(TextBuilder.TEXT_BUILDER_IMPL_CONFIG_NAME,
                 TextBuilder.SPACE_BUILDER.getClass().getName());

        final DictionaryBasedEntityRecognizerReducer reducer =
            DictionaryBasedEntityRecognizerReducer.class.newInstance();
        reducer.configure(conf);

        this.reducer = reducer;
    }
}
