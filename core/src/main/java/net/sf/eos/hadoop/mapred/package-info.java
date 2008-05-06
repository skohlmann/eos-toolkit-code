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
 * <p>Contains the base infrastructure for mapreduce handling.</p>
 * <p>{@link net.sf.eos.hadoop.mapred.AbstractEosDriver} supports the development
 * of mapreduce-Drivers. The {@link net.sf.eos.hadoop.mapred.KeyGenerator}
 * and its abstract factory implementation
 * {@link net.sf.eos.hadoop.mapred.AbstractKeyGenerator} supports key generation
 * in the map-task of a mapreduce job.</p>
 *
 * @since 0.1.0
 */
package net.sf.eos.hadoop.mapred;
