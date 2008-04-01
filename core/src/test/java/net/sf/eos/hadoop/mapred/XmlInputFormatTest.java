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
package net.sf.eos.hadoop.mapred;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/** Experimental */
public class XmlInputFormatTest {

    private XmlInputFormat format;

    @Test
    public void simpleCreateEndElement() {
        final String endElement =
            this.format.createEndElementFromStartElement("<d");
        assertEquals("</d>", endElement);
    }

    @Test
    public void simpleCreateEndElementWithCloseGreaterThanSign() {
        final String endElement =
            this.format.createEndElementFromStartElement("<d>");
        assertEquals("</d>", endElement);
    }

    @Test
    public void simpleCreateEndElementWithNoElementName() {
        final String endElement =
            this.format.createEndElementFromStartElement("<");
        assertNull(endElement);
    }

    @Test
    public void simpleCreateEndElementWithWhitespaceAfterOpenLessThan() {
        final String endElement =
            this.format.createEndElementFromStartElement("< ");
        assertEquals("</>", endElement);
    }

    @Test
    public void simpleCreateEndElementWithNull() {
        final String endElement =
            this.format.createEndElementFromStartElement(null);
        assertNull(endElement);
    }

    @Test
    public void simpleCreateEndElementWithLengthZeroString() {
        final String endElement =
            this.format.createEndElementFromStartElement("");
        assertNull(endElement);
    }

    @Test
    public void simpleCreateEndElementNoLessThanStartCharacter() {
        final String endElement =
            format.createEndElementFromStartElement("d");
        assertNull(endElement);
    }

    @Before
    public void createAbstractXmlInputFormat() {
        this.format = new DummyXmlInputFormat();
    }

    public final static class DummyXmlInputFormat 
            extends XmlInputFormat<LongWritable> {
        @Override
        public RecordReader getRecordReader(final InputSplit split,
                                            final JobConf conf,
                                            final Reporter reporter)
                throws IOException {
            return null;
        }
        
    }
}
