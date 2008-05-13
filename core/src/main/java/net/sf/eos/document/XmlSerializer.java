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

import net.sf.eos.io.NewlineReplaceWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Serializer and deserializer for an {@link EosDocument}. See
 * {@link XmlSerializer.ElementName} for element names. The order of the elements in
 * the root container are not defined. Also the order in the meta container
 * is not defined. If the root container contains more than one <em>title</em>
 * or <em>text</em> element, the latest elements may win. If the meta container
 * contains more than one <em>key</em>, the latest may win.
 * @author Sascha Kohlmann
 */
public class XmlSerializer extends Serializer {

    static final Log LOG = LogFactory.getLog(XmlSerializer.class.getName());

    /**
     * Represents the XML element names of a serialized &#949;&#959;s document.
     * @author Sascha Kohlmann
     */
    public enum ElementName {
        /** Root element of an &#949;&#959;s document. */
        d,
        /** Container for a meta data entry. */
        m,
        /** key of a meta data entry. There is only one key in a meta data
         * entry. */
        k,
        /**value of a meta data entry. There may be more than one value. */
        v,
        /** Title of a document. */
        ti,
        /** Text of a document. */
        te}

    @SuppressWarnings("nls")
    private static final String XML_OPEN = "<";
    @SuppressWarnings("nls")
    private static final String XML_CLOSE = ">";
    @SuppressWarnings("nls")
    private static final String XML_CLOSE_TAG = "</";

    /*
     * @see  net.sf.eos.document.Serializer#serialize(net.sf.eos.document.EosDocument, java.io.Writer)
     */
    @Override
    @SuppressWarnings("nls")
    public void serialize(final EosDocument doc, final Writer out)
            throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("start serialize EosDocument");
        }
        final NewlineReplaceWriter writer = new NewlineReplaceWriter(out);
        writer.write(XML_OPEN + ElementName.d.name() + XML_CLOSE);
        final  Map<String, List<String>> meta = doc.getMeta();
        if (meta != null && meta.size() != 0) {
            for (final Entry<String, List<String>> entry : meta.entrySet()) {
                writer.write(XML_OPEN + ElementName.m.name() + XML_CLOSE);

                writer.write(XML_OPEN + ElementName.k.name() + XML_CLOSE);
                writer.write(StringEscapeUtils.escapeXml(entry.getKey()));
                writer.write(XML_CLOSE_TAG + ElementName.k.name() + XML_CLOSE);

                for (final String value : entry.getValue()) {
                    writer.write(XML_OPEN + ElementName.v.name() + XML_CLOSE);
                    writer.write(StringEscapeUtils.escapeXml(value));
                    writer.write(XML_CLOSE_TAG + ElementName.v.name() 
                                 + XML_CLOSE);
                }
                writer.write(XML_CLOSE_TAG + ElementName.m.name() + XML_CLOSE);
            }
        }

        final CharSequence title = doc.getTitle();
        if (title != null) {
            final String asString = title.toString();
            final String escaped = StringEscapeUtils.escapeXml(asString);
            writer.write(XML_OPEN + ElementName.ti.name() + XML_CLOSE);
            writer.write(escaped);
            writer.write(XML_CLOSE_TAG + ElementName.ti.name() + XML_CLOSE);
        }

        final CharSequence text = doc.getText();
        if (text != null) {
            final String asString = text.toString();
            final String escaped = StringEscapeUtils.escapeXml(asString);
            writer.write(XML_OPEN + ElementName.te.name() + XML_CLOSE);
            writer.write(escaped);
            writer.write(XML_CLOSE_TAG + ElementName.te.name() + XML_CLOSE);
        }

        writer.write(XML_CLOSE_TAG + ElementName.d.name() + XML_CLOSE);
        if (LOG.isDebugEnabled()) {
            LOG.debug("end serialize EosDocument");
        }
    }

    /*
     * @see net.sf.eos.document.Serializer#serialize(java.io.Reader)
     */
    @Override
    @SuppressWarnings("nls")
    public EosDocument deserialize(final Reader in)
            throws IOException, ParserConfigurationException, SAXException {

        final XmlEosDocumentHandler handler = new XmlEosDocumentHandler();
        if (LOG.isDebugEnabled()) {
            LOG.debug(handler.id + " start loading EosDocument");
        }

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        final SAXParser parser = factory.newSAXParser();

        final InputSource source = new InputSource(in);
        parser.parse(source, handler);

        final EosDocument doc = handler.getDocument();
        if (LOG.isDebugEnabled()) {
            LOG.debug(handler.id + " start loading EosDocument - " + doc);
        }

        return doc;
    }

    /**
     * Deserializes a {@link EosDocument} which is serialized by
     * {@link XmlSerializer#serialize(EosDocument, Writer)}.
     * @author Sascha Kohlmann
     */
    protected final class XmlEosDocumentHandler extends DefaultHandler2 {

        private StringBuilder sb = null;

        private EosDocument doc = new EosDocument();

        private String key = null;
        private List<String> value = null;

        private boolean inMeta = false;
        private boolean inKey = false;
        private boolean inValue = false;

        final int id = System.identityHashCode(this);

        /*
         * @see org.xml.sax.helpers.DefaultHandler#startDocument()
         */
        @Override
        @SuppressWarnings("nls")
        public void startDocument() {
            if (LOG.isDebugEnabled()) {
                LOG.debug(this.id + " start parsing EosDocument");
            }
        }

        /*
         * @see org.xml.sax.helpers.DefaultHandler#endDocument()
         */
        @Override
        @SuppressWarnings("nls")
        public void endDocument() {
            if (LOG.isDebugEnabled()) {
                LOG.debug(this.id + " end parsing EosDocument");
            }
        }

        /*
         * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        @Override
        public void startElement(final String uri,
                                 final String localName,
                                 final String qName,
                                 final Attributes attributes) {
            if (ElementName.te.name().equals(qName)) {
                this.sb = new StringBuilder();
            } else if (ElementName.ti.name().equals(qName)) {
                this.sb = new StringBuilder();
            } else if (ElementName.m.name().equals(qName)) {
                this.inMeta = true;
                this.key = null;
                this.value = new ArrayList<String>();
            } else if (ElementName.k.name().equals(qName)) {
                this.inKey = true;
                this.sb = new StringBuilder();
            } else if (ElementName.v.name().equals(qName)) {
                this.inValue = true;
                this.sb = new StringBuilder();
            }
        }

        /*
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
         */
        @Override
        @SuppressWarnings("nls")
        public void endElement(final String uri,
                               final String localName,
                               final String qName) {

            if (ElementName.te.name().equals(qName)) {
                final String text = this.sb.toString();
                this.doc.setText(text);

                if (LOG.isTraceEnabled()) {
                    final StringBuilder lbuf =
                        new StringBuilder(this.id + " text: ");
                    lbuf.append(text);
                    LOG.trace(lbuf.toString());
                }

            } else if (ElementName.ti.name().equals(qName)) {
                final String title = sb.toString();
                this.doc.setTitle(title);

                if (LOG.isTraceEnabled()) {
                    final StringBuilder lbuf =
                        new StringBuilder(this.id + " title: ");
                    lbuf.append(title);
                    LOG.trace(lbuf.toString());
                }

            } else if (ElementName.m.name().equals(qName)) {
                assert this.inMeta == true;
                final Map<String, List<String>> meta = this.doc.getMeta();
                assert this.key != null;
                meta.put(this.key, this.value);

                if (LOG.isTraceEnabled()) {
                    final StringBuilder lbuf =
                        new StringBuilder(this.id + " key: ");
                    lbuf.append(this.key);
                    lbuf.append(" values: ");
                    for (final String v : this.value) {
                        lbuf.append("value=");
                        lbuf.append(v);
                    }
                    LOG.trace(lbuf.toString());
                }

                this.inMeta = false;
            } else if (ElementName.k.name().equals(qName)) {
                assert this.inKey == true;
                this.key = this.sb.toString();
            } else if (ElementName.v.name().equals(qName)) {
                assert this.inValue == true;
                assert this.value != null;
                final String v = this.sb.toString();
                this.value.add(v);
            }
        }

        /*
         * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
         */
        @Override
        public void characters(final char[] ch,
                               final int start,
                               final int length) {
            if (this.sb != null) {
                this.sb.append(ch, start, length);
            }
        }

        /**
         * Returns a document, constructed from the value of the parser.
         * @return a document if created. may be {@code null} or an empty
         *         document.
         */
        
        @SuppressWarnings("unchecked")
        public EosDocument getDocument() {
            assert this.doc != null;
            if (this.doc.getMeta() == null) {
                this.doc.setMeta(Collections.EMPTY_MAP);
            }
            return this.doc;
        }
    }
}
