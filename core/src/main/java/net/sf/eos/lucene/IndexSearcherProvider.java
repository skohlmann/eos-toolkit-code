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

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Service;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * Based on {@link DirectoryProvider}.
 * @author Sascha Kohlmann
 */
@Service(
    factory=DirectoryProvider.class
)
public class IndexSearcherProvider extends SearcherProvider {

    @Override
    public Searcher newSearcher(final Configuration conf)
            throws EosException {
        final DirectoryProvider provider = DirectoryProvider.newInstance(conf);
        final Directory directory = provider.newDirectory(conf);
        try {
            return new IndexSearcher(directory);
        } catch (final CorruptIndexException e) {
            final String message = e.getMessage();
            throw new EosException(message, e);
        } catch (final IOException e) {
            final String message = e.getMessage();
            throw new EosException(message, e);
        }
    }
}
