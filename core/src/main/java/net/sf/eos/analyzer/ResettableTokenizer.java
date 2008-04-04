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

/**
 * Implementation that are prepared for reuse should implement this interface.
 * @author Sascha Kohlmann
 * @see ResettableTokenFilter
 */
public interface ResettableTokenizer extends Tokenizer {


    /** Inits the tokenizer woith new input data.
     * @param input represents new input data for the tokenizer.
     */
    void reset(final CharSequence input) throws TokenizerException;
}
