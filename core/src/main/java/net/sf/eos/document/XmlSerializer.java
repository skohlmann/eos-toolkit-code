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

import org.apache.commons.lang.StringEscapeUtils;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Serializer and deserializer for an {@link EosDocument}. See
 * {@link XmlSerializer.Xml} for element names. The order of the elements in
 * the root container are not defined. Also the order in the meta container
 * is not defined. If the root container contains more than one <em>title</em>
 * or <em>text</em> element, the lastes elements may win. If the meta container
 * contains more than one <em>key</em>, the latest may win.
 * @author Sascha Kohlmann
 */
public class XmlSerializer extends Serializer {

    static final Level LEVEL = Level.INFO;
    static final Logger LOG = Logger.getLogger(XmlSerializer.class.getName());

    /**
     * Represents the XML emlement names of a serialized
     *  &#949;&#183;&#959;&#183;s&#183;&#183;&#183; document.
     * @author Sascha Kohlmann
     */
    public enum Xml {
        /** Root element of an &#949;&#183;&#959;&#183;s&#183;&#183;&#183;
         * document. */
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
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("start serialize EosDocument");
        }
        final NewlineReplaceWriter writer = new NewlineReplaceWriter(out);
        writer.write(XML_OPEN + Xml.d.name() + XML_CLOSE);
        final  Map<String, List<String>> meta = doc.getMeta();
        if (meta != null && meta.size() != 0) {
            for (final Entry<String, List<String>> entry : meta.entrySet()) {
                writer.write(XML_OPEN + Xml.m.name() + XML_CLOSE);

                writer.write(XML_OPEN + Xml.k.name() + XML_CLOSE);
                writer.write(StringEscapeUtils.escapeXml(entry.getKey()));
                writer.write(XML_CLOSE_TAG + Xml.k.name() + XML_CLOSE);

                for (final String value : entry.getValue()) {
                    writer.write(XML_OPEN + Xml.v.name() + XML_CLOSE);
                    writer.write(StringEscapeUtils.escapeXml(value));
                    writer.write(XML_CLOSE_TAG + Xml.v.name() + XML_CLOSE);
                }
                writer.write(XML_CLOSE_TAG + Xml.m.name() + XML_CLOSE);
            }
        }

        final CharSequence title = doc.getTitle();
        if (title != null) {
            final String asString = title.toString();
            final String escaped = StringEscapeUtils.escapeXml(asString);
            writer.write(XML_OPEN + Xml.ti.name() + XML_CLOSE);
            writer.write(escaped);
            writer.write(XML_CLOSE_TAG + Xml.ti.name() + XML_CLOSE);
        }

        final CharSequence text = doc.getText();
        if (text != null) {
            final String asString = text.toString();
            final String escaped = StringEscapeUtils.escapeXml(asString);
            writer.write(XML_OPEN + Xml.te.name() + XML_CLOSE);
            writer.write(escaped);
            writer.write(XML_CLOSE_TAG + Xml.te.name() + XML_CLOSE);
        }

        writer.write(XML_CLOSE_TAG + Xml.d.name() + XML_CLOSE);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("end serialize EosDocument");
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
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine(handler.id + " start loading EosDocument");
        }

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        final SAXParser parser = factory.newSAXParser();

        final InputSource source = new InputSource(in);
        parser.parse(source, handler);

        final EosDocument doc = handler.getDocument();
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine(handler.id + " start loading EosDocument - " + doc);
        }

        return doc;
    }

    /**
     * Deserilalizes a {@link EosDocument} which is serilized by
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
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(this.id + " start parsing EosDocument");
            }
        }

        /*
         * @see org.xml.sax.helpers.DefaultHandler#endDocument()
         */
        @Override
        @SuppressWarnings("nls")
        public void endDocument() {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(this.id + " end parsing EosDocument");
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
            if (Xml.te.name().equals(qName)) {
                this.sb = new StringBuilder();
            } else if (Xml.ti.name().equals(qName)) {
                this.sb = new StringBuilder();
            } else if (Xml.m.name().equals(qName)) {
                this.inMeta = true;
                this.key = null;
                this.value = new ArrayList<String>();
            } else if (Xml.k.name().equals(qName)) {
                this.inKey = true;
                this.sb = new StringBuilder();
            } else if (Xml.v.name().equals(qName)) {
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

            if (Xml.te.name().equals(qName)) {
                final String text = this.sb.toString();
                this.doc.setText(text);

                if (LOG.isLoggable(Level.FINER)) {
                    final StringBuilder lbuf =
                        new StringBuilder(this.id + " text: ");
                    lbuf.append(text);
                    LOG.finer(lbuf.toString());
                }

            } else if (Xml.ti.name().equals(qName)) {
                final String title = sb.toString();
                this.doc.setTitle(title);

                if (LOG.isLoggable(Level.FINER)) {
                    final StringBuilder lbuf =
                        new StringBuilder(this.id + " title: ");
                    lbuf.append(title);
                    LOG.finer(lbuf.toString());
                }

            } else if (Xml.m.name().equals(qName)) {
                assert this.inMeta == true;
                final Map<String, List<String>> meta = this.doc.getMeta();
                assert this.key != null;
                meta.put(this.key, this.value);

                if (LOG.isLoggable(Level.FINER)) {
                    final StringBuilder lbuf =
                        new StringBuilder(this.id + " key: ");
                    lbuf.append(this.key);
                    lbuf.append(" values: ");
                    for (final String v : this.value) {
                        lbuf.append("value=");
                        lbuf.append(v);
                    }
                    LOG.finer(lbuf.toString());
                }

                this.inMeta = false;
            } else if (Xml.k.name().equals(qName)) {
                assert this.inKey == true;
                this.key = this.sb.toString();
            } else if (Xml.v.name().equals(qName)) {
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
         * @return a document if created. may be <code>null</code> or an empty
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
