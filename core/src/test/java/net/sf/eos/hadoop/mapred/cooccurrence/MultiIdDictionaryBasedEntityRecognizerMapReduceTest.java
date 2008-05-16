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
package net.sf.eos.hadoop.mapred.cooccurrence;

import net.sf.eos.analyzer.TokenizerProvider;
import static net.sf.eos.document.EosDocument.ID_META_KEY;
import static net.sf.eos.document.EosDocument.YEAR_META_KEY;
import net.sf.eos.document.Serializer;
import net.sf.eos.document.XmlSerializer;
import net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer;
import net.sf.eos.entity.SimpleLongestMatchDictionaryBasedEntityRecognizer;
import static net.sf.eos.hadoop.mapred.cooccurrence.DictionaryBasedEntityRecognizerReducer.META_FIELD_FOR_SEPARATION_CONFIG_NAME;
import net.sf.eos.hadoop.DistributedCacheStrategy;
import net.sf.eos.hadoop.TestDistributedCacheStrategy;
import net.sf.eos.hadoop.mapred.Index;
import net.sf.eos.hadoop.mapred.cooccurrence.DictionaryBasedEntityRecognizerMapper;
import net.sf.eos.hadoop.mapred.cooccurrence.DictionaryBasedEntityRecognizerReducer;
import net.sf.eos.medline.MedlineTokenizerProvider;
import net.sf.eos.trie.AbstractTrieLoader;
import net.sf.eos.trie.XmlTrieLoader;

import org.apache.hadoop.filecache.DistributedCache;
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

public class MultiIdDictionaryBasedEntityRecognizerMapReduceTest
        extends HadoopTestCase {

    private static final String INPUT_EOSDOCS =
        "DictionaryBasedEntityRecognizerMapReduceTest.eosdocs";
    private static final String TRIEX_DAT = "simple.triex";

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

    public MultiIdDictionaryBasedEntityRecognizerMapReduceTest()
            throws IOException {
        this(HadoopTestCase.LOCAL_MR, HadoopTestCase.LOCAL_FS, 2, 2);
    }

    public MultiIdDictionaryBasedEntityRecognizerMapReduceTest(
            final int mrMode, 
            final int fsMode,
            final int taskTrackers,
            final int dataNodes)
                throws IOException {
        super(mrMode, fsMode, taskTrackers, dataNodes);
    }

    public void testSimpleMapReduce() throws Exception {
        final JobConf jobConf = createJobConf();
        jobConf.setMapperClass(DictionaryBasedEntityRecognizerMapper.class);
        jobConf.setReducerClass(DictionaryBasedEntityRecognizerReducer.class);
        jobConf.setJobName("eos entity multi id test");

        jobConf.addInputPath(new Path(LOCAL_PATH, INPUT_EOSDOCS));

        final Path p = new Path(this.resultPath);
        jobConf.setOutputPath(p);
        jobConf.setOutputKeyClass(Text.class);
        jobConf.setOutputValueClass(Text.class);

        jobConf.setMapOutputKeyClass(Text.class);
        jobConf.setMapOutputValueClass(Text.class);

        jobConf.set(META_FIELD_FOR_SEPARATION_CONFIG_NAME,
                    ID_META_KEY + ", " + YEAR_META_KEY);
        jobConf.set(Serializer.SERIALIZER_IMPL_CONFIG_NAME,
                    XmlSerializer.class.getName());
        jobConf.set(TokenizerProvider.TOKENIZER_PROVIDER_IMPL_CONFIG_NAME,
                    MedlineTokenizerProvider.class.getName());
        jobConf.set(AbstractTrieLoader.TRIE_LOADER_IMPL_CONFIG_NAME,
                    XmlTrieLoader.class.getName());
        jobConf.set(AbstractDictionaryBasedEntityRecognizer
                        .ABSTRACT_DICTIONARY_BASED_ENTITY_RECOGNIZER_IMPL_CONFIG_NAME,
                    SimpleLongestMatchDictionaryBasedEntityRecognizer
                        .class.getName());

        // For Tests
        DistributedCache.addCacheFile(new Path(LOCAL_PATH, TRIEX_DAT).toUri(),
                                      jobConf);
        jobConf.set(DistributedCacheStrategy.STRATEGY_IMPL_CONFIG_NAME,
                    TestDistributedCacheStrategy.class.getName());

        final RunningJob job = JobClient.runJob(jobConf);
        job.waitForCompletion();

        final Counters counters = job.getCounters();
        assertEquals(4, counters.getCounter(Index.REDUCE));
        assertEquals(7, counters.getCounter(Index.MAP));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.resultPath =
            LOCAL_PATH 
            + "/MultiIdDictionaryBasedEntityRecognizerMapReduceTest.testresult";
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        final FileSystem fs = getFileSystem();
        fs.delete(new Path(this.resultPath));
        fs.close();
    }
}
