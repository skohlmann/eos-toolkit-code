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
package net.sf.eos.contrib.converter.medline;

import net.sf.eos.contrib.converter.Conversion;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MedlineMapper extends EosDocumentSupportMapReduceBase
                           implements Mapper<Text, Text, Text, Text> {

    /** For logging. */
    private static final Log LOG = LogFactory.getLog(MedlineMapper.class);

    private static final String START_IGNORE = "<MedlineCitationSet>";
    private static final String END_IGNORE = "</MedlineCitationSet>";
    
    private static final Pattern START_IGNORE_PATTERN =
        Pattern.compile(Pattern.quote(START_IGNORE));
    private static final Pattern END_IGNORE_PATTERN =
        Pattern.compile(Pattern.quote(END_IGNORE));

    private JobConf conf;

    public void map(final Text positionInFile,
                    final Text medlineCitationDoc,
                    final OutputCollector<Text, Text> outputCollector,
                    final Reporter reporter) throws IOException {

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        configureSaxFactory(factory);
        try {
            final SAXParser parser = factory.newSAXParser();
            final String medlineCitationAsString = textToString(positionInFile);

            LOG.info("MedlineCitation: \"" + medlineCitationAsString + "\"");
            final StringReader reader = new StringReader(medlineCitationAsString);
            final InputSource source = new InputSource(reader);
            final MedlineHandler handler = new MedlineHandler();

            parser.parse(source, handler);

            final EosDocument eosDoc = handler.getEosDocument();
            final Text eosOut = eosDocumentToText(eosDoc);
            LOG.info("EosDocument: " + eosOut.toString());
            final String id = getIdFromEosDocument(eosDoc);
            final Text outKey = new Text(id);
            outputCollector.collect(outKey, eosOut);
            reporter.incrCounter(Conversion.CONVERTED, 1);

        } catch (final ParserConfigurationException e) {
            LOG.error(e.getMessage(), e);
            reporter.incrCounter(Conversion.FAILED, 1);
            throw new IOException(e.getMessage() + " - " + e.getClass());
        } catch (final SAXException e) {
            LOG.error(e.getMessage(), e);
            reporter.incrCounter(Conversion.FAILED, 1);
            throw new IOException(e.getMessage() + " - " + e.getClass());
         } catch (final Exception e) {
             LOG.error(e.getMessage(), e);
             reporter.incrCounter(Conversion.FAILED, 1);
             throw new IOException(e.getMessage() + " - " + e.getClass());
        }
    }

    String textToString(final Text text) {
        String medlineCitationAsString = text.toString();
        medlineCitationAsString = medlineCitationAsString.replace(START_IGNORE, "");
        medlineCitationAsString = medlineCitationAsString.replace(END_IGNORE, "");
        return medlineCitationAsString;
    }

    final String getIdFromEosDocument(final EosDocument doc) {
        assert doc != null;
        final Map<String, List<String>> meta = doc.getMeta();
        final List<String> values = meta.get(EosDocument.ID_META_KEY);
        final String id = values.get(0);
        return id;
    }

    private void configureSaxFactory(final SAXParserFactory factory) {
            factory.setValidating(false);
            setSaxParserFeature(
                    factory,
                    "http://xml.org/sax/features/validation",
                    false
            );
            setSaxParserFeature(
                    factory,
                    "http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
                    false
            );
            setSaxParserFeature(
                    factory,
                    "http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false
            );
    }

    final void setSaxParserFeature(final SAXParserFactory factory,
                                   final String name,
                                   final boolean value) {
        try {
            factory.setFeature(name, value);
        } catch (final SAXNotRecognizedException e) {
            LOG.info(e.getMessage(), e);
        } catch (final SAXNotSupportedException e) {
            LOG.info(e.getMessage(), e);
        } catch (final ParserConfigurationException e) {
            LOG.info(e.getMessage(), e);
        }
    }

    @Override
    public void configure(final JobConf conf) {
        super.configure(conf);
        this.conf = conf;
    }
}
