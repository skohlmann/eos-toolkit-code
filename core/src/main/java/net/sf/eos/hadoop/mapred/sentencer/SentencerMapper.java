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
package net.sf.eos.hadoop.mapred.sentencer;


import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.TokenizerBuilder;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;
import net.sf.eos.sentence.Sentencer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SentencerMapper extends EosDocumentSupportMapReduceBase
        implements Mapper<LongWritable, Text, Text, Text> {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(SentencerMapper.class.getName());

    private JobConf conf;

    public void map(final LongWritable positionInFile,
                    final Text eosDoc,
                    final OutputCollector<Text, Text> outputCollector,
                    final Reporter reporter) throws IOException {

        final Configuration config = new HadoopConfigurationAdapter(this.conf);

        try {
            final EosDocument doc = textToEosDocument(eosDoc);
            final TokenizerBuilder tokenBuilder =
                TokenizerBuilder.newInstance(config);
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("TokenizerBuilder instanceof "
                         + tokenBuilder.getClass());
            }
            final TextBuilder textBuilder =
                TextBuilder.newInstance(config);
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("TextBuilder instanceof " + textBuilder.getClass());
            }
            final Sentencer sentencer = Sentencer.newInstance(config);
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("Sentencer instanceof " + sentencer.getClass());
            }

            final ResettableTokenizer tokenizer =
                tokenBuilder.newResettableTokenizer();
            final SentenceTokenizer sentenceTokenizer =
                new SentenceTokenizer();

            final Map<String, EosDocument> docs = 
                sentencer.toSentenceDocuments(doc,
                                              sentenceTokenizer,
                                              tokenizer,
                                              textBuilder);

            for (final Entry<String, EosDocument> entry : docs.entrySet()) {
                // TODO: Support for different key. At this time alle different
                //       Sentences were remove. Later the construction of the
                //       key will use different meta values
                //       (see mapred.entity.DictionaryBasedEntityRecognizerReducer)
                final String key = entry.getKey();
                final Text keyAsText = new Text(key);
                final EosDocument toWrite = entry.getValue();
                final Text docAsText = eosDocumentToText(toWrite);
                outputCollector.collect(keyAsText, docAsText);
                reporter.incrCounter(Index.MAP, 1);
            }

        } catch (final EosException e) {
            reporter.incrCounter(Index.EOS_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        } catch (final Exception e) {
            reporter.incrCounter(Index.IO_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        }
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
