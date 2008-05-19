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
package net.sf.eos.util;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import net.sf.eos.Provider;
import net.sf.eos.config.ConfigurableProvider;
import net.sf.eos.config.Configuration;


public class ProviderFactoryTest {

    @Test
    public void creatInstanceWithClass() {
        final Provider<String> p =
            ProviderFactory.newProvider(StringProvider.class);

        assertTrue(p instanceof StringProvider);
    }

    @Test(expected=IllegalArgumentException.class)
    public void creatInstanceWithClassAsNull() {
        ProviderFactory.newProvider(null);
    }

    @Test
    public void creatInstanceWithConfigurableProvider() {
        final Provider<String> p =
            ProviderFactory.newProvider(ConfigurableStringProvider.class,
                                        new Configuration());

        assertTrue(p instanceof ConfigurableProvider);
    }

    @Test(expected=IllegalArgumentException.class)
    public void creatInstanceWithConfigurableProviderWithNullClass() {
        ProviderFactory.newProvider((Class) null,
                                    new Configuration());
    }

    @Test(expected=IllegalArgumentException.class)
    public void creatInstanceWithConfigurableProviderWithNullConfiguration() {
        ProviderFactory.newProvider(ConfigurableStringProvider.class,
                                    null);
    }

    @Test
    public void creatInstanceWithClassWithConfigurationAndName() {
        final String key = "key";
        final Configuration config = new Configuration();
        config.set(key, StringProvider.class.getName());
        final Provider<String> p =
            ProviderFactory.newProvider(key, config);

        assertTrue(p instanceof StringProvider);
    }

    @Test
    public void creatInstanceWithClassWithConfigurationAndDefaultName() {
        final String key = "key";
        final Configuration config = new Configuration();
        final Provider<String> p =
            ProviderFactory.newProvider(key,
                                        config,
                                        StringProvider.class.getName());

        assertTrue(p instanceof StringProvider);
    }

    @Test
    public void creatInstanceWithClassWithConfigurationAndNoDefaultName() {
        final String key = "key";
        final Configuration config = new Configuration();
        config.set(key, ConfigurableStringProvider.class.getName());
        final Provider<String> p =
            ProviderFactory.newProvider(key,
                                        config,
                                        StringProvider.class.getName());

        assertTrue(p instanceof ConfigurableStringProvider);
    }

    final static class StringProvider implements Provider<String> {
        public String get() {
            return "";
        }
    }

    final static class ConfigurableStringProvider implements ConfigurableProvider<String> {
        public String get() {
            return "";
        }
        public void configure(final Configuration config) {
        }
    }
}
