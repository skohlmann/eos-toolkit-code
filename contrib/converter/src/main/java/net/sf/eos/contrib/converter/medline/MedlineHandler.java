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
package net.sf.eos.contrib.converter.medline;

import net.sf.eos.document.EosDocument;
import net.sf.eos.util.EqualsAndHashUtil;

import org.xml.sax.Attributes;
import org.xml.sax.ext.DefaultHandler2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedlineHandler extends DefaultHandler2 {

    public static final String TAG_MEDLINE_CITATION = "MedlineCitation";
    public static final String TAG_PMID = "PMID";
    public static final String TAG_DATE_CREATED = "DateCreated";
    public static final String TAG_PUB_DATE = "PubDate";
    public static final String TAG_YEAR = "Year";
    public static final String TAG_ABSTRACT = "Abstract";
    public static final String TAG_ABSTRACT_TEXT = "AbstractText";
    public static final String TAG_ARTICLE_TITLE = "ArticleTitle";
    public static final String TAG_AUTHOR_LIST = "AuthorList";
    public static final String TAG_AUTHOR = "Author";
    public static final String TAG_ARTICLE = "Article";
    public static final String TAG_LAST_NAME = "LastName";
    public static final String TAG_MEDLINE_DATE = "MedlineDate";

    private static final String[] PATH_PMID =
        new String[] {TAG_MEDLINE_CITATION, TAG_PMID};
    private static final String[] PATH_CREATION_DATE_YEAR =
        new String[] {TAG_MEDLINE_CITATION, TAG_DATE_CREATED, TAG_YEAR};
    private static final String[] PATH_ARTICLE_TITLE =
        new String[] {TAG_MEDLINE_CITATION, TAG_ARTICLE, TAG_ARTICLE_TITLE};
    private static final String[] PATH_ABSTRACT_TEXT =
        new String[] {TAG_MEDLINE_CITATION,
                      TAG_ARTICLE,
                      TAG_ABSTRACT,
                      TAG_ABSTRACT_TEXT};
    private static final String[] PATH_AUTHOR_LAST_NAME =
        new String[] {TAG_MEDLINE_CITATION,
                      TAG_ARTICLE,
                      TAG_AUTHOR_LIST,
                      TAG_AUTHOR,
                      TAG_LAST_NAME};

    static final Level LEVEL = Level.INFO;
    static final Logger LOG = Logger.getLogger(MedlineHandler.class.getName());

    private EosDocument doc = null;
    private Stack<String> tags = new Stack<String>();

    private StringBuilder sb = null;

    /*
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    @SuppressWarnings("nls")
    public void startDocument() {
        this.doc = new EosDocument();
    }

    /*
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    @SuppressWarnings("nls")
    public void endDocument() {
        assert this.doc != null;
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("DOC: " + this.doc);
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
        this.tags.push(qName);
        final int size = this.tags.size();
        String [] path = new String[size];
        path = this.tags.toArray(path);
        if (EqualsAndHashUtil.equals(path, PATH_PMID)
                || EqualsAndHashUtil.equals(path, PATH_CREATION_DATE_YEAR)
                || EqualsAndHashUtil.equals(path, PATH_ARTICLE_TITLE)
                || EqualsAndHashUtil.equals(path, PATH_ABSTRACT_TEXT)
                || EqualsAndHashUtil.equals(path, PATH_AUTHOR_LAST_NAME)) {
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
        final int size = this.tags.size();
        String [] path = new String[size];
        path = this.tags.toArray(path);
        if (EqualsAndHashUtil.equals(path, PATH_PMID)) {
            final String id = this.sb.toString();
            addMetaToEosDocument(EosDocument.ID_META_KEY, id);
        } else if (EqualsAndHashUtil.equals(path, PATH_CREATION_DATE_YEAR)) {
            final String year = this.sb.toString();
            addMetaToEosDocument(EosDocument.YEAR_META_KEY, year);
        } else if (EqualsAndHashUtil.equals(path, PATH_ARTICLE_TITLE)) {
            final String title = this.sb.toString();
            this.doc.setTitle(title);
        } else if (EqualsAndHashUtil.equals(path, PATH_ABSTRACT_TEXT)) {
            final String text = this.sb.toString();
            this.doc.setText(text);
        } else if (EqualsAndHashUtil.equals(path, PATH_AUTHOR_LAST_NAME)) {
            final String name = this.sb.toString();
            addMetaToEosDocument(EosDocument.CREATOR_META_KEY, name);
        }
        this.tags.pop();
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

    public EosDocument getEosDocument() {
        return this.doc;
    }

    final void addMetaToEosDocument(final String metaKey,
                                    final String metaValueToAdd) {
        assert this.doc != null;
        Map<String, List<String>> meta = this.doc.getMeta();
        if (meta == null) {
            meta = new HashMap<String, List<String>>();
            this.doc.setMeta(meta);
        }
        List<String> values = meta.get(metaKey);
        if (values == null) {
            values = new ArrayList<String>();
            meta.put(metaKey, values);
        }
        values.add(metaValueToAdd);
    }
}
