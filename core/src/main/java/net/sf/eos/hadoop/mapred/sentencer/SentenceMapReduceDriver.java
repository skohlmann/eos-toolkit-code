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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.util.ToolRunner;

import net.sf.eos.hadoop.mapred.AbstractEosDriver;

/**
 * 
 * @author Sascha Kohlmann
 * @see SentenceMapper
 * @see SentenceReducer
 */
public class SentenceMapReduceDriver extends AbstractEosDriver {

    /**
     * @param args
     */
    public static void main(final String[] args) throws Exception {
        final int res = ToolRunner.run(new Configuration(),
                                       new SentenceMapReduceDriver(),
                                       args);
        System.exit(res);
    }

    /** Starts the job. */
    @Override
    public int run(final String[] args) throws Exception {
        super.run(args);
        final JobConf conf = getJobConf();

        conf.setJobName("\u03b5\u00b7\u03bf\u00b7s\u00b7\u00b7\u00b7 Sentencer");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(Text.class);

        conf.setMapperClass(SentencerMapper.class);
        conf.setReducerClass(SentenceReducer.class);

        return doJob(conf);
    }
}
