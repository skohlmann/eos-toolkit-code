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

import net.sf.eos.Supplier;
import net.sf.eos.config.ConfigurableSupplier;
import net.sf.eos.config.Configuration;


public class SupplierFactoryTest {

    @Test
    public void creatInstanceWithClass() {
        final Supplier<String> p =
            SupplierFactory.newSupplier(StringSupplier.class);

        assertTrue(p instanceof StringSupplier);
    }

    @Test(expected=IllegalArgumentException.class)
    public void creatInstanceWithClassAsNull() {
        SupplierFactory.newSupplier(null);
    }

    @Test
    public void creatInstanceWithConfigurableProvider() {
        final Supplier<String> p =
            SupplierFactory.newSupplier(ConfigurableStringSupplier.class,
                                        new Configuration());

        assertTrue(p instanceof ConfigurableSupplier);
    }

    @Test(expected=IllegalArgumentException.class)
    public void creatInstanceWithConfigurableSupplierWithNullClass() {
        SupplierFactory.newSupplier((Class) null,
                                    new Configuration());
    }

    @Test(expected=IllegalArgumentException.class)
    public void creatInstanceWithConfigurableSupplierWithNullConfiguration() {
        SupplierFactory.newSupplier(ConfigurableStringSupplier.class,
                                    null);
    }

    @Test
    public void creatInstanceWithClassWithConfigurationAndName() {
        final String key = "key";
        final Configuration config = new Configuration();
        config.set(key, StringSupplier.class.getName());
        final Supplier<String> p =
            SupplierFactory.newSupplier(key, config);

        assertTrue(p instanceof StringSupplier);
    }

    @Test
    public void creatInstanceWithClassWithConfigurationAndDefaultName() {
        final String key = "key";
        final Configuration config = new Configuration();
        final Supplier<String> p =
            SupplierFactory.newSupplier(key,
                                        config,
                                        StringSupplier.class.getName());

        assertTrue(p instanceof StringSupplier);
    }

    @Test
    public void creatInstanceWithClassWithConfigurationAndNoDefaultName() {
        final String key = "key";
        final Configuration config = new Configuration();
        config.set(key, ConfigurableStringSupplier.class.getName());
        final Supplier<String> p =
            SupplierFactory.newSupplier(key,
                                        config,
                                        StringSupplier.class.getName());

        assertTrue(p instanceof ConfigurableStringSupplier);
    }

    final static class StringSupplier implements Supplier<String> {
        public String get() {
            return "";
        }
    }

    final static class ConfigurableStringSupplier implements ConfigurableSupplier<String> {
        public String get() {
            return "";
        }
        public void configure(final Configuration config) {
        }
    }
}
