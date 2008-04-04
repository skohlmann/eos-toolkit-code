/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.eos.hadoop.mapred.index;


import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.lucene.AnalyzerFactory;
import net.sf.eos.lucene.SimilarityFactory;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ObjectWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputFormatBase;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Progressable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.Similarity;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Support to write a Lucene index in a Hadoop Filesystem.
 * <p>Parts are copied from Nutch source code.</p>
 * @author Nutch Team
 * @author Sascha Kohlmann
 */
public class LuceneOutputFormat<K extends WritableComparable,
                                V extends ObjectWritable>
        extends OutputFormatBase<K, V> {

    /** The name of the merge factory value. Default value is 10. */
    public static final String MERGE_FACTOR_CONFIG_NAME =
        "net.sf.eos.hadoop.lucene.LuceneOutputFormat.writer.mergeFactor";

    /** The name of the max buffered docs value. Default value is 10. */
    public static final String MAX_BUFFERED_DOCS_CONFIG_NAME =
        "net.sf.eos.hadoop.lucene.LuceneOutputFormat.writer.maxBufferedDocs";

    /** The name of the max merge docs value. Default value is
     * {@link Integer#MAX_VALUE}. */
    public static final String MAX_MERGE_DOCS_CONFIG_NAME =
        "net.sf.eos.hadoop.lucene.LuceneOutputFormat.writer.maxMergeDocs";

    /** The RAM buffer size in MB. Default value is 200. */
    public static final String RAM_BUFFER_SIZE_MB_CONFIG_NAME =
        "net.sf.eos.hadoop.lucene.LuceneOutputFormat.writer.RAMBufferSizeMB";

    /** The maximum field length. Default value is 100000. */
    public static final String MAX_FIELD_LENGTH_CONFIG_NAME =
        "net.sf.eos.hadoop.lucene.LuceneOutputFormat.writer.maxFieldLength";

    public static final String DONE_NAME = "index.done";

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(LuceneOutputFormat.class.getName());

    /**
     * To configure see <code><em>XXX</em>_CONFIG_NAME</code> keys. Uses
     * internaly the instances of {@link AnalyzerFactory} and
     * {@link SimilarityFactory}.
     */
    @SuppressWarnings("cast")
    @Override
    public RecordWriter<K, V> getRecordWriter(final FileSystem fileSystem,
                                              final JobConf job,
                                              final String name,
                                              final Progressable progress)
            throws IOException {

        final Path perm = new Path(job.getOutputPath(), name);
        final Path temp = job.getLocalPath(
                "index/_"+Integer.toString(new Random().nextInt()));

        if (LOG.isLoggable(Level.INFO)) {
            LOG.info("path (perm): " + perm.getName());
            LOG.info("path (temp): " + temp.getName());
        }

        fileSystem.delete(perm);    // delete old, if any

        final Configuration config = new Configuration();
        HadoopConfigurationAdapter.addHadoopConfigToEosConfig(job, config);

        try {
            AnalyzerFactory analyzerFactory =
                AnalyzerFactory.newInstance(config);
            final Analyzer analyzer = analyzerFactory.newAnalyzer();

            final IndexWriter writer =  // build locally first
                new IndexWriter(
                        fileSystem.startLocalOutput(perm, temp).toString(),
                        analyzer,
                        true);

            final int mergeFactor = job.getInt(MERGE_FACTOR_CONFIG_NAME, 10);
            writer.setMergeFactor(mergeFactor);
            final int maxBufferedDocs = 
                job.getInt(MAX_BUFFERED_DOCS_CONFIG_NAME, 10);
            writer.setMaxBufferedDocs(maxBufferedDocs);
            final int maxMergeDocs = 
                job.getInt(MAX_MERGE_DOCS_CONFIG_NAME, Integer.MAX_VALUE);
            writer.setMaxMergeDocs(maxMergeDocs);
            final double ramBufferSize = (double)
                job.getFloat(RAM_BUFFER_SIZE_MB_CONFIG_NAME, 200.0f);
            writer.setRAMBufferSizeMB(ramBufferSize);
            final int maxFieldLength = 
                job.getInt(MAX_FIELD_LENGTH_CONFIG_NAME, 100000);
            writer.setMaxFieldLength(maxFieldLength);

            final SimilarityFactory similarityFactory =
                SimilarityFactory.newInstance(config);
            final Similarity similarity = similarityFactory.newSimilarity();

            writer.setSimilarity(similarity);

            if (LOG.isLoggable(Level.CONFIG)) {
                LOG.config("mergeFactor: " + mergeFactor);
                LOG.config("maxBufferedDocs: " + maxBufferedDocs);
                LOG.config("maxMergeDocs: " + maxMergeDocs);
                LOG.config("RAMBufferSizeDB: " + ramBufferSize);
                LOG.config("maxFieldLength: " + maxFieldLength);
                LOG.config("Lucene Analyzer instance: "
                           + analyzer.getClass().getName());
                LOG.config("Lucene Similarity instance: "
                           + similarity.getClass().getName());
            }

            final RecordWriterImpl<K, V> recordWriter
                = new RecordWriterImpl<K, V>(fileSystem, writer, analyzer);
            recordWriter.setPermanentPath(perm);
            recordWriter.setTemporaryPath(temp);

            return recordWriter;

        } catch (final EosException e) {
            final IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        }
    }

    static class RecordWriterImpl<K extends WritableComparable,
                                  V extends ObjectWritable>
            implements RecordWriter<K, V> {

        private boolean closed;

        /** The logging of this class. */
        private static final Logger LOG = Logger
                .getLogger(RecordWriterImpl.class.getName());

        private IndexWriter writer;
        private Analyzer luceneAnalyzer;
        private FileSystem fs;
        private Path permanentPath;
        private Path temporaryPath;

        public RecordWriterImpl(final FileSystem fileSystem,
                                final IndexWriter indexWriter,
                                final Analyzer analyzer) {
            this.writer = indexWriter;
            this.luceneAnalyzer = analyzer;
            this.fs = fileSystem;
        }

        public void setTemporaryPath(final Path temp) {
            this.temporaryPath = temp;
        }

        public void setPermanentPath(final Path perm) {
            this.permanentPath = perm;
        }

        /** {@inheritDocs} */
        public void write(final K key, final V value)
                throws IOException {

            final Document doc = (Document) value.get();
            LOG.finer(" Indexing document ....");
            this.writer.addDocument(doc, this.luceneAnalyzer);
        }

        /** {@inheritDocs} */
        public void close(final Reporter reporter) throws IOException {
            // spawn a thread to give progress heartbeats
            final Thread prog = new Thread() {
                @Override
                public void run() {
                    while (! RecordWriterImpl.this.closed) {
                        try {
                            reporter.setStatus("closing");
                            Thread.sleep(1000);
                        } catch (final InterruptedException e) {
                            continue; 
                        } catch (Throwable e) {
                            return;
                        }
                    }
                }
            };

            try {
                prog.start();
                LOG.info("Optimizing index --> " + this.writer.docCount()
                        + " documents.");
                // optimize & close index
                this.writer.optimize();
                this.writer.close();
                //copy to dfs
                this.fs.completeLocalOutput(this.permanentPath,
                                            this.temporaryPath);

                this.fs.createNewFile(new Path(this.permanentPath, DONE_NAME));
            } finally {
                this.closed = true;
            }
        }
    };
}
