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

import net.sf.eos.EosException;

/** Base interface for index lookup.
 * @author Sascha Kohlmann*/
public interface EosLookup {

    /**
     * Creates a new query instance to configure and than execute.
     * @return a new query instance
     * @throws EosException if an error occurs
     */
    EosQuery newQuery() throws EosException;
}
