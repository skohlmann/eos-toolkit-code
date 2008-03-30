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
package net.sf.eos.sentence;


import net.sf.eos.EosException;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.SentenceTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.document.EosDocument;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DefaultSentencer extends Sentencer {

    /** For logging. */
    private static final Logger LOG = 
        Logger.getLogger(DefaultSentencer.class.getName());

    public DefaultSentencer() {
        super();
    }

    @Override
    public Map<String, EosDocument> 
            toSentenceDocuments(final EosDocument doc,
                                final SentenceTokenizer sentencer,
                                final ResettableTokenizer tokenizer,
                                final TextBuilder builder)
            throws EosException {

        final Map<String, EosDocument> retval =
            new HashMap<String, EosDocument>();
        final MessageDigest md = createDigester();

        final Map<String, List<String>> meta = doc.getMeta();

//        final List<String> years = meta.get(EosDocument.YEAR_META_KEY);
//        if (years == null || years.size() == 0) {
//            LOG.warning("document contains no year: " + doc);
//            return retval;
//        }
//        final String year = years.get(0);
//        if (year == null || year.length() == 0) {
//            LOG.warning("document year is null or has length zero: " + doc);
//            return retval;
//        }

        final CharSequence newTitle = extractTitle(doc, tokenizer, builder);
        final List<CharSequence> sentences =
            extractSentences(doc, sentencer, tokenizer, builder);

        for (final CharSequence newText : sentences) {
            final EosDocument newDoc = new EosDocument();
            newDoc.setText(newText);
            newDoc.setTitle(newTitle);
            final Map<String, List<String>> newMeta = newDoc.getMeta();
            newMeta.putAll(meta);

            try {
                final byte[] bytes = ("" + newText).getBytes("UTF-8");
                md.reset();
                final byte[] key = md.digest(bytes);
                final char[] asChar = Hex.encodeHex(key);
                final String asString = new String(asChar);
                retval.put(asString, newDoc);
            } catch (final UnsupportedEncodingException e) {
                throw new TokenizerException(e);
            }
        }
        return retval;
    }

    final List<CharSequence> extractSentences(
            final EosDocument doc,
            final SentenceTokenizer sentencer,
            final ResettableTokenizer tokenizer,
            final TextBuilder builder)
                throws EosException {
        final List<CharSequence> sentences = new ArrayList<CharSequence>();

        final CharSequence text = doc.getText();
        sentencer.reset(text);
        Token sentence = null;
        while ((sentence = sentencer.next()) != null) {
            final CharSequence seq = sentence.getTokenText();
            tokenizer.reset(seq);
            final List<Token> textTokens = new ArrayList<Token>();
            Token textToken = null;

            while ((textToken = tokenizer.next()) != null) {
                textTokens.add(textToken);
            }

            final CharSequence newText = builder.buildText(textTokens);
            sentences.add(newText);
        }

        return sentences;
    }

    final CharSequence extractTitle(final EosDocument doc,
                                    final ResettableTokenizer tokenizer,
                                    final TextBuilder builder)
            throws EosException {

        final CharSequence title = doc.getTitle();
        final List<Token> titleTokens = new ArrayList<Token>();
        tokenizer.reset(title);
        Token titleToken = null;
        while ((titleToken = tokenizer.next()) != null) {
            titleTokens.add(titleToken);
        }
        final CharSequence newTitle = builder.buildText(titleTokens);
        return newTitle;
    }
}
