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
 */
package net.sf.eos.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * The implementation replaces linefeed (ASCII {@literal 0x0a}) and carriage
 * return (ASCII {@literal 0x0d}) characters thru a space character
 * (ASCII {@literal 0x20}).
 * @author Sascha Kohlmann
 */
public class NewlineReplaceWriter extends FilterWriter {

    /**
     * Creates a new writer.
     * @param out a writer to decorate
     */
    public NewlineReplaceWriter(final Writer out) {
        super(out);
    }

    /*
     * @see java.io.FilterWriter#write(int)
     */
    @Override
    public void write(final int c) throws IOException {
        if (c == '\n' || c == '\r') {
            this.out.write(' ');
        } else {
            this.out.write(c);
        }
    }

    /*
     * @see java.io.FilterWriter#write(char[], int, int)
     */
    @Override
    public void write(final char cbuf[],
                      final int off,
                      final int len) throws IOException {
        final char[] nbuf = new char[len];

        for (int i = 0, j = off; i < len; j++, i++) {
            char c = cbuf[j];
            if (c == '\n' || c == '\r') {
                c = ' ';
            }
            nbuf[i] = c;
        }
        this.out.write(nbuf, 0, nbuf.length);
    }

    /*
     * @see java.io.FilterWriter#write(java.lang.String, int, int)
     */
    @Override
    public void write(final String str,
                      final int off,
                      final int len) throws IOException {
        final String sub = str.substring(off, (off+ len));
        final char[] cs = sub.toCharArray();
        write(cs, 0, cs.length);
    }
}
