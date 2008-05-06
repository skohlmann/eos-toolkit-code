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

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;

public class LocalFsDirectoryProvider extends DirectoryProvider {

    public static final String LOCAL_PATH_CONFIG_NAME =
        "net.sf.eos.lucene.LocalFsDirectoryProvider.path";

    @Override
    public Directory newDirectory(final Configuration conf) throws EosException {
        final String path = conf.get(LOCAL_PATH_CONFIG_NAME);
        try {
            return FSDirectory.getDirectory(path);
        } catch (final IOException e) {
            throw new EosException(e);
        }
    }
}
