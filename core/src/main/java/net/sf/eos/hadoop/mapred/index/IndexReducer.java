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
package net.sf.eos.hadoop.mapred.index;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;

import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.Iterator;

public class IndexReducer<K extends WritableComparable>
        extends EosDocumentSupportMapReduceBase
        implements Reducer<K, Text, K, ObjectWritable> {

    private JobConf conf;

    public void reduce(final K key,
                       final Iterator<Text> lineIterator,
                       final OutputCollector<K, ObjectWritable> output,
                       final Reporter reporter) throws IOException {

        assert this.conf != null;
        final Configuration lconf =
            new HadoopConfigurationAdapter(this.conf);
        try {
            final LuceneDocumentCreator creator =
                LuceneDocumentCreator.newInstance(lconf);

            while (lineIterator.hasNext()) {
                final Text text = lineIterator.next();
                final EosDocument doc = textToEosDocument(text);
                final Document lDoc =
                    creator.createLuceneForEosDocument(doc);

                if (lDoc != null) {
                    output.collect(key, new ObjectWritable(lDoc));
                    reporter.incrCounter(Index.REDUCE, 1);
                }
            }
        } catch (final EosException e) {
            final IOException ioe = new IOException();
            ioe.initCause(e);
            reporter.incrCounter(Index.EOS_EXCEPTION, 1);
            throw ioe;
        } catch (final IOException e) {
            reporter.incrCounter(Index.IO_EXCEPTION, 1);
            throw e;
        } catch (final Exception e) {
            final IOException ioe = new IOException();
            reporter.incrCounter(Index.OTHER_EXCEPTION, 1);
            ioe.initCause(e);
            throw ioe;
        }
    }

    /**
     * @param conf the configuration
     */
    @Override
    public void configure(@SuppressWarnings("hiding") final JobConf conf) {
        super.configure(conf);
        this.conf = conf;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
