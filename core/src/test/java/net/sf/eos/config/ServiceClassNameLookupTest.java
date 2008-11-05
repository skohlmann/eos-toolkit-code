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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport;

public class ServiceClassNameLookupTest {

    @Test
    public void noServiceNameFile() {
        assertEquals("test", this.support.serviceClassNameLookup("zulu", "test"));
    }

    @Test
    public void correctServiceNameFile() {
        assertEquals("abc", this.support.serviceClassNameLookup("inject/", "correctFileValue"));
    }

    @Test
    public void noServiceNameValue() {
        try {
            this.support.serviceClassNameLookup("inject/", "noFileValue");
            fail("Oops... missing InjectionException");
        } catch (final InjectionException e) {
            // 
        }
    }

    @Test
    public void serviceNameWithSpace() {
        try {
            this.support.serviceClassNameLookup("inject/", "serviceNameWithSpace");
            fail("Oops... missing InjectionException");
        } catch (final InjectionException e) {
            // 
        }
    }

    @Test
    public void serviceNameWithTab() {
        try {
            this.support.serviceClassNameLookup("inject/", "serviceNameWithTab");
            fail("Oops... missing InjectionException");
        } catch (final InjectionException e) {
            // 
        }
    }

    @Test
    public void nameWithIllegalJavaStart() {
        try {
            this.support.serviceClassNameLookup("inject/", "nameWithIllegalJavaStart");
            fail("Oops... missing InjectionException");
        } catch (final InjectionException e) {
            // 
        }
    }

    @Test
    public void illegaljavaName() {
        try {
            this.support.serviceClassNameLookup("inject/", "illegaljavaName");
            fail("Oops... missing InjectionException");
        } catch (final InjectionException e) {
            // 
        }
    }

    @Test
    public void serviceNameWithComment() {
        assertEquals("rubikon",
                     this.support.serviceClassNameLookup("inject/", "serviceNameWithComment"));
    }

    @SuppressWarnings("unchecked")
    private DefaultInjectionSupport support;

    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        this.support = new DefaultInjectionSupport();
    }
    @After
    public void teardown() {
        this.support = null;
    }
}
