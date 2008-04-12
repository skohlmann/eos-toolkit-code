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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MedlineMapper extends EosDocumentSupportMapReduceBase
                           implements Mapper<Text, Text, LongWritable, Text> {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(MedlineMapper.class.getName());

    private JobConf conf;

    public void map(Text positionInFile,
                    Text medlineCitationDoc,
                    final OutputCollector<LongWritable, Text> outputCollector,
                    final Reporter reporter) throws IOException {

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        configureSaxFactory(factory);
        try {
            final SAXParser parser = factory.newSAXParser();
            final String medlineCitationAsString =
                medlineCitationDoc.toString();

            StringReader reader = new StringReader(medlineCitationAsString);
            final InputSource source = new InputSource(reader);
            final MedlineHandler handler = new MedlineHandler();

            parser.parse(source, handler);

            // for GC
            reader = null;
            final EosDocument doc = handler.getEosDocument();
            final Text out = eosDocumentToText(doc);
            final Random rand = new Random();
            final long keyValue = rand.nextLong();
            final LongWritable key = new LongWritable(keyValue);
            outputCollector.collect(key, out);
            reporter.incrCounter(Conversion.CONVERTED, 1);

        } catch (final ParserConfigurationException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            reporter.incrCounter(Conversion.FAILED, 1);
        } catch (final SAXException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            reporter.incrCounter(Conversion.FAILED, 1);
        } catch (final Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            reporter.incrCounter(Conversion.FAILED, 1);
        }
    }

    private void configureSaxFactory(final SAXParserFactory factory) {
        try {
            factory.setValidating(false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
        } catch (final SAXNotRecognizedException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        } catch (final SAXNotSupportedException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        } catch (final ParserConfigurationException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        }

        try {
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar",
                false);
        } catch (final SAXNotRecognizedException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        } catch (final SAXNotSupportedException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        } catch (final ParserConfigurationException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        }

        try {
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                               false);
        } catch (final SAXNotRecognizedException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        } catch (final SAXNotSupportedException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        } catch (final ParserConfigurationException e) {
            LOG.log(Level.CONFIG, e.getMessage(), e);
        }
    }

    @Override
    public void configure(final JobConf conf) {
        super.configure(conf);
        this.conf = conf;
    }
}
