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
package net.sf.eos.lucene;

import static org.apache.lucene.document.Field.Index.TOKENIZED;
import static org.apache.lucene.document.Field.Index.UN_TOKENIZED;
import static org.apache.lucene.document.Field.Store.YES;
import static org.apache.lucene.document.Field.Store.NO;
import net.sf.eos.EosException;
import net.sf.eos.document.EosDocument;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 
 * <table border='1' cellspacing='1' cellpadding='4'>
 *   <tr><th>Name</th><th>Value of</th><th>Store</th><th>Tokenize</th><th>Comment</th></tr>
 *   <tr><td><tt>CONTENT</tt></td><td>{@link EosDocument#getText() EosDocument.getText()}</td><td>Yes</td><td>TOKENIZED</td><td> </td></tr>
 *   <tr><td><tt>CREATOR</tt></td><td>{@link EosDocument#CREATOR_META_KEY EosDocument.CREATOR_META_KEY}</td><td>Yes</td><td>TOKENIZED</td><td>All values where added to the field</td></tr>
 *   <tr><td><tt>ID</tt></td><td>{@link EosDocument#ID_META_KEY EosDocument.ID_META_KEY}</td><td>Yes</td><td>UN_TOKENIZED</td><td>All values where added to the field</td></tr>
 *   <tr><td><tt>YEAR</tt></td><td>{@link EosDocument#YEAR_META_KEY EosDocument.YEAR_META_KEY}</td><td>Yes</td><td>UN_TOKENIZED</td><td>All values where added to the field</td></tr>
 * </table>
 *
 * @author Sascha Kohlmann
 */
public class DefaultLuceneDocumentCreator extends LuceneDocumentCreator
        implements CommonDocument {

    /** Creates a new document. */
    @Override
    public Document createLuceneForEosDocument(final EosDocument doc)
            throws EosException {

        final Document lDoc = new Document();
        final String text = doc.getText().toString();
        final Field content = new Field(FieldName.CONTENT.name(),
                                        text,
                                        NO,
                                        TOKENIZED);
        lDoc.add(content);

        final Map<String, List<String>> meta = doc.getMeta();
        if (meta != null) {
            for (final Entry<String, List<String>> entry : meta.entrySet()) {
                final List<String> values = entry.getValue();
                if (values != null && values.size() != 0) {
                    final String key = entry.getKey();
                    for (final String value : values) {
                        Field f = null;
                        if (EosDocument.YEAR_META_KEY.equals(key)) {
                            f = new Field(FieldName.YEAR.name(),
                                          value,
                                          YES,
                                          UN_TOKENIZED);
                        } else if (EosDocument.ID_META_KEY.equals(key)) {
                            f = new Field(FieldName.ID.name(),
                                          value,
                                          YES,
                                          UN_TOKENIZED);
                        } else if (EosDocument.CREATOR_META_KEY.equals(key)) {
                            f = new Field(FieldName.CREATOR.name(),
                                          value,
                                          YES,
                                          TOKENIZED);
                        }
                        if (f != null) {
                            lDoc.add(f);
                        }
                    }
                }
            }
        }

        return lDoc;
    }
}
