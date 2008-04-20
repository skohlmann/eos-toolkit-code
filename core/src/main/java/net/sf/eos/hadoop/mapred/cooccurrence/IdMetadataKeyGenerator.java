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
package net.sf.eos.hadoop.mapred.cooccurrence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.AbstractKeyGenerator;

/**
 * Creates a new map based on the document metadata value of
 * {@link EosDocument#ID_META_KEY} and optionally other metadata information.
 * Will sort optional metadata values.
 * @author Sascha Kohlmann
 */
public class IdMetadataKeyGenerator extends AbstractKeyGenerator<Text> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(IdMetadataKeyGenerator.class.getName());

    private final static String META_DELIMITER = "+";

    /** The meta field for separation.
     * Default value is {@link EosDocument#YEAR_META_KEY}. */
    public static final String META_FIELD_FOR_SEPARATION_CONFIG_NAME =
        "net.sf.eos.hadoop.mapred.cooccurrence.IdMetadataKeyGenerator.metaKeys";

    /**
     * @throws EosException if <em>doc</em> doesn't contains a metadata value
     *                      for {@link EosDocument#ID_META_KEY}.
     */
    public Map<Text, EosDocument> createKeysForDocument(final EosDocument doc)
            throws EosException {
        final Map<String, List<String>> meta = doc.getMeta();
        if (meta == null) {
            throw new EosException("document contains no metadata value.");
        }
        final List<String> ids = meta.get(EosDocument.ID_META_KEY);
        if (ids == null || ids.isEmpty()) {
            throw new EosException("document contains no ID metadata value.");
        }

        final List<String> metaKeys = getMetaKeys();
        final Map<Text, EosDocument> retval = new HashMap<Text, EosDocument>();
        for (final String id : ids) {
            final StringBuilder sb = new StringBuilder(id);
            sb.append(META_DELIMITER);

            // Append metadata values in the defined sequence.
            for (final String metaKey : metaKeys) {
                final List<String> metaValues = meta.get(metaKey);
                if (metaValues != null) {
                    final List<String> sorted = new ArrayList<String>(metaValues);
                    Collections.sort(sorted);
                    for (final String value : sorted) {
                        sb.append(value);
                        sb.append(META_DELIMITER);
                    }
                } else {
                    LOG.debug("document contains no metavalue for key '"
                              + metaKey + "'");
                }
            }

            // Create new entry
            final String key = sb.toString();
            final Text newTextKey = new Text(key);
            retval.put(newTextKey, doc);
        }

        return retval;
    }

    final List<String> getMetaKeys() {
        final Configuration lconf = getConfiguration();
        final List<String> keys = new ArrayList<String>();
        final String value = lconf.get(META_FIELD_FOR_SEPARATION_CONFIG_NAME,
                                       EosDocument.YEAR_META_KEY);
        for (final StringTokenizer st = new StringTokenizer(value, ", ");
                st.hasMoreTokens(); ) {
            final String key = st.nextToken();
            // Filter out ID
            if (! EosDocument.ID_META_KEY.equals(key)) {
                keys.add(key);
            } else {
                LOG.debug("value of key " + META_FIELD_FOR_SEPARATION_CONFIG_NAME
                          + " contains " + EosDocument.ID_META_KEY
                          + " - ignore it.");
            }
        }
        if (keys.size() == 0) {
            keys.add(EosDocument.ID_META_KEY);
        }
        return keys;
    }
}
