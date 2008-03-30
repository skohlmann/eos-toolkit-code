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
package net.sf.eos.config;

import java.util.Map.Entry;

/**
 * Hadoop Configuration holder.
 * @author Sascha Kohlmann
 */
public class HadoopConfigurationAdapter extends Configuration {

    /**
     * Copy constructor for Hadoop configuration.
     * @param config hadoop configuration
     */
    public HadoopConfigurationAdapter(final org.apache.hadoop.conf.Configuration config) {
        super();
        for (final Entry<String, String> entry : config) {
            final String name = entry.getKey();
            final String value = entry.getValue();
            set(name, value);
        }
    }

    public static void addHadoopConfigToEosConfig(
            final org.apache.hadoop.conf.Configuration from,
            final Configuration to) {

        for (final Entry<String, String> entry : from) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            to.set(key, value);
        }
    }
}
