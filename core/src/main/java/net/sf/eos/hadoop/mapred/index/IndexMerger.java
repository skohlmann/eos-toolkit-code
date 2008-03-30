/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.util.Tool;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

/**
 * IndexMerger creates an index for the output corresponding to a
 * single fetcher run.
 *
 * @author Doug Cutting
 * @author Mike Cafarella
 */
public class IndexMerger extends Configured implements Tool {
  public static final Log LOG = LogFactory.getLog(IndexMerger.class);

  public static final String DONE_NAME = "merge.done";

  public IndexMerger() {

  }

  public IndexMerger(final Configuration conf) {
    setConf(conf);
  }

  /**
   * Merge all input indexes to the single output index
   */
  public void merge(Path[] indexes, Path outputIndex, Path localWorkingDir)
          throws IOException {

    if (LOG.isInfoEnabled()) {
      LOG.info("merging indexes to: " + outputIndex);
    }
    final FileSystem localFs = FileSystem.getLocal(getConf());

    if (localWorkingDir == null) {
      localWorkingDir = new Path("indexmerger-" + System.currentTimeMillis());
    }

    if (localFs.exists(localWorkingDir)) {
      localFs.delete(localWorkingDir);
    }

    localFs.mkdirs(localWorkingDir);

    // Get local output target //
    final FileSystem fs = FileSystem.get(getConf());
    final Path tmpLocalOutput = new Path(localWorkingDir, "merge-output");
    final Path localOutput = fs.startLocalOutput(outputIndex, tmpLocalOutput);

    final Directory[] dirs = new Directory[indexes.length];
    for (int i = 0; i < indexes.length; i++) {
      if (LOG.isInfoEnabled()) { LOG.info("Adding " + indexes[i]); }
      dirs[i] = new FsDirectory(fs, indexes[i], false, getConf());
    }

    // Patch SK - in urspruenglicher Applikation nicht enthalten
//    final FsDirectory outDir = new FsDirectory(fs, localOutput, true, getConf());

    // Merge indices //
    IndexWriter writer = new IndexWriter(localOutput.toString(), null, true);
//    IndexWriter writer = new IndexWriter(outDir, null, true); // Patch SK damit n HDFS System geschrieben wird
    writer.setMergeFactor(getConf().getInt("indexer.mergeFactor", IndexWriter.DEFAULT_MERGE_FACTOR));
    writer.setMaxBufferedDocs(getConf().getInt("indexer.minMergeDocs", IndexWriter.DEFAULT_MAX_BUFFERED_DOCS));
    writer.setMaxMergeDocs(getConf().getInt("indexer.maxMergeDocs", IndexWriter.DEFAULT_MAX_MERGE_DOCS));
    writer.setTermIndexInterval(getConf().getInt("indexer.termIndexInterval", IndexWriter.DEFAULT_TERM_INDEX_INTERVAL));
    writer.setUseCompoundFile(false);
    writer.addIndexes(dirs);
    writer.close();

    // Put target back //
    LOG.info("completeLocalOutput: from '" + tmpLocalOutput.toString() + "' to '" + outputIndex.toString() + "'");
//    fs.completeLocalOutput(outputIndex, tmpLocalOutput);
//    FileSystem.getLocal(getConf()).delete(localWorkingDir);
    if (LOG.isInfoEnabled()) {  
        LOG.info("done merging");
    }

  }

  public int run(String[] args) throws Exception {
    String usage = "IndexMerger [-workingdir <workingdir>] outputIndex indexesDir...";
    if (args.length < 2) {
      System.err.println("Usage: " + usage);
      return -1;
    }

    //
    // Parse args, read all index directories to be processed
    //
    final FileSystem fs = FileSystem.get(getConf());
    List<Path> indexDirs = new ArrayList<Path>();

    Path workDir = null;
    int i = 0;
    if ("-workingdir".equals(args[i])) {
      i++;
      workDir = new Path(args[i++], "indexmerger-" + System.currentTimeMillis());
    }

    Path outputIndex = new Path(args[i++]);

    for (; i < args.length; i++) {
      indexDirs.addAll(Arrays.asList(fs.listPaths(new Path[] {new Path(args[i])})));
    }

    //
    // Merge the indices
    //

    Path[] indexFiles = (Path[])indexDirs.toArray(new Path[indexDirs.size()]);

    try {
      merge(indexFiles, outputIndex, workDir);
      return 0;
    } catch (Exception e) {
      LOG.fatal("IndexMerger: " + StringUtils.stringifyException(e));
      return -1;
    }
  }
}
