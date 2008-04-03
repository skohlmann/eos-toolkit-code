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
package net.sf.eos.hadoop.mapred.sentencer;

import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.eos.config.Configuration;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.AbstractKeyGenerator;
import net.sf.eos.hadoop.mapred.KeyGenerator;

public class TextMetaKeyGeneratorTest {

    private EosDocument doc;
    private Configuration conf;

    @Test
    public void withNoMetadataKey() throws Exception {
        final Map<String, List<String>> meta = this.doc.getMeta();
        final List<String> dates = new ArrayList<String>();
        dates.add("2008");
        dates.add("2009");
        meta.put(EosDocument.YEAR_META_KEY, dates);

        final KeyGenerator<Text> generator = (KeyGenerator<Text>)
            AbstractKeyGenerator.newInstance(this.conf);
        final Map<Text, EosDocument> keys = 
           generator.createKeysForDocument(this.doc);

        assertEquals(1, keys.size());
    }

    @Test
    public void withEosDocumentMetaDate() throws Exception {
        this.conf.set(
                TextMetaKeyGenerator.META_FIELD_FOR_KEY_CONFIG_NAME,
                EosDocument.YEAR_META_KEY
        );
        final Map<String, List<String>> meta = this.doc.getMeta();
        final List<String> dates = new ArrayList<String>();
        dates.add("2008");
        dates.add("2009");
        meta.put(EosDocument.YEAR_META_KEY, dates);

        final KeyGenerator<Text> generator = (KeyGenerator<Text>)
            AbstractKeyGenerator.newInstance(this.conf);
        final Map<Text, EosDocument> keys = 
           generator.createKeysForDocument(this.doc);

        assertEquals(2, keys.size());
        assertTrue(keys.containsKey(new Text("2008")));
        assertTrue(keys.containsKey(new Text("2009")));
    }

    @Before
    public void createEosDocument() {
        this.doc = new EosDocument();
    }

    @Before
    public void createConfiguration() {
        this.conf = new Configuration();
        this.conf.set(
                AbstractKeyGenerator.ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME,
                TextMetaKeyGenerator.class.getName()
        );
    }
}
