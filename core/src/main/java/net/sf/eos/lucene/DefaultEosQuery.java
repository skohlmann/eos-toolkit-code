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
package net.sf.eos.lucene;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryParser.QueryParser;

import java.util.ArrayList;
import java.util.List;

import net.sf.eos.EosException;
import net.sf.eos.lucene.DefaultEosQuery.Entry.Bool;

class DefaultEosQuery implements CommonDocument, EosQuery {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(DefaultEosQuery.class.getName());

    public static class Entry {

        public static enum Bool {AND, OR, AND_NOT}

        private final Bool bool;
        private final String fieldName;
        private final String lowerBoundValue;
        private final String upperBoundValue;

        public Entry (final Bool op,
                      final String fieldName,
                      final String lowerBoundValue) {
            this(op, fieldName, lowerBoundValue, null);
        }

        public Entry (@SuppressWarnings("hiding") final Bool op,
                      @SuppressWarnings("hiding") final String fieldName,
                      @SuppressWarnings("hiding") final String lowerBoundValue,
                      @SuppressWarnings("hiding") final String upperBoundValue) {
            this.bool = op;
            this.fieldName = fieldName;
            this.lowerBoundValue = lowerBoundValue;
            this.upperBoundValue = upperBoundValue;
        }

        public Bool getBool() {
            return this.bool;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public String getLowerBoundValue() {
            return this.lowerBoundValue;
        }

        public String getUpperBoundValue() {
            return this.upperBoundValue;
        }
    }

    private final List<Entry> query = new ArrayList<Entry>();

    public DefaultEosQuery and(final String phrase) {
        andMeta(FieldName.CONTENT.name(), phrase);
        return this;
    }

    public DefaultEosQuery andNot(final String phrase) throws EosException {
        andNotMeta(FieldName.CONTENT.name(), phrase);
        return this;
    }

    public DefaultEosQuery andMeta(final String fieldName, final String value) {
        final Entry entry = new Entry(Bool.AND, fieldName, value);
        this.query.add(entry);
        return this;
    }

    public DefaultEosQuery andNotMeta(final String fieldName, final String value) {
        final Entry entry = new Entry(Bool.AND_NOT, fieldName, value);
        this.query.add(entry);
        return this;
    }


    public DefaultEosQuery andMetaRange(final String fieldName,
                                        final String lowerBound,
                                        final String upperBound) {
        final Entry entry = new Entry(Bool.AND, fieldName, lowerBound, upperBound);
        this.query.add(entry);
        return this;
    }

    public DefaultEosQuery or(final String phrase) {
        orMeta(FieldName.CONTENT.name(), phrase);
        return this;
    }

    public DefaultEosQuery orMeta(final String fieldName, final String value) {
        final Entry entry = new Entry(Bool.OR, fieldName, value);
        this.query.add(entry);
        return this;
    }

    public DefaultEosQuery orMetaRange(final String fieldName,
                                       final String lowerBound,
                                       final String upperBound) {
        final Entry entry = new Entry(Bool.OR, fieldName, lowerBound, upperBound);
        this.query.add(entry);
        return this;
    }

    @SuppressWarnings("nls")
    public String executableQuery() throws EosException {
        final StringBuilder sb = new StringBuilder();
        final int size = this.query.size();
        Entry[] entries = this.query.toArray(new Entry[size]);

        for (int i = 0; i < size; i++) {
            final Bool b = entries[i].getBool();
            if (i != 0) {
                if (b == Bool.AND || b == Bool.OR) {
                    sb.append(b);
                } else {
                    sb.append(Bool.AND);
                }
                sb.append(" ");
            }
            if (b == Bool.AND_NOT) {
                sb.append("-");
            }
            final String fieldName = entries[i].getFieldName();
            sb.append(fieldName);
            sb.append(":");

            final String lower = entries[i].getLowerBoundValue();
            final String lowerEscaped = QueryParser.escape(lower);
            final String upper = entries[i].getUpperBoundValue();
            String upperEscaped = null;
            if (upper != null) {
                upperEscaped = QueryParser.escape(upper);
            }

            if (upper != null) {
                sb.append("[");
            }
            sb.append("\"");

            sb.append(lowerEscaped);
            if (upper != null) {
                sb.append("\" TO \"");
                sb.append(upperEscaped);
            }
            sb.append("\"");
            if (upper != null) {
                sb.append("]");
            }
            sb.append(" ");
        }

        final String q = sb.toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("QUERY: " + q);
        }

        return q;
    }
}
