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
package net.sf.eos.hadoop.mapred.decompose;


import net.sf.eos.document.EosDocument;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentCreator {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        final EosDocument doc1 = new EosDocument();
        doc1.setTitle("This is the title");
        doc1.setText("A simple first sentence. And a second sentence.");
        final List<String> years1 = new ArrayList<String>();
        years1.add("2006");
        final Map<String, List<String>> meta1 =
            new HashMap<String, List<String>>();
        meta1.put(EosDocument.YEAR_META_KEY, years1);
        doc1.setMeta(meta1);

        final EosDocument doc2 = new EosDocument();
        doc2.setTitle("The title of the second document");
        doc2.setText("The first sentence of the second document. "
                     + "Another simple sentence.");
        final List<String> years2 = new ArrayList<String>();
        years2.add("2006");
        final Map<String, List<String>> meta2 =
            new HashMap<String, List<String>>();
        meta2.put(EosDocument.YEAR_META_KEY, years2);
        doc2.setMeta(meta2);
        System.out.println("" + doc1);
        System.out.println("" + doc2);
    }

    @Test
    public void dummy() {
        
    }
}
