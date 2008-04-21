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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.EosException;

import java.util.List;

public class DefaultLuceneEosLookup extends LuceneEosLookup
        implements CommonDocument {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DefaultLuceneEosLookup.class.getName());

    @Override
    public List<LookupEntry> lookup(final EosQuery query) throws EosException {
        if (! (query instanceof DefaultEosQuery)) {
            LOG.warn("query instance not of type "
                     + DefaultEosQuery.class.getName() + ": "
                     + query.getClass().getName()
                     + " - will try it anyway.");
        }
        return null;
    }

    @Override
    public EosQuery newQuery() {
        return new DefaultEosQuery();
    }
}
