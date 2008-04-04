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
package net.sf.eos.entity;

import net.sf.eos.analyzer.Tokenizer;

/**
 * The strategy for entity recognation is not defined. Implementation only
 * must set the defined {@link #ENTITY_TYPE} value in the recognized
 * {@link net.sf.eos.analyzer.Token}.
 * @author Sascha Kohlmann
 */
public interface EntityRecognizer extends Tokenizer {

    /**
     * The {@link net.sf.eos.analyzer.Token#getType() type} of a recognized
     * token.
     * @see net.sf.eos.analyzer.Token
     */
    @SuppressWarnings("nls")
    public final static String ENTITY_TYPE = "entity";
}
