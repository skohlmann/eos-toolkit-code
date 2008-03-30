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
package net.sf.eos.document;

import net.sf.eos.document.NewlineReplaceWriter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;


public class NewlineReplaceWriterTest {

    @Test
    public void lineFeedReplacementForString() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        replaced.write("this\ris");
        replaced.flush();

        assertEquals("this is", writer.getBuffer().toString());
    }

    @Test
    public void lineCarriageReturnReplacementForString() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        replaced.write("this\nis");
        replaced.flush();

        assertEquals("this is", writer.getBuffer().toString());
    }

    @Test
    public void lineFeedReplacementForChar() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        replaced.write('t');
        replaced.write('\r');
        replaced.write('i');
        replaced.flush();

        assertEquals("t i", writer.getBuffer().toString());
    }

    @Test
    public void lineCarriageReturnReplacementForChar() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        replaced.write('t');
        replaced.write('\n');
        replaced.write('i');
        replaced.flush();

        assertEquals("t i", writer.getBuffer().toString());
    }

    @Test
    public void lineFeedReplacementForCharArray1() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\ris ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 1, 7);
        replaced.flush();

        assertEquals("this is", writer.getBuffer().toString());
    }

    @Test
    public void lineCarriageReturnReplacementForCharArray1() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\nis ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 1, 7);
        replaced.flush();

        assertEquals("this is", writer.getBuffer().toString());
    }

    @Test
    public void lineFeedReplacementForCharArray2() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\ris ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 0, 9);
        replaced.flush();

        assertEquals(" this is ", writer.getBuffer().toString());
    }

    @Test
    public void lineCarriageReturnReplacementForCharArray2() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\nis ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 0, 9);
        replaced.flush();

        assertEquals(" this is ", writer.getBuffer().toString());
    }

    @Test
    public void lineFeedReplacementForCharArray3() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\ris ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 1, 8);
        replaced.flush();

        assertEquals("this is ", writer.getBuffer().toString());
    }

    @Test
    public void lineCarriageReturnReplacementForCharArray3() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\nis ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 1, 8);
        replaced.flush();

        assertEquals("this is ", writer.getBuffer().toString());
    }


    @Test
    public void lineFeedReplacementForCharArray4() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\ris ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 0, 8);
        replaced.flush();

        assertEquals(" this is", writer.getBuffer().toString());
    }

    @Test
    public void lineCarriageReturnReplacementForCharArray4() throws Exception {
        final StringWriter writer = new StringWriter();
        final NewlineReplaceWriter replaced = new NewlineReplaceWriter(writer);
        final String text = " this\nis ";
        final char[] cs = text.toCharArray();
        replaced.write(cs, 0, 8);
        replaced.flush();

        assertEquals(" this is", writer.getBuffer().toString());
    }
}
