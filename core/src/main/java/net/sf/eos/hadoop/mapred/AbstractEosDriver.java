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
package net.sf.eos.hadoop.mapred;

import static net.sf.eos.util.Conditions.checkState;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.util.Tool;

/**
 * Support for some generic configuration data. The {@link #run(String[])}
 * analyze the parameter "<tt>-s</tt>" or "<tt>--source</tt>" for the source
 * (<em>input</em>-Path) parameter and "<tt>-d</tt>" or "<tt>--dest</tt>" for
 * the destination (<em>output</em>-Path).
 * @author Sascha Kohlmann
 */
public abstract class AbstractEosDriver extends Configured implements Tool {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(AbstractEosDriver.class.getName());

    /** Short commandline parameter name for the <em>source</em> or
     * <em>input</em> path. */ 
    @SuppressWarnings("nls")
    public static final String SOURCE_SHORT_CMD_ARG = "s";
    /** Long commandline parameter name for the <em>source</em> or
     * <em>input</em> path. */ 
    @SuppressWarnings("nls")
    public static final String SOURCE_LONG_CMD_ARG = "source";

    /** Short commandline parameter name for the <em>destination</em> or
     * <em>output</em> path. */ 
    @SuppressWarnings("nls")
    public static final String DESTINATION_SHORT_CMD_ARG = "d";
    /** Long commandline parameter name for the <em>destination</em> or
     * <em>output</em> path. */ 
    @SuppressWarnings("nls")
    public static final String DESTINATION_LONG_CMD_ARG = "dest";

    @SuppressWarnings("nls")

    private JobConf jobConf = null;

    /**
     * Implementations of {@code AbstractEosDriver} must call
     * {@code super.run(String[])}. After the call {@link #getJobConf()}
     * returns a value != {@code null}.
     * <p>Never starts a job. This is part of the implementing driver.</p>
     * @return always zero.
     */
    @SuppressWarnings("nls")
    public int run(final String[] args) throws Exception {
        Configuration conf = getConf();
        if (conf == null) {
            conf = new Configuration();
        }
        this.jobConf = new JobConf(conf, this.getClass());
        final Parser parser = new GnuParser();
        final Options options = createOptions();
        final CommandLine cmdLine = parser.parse(options, args);

        final String source = cmdLine.getOptionValue(SOURCE_SHORT_CMD_ARG);
        final String dest = cmdLine.getOptionValue(DESTINATION_SHORT_CMD_ARG);

        if (source == null || source.length() == 0) {
            LOG.warn("Arguments contains no source path.");
        } else {
            final Path in = new Path(source);
            this.jobConf.setInputPath(in);
        }
        if (dest == null || dest.length() == 0) {
            LOG.warn("Arguments contains no destination path.");
        } else {
            final Path out = new Path(dest);
            this.jobConf.setOutputPath(out);
        }

        return 0;
    }

    /**
     * Returns the job configuration. Throws an exception if method was called
     * before an implementation calls {@link #run(String[])}.
     * @return the job configuration
     * @throws IllegalStateException if called before {@link #run(String[])}
     */
    @SuppressWarnings("nls")
    protected final JobConf getJobConf() {
        checkState(this.jobConf != null, "Called before run(String[]) thru super.");

        return this.jobConf;
    }

    protected Options createOptions() {
        return new Options()
            .addOption(SOURCE_SHORT_CMD_ARG,
                       SOURCE_LONG_CMD_ARG,
                       true,
                       "Path to the source folder of the input data")
            .addOption(DESTINATION_SHORT_CMD_ARG,
                       DESTINATION_LONG_CMD_ARG,
                       true,
                       "Path to the destination folder of the output data");
    }

    /**
     * Runs the job for the given configuration.
     * @param conf the job configuration
     * @return the job status. 0 if the job success. 1 if the job fails.
     * @throws Exception if an error occurs
     */
    protected int doJob(final JobConf conf) throws Exception {
        final RunningJob job = JobClient.runJob(conf);
        job.waitForCompletion();
        return job.isSuccessful() ? 0 : 1;
    }
}
