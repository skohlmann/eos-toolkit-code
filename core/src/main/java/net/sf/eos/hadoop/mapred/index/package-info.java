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


/**
 * Mapreduce task to create a Lucene index in the
 * <acronym title='Hadoop Distributed Filesystem'>HDFS</acronym>.
 * 
 * <p>{@link net.sf.eos.hadoop.mapred.index.IndexMapReduceDriver} is the main
 * class to start the indexing process.
 * {@link net.sf.eos.hadoop.mapred.index.IndexMerger} creates a compact,
 * optimized Lucene index from the different indices in the
 * <acronym title='Hadoop Distributed Filesystem'>HDFS</acronym>.</p>
 *
 * <p>Some code is taken from the
 * <a href='http://lucene.apache.org/nutch' title='Homepage'>Nutch</a> project
 * of the <a href='http://www.apache.org/' title='Homepage'>Apache</a> Foundation.
 * This code comes under the terms of the
 * <a href='http://www.apache.org/licenses/LICENSE-2.0' title='License'>Apache 2</a>
 * license.</p>
 *
 * @since 0.1.0
 * @author Sascha Kohlmann
 * @see net.sf.eos.lucene
 */
package net.sf.eos.hadoop.mapred.index;
