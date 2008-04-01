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
package net.sf.eos.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WalkOverInputStreamTest {

    @Test(expected=IllegalArgumentException.class)
    public void fromNull() throws IOException {
        final InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        new WalkOverInputStream(null, in);
    }

    @Test(expected=IllegalArgumentException.class)
    public void toNull() throws IOException {
        final InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
        new WalkOverInputStream(in, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void fromAndToNull() throws IOException {
        new WalkOverInputStream(null, null);
    }

    @Test
    public void checkWalkOver() throws IOException {
        final byte[] fromArray = new byte[] {0, 1};
        final byte[] toArray = new byte[] {2, 3};
        final InputStream fromStream = new ByteArrayInputStream(fromArray);
        final InputStream toStream = new ByteArrayInputStream(toArray);

        final InputStream test = new WalkOverInputStream(fromStream, toStream);

        assertEquals(0, test.read());
        assertEquals(1, test.read());
        assertEquals(2, test.read());
        assertEquals(3, test.read());
        assertEquals(-1, test.read());
    }
}
