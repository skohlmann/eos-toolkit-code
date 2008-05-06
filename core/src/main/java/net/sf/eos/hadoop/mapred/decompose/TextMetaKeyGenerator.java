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
package net.sf.eos.hadoop.mapred.decompose;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.AbstractKeyGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextMetaKeyGenerator extends AbstractKeyGenerator<Text> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(TextMetaKeyGenerator.class.getName());

    /** The meta field for separation.
     * <p>Default value is {@link EosDocument#YEAR_META_KEY}.</p> */
    @SuppressWarnings("nls")
    public static final String META_FIELD_FOR_KEY_CONFIG_NAME =
        "net.sf.eos.hadoop.mapred.sentencer.TextMetadataKeyGenerator.metaKey";

    @SuppressWarnings("nls")
    public Map<Text, EosDocument> createKeysForDocument(final EosDocument doc)
            throws EosException {

        final Configuration conf = getConfiguration();
        assert conf != null;
        final Map<Text, EosDocument> retval =
            new HashMap<Text, EosDocument>();
        final String metaKey = conf.get(META_FIELD_FOR_KEY_CONFIG_NAME);

        if (LOG.isDebugEnabled()) {
            LOG.debug("metaKey: " + metaKey);
        }

        if (metaKey == null || metaKey.length() == 0) {
            final Text t = new Text("");
            retval.put(t, doc);
        } else {
            final Map<String, List<String>> meta = doc.getMeta();
            final List<String> metadata = meta.get(metaKey);
            for (final String s : metadata) {
                final Text t = new Text(s);
                retval.put(t, doc);
            }
        }

        return retval;
    }

    @Override
    public void configure(
            @SuppressWarnings("hiding") final Configuration config) {
        LOG.info(config);
        super.configure(config);
    }
}
