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

import net.sf.eos.Function;
import net.sf.eos.util.Compositions;
import net.sf.eos.util.functions.ConfigurationKeyIntrospectorFunction.ConfigurationKeySupport;
import net.sf.eos.util.functions.ConfigurationKeyIntrospectorFunctionTest.Dummy;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultValueConfigurationKeyFunctionTest {

    private ConfigurationKeyIntrospectorFunction introspector;

    @Test
    public void creation() {
        new DefaultValueConfigurationKeyFunction();
    }

    @Test
    public void simpleDefaultValue() {
        final ConfigurationKeySupport support =
            new ConfigurationKeySupport(Dummy.class, "key");
        final DefaultValueConfigurationKeyFunction function =
            new DefaultValueConfigurationKeyFunction();
        final Function<ConfigurationKeySupport, String> composition =
            Compositions.compose(this.introspector, function);
        final String value = composition.apply(support);
        assertEquals("nothing", value);
    }

    @Test
    public void nullValue() {
        final DefaultValueConfigurationKeyFunction function =
            new DefaultValueConfigurationKeyFunction();
        assertNull(function.apply(null));
    }

    @Before
    public void setUp() throws Exception {
        this.introspector = new ConfigurationKeyIntrospectorFunction();
    }
}
