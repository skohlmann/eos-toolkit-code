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

import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.util.EqualsAndHashUtil;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a simple &#949;&#183;&#959;&#183;s&#183;&#183;&#183; document.
 * An instance of this document may never contains any linefeed (ASCII 0xa) 
 * or carriage return (ASCII 0x0d) characters.
 * @author Sascha Kohlmann
 */
public final class EosDocument {

    /** A metadata key for the year in a metadata. */
    @SuppressWarnings("nls")
    public final static String YEAR_META_KEY = "EosDocument/date";

    /** A metadata key for the creator in a metadata. */
    @SuppressWarnings("nls")
    public final static String CREATOR_META_KEY = "EosDocument/creator";

    /** A metadata key for the IDs in a metadata. */
    @SuppressWarnings("nls")
    public final static String ID_META_KEY = "EosDocument/id";

    private CharSequence text;
    private CharSequence title;
    private Map<String, List<String>> meta =
        new HashMap<String, List<String>>();

    /** Returns the text of the document.
     * @return the text*/
    public CharSequence getText() {
        return this.text;
    }
    /** Returns the title of the document.
     * @return the title of the document */
    public CharSequence getTitle() {
        return this.title;
    }
    /** Returns the metadata of the document.
     * @return the metadata of the document */
    public Map<String, List<String>> getMeta() {
        return this.meta;
    }
    /** Sets the text of the document.
     * @param text the text of the document */
    public void setText(@SuppressWarnings("hiding") final CharSequence text) {
        this.text = text;
    }
    /** Sets the title of the document.
     * @param title the title of the document */
    public void setTitle(@SuppressWarnings("hiding") final CharSequence title) {
        this.title = title;
    }
    /** Sets the metadata of the document.
     * @param meta the metadata of the document */
    public void setMeta(
            @SuppressWarnings("hiding") final Map<String, List<String>> meta) {
        this.meta = meta;
    }

    /**
     * The value of the returnd string may change in future implementations.
     * Don't use for information extraction.
     * @return a <code>String</code> representation of the document
     * @see Serializer for serialization
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        final StringWriter writer = new StringWriter();
        final XmlSerializer serializer = new XmlSerializer();
        final StringBuffer sb = new StringBuffer("EosDocument[");
        try {
            serializer.serialize(this, writer);
            sb.append(writer.toString());
            sb.append("]");
        } catch (final IOException e) {
            sb.append("throws IOException:");
            sb.append(e.getMessage());
            sb.append("]");
        }

        return sb.toString();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (! (other instanceof EosDocument)) {
            return false;
        }
        final EosDocument doc = (EosDocument) other;
        return EqualsAndHashUtil.equals(this.text, doc.text)
                && EqualsAndHashUtil.equals(this.title, doc.title)
                && EqualsAndHashUtil.equals(this.meta, doc.meta);
    }

    /**
     * Implemented for {@linkplain Object#hashCode() hash contract}. Don't use
     * a <code>EosDocument</code> instance in a hash oriented dictionary.
     */
    @Override
    public int hashCode() {
        int hash = EqualsAndHashUtil.hash(this.text);
        hash *= EqualsAndHashUtil.hash(this.title);
        hash *= EqualsAndHashUtil.hash(this.meta);
        return hash;
    }
}
