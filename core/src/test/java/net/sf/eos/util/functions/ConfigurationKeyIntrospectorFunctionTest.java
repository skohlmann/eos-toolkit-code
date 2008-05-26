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
package net.sf.eos.util.functions;

import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.util.functions.ConfigurationKeyIntrospectorFunction.ConfigurationKeySupport;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ConfigurationKeyIntrospectorFunctionTest {

    @Test
    public void creation() {
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(Dummy.class, "key");
    }

    @Test
    public void simpleConfigurationKey() {
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(Dummy.class, "key");
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKey key = function.apply(support);
        assertNotNull(key);
        assertEquals("nothing", key.defaultValue());
    }

    @Test
    public void nullToApply() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKey key = function.apply(null);
        assertNull(key);
    }

    @Test
    public void nullClassOfSupportIsNull() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(null, "key");
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    @Test
    public void nullValueOfSupportIsNull() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(Dummy.class, null);
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    @Test
    public void noConfigurationKey() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(DummyWithNoKey.class, "key");
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    @Test
    public void noPublicConfigurationKey() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(DummyWithNoPublicConfigurationKey.class, "key");
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    @Test
    public void noFinalConfigurationKey() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(this.getClass(), "key");
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    @Test
    public void noStaticConfigurationKey() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(DummyWithNoStaticConfigurationKey.class, "key");
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    @Test
    public void privateConfigurationKey() {
        final ConfigurationKeyIntrospectorFunction function =
            new ConfigurationKeyIntrospectorFunction();
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(DummyWithPrivateConfigurationKey.class, "key");
        final ConfigurationKey key = function.apply(support);
        assertNull(key);
    }

    public class Dummy {
        public final static String SOMETHNG = "value";
        @ConfigurationKey(description="test", defaultValue="nothing")
        public final static String KEY = "key";
    }

    public class DummyWithNoKey {
        public final static String KEY = "key";
    }

    public class DummyWithNoPublicConfigurationKey {
        @ConfigurationKey(description="test", defaultValue="nothing")
        protected final static String KEY = "key";
    }

    public class DummyWithNoStaticConfigurationKey {
        @ConfigurationKey(description="test", defaultValue="nothing")
        public final String KEY = "key";
    }

    @ConfigurationKey(description="test", defaultValue="nothing")
    public static String KEY = "key";
}
