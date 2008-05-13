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
package net.sf.eos.analyzer;

import net.sf.eos.Metadata;

/**
 * A Token represents a part of a tokenized text.
 * @see Tokenizer
 * @author Sascha Kohlmann
 */
@SuppressWarnings("nls")
public interface Token extends Metadata {

    /** The default type of a token. */
    public final static String DEFAULT_TYPE = "word";

    /**
     * Returns the text of a token. The text may be a single word or a sequence
     * of words, e.g. a sentence.
     * @return the character sequence of the token
     */
    CharSequence getTokenText();

    /**
     * Returns the type of the token.
     * @return the type of a token.
     */
    String getType();
}
