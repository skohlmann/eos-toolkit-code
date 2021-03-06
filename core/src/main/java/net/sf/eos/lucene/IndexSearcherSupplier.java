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
import net.sf.eos.config.ConfigurationException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.Directory;

import java.io.IOException;

/**
 * Based on {@link DirectorySupplier}.
 * @author Sascha Kohlmann
 */
public class IndexSearcherSupplier extends SearcherSupplier {

    @Override
    public Searcher get(final Configuration conf) {
        try {
            final DirectorySupplier provider = DirectorySupplier.newInstance(conf);
            final Directory directory = provider.get(conf);
            return new IndexSearcher(directory);
        } catch (final CorruptIndexException e) {
            final String message = e.getMessage();
            throw new ConfigurationException(message, e);
        } catch (final IOException e) {
            final String message = e.getMessage();
            throw new ConfigurationException(message, e);
        } catch (final EosException e) {
            final String message = e.getMessage();
            throw new ConfigurationException(message, e);
        }
    }
}
