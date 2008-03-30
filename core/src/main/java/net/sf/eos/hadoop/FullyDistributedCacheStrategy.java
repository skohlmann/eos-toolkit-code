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
package net.sf.eos.hadoop;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Supports the cache in a fully distributed environment.
 * @author Sascha Kohlmann
 */
public class FullyDistributedCacheStrategy implements DistributedCacheStrategy {

    /** The logging of this class. */
    private static final Logger LOG =
        Logger.getLogger(FullyDistributedCacheStrategy.class.getName());

    /*
     * @see net.sf.eos.hadoop.DistributedCacheStrategy#distributedCachePathes(org.apache.hadoop.mapred.JobConf)
     */
    @SuppressWarnings("nls")
    public Path[] distributedCachePathes(final JobConf conf) throws IOException
    {
        final List<Path> pathes = new ArrayList<Path>();
        final Path[] pathList = DistributedCache.getLocalCacheFiles(conf);

        for (final Path path : pathList) {
            final Path qualified = new Path("file", "", path.toString());
            LOG.info("path: " + path + " - qualified path: " + qualified);
            pathes.add(qualified);
        }

        return pathes.toArray(new Path[pathList.length]);
    }
}
