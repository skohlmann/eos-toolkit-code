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

import net.sf.eos.analyzer.TokenizerProvider;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.lucene.AnalyzerProvider;
import net.sf.eos.lucene.DefaultLuceneDocumentCreator;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.HadoopTestCase;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class SimpleIndexTest extends HadoopTestCase {

    private static final String INPUT_EOSDOCS =
        "SimpleIndexTest.eosdocs";

    /** The package name for property prefixing. */
    public static final String LOCAL_PATH;

    static {
        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = TokenizerProvider.class.getClassLoader();
        }
        final URL resource = classLoader.getResource(INPUT_EOSDOCS);

        final String path = resource.getPath();
        final int lastIndexOf = path.lastIndexOf("/");
        LOCAL_PATH = path.substring(0, lastIndexOf);
    }

    private String resultPath;

    public SimpleIndexTest() throws IOException {
        this(HadoopTestCase.LOCAL_MR, HadoopTestCase.LOCAL_FS, 2, 2);
    }

    public SimpleIndexTest(final int mrMode, 
                           final int fsMode,
                           final int taskTrackers,
                           final int dataNodes)
            throws IOException {
        super(mrMode, fsMode, taskTrackers, dataNodes);
    }

    @Test
    public void testCreateIndex() throws Exception {
        final JobConf jobConf = createJobConf();
        jobConf.setJobName("eos Indexing Test");

        jobConf.addInputPath(new Path(LOCAL_PATH, INPUT_EOSDOCS));

        final Path p = new Path(this.resultPath);
        jobConf.setOutputPath(p);
        jobConf.setMapperClass(IndexMapper.class);
        jobConf.setReducerClass(IndexReducer.class);

        jobConf.setOutputFormat(LuceneOutputFormat.class);
        jobConf.setOutputKeyClass(LongWritable.class);
        jobConf.setOutputValueClass(Text.class);

        final RunningJob job = JobClient.runJob(jobConf);
        job.waitForCompletion();

        final String fileSeparator = System.getProperty("file.separator");
        final String indexPath = this.resultPath + fileSeparator + "part-00000";

        final Directory indexDir = FSDirectory.getDirectory(indexPath);
        final IndexSearcher searcher = new IndexSearcher(indexDir);
        final String queryString = 
            DefaultLuceneDocumentCreator.FieldName.CONTENT.name() + ":indexing";

        final Configuration lconf = new HadoopConfigurationAdapter(jobConf);

        final AnalyzerProvider provider = AnalyzerProvider.newInstance(lconf);
        final Analyzer analyzer = provider.get();
        final QueryParser parser =
            new QueryParser(
                    DefaultLuceneDocumentCreator.FieldName.CONTENT.name(),
                    analyzer
            );
        final Query query = parser.parse(queryString);
        final Hits hits = searcher.search(query);

        assertEquals(1, hits.length());
        final Hit hit = (Hit) hits.iterator().next();
        assertEquals("#2",
                     hit.get(DefaultLuceneDocumentCreator.FieldName.ID.name()));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.resultPath =
            LOCAL_PATH + "/SimpleIndexTest.testresult";
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        final FileSystem fs = getFileSystem();
        fs.delete(new Path(this.resultPath));
        fs.close();
    }
}
