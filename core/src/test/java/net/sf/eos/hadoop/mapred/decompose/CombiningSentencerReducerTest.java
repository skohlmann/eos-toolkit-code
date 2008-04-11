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


import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.hadoop.mapred.Index;
import net.sf.eos.hadoop.mapred.decompose.SentencerMapper;
import net.sf.eos.hadoop.mapred.decompose.SentencerReducer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.HadoopTestCase;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;

import java.io.IOException;
import java.net.URL;

public class CombiningSentencerReducerTest extends HadoopTestCase {

   private static final String INPUT_EOSDOCS =
       "CombiningSentencerReducerTest.eosdocs";

   /** The package name for property prefixing. */
   public static final String LOCAL_PATH;

   static {
       final Thread t = Thread.currentThread();
       ClassLoader classLoader = t.getContextClassLoader();
       if (classLoader == null) {
           classLoader = TokenizerBuilder.class.getClassLoader();
       }
       final URL resource = classLoader.getResource(INPUT_EOSDOCS);

       final String path = resource.getPath();
       final int lastIndexOf = path.lastIndexOf("/");
       LOCAL_PATH = path.substring(0, lastIndexOf);
   }

   private String resultPath;

   public CombiningSentencerReducerTest() throws IOException {
       this(HadoopTestCase.LOCAL_MR, HadoopTestCase.LOCAL_FS, 2, 2);
   }

   public CombiningSentencerReducerTest(final int mrMode, 
                                        final int fsMode,
                                        final int taskTrackers,
                                        final int dataNodes)
           throws IOException {
       super(mrMode, fsMode, taskTrackers, dataNodes);
   }

   @Override
   protected void setUp() throws Exception {
       super.setUp();
       this.resultPath =
           LOCAL_PATH + "/CombiningSentencerReducerTest.testresult";
   }

   @Override
   protected void tearDown() throws Exception {
       super.tearDown();
       final FileSystem fs = getFileSystem();
       fs.delete(new Path(this.resultPath));
       fs.close();
   }

   public void testSimpleJob() throws Exception {
       final JobConf jobConf = createJobConf();
       jobConf.setMapperClass(SentencerMapper.class);
       jobConf.setReducerClass(SentencerReducer.class);
       jobConf.setJobName("eos Sentencer Combining Test");

       jobConf.addInputPath(new Path(LOCAL_PATH, INPUT_EOSDOCS));

       final Path p = new Path(this.resultPath);
       jobConf.setOutputPath(p);
       jobConf.setOutputKeyClass(Text.class);
       jobConf.setOutputValueClass(Text.class);

       jobConf.setMapOutputKeyClass(Text.class);
       jobConf.setMapOutputValueClass(Text.class);

       final RunningJob job = JobClient.runJob(jobConf);
       job.waitForCompletion();

       final Counters counters = job.getCounters();

       assertEquals(4, counters.getCounter(Index.MAP));
       assertEquals(2, counters.getCounter(Index.REDUCE));
   }
}
