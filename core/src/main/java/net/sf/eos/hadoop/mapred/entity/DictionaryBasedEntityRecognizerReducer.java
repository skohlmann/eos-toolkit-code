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
package net.sf.eos.hadoop.mapred.entity;

import net.sf.eos.EosException;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DictionaryBasedEntityRecognizerReducer
        extends EosDocumentSupportMapReduceBase
        implements Reducer<Text, Text, Text, Text> {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(DictionaryBasedEntityRecognizerReducer.class.getName());

    private final static String NULL = "<null>";
    private final static String UNDERLINE = "_";

    /** The meta field for separation.
     * Default value is {@link EosDocument#ID_META_KEY}. */
    public static final String META_FIELD_FOR_SEPARATION_CONFIG_NAME =
        "net.sf.eos.hadoop.mapred.entity.DictionaryBasedEntityRecognizerReducer.metaKeys";

    private JobConf conf;

    public void reduce(final Text key,
                       final Iterator<Text> valuesIterator,
                       final OutputCollector<Text, Text> outputCollector,
                       final Reporter reporter) throws IOException {
        try {
            final Map<String, EosDocument> docs =
                createCombinedEosDocumentsFromIterator(valuesIterator);

            for (final Entry<String, EosDocument> entry : docs.entrySet()) {

                final String newKey = entry.getKey();
                final Text textKey = new Text(newKey);
                final EosDocument doc = entry.getValue();
                final Text textValue = this.eosDocumentToText(doc);
                outputCollector.collect(textKey, textValue);

                reporter.incrCounter(Index.REDUCE, 1);
            }

        } catch (final EosException e) {
            reporter.incrCounter(Index.EOS_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        } catch (final Exception e) {
            reporter.incrCounter(Index.IO_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        }
    }

    final Map<String, EosDocument> createCombinedEosDocumentsFromIterator(
            final Iterator<Text> valuesIterator) throws Exception, IOException {

        final List<String> keys = getMetaKeys();

        // For meta collecting
        final Map<String, EosDocument> retval = 
            new HashMap<String, EosDocument>();

        while (valuesIterator.hasNext()) {
            final Text eosDoc = valuesIterator.next();
            if (LOG.isLoggable(Level.FINER)) {
                LOG.finer("EosDocument to handle: " + eosDoc.toString());
            }
            final EosDocument doc = textToEosDocument(eosDoc);
            assert doc != null;

            final StringBuilder newKey = new StringBuilder();
            final Map<String, List<String>> meta = doc.getMeta();

            if (meta != null) {
                for (final String key : keys) {
                    final List<String> values = meta.get(key);

                    if (values != null) {

                        final List<String> sortedValues =
                            new ArrayList<String>();
                        sortedValues.addAll(values);
                        Collections.sort(sortedValues);

                        for (final String value : sortedValues) {
                            if (value != null) {
                                newKey.append(value);
                            } else {
                                newKey.append(NULL);
                            }
                        }
                    } else {
                        newKey.append(NULL);
                    }
                    newKey.append(UNDERLINE);
                }
            }
            if (newKey.length() == 0) {
                newKey.append(NULL);
            }

            final String keyRaw = newKey.toString();
            final String key = replaceWhitespaceWithUnderline(keyRaw);
            final EosDocument valueDoc = retval.get(key);

            if (valueDoc == null) {
                retval.put(key, doc);
            } else {
                combineDocuments(doc, valueDoc);
            }
        }

        return retval;
    }

    final String replaceWhitespaceWithUnderline(final String toReplace) {
        final StringBuilder sb = new StringBuilder();
        final int length = toReplace.length();
        for (int i = 0; i < length; i++) {
            final char c = toReplace.charAt(i);
            if (Character.isWhitespace(c)) {
                sb.append(UNDERLINE);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    final Map<String, List<String>> 
            metadataSetToList(final Map<String, Set<String>> meta) {

        assert meta != null;
        final Map<String, List<String>> newMetaData =
            new HashMap<String, List<String>>();

        for (final Entry<String, Set<String>> entry : meta.entrySet()) {
            final String key = entry.getKey();
            final Set<String> oldValue = entry.getValue();
            final List<String> newValue = new ArrayList<String>();
            newValue.addAll(oldValue);
            newMetaData.put(key, newValue);
        }

        return newMetaData;
    }

    @Override
    public void configure(final JobConf conf) {
        super.configure(conf);
        this.conf = conf;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    final void combineDocuments(final EosDocument from, final EosDocument to)
            throws EosException {

        if (from != null) {
            assert this.conf != null;
            final Configuration lconf =
                new HadoopConfigurationAdapter(this.conf);
            final TextBuilder builder = TextBuilder.newInstance(lconf);

            final CharSequence fromTitle = from.getTitle();
            if (fromTitle != null) {
                final CharSequence toTitle = to.getTitle();
                if (toTitle == null) {
                    to.setTitle(fromTitle);
                } else {
                    final CharSequence combined =
                        builder.buildText(toTitle, fromTitle);
                    to.setTitle(combined);
                }
            }

            final CharSequence fromText = from.getText();
            if (fromText != null) {
                final CharSequence toText = to.getText();
                if (toText == null) {
                    to.setText(fromText);
                } else {
                    final CharSequence combined =
                        builder.buildText(toText, fromText);
                    to.setText(combined);
                }
            }

            final Map<String, List<String>> fromMeta = from.getMeta();
            final Map<String, List<String>> toMeta = to.getMeta();

            if (toMeta == null || toMeta.size() == 0) {
                to.setMeta(fromMeta);
            } else if (fromMeta != null && fromMeta.size() != 0) {

                for (final Entry<String, List<String>> entry
                        : fromMeta.entrySet()) {

                    final String key = entry.getKey();
                    final List<String> values = entry.getValue();
                    final List<String> toValues = toMeta.get(key);

                    if (toValues == null || toValues.size() == 0) {
                        toMeta.put(key, values);
                    } else {
                        for (final String fromValue : values) {
                            if (! toValues.contains(fromValue)) {
                                toValues.add(fromValue);
                            }
                        }
                    }
                }
            }
        }
    }

    final List<String> getMetaKeys() {
        assert this.conf != null;
        final Configuration lconf =
            new HadoopConfigurationAdapter(this.conf);
        final List<String> keys = new ArrayList<String>();
        final String value = lconf.get(META_FIELD_FOR_SEPARATION_CONFIG_NAME,
                                       EosDocument.ID_META_KEY);
        for (final StringTokenizer st = new StringTokenizer(value, ", ");
                st.hasMoreTokens(); ) {
            final String key = st.nextToken();
            keys.add(key);
        }
        if (keys.size() == 0) {
            keys.add(EosDocument.ID_META_KEY);
        }
        return keys;
    }
}
