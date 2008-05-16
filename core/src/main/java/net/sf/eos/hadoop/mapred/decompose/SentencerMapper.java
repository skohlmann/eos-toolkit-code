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
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.TokenizerProvider;
import net.sf.eos.analyzer.TextBuilder.SpaceBuilder;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.HadoopConfigurationAdapter;
import net.sf.eos.config.Service;
import net.sf.eos.config.Services;
import net.sf.eos.document.EosDocument;
import net.sf.eos.hadoop.mapred.AbstractKeyGenerator;
import static net.sf.eos.hadoop.mapred.AbstractKeyGenerator.ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME;
import net.sf.eos.hadoop.mapred.EosDocumentSupportMapReduceBase;
import net.sf.eos.hadoop.mapred.Index;
import net.sf.eos.hadoop.mapred.KeyGenerator;
import net.sf.eos.sentence.Sentencer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

@Services(
    services={
        @Service(
            factory=AbstractKeyGenerator.class,
            implementation=TextMetaKeyGenerator.class,
            description="Tokenizer for coocurence analyzing."
        ),
        @Service(
            factory=TextBuilder.class,
            implementation=SpaceBuilder.class
        ),
        @Service(
            factory=TokenizerProvider.class
        ),
        @Service(
            factory=Sentencer.class
        )
    }
)
public class SentencerMapper extends EosDocumentSupportMapReduceBase
        implements Mapper<LongWritable, Text, Text, Text> {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(SentencerMapper.class.getName());

    private JobConf conf;

    public void map(final LongWritable positionInFile,
                    final Text eosDoc,
                    final OutputCollector<Text, Text> outputCollector,
                    final Reporter reporter) throws IOException {

        final Configuration config = new HadoopConfigurationAdapter(this.conf);

        try {
            final EosDocument doc = textToEosDocument(eosDoc);
            final TokenizerProvider tokenBuilder =
                TokenizerProvider.newInstance(config);
            if (LOG.isDebugEnabled()) {
                LOG.debug("TokenizerBuilder instanceof "
                          + tokenBuilder.getClass());
            }
            final TextBuilder textBuilder =
                TextBuilder.newInstance(config);
            if (LOG.isDebugEnabled()) {
                LOG.debug("TextBuilder instanceof " + textBuilder.getClass());
            }
            final Sentencer sentencer = Sentencer.newInstance(config);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Sentencer instanceof " + sentencer.getClass());
            }

            final ResettableTokenizer tokenizer =
                tokenBuilder.get();
            final SentenceTokenizer sentenceTokenizer =
                new SentenceTokenizer();

            final Map<String, EosDocument> docs = 
                sentencer.toSentenceDocuments(doc,
                                              sentenceTokenizer,
                                              tokenizer,
                                              textBuilder);

            final KeyGenerator<Text> generator = newGenerator();
            for (final Entry<String, EosDocument> entry : docs.entrySet()) {
                final String key = entry.getKey();
                final EosDocument toWrite = entry.getValue();
                final Map<Text, EosDocument> toStore =
                    generator.createKeysForDocument(toWrite);

                for (final Entry<Text, EosDocument> e : toStore.entrySet()) {
                    final Text textKey = e.getKey();
                    final String asString = textKey.toString();
                    final String addKey = key + "+" + asString;
                    final Text keyAsText = new Text(addKey);
                    final Text docAsText = eosDocumentToText(toWrite);
                    outputCollector.collect(keyAsText, docAsText);
                    reporter.incrCounter(Index.MAP, 1);
                }
            }

        } catch (final EosException e) {
            reporter.incrCounter(Index.EOS_EXCEPTION, 1);
            throw new IOException(e.getMessage());
        } catch (final Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            reporter.incrCounter(Index.IO_EXCEPTION, 1);
            throw new IOException("" + e.getClass() + " - " + e.getMessage());
        }
    }

    protected KeyGenerator<Text> newGenerator() throws EosException {
        final Configuration lconf = new Configuration(); 
        HadoopConfigurationAdapter.addHadoopConfigToEosConfig(this.conf, lconf);
        
        final String implName =
            lconf.get(ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME);
        if (implName == null || implName.length() == 0) {
            lconf.set(ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME,
                    TextMetaKeyGenerator.class.getName());
        }
        final KeyGenerator<Text> newInstance =
            (KeyGenerator<Text>) AbstractKeyGenerator.newInstance(lconf);

        if (LOG.isDebugEnabled()) {
            LOG.debug("KeyGenerator<Text> instance is "
                      + newInstance.getClass());
        }
        return newInstance;
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
