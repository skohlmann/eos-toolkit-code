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


import net.sf.eos.EosException;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class SentencerReducer extends EosDocumentSupportMapReduceBase
                              implements Reducer<Text, Text, Text, Text> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(SentencerReducer.class.getName());

    private static final Text EMPTY = new Text();

    private JobConf conf;

    /*
     * @see org.apache.hadoop.mapred.Reducer#reduce(org.apache.hadoop.io.WritableComparable, java.util.Iterator, org.apache.hadoop.mapred.OutputCollector, org.apache.hadoop.mapred.Reporter)
     */
    @SuppressWarnings("nls")
    public void reduce(final Text key,
                       final Iterator<Text> valuesIterator,
                       final OutputCollector<Text, Text> outputCollector,
                       final Reporter reporter) throws IOException {

        try {
            final EosDocument doc =
                createEosDocumentFromIterator(valuesIterator);
            final Text docAsText = eosDocumentToText(doc);

            outputCollector.collect(EMPTY, docAsText);
            reporter.incrCounter(Index.REDUCE, 1);

        } catch (final EosException e) {
            reporter.incrCounter(Index.EOS_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        } catch (final Exception e) {
            reporter.incrCounter(Index.IO_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        }
    }

    final EosDocument createEosDocumentFromIterator(
            final Iterator<Text> valuesIterator) throws Exception, IOException {

        final Map<String, Set<String>> metaData =
            new HashMap<String, Set<String>>();
        EosDocument doc = null;

        while (valuesIterator.hasNext()) {
            final Text eosDoc = valuesIterator.next();
            doc = textToEosDocument(eosDoc);
            assert doc != null;

            final Map<String, List<String>> meta = doc.getMeta();
            if (meta != null) {
                for (final Entry<String, List<String>> entry 
                        : meta.entrySet()) {
                    final String metaKey = entry.getKey();
                    Set<String> collector = metaData.get(metaKey);
                    if (collector == null) {
                        collector = new HashSet<String>();
                        metaData.put(metaKey, collector);
                    }
                    final List<String> value = entry.getValue();
                    collector.addAll(value);
                }
            }
        }

        final Map<String, List<String>> newMeta = transformToList(metaData);
        assert doc != null;
        doc.setMeta(newMeta);

        return doc;
    }

    /**
     * Transforms a {@code Map} of metadata {@code Set}s into a
     * {@code Map} of metadata {@code List}s.
     * @param metaData the metadata to transform
     * @return the transformed map of lists.
     */
    final Map<String, List<String>> transformToList(
            final Map<String, Set<String>> metaData) {

        final Map<String, List<String>> newMeta =
            new HashMap<String, List<String>>();
        for (final Entry<String, Set<String>> entry : metaData.entrySet()) {
            final String metaKey = entry.getKey();
            final List<String> metaValue = new ArrayList<String>();
            final Collection<String> metaCol = entry.getValue();
            metaValue.addAll(metaCol);
            newMeta.put(metaKey, metaValue);
        }
        return newMeta;
    }

    @Override
    public void configure(final JobConf conf) {
        super.configure(conf);
        this.conf = conf;
    }
}
