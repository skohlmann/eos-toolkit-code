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

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;

import java.io.IOException;

/**
 * Supports different strategies to fetch the cache path.
 * @author Sascha Kohlmann
 * @see FullyDistributedCacheStrategy
 * @see TestDistributedCacheStrategy
 */
public interface DistributedCacheStrategy {

    /** The key for the name of the strategy implementing class. */
    @SuppressWarnings("nls")
    String STRATEGY_CONFIG_NAME =
        "net.sf.eos.hadoop.DistributedCacheStrategy.impl";

    /**
     * Strategies implementing classes must implement this method.
     * @param conf the environment configuration to get the cache
     * @return pathes to the chache.
     * @throws IOException if an error occurs
     */
    Path[] distributedCachePathes(final JobConf conf) throws IOException;
}
