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

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.BufferedIndexOutput;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.Lock;

/**
 * Reads a Lucene index stored in Hadoops DFS.
 * <p>Copied from Nutch source code.</p>
 * @see org.apache.nutch.indexer.FsDirectory
 * @author Nutch Team
 */
public class FsDirectory extends Directory {

  private FileSystem fs;
  private Path directory;
  private int ioFileBufferSize;

  public FsDirectory(final FileSystem fs, final Path directory,
          final boolean create, final Configuration conf)
    throws IOException {

    this.fs = fs;
    this.directory = directory;
    this.ioFileBufferSize = conf.getInt("io.file.buffer.size", 4096);

    if (create) {
      create();
    }

    final FileStatus fileStatus = fs.getFileStatus(directory);
    if ( ! fileStatus.isDir()) {
      throw new IOException(directory + " not a directory");
    }
  }

  private void create() throws IOException {
    if (! this.fs.exists(this.directory)) {
        this.fs.mkdirs(this.directory);
    }

    final FileStatus fileStatus = this.fs.getFileStatus(this.directory);

    if (! fileStatus.isDir()) {
      throw new IOException(this.directory + " not a directory");
    }

    // clear old files
    Path[] files = this.fs.listPaths(new Path[] {this.directory});
    for (int i = 0; i < files.length; i++) {
      if (! this.fs.delete(files[i]))
        throw new IOException("Cannot delete " + files[i]);
    }
  }

  @Override
public String[] list() throws IOException {
    Path[] files = this.fs.listPaths(new Path[] {this.directory});
    if (files == null) return null;

    String[] result = new String[files.length];
    for (int i = 0; i < files.length; i++) {
      result[i] = files[i].getName();
    }
    return result;
  }

  @Override
public boolean fileExists(final String name) throws IOException {
    return this.fs.exists(new Path(this.directory, name));
  }

  @Override
public long fileModified(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
public void touchFile(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
public long fileLength(String name) throws IOException {
        final Path path = new Path(this.directory, name);
        final FileStatus fileStatus = this.fs.getFileStatus(path);
        return fileStatus.getLen();
  }

  @Override
public void deleteFile(String name) throws IOException {
    if (! this.fs.delete(new Path(this.directory, name)))
      throw new IOException("Cannot delete " + name);
  }

  @Override
public void renameFile(String from, String to) throws IOException {
    // DFS is currently broken when target already exists,
    // so we explicitly delete the target first.
    Path target = new Path(this.directory, to);
    if (this.fs.exists(target)) {
        this.fs.delete(target);
    }
    this.fs.rename(new Path(this.directory, from), target);
  }

  @Override
public IndexOutput createOutput(String name) throws IOException {
    Path file = new Path(directory, name);
    if (fs.exists(file) && !fs.delete(file))      // delete existing, if any
      throw new IOException("Cannot overwrite: " + file);

    return new DfsIndexOutput(file, this.ioFileBufferSize);
  }


  @Override
public IndexInput openInput(final String name) throws IOException {
    return new DfsIndexInput(new Path(this.directory, name), this.ioFileBufferSize);
  }

  /** {@inheritDoc} */
  @Override
public Lock makeLock(final String name) {
    return new Lock() {
      @Override
    public boolean obtain() {
        return true;
      }
      @Override
    public void release() {
      }
      @Override
    public boolean isLocked() {
        throw new UnsupportedOperationException();
      }
      @Override
    public String toString() {
        return "Lock@" + new Path(directory, name);
      }
    };
  }

  @Override
public synchronized void close() throws IOException {
      this.fs.close();
  }

  @Override
public String toString() {
    return this.getClass().getName() + "@" + this.directory;
  }


  private class DfsIndexInput extends BufferedIndexInput {

    /** Shared by clones. */
    private class Descriptor {
      public FSDataInputStream in;
      public long position;                       // cache of in.getPos()
      public Descriptor(Path file, int ioFileBufferSize) throws IOException {
        this.in = fs.open(file);
      }
    }

    private final Descriptor descriptor;
    private final long length;
    private boolean isClone;

    public DfsIndexInput(Path path, int ioFileBufferSize) throws IOException {
      this.descriptor = new Descriptor(path,ioFileBufferSize);
      final FileStatus fileStatus = fs.getFileStatus(path);
      this.length = fileStatus.getLen();
    }

    @Override
    protected void readInternal(byte[] b, int offset, int len)
      throws IOException {
      synchronized (this.descriptor) {
        long position = getFilePointer();
        if (position != this.descriptor.position) {
            this.descriptor.in.seek(position);
            this.descriptor.position = position;
        }
        int total = 0;
        do {
          int i = this.descriptor.in.read(b, offset+total, len-total);
          if (i == -1) {
            throw new IOException("read past EOF");
          }

          this.descriptor.position += i;
          total += i;
        } while (total < len);
      }
    }

    @Override
    public void close() throws IOException {
      if (! this.isClone) {
          this.descriptor.in.close();
      }
    }

    @Override
    protected void seekInternal(long position) {} // handled in readInternal()

    @Override
    public long length() {
      return length;
    }

    @Override
    protected void finalize() throws IOException {
      close();                                      // close the file
    }

    @Override
    public Object clone() {
      DfsIndexInput clone = (DfsIndexInput)super.clone();
      clone.isClone = true;
      return clone;
    }
  }

  private class DfsIndexOutput extends BufferedIndexOutput {
    private FSDataOutputStream out;

    public DfsIndexOutput(Path path, int ioFileBufferSize) throws IOException {
        this.out = fs.create(path);
    }

    @Override
    public void flushBuffer(byte[] b, int offset, int size) throws IOException {
        this.out.write(b, offset, size);
    }

    @Override
    public void close() throws IOException {
      super.close();
      this.out.close();
    }

    @Override
    public void seek(long pos) throws IOException {
      throw new UnsupportedOperationException();
    }

    @Override
    public long length() throws IOException {
      return this.out.getPos();
    }

    @Override
    protected void finalize() throws IOException {
        this.out.close();                                // close the file
    }

  }

}

