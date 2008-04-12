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
package net.sf.eos.trie;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.trie.TrieSource.TrieEntry;
import net.sf.eos.trie.TrieSource.TrieEntryEvent;
import net.sf.eos.trie.TrieSource.TrieEntryListener;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * <p>The builder creates a trie from a simple XML file. The file must be
 * like the following <acronym title='Document Type Definition'>DTD</acronym>:
 * </p>
 * <pre>
 * &lt;!ELEMENT trie  (entry*)     >
 * &lt;!ELEMENT entry (key, value) >
 * &lt;!ELEMENT key   (#PCDATA)    >
 * &lt;!ELEMENT value (#PCDATA)    ></pre>
 * <p>The builder doesn't validate validates the XML structure.</p>
 * <p>If a key is twice in the XML structure the builder adds it to the
 * value <code>Collection</code>.
 * @author Sascha Kohlmann
 */
@SuppressWarnings("nls")
public class XmlTrieLoader 
        extends AbstractTrieLoader<CharSequence, Set<CharSequence>> {

    static final Log LOG = LogFactory.getLog(XmlTrieLoader.class.getName());

    /** A Tokenizer to tokenize the value of the trie before storing. */
    private ResettableTokenizer tokenizer;
    private TextBuilder textBuilder = TextBuilder.SPACE_BUILDER;

    /**
     * Creates a <code>Trie</code> from the <code>InputStream</code>.
     */
    @Override
    public void loadTrie(final InputStream trieData,
                         final Trie<CharSequence, Set<CharSequence>> trie)
            throws Exception {

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        final SAXParser parser = factory.newSAXParser();

        final TrieHandler source = new TrieHandler();
        source.addTrieEntryListener(new TrieEntryListener() {
            public void onEntry(final TrieEntryEvent event) {
                final TrieEntry entry = (TrieEntry) event.getSource();
                handleNewTrieEntryForCharSequenceTrie(entry, trie);
            }
        });

        final long start = System.currentTimeMillis();
        LOG.trace("start time since epoche: " + start + "ms");
        final MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        final StringBuilder lsb =
            new StringBuilder("Heap Memory statistics:\n");
        lsb.append("  heap: ");
        final MemoryUsage heap = memBean.getHeapMemoryUsage();
        lsb.append(heap);
        lsb.append("\n  nonheap: ");
        final MemoryUsage nonheap = memBean.getNonHeapMemoryUsage();
        lsb.append(nonheap);
        LOG.debug(lsb.toString());
        logStatistic();

        parser.parse(trieData, source);

        logStatistic();
        LOG.debug("Build time: " 
                  + (System.currentTimeMillis() - start) + "ms"
                  + " for " + trie.size() + " entries");
    }

    final void handleNewTrieEntryForCharSequenceTrie(
                final TrieEntry entry,
                final Trie<CharSequence, Set<CharSequence>> trie) {
        final String key = entry.getKey();
//        final byte[] keyArray = toUtf8ByteArray(key);
        final String value = entry.getValue();
        final CharSequence rebuildedValue = rebuildValue(value);
//        final byte[] valueArray = toUtf8ByteArray(rebuildedValue);

//        Set<byte[]> values = trie.get(keyArray);
//        if (values == null) {
//            values = new HashSet<byte[]>();
//            trie.put(keyArray, values);
//        }
//        values.add(valueArray);
        Set<CharSequence> values = trie.get(key);
        if (values == null) {
            values = new HashSet<CharSequence>();
            trie.put(key, values);
        }
        values.add(rebuildedValue);
    }

    final void logStatistic() {
        final List<MemoryPoolMXBean> pool =
            ManagementFactory.getMemoryPoolMXBeans();
        final StringBuilder lsb =
            new StringBuilder("Memory statistics:\n");
        for (final MemoryPoolMXBean bean : pool) {
            lsb.append("  name: ");
            lsb.append(bean.getName());
            final MemoryUsage peakUsage = bean.getPeakUsage();
            lsb.append(" |Êpeak: ");
            lsb.append(peakUsage);
            final MemoryUsage usage = bean.getUsage();
            lsb.append(" |Êusage: ");
            lsb.append(usage);
            lsb.append("\n");
        }

        LOG.debug(lsb.toString());
    }

    /**
     * @return the tokenizer
     */
    public ResettableTokenizer getTokenizer() {
        return this.tokenizer;
    }

    /**
     * @param tokenizer the tokenizer to set
     */
    public void setTokenizer(
            @SuppressWarnings("hiding") final ResettableTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * Sets a builder. The implementation has default builder of instance
     * {@link TextBuilder#SPACE_BUILDER} setted at construction time.
     * @param builder a builder to set or <code>null</code>
     */
    public void setTextBuilder(final TextBuilder builder) {
        this.textBuilder = builder;
    }

    /**
     * Returns a setted builder.
     * @return a setted builder or <code>null</code>.
     */
    public TextBuilder getTextBuilder() {
        return this.textBuilder;
    }

    /**
     * Rebuilds a sequence of chars if the loader has a setted 
     * {@link #setTokenizer(ResettableTokenizer)} and a setted
     * {@link #setTextBuilder(TextBuilder)}.
     * @param value the value to rebuild.
     * @return a rebuilded character sequence.
     */
    protected CharSequence rebuildValue(final CharSequence value) {
        if (value == null) {
            return value;
        }

        final ResettableTokenizer t = getTokenizer();
        if (t == null) {
            return value;
        }
        final TextBuilder b = getTextBuilder();
        if (b == null) {
            return value;
        }

        try {
            t.reset(value);
            final List<Token> tokens = new ArrayList<Token>();
            Token to = null;
            while ((to = t.next()) != null) {
                tokens.add(to);
            }
            final CharSequence newText = b.buildText(tokens);

            return newText;

        } catch (final TokenizerException e) {
            throw new RuntimeException(e);
        }
    }

    final byte[] toUtf8ByteArray(final CharSequence s) {
        try {
            return s.toString().getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new InternalError("UFT-8 not supported");
        }
    }
}
