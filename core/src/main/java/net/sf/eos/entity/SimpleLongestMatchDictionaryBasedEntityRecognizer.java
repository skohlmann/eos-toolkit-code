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
package net.sf.eos.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.analyzer.AbstractToken;
import net.sf.eos.analyzer.TextBuilder;
import net.sf.eos.analyzer.Token;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.TokenizerException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * A simple matcher for named entities. The implementation sluices
 * {@link Token Tokens} of a defined maximum length throught the recognizer.
 * If a token combination matches a key in the
 * {@link AbstractDictionaryBasedEntityRecognizer#getEntityMap() entity map},
 * a new <code>Token</code> of type {@link EntityRecognizer#ENTITY_TYPE} 
 * is created and return by {@link #next()}.
 * @author Sascha Kohlmann
 */
public class SimpleLongestMatchDictionaryBasedEntityRecognizer
        extends AbstractDictionaryBasedEntityRecognizer {

    static final Log LOG =
        LogFactory.getLog(SimpleLongestMatchDictionaryBasedEntityRecognizer.class.getName());

    private Map<CharSequence, Set<CharSequence>> entities;
    private Queue<Token> retvalBuffer = new LinkedList<Token>();
    private FixedSizeQueue<Token> longestMatchQueue = null;

    /**
     * Creates a new instance.
     * @param source the source tokenizer
     */
    public SimpleLongestMatchDictionaryBasedEntityRecognizer(
                @SuppressWarnings("hiding") final Tokenizer source) {
        super(source);
    }

    /**
     * Returned <code>Token</code> may be of type
     * {@link EntityRecognizer#ENTITY_TYPE} or any different type.
     * @throws IllegalStateException if {@link #getEntityMap()} returns
     *                               <code>null</code>
     * @see net.sf.eos.analyzer.Tokenizer#next()
     */
    @Override
    public Token next() throws TokenizerException {
        assert this.retvalBuffer != null;
        if (! this.retvalBuffer.isEmpty()) {
            return this.retvalBuffer.poll();
        }
        final int max = getMaxToken();
        assert max >= 1;
        if (this.longestMatchQueue == null) {
            this.longestMatchQueue = new FixedSizeQueue<Token>(max);
        }

        final Tokenizer source = getSource();
        assert source != null;
        final Map<CharSequence, Set<CharSequence>> entityMap = getEntityMap();
        if (entityMap == null) {
            throw new IllegalStateException("entitymap is null");
        }
        {
            Token t = null;
            while (this.longestMatchQueue.size() != max
                    && (t = source.next()) != null) {
                if (this.longestMatchQueue.offerFix(t) != null) {
                    throw new TokenizerException("internal error in size"
                                                 + " handling");
                }
            }
        }
        if (this.longestMatchQueue.size() != 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("END REACHED: "
                          + this.longestMatchQueue.toString());
            }
            final Match match =
                checkForLongestMatchInTrie(this.longestMatchQueue, entityMap);

            if (match != null) {
                final Token token =
                    new AbstractToken(match.key, ENTITY_TYPE) {};
                final Map<String, List<String>> meta = token.getMeta();
                final List<String> ids = new ArrayList<String>();
                for (final CharSequence cs : match.value) {
                    ids.add("" + cs);
                }
                meta.put(ENTITY_ID_KEY, ids);

                return token;
            }
            final Token old = this.longestMatchQueue.poll();
            return old;
        }

        return null;
    }

    final Match checkForLongestMatchInTrie(
            final LinkedList<Token> tokens,
            final Map<CharSequence, Set<CharSequence>> trie) {
        final int size = tokens.size();
        final Token[] ts = tokens.toArray(new Token[size]);
        for (int i = size; i != 0; i--) {
            Token[] t;
            if (size != i) {
                t = new Token[i];
                System.arraycopy(ts, 0, t, 0, i);
            } else {
                t = ts;
            }
            final CharSequence seq = toCharSequence(t);
            if (LOG.isDebugEnabled()) {
                LOG.debug("seq: '" + seq + "'");
            }
            final Set<CharSequence> value = trie.get(seq);
            if (LOG.isDebugEnabled()) {
                LOG.debug("from trie: " + value);
            }
            if (value != null) {
                for (int j = 0; j < i; j++) {
                    tokens.poll();
                }
                final Match match = new Match(seq, value);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("match: " + match);
                }
                return match;
            }
        }
        return null;
    }

    final CharSequence toCharSequence(final Token[] tokens) {
        for (int i = 0; i < tokens.length; i++) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("token: " + tokens[i]);
            }
        }
        final TextBuilder builder = getTextBuilder();
        if (builder == null) {
            return TextBuilder.SPACE_BUILDER.buildText(tokens);
        }
        return builder.buildText(tokens); 
    }

    final static class Match {

        final CharSequence key;
        final Set<CharSequence> value;

        public Match(final CharSequence match, final Set<CharSequence> value) {
            this.key = match;
            this.value = value;
        }

        /*
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            final StringBuilder sb =
                new StringBuilder(this.getClass().getSimpleName());

            sb.append("[[key:");
            sb.append(this.key);
            sb.append("][value:");
            sb.append(this.value);
            sb.append("]]");

            return sb.toString();
        }
    }

    /**
     * Only for internal use!
     * @author Sascha Kohlmann
     */
    final static class FixedSizeQueue<E> extends LinkedList<E> {

        private int maxSize = 0;

        public FixedSizeQueue(final int size) {
            if (size < 0) {
                throw new IllegalArgumentException("size < 0");
            }
            this.maxSize = size;
        }

        @Override
        public boolean offer(final E e) {
            throw new UnsupportedOperationException("use offerFix(E) instead");
        }

        public E offerFix(final E e) {
            if (super.offer(e)) {
                if (size() > maxSize) {
                    return poll();
                }
            }
            return null;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("FixedSizeQueue[");

            for (final E e : this) {
                sb.append("[");
                sb.append(e);
                sb.append("]");
            }
            sb.append("]");

            return sb.toString();
        }
    }
}
