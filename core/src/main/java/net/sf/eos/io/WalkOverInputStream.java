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

import java.io.IOException;
import java.io.InputStream;

/**
 * Walks over from a first (<em>from</em>) stream to the second (<em>to</em>)
 * stream if the first stream returns -1.
 * @author Sascha Kohlmann
 */
public final class WalkOverInputStream extends InputStream {

    private final InputStream from;
    private final InputStream to;
    private boolean inFrom = true;

    /**
     * Creates a new instance.
     * @param from the first stream to walk over to the <em>to</em> stream
     * @param to the second stream, read if <em>from</em> returns -1
     * @throws IllegalArgumentException if one parameter is <code>null</code>
     */
    public WalkOverInputStream(
            @SuppressWarnings("hiding") final InputStream from,
            @SuppressWarnings("hiding") final InputStream to) {

        if (from == null || to == null) {
            throw new IllegalArgumentException("one or both parameter are null");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Reads from noth streams. Walks over from the <em>from</em> stream to
     * the <em>to</em> stream if <em>from</em> stream returns -1. Returns
     * -1 if <em>to</em> stream also returns -1.
     * @return the next byte of data, or -1 if the end of the stream is reached
     */
    @Override
    public int read() throws IOException {
        if (this.inFrom) {
            assert this.from != null;
            final int retval = this.from.read();
            if (retval >= 0) {
                return retval;
            }
            this.inFrom = false;
        }

        assert this.to != null;
        return this.to.read();
    }

    /**
     * @return always <code>false</code>
     */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
     * Close both streams.
     */
    @Override
    public void close() throws IOException {
        assert this.from != null;
        assert this.to != null;

        this.from.close();
        this.to.close();
    }

    /*
     * @see java.io.InputStream#skip(long)
     */
    @Override
    public long skip(final long length) throws IOException {
        if (this.inFrom) {
            assert this.from != null;
            return this.from.skip(length);
        }

        assert this.to != null;
        return this.to.skip(length);
    }
}
