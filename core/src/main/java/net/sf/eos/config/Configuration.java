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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A simple name value configuration handler.
 * @author Sascha Kohlmann
 */
public class Configuration implements Iterable<Entry<String, String>> {

    private final Map<String, String> config = new HashMap<String, String>();

    /** Creates a new instance. */
    public Configuration() {
        ;
    }

    /** 
     * Copy constructor.
     * @param toCopy the <code>Configuration</code> to copy.
     */
    public Configuration(final Configuration toCopy) {
        for (final Entry<String, String> entry : toCopy.config.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            this.config.put(key, value);
        }
    }

    /**
     * Puts the configuration for the name and the value
     * @param name the name of the configuration value
     * @param value the value of the configuration name.
     */
    public void set(final String name, final String value) {
        assert this.config != null;
        this.config.put(name, value);
    }

    /**
     * Returns a value for the given name.
     * @param name the name to look up for a value
     * @return a value or <code>null</code>
     */
    public String get(final String name) {
        assert this.config != null;
        return this.config.get(name);
    }

    /**
     * Returns a value for the given name.
     * @param name the name to look up for a value
     * @param defaultValue a default value
     * @return a value or <code>null</code>
     */
    public String get(final String name, final String defaultValue) {
        assert this.config != null;
        final String retval = this.config.get(name);
        return retval != null ? retval : defaultValue;
    }

    /**
     * Returns an iterator for the configuration entries.
     * @return an iterator for the configuration entries
     */
    public Iterator<Entry<String, String>> iterator() {
        assert this.config != null;
        return this.config.entrySet().iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Configuration[\n");
        for (final Entry<String, String> entry : this) {
            sb.append("        ");
            sb.append(entry.getKey());
            sb.append("  =  ");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        sb.append("]");

        return sb.toString();
    }
}
