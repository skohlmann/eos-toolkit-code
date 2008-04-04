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

import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class IndexMapper<K extends WritableComparable>
        extends EosDocumentSupportMapReduceBase
        implements Mapper<K, Text, K, Text> {

    private JobConf conf;

    public void map(final K key,
                    final Text value,
                    final OutputCollector<K, Text> output,
                    final Reporter reporter)
            throws IOException {
        output.collect(key, value);
        reporter.incrCounter(Index.MAP, 1);
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
