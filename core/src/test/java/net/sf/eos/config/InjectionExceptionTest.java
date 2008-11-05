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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;



/**
 * @author Sascha Kohlmann
 */
public class InjectionExceptionTest {

    @Test
    public void createWithDefaultConstructor() {
        new InjectionException();
    }

    @Test
    public void createWithMessage() {
        final String message = "simple message";
        final Exception e = new InjectionException(message);
        assertEquals(message, e.getMessage());
    }

    @Test
    public void createWithThrowable() {
        final String message = "simple message";
        final Exception e = new InjectionException(new IllegalArgumentException(message));
        assertEquals(message, e.getCause().getMessage());
        assertTrue(IllegalArgumentException.class == e.getCause().getClass());
    }


    @Test
    public void createWithThrowableAndMessage() {
        final String message = "simple message";
        final Exception e =
            new InjectionException(message, new IllegalStateException(message));
        assertEquals(message, e.getCause().getMessage());
        assertTrue(IllegalStateException.class == e.getCause().getClass());
        assertEquals(message, e.getMessage());
    }
}
