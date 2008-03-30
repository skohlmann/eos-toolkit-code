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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.ToolRunner;

import java.util.logging.Logger;

import net.sf.eos.hadoop.DistributedCacheStrategy;
import net.sf.eos.hadoop.FullyDistributedCacheStrategy;
import net.sf.eos.hadoop.mapred.AbstractEosDriver;
import net.sf.eos.trie.TrieLoader;

/**
 * The driver supports the base arguments. To run the driver set the path
 * to the {@linkplain #TRIE_LONG_CMD_ARG trie}.
 * @author Sascha Kohlmann
 */
public class DictionaryBasedEntityRecognizerMapReduceDriver
        extends AbstractEosDriver {

    /** Short commandline parameter name for the <em>path</em> to
     * the trie data. The parameter is required.
     * @see TrieLoader
     * @see DistributedCacheStrategy */ 
    @SuppressWarnings("nls")
    public static final String TRIE_SHORT_CMD_ARG = "t";

    /** Long commandline parameter name for the <em>path</em> to
     * the trie data. The parameter is required. 
     * @see TrieLoader
     * @see DistributedCacheStrategy */ 
    @SuppressWarnings("nls")
    public static final String TRIE_LONG_CMD_ARG = "trie";

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(DictionaryBasedEntityRecognizerMapReduceDriver.class.getName());

    /**
     * The parameter "<tt>-t</tt>" or "<tt>--trie</tt>" must be set use the
     * trie data.
     * @param args the command line arguments
     * @see #TRIE_LONG_CMD_ARG
     * @see AbstractEosDriver#DESTINATION_LONG_CMD_ARG
     * @see AbstractEosDriver#SOURCE_LONG_CMD_ARG
     * @see GenericOptionsParser
     */
    public static void main(final String[] args) throws Exception {
        final int res = ToolRunner.run(
                new Configuration(),
                new DictionaryBasedEntityRecognizerMapReduceDriver(),
                args);
        System.exit(res);
    }

    /** Starts the job. */
    @Override
    public int run(final String[] args) throws Exception {
        super.run(args);
        final JobConf conf = getJobConf();

        final Parser parser = new GnuParser();
        final Options options = createOptions();
        final CommandLine cmdLine = parser.parse(options, args);

        final String triePath = cmdLine.getOptionValue(TRIE_LONG_CMD_ARG);
        if (triePath == null || triePath.length() == 0) {
            LOG.severe("No Trie data path given - exiting");
            return 1;
        }
        LOG.info("Trie path: " + triePath);
        DistributedCache.addCacheFile(new Path(triePath).toUri(), conf);

        if (conf.get(DistributedCacheStrategy.STRATEGY_CONFIG_NAME) == null) {
            conf.set(DistributedCacheStrategy.STRATEGY_CONFIG_NAME,
                    FullyDistributedCacheStrategy.class.getName());
            LOG.config("No CacheStrategy given. Use '"
                       + FullyDistributedCacheStrategy.class.getName()
                       + "'");
        }

        conf.setJobName("\u03b5\u00b7\u03bf\u00b7s\u00b7\u00b7\u00b7 Entity");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapOutputKeyClass(Text.class);
        conf.setMapOutputValueClass(Text.class);

        conf.setMapperClass(DictionaryBasedEntityRecognizerMapper.class);
        conf.setReducerClass(DictionaryBasedEntityRecognizerReducer.class);

        return doJob(conf);
    }

    final Options createOptions() {
        final Option option =
            new Option(TRIE_SHORT_CMD_ARG,
                       TRIE_LONG_CMD_ARG,
                       true,
                       "Path to trie data");
        option.setRequired(true);

        return new Options().addOption(option);
    }
}
