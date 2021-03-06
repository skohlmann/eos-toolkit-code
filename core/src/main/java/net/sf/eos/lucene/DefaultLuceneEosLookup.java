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

import net.sf.eos.config.Configuration;
import net.sf.eos.search.EosQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultLuceneEosLookup extends LuceneEosLookup
        implements CommonDocument {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DefaultLuceneEosLookup.class.getName());

    public EosQuery newQuery() {
        final DefaultEosQuery query = new DefaultEosQuery();
        final Configuration conf = getConfiguration();
        query.configure(conf);
        return query;
    }
}
