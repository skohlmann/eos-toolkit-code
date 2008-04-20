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

import net.sf.eos.document.EosDocument;

public interface CommonDocument {

    /** The field names. */
    public enum FieldName {
        /** The name of the content field. Represents the value of
         * {@link EosDocument#getText() EosDocument.getText()}.
         * @see DefaultLuceneDocumentCreator */
        CONTENT,
        /** The name of the {@link EosDocument#CREATOR_META_KEY creator} meta
         * values.
         * @see DefaultLuceneDocumentCreator */
        CREATOR,
        /** The name of the {@link EosDocument#ID_META_KEY ID} meta
         * values.
         * @see DefaultLuceneDocumentCreator */
        ID,
        /** The name of the {@link EosDocument#YEAR_META_KEY creator} meta
         * values.
         * @see DefaultLuceneDocumentCreator */
        YEAR,
    }
}
