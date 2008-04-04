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
package net.sf.eos.hadoop.mapred;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.document.EosDocument;
import net.sf.eos.document.Serializer;

import org.apache.commons.io.input.CharSequenceReader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Support for handling Map/Reduce jobs with {@link EosDocument}.
 * @author Sascha Kohlmann
 */
public abstract class EosDocumentSupportMapReduceBase extends MapReduceBase {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(EosDocumentSupportMapReduceBase.class.getName());

    private JobConf conf;

    /**
     * Returns a <code>Serializer</code> instance. Uses the instance defined in
     * {@link Serializer#SERIALIZER_IMPL_CONFIG_NAME}. If no configuration
     * is defined, the default implementation is used.
     * @return a <code>Serializer</code> instance
     * @throws EosException if an error occurs
     */
    @SuppressWarnings("nls")
    protected Serializer getSerializer() throws EosException {
        assert this.conf != null;
        final Configuration config = new HadoopConfigurationAdapter(this.conf);
        final Serializer serializer = Serializer.newInstance(config);
        LOG.fine("Serializer instanceof " + serializer.getClass());

        return serializer;
    }

    /**
     * Transforms a <code>EosDocument</code> to an Hadoop <code>Text</code>.
     * @param doc the <code>EosDocument</code> to transform
     * @return a serialized document
     * @throws Exception if an error occurs
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("nls")
    protected Text eosDocumentToText(final EosDocument doc)
            throws IOException, Exception {
        final Serializer serializer = getSerializer();
        final Writer writer = new StringWriter();
        serializer.serialize(doc, writer);
        final String docAsString = writer.toString();
        final Text docAsText = new Text(docAsString);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("seralized EosDocument: " + docAsText);
        }

        return docAsText;
    }

    /**
     * Transforms a Hadoop <code>Text</code> to an <code>EosDocument</code>.
     * @param eosDoc the document as Hadoop <code>Text</code>.
     * @return a deserialized document
     * @throws Exception if an error occurs
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("nls")
    protected EosDocument textToEosDocument(final Text eosDoc)
            throws Exception, IOException {
        final Serializer serializer = getSerializer();
        final CharSequence text = eosDoc.toString();
        final Reader reader = new CharSequenceReader(text);
        final EosDocument doc = serializer.deserialize(reader);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("doc: " + doc);
        }

        return doc;
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
}
