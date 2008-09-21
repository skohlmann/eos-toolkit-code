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
package net.sf.eos.analyzer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.eos.util.Conditions.checkArgumentNotNull;

/**
 * Simple implementation for reuse.
 * @author Sascha Kohlmann
 */
public abstract class AbstractToken implements Token {

    private final CharSequence text;
    private final Map<String, List<String>> metadata =
        new HashMap<String, List<String>>();
    private final String type;

    /** Creates a new token for the given sequence. {@link #getType()} returns
     * the {@link Token#DEFAULT_TYPE}.
     * @param tokenSequence the char sequence of the token. May not be
     *                      <code>null</code>.
     * @throws IllegalArgumentException if <em>tokenSequence</em> is
     *                                  <code>null</code>
     */
    public AbstractToken(final CharSequence tokenSequence) {
        this(tokenSequence, DEFAULT_TYPE, Collections.EMPTY_MAP);
    }

    public AbstractToken(final CharSequence tokenSequence,
                         final Map<String, List<String>> metadata) {
        this(tokenSequence, DEFAULT_TYPE, metadata);
    }

    public AbstractToken(final CharSequence tokenSequence, final String type) {
        this(tokenSequence, type, Collections.EMPTY_MAP);
    }

    /**
     * Copy constructor.
     * @param t the token to copy.
     */
    public AbstractToken(final Token t) {
        this(t.getTokenText(), t.getType(), t.getMeta());
    }

    /** Creates a new token for the given sequence for a special <em>type</em>.
     * @param tokenSequence the char sequence of the token. May not be
     *                      <code>null</code>.
     * @param type the type of the token. May not be <code>null</code>.
     * @throws IllegalArgumentException if <em>tokenSequence</em> or
     *                                  <em>type</em> are <code>null</code>
     */
    @SuppressWarnings("nls")
    public AbstractToken(final CharSequence tokenSequence,
                         final String type,
                         final Map<String, List<String>> metadata) {
        this.text = checkArgumentNotNull(tokenSequence, "tokenSequence is null");
        this.type = checkArgumentNotNull(type, "type is null");
        this.metadata.putAll(metadata);
    }

    /**
     * @see Token#getTokenText()
     */
    public CharSequence getTokenText() {
        assert this.text != null;
        return this.text;
    }

    /**
     * @see Token#getType()
     */
    public String getType() {
        assert this.type != null;
        return this.type;
    }

    /**
     * @see Token#getMeta()
     */
    public Map<String, List<String>> getMeta() {
        assert this.metadata != null;
        return this.metadata;
    }

    @Override
    public String toString() {
        final StringBuilder sb =
            new StringBuilder(this.getClass().getSimpleName());

        sb.append("[tokenText:");
        sb.append(getTokenText());
        sb.append("]");

        return sb.toString();
    }
}
