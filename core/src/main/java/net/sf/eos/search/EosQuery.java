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
package net.sf.eos.search;

import java.util.List;

import net.sf.eos.EosException;
import net.sf.eos.config.Configurable;

/**
 * A query is always constructed by the {@link EosLookup} instance and supports
 * simple lookups in an entity oriented search index.
 * 
 * <p>The query is very simple. At this time an entity oriented search system
 * doesn't need sophisticated queries.
 *
 * @author Sascha Kohlmann
 */
public interface EosQuery extends Configurable {

    /**
     * Adds a phrase to lookup with the boolean {@literal and} operation.
     * @param phrase the phrase to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery and(final String phrase) throws EosException;

    /**
     * Adds a phrase to lookup with the boolean {@literal or} operation.
     * @param phrase the phrase to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery or(final String phrase) throws EosException;

    /**
     * Adds a phrase not to lookup with the boolean {@literal and} operation.
     * @param phrase the phrase to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery andNot(final String phrase) throws EosException;

    /**
     * Adds a meta entry for a special field to lookup with the boolean
     * {@literal and} operation.
     * @param fieldName the meta field name to lookup
     * @param value the meta value to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery andMeta(final String fieldName, final String value) throws EosException;

    /**
     * Adds a meta entry for a special field to not lookup with the boolean
     * {@literal and} operation.
     * @param fieldName the meta field name to lookup
     * @param value the meta value to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery andNotMeta(final String fieldName, final String value) throws EosException;

    /**
     * Adds a meta entry for a special field to lookup with the boolean
     * {@literal or} operation.
     * @param fieldName the meta field name to lookup
     * @param value the meta value to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery orMeta(final String fieldName, final String value) throws EosException;

    /**
     * Adds a meta entry for a special field to lookup with the boolean
     * {@literal and} operation. The two value parameters spans a range between
     * the lower bound value and the upper bound value.
     * @param fieldName the meta field name to lookup
     * @param lowerBoundValue the meta lower bound value to lookup
     * @param upperBoundValue the meta upper bound value to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery andMetaRange(final String fieldName,
                          final String lowerBoundValue,
                          final String upperBoundValue) throws EosException;

    /**
     * Adds a meta entry for a special field to lookup with the boolean
     * {@literal or} operation. The two value parameters spans a range between
     * the lower bound value and the upper bound value.
     * @param fieldName the meta field name to lookup
     * @param lowerBoundValue the meta lower bound value to lookup
     * @param upperBoundValue the meta upper bound value to lookup
     * @return this instance
     * @throws EosException if an error occurs
     */
    EosQuery orMetaRange(final String fieldName,
                         final String lowerBoundValue,
                         final String upperBoundValue) throws EosException;

    /**
     * Creates the executable query.
     * @return the executable query
     * @throws EosException if an error occurs
     */
    List<LookupEntry> execute() throws EosException;
}
