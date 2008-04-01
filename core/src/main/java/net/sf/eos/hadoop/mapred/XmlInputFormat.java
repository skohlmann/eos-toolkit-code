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
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConfigurable;
import org.apache.hadoop.mapred.JobConf;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Experimental */
public abstract class XmlInputFormat<E extends Writable> 
        extends FileInputFormat<LongWritable, E> implements JobConfigurable {

    static final Logger LOG =
        Logger.getLogger(XmlInputFormat.class.getName());

    /** Defined the name of the start element. The name must start with a
     * Less-Than sign, followed by a legal XML element name character. There
     * is no need for a closing Greater-Than sign. */
    @SuppressWarnings("nls")
    public static final String START_ELEMENT_CONFIG_NAME =
        "net.sf.eos.hadoop.mapred.XmlInputFormat.startElement";

    /** The value for the input character encoding. The default value is
     * "<tt>UTF-8</tt>". */
    @SuppressWarnings("nls")
    public static final String ENCODING_CONFIG_NAME =
        "net.sf.eos.hadoop.mapred.XmlInputFormat.encoding";

    private String startElement = null;
    private String endElement = null;
    private String enc = null;

    private JobConf conf;

    @SuppressWarnings("nls")
    public void configure(final JobConf conf) {
        this.conf = conf;
        this.startElement = conf.get(START_ELEMENT_CONFIG_NAME);
        if (this.startElement == null || this.startElement.length() == 0) {
            LOG.warning("startElement not configured");
        } else {
            this.endElement =
                createEndElementFromStartElement(this.startElement);
        }
        this.enc = conf.get(ENCODING_CONFIG_NAME, "UTF-8");
    }

    /**
     * Returns the start element.
     * @return the start element
     */
    public String getStartElement() {
        return this.startElement;
    }

    /**
     * Returns the end element.
     * @return the end element
     */
    public String getEndElement() {
        return this.endElement;
    }

    /**
     * Returns the encoding.
     * @return the encoding
     */
    public String getEncoding() {
        return this.enc;
    }

    /**
     * Creates the end element name for the start element, setted thru the
     * {@link #configure(JobConf)}. 
     * @param start th
     * @return the end element or <code>null</code> if it is not possible to
     *         create the end element.
     * @see #START_ELEMENT_CONFIG_NAME
     */
    @SuppressWarnings("nls")
    public String createEndElementFromStartElement(final String start) {
        if (start == null) {
            return null;
        }
        final char[] ca = start.toCharArray();
        if (ca.length <= 1) {
            LOG.warning("Element contains no name characters");
            return null;
        }
        if (ca[0] != '<') {
            LOG.warning("startElement is not '<'");
            return null;
        }

        final StringBuilder sb = new StringBuilder("</");

        for (int i = 1; i < ca.length; i++) {
            final char c = ca[i];
            if (c == '>' || Character.isWhitespace(c)) {
                break;
            }
            sb.append(c);
        }
        sb.append(">");

        final String element = sb.toString();
        if (LOG.isLoggable(Level.CONFIG)) {
            LOG.config("close element is: " + element);
        }

        if (element.equals("</>")) {
            LOG.warning("End element maybe not legal: " + element);
        }
        return element;
    }
}
