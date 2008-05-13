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

import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigurationTest {

    @Test
    public void creation() {
        new Configuration();
    }

    @Test
    public void simpleSetAndGet() {
        final Configuration config = new Configuration();
        config.set("key", "value");
        assertEquals("value", config.get("key"));
    }

    @Test
    public void defaultGet() {
        final Configuration config = new Configuration();
        assertEquals("default", config.get("key", "default"));
        assertEquals("version", config.get("key", "version"));
    }

    @Test
    public void remove() {
        final Configuration config = new Configuration();
        config.set("key1", "value");
        config.set("key2", "value");

        for (final Iterator<Entry<String, String>> itr = config.iterator() ; itr.hasNext(); ) {
            final Entry<String, String> e = itr.next();
            if ("key1".equals(e.getKey())) {
                 itr.remove();
            }
        }

        int count = 0;
        for (final Entry<String, String> e : config) {
            count++;
        }
        assertEquals(1, count);
    }

    @Test
    public void booleanGet() {
        final Configuration config = new Configuration();
        config.set("key", "true");
        assertTrue(config.getBoolean("key", false));
    }

    @Test
    public void booleanGetWithIllegalValue() {
        final Configuration config = new Configuration();
        config.set("key", "1234");
        assertFalse(config.getBoolean("key", false));
        assertTrue(config.getBoolean("key", true));
    }

    @Test
    public void booleanGetWithNoValue() {
        final Configuration config = new Configuration();
        assertFalse(config.getBoolean("key", false));
        assertTrue(config.getBoolean("key", true));
    }

    @Test
    public void intGet() {
        final Configuration config = new Configuration();
        config.set("key", "1");
        assertEquals(1, config.getInt("key", 2));
    }

    @Test
    public void intGetWithIllegalValue() {
        final Configuration config = new Configuration();
        config.set("key", "test");
        assertEquals(1, config.getInt("key", 1));
    }

    @Test
    public void intGetWithNoValue() {
        final Configuration config = new Configuration();
        assertEquals(1, config.getInt("key", 1));
    }

    @Test
    public void floatGet() {
        final Configuration config = new Configuration();
        config.set("key", "1.1");
        assertEquals(Float.floatToRawIntBits(1.1f),
                     Float.floatToRawIntBits(config.getFloat("key", (1.2f))));
    }

    @Test
    public void floatGetWithIllegalValue() {
        final Configuration config = new Configuration();
        config.set("key", "test");
        assertEquals(Float.floatToRawIntBits(1.1f),
                     Float.floatToRawIntBits(config.getFloat("key", (1.1f))));
    }

    @Test
    public void floatGetWithNoValue() {
        final Configuration config = new Configuration();
        assertEquals(Float.floatToRawIntBits(1.1f),
                     Float.floatToRawIntBits(config.getFloat("key", (1.1f))));
    }
}
