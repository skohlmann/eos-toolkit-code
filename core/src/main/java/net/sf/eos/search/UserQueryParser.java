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
package net.sf.eos.search;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.eos.Function;

/**
 * Simple splitter for user search queries. User search queries must be very
 * simple to understand (for the most users, not for power user).
 *
 * <p>A search query is a quantity of phrases. A term is a literal without any
 * whitespace character. Whitespace characters are the delimiter of the terms
 * in a search query. A compound is a quantity of terms enclosed by two 
 * ASCII&nbsp;{@literal 0x22} (") characters called phrase. The processor}}
 * returns a list of terms and phrases in order of there index position in
 * the query. The processor removes ASCII&nbsp;{@literal 0x22} characters an
 * normalizes following whitespace characters to only one
 * ASCII&nbsp;{@literal 0x20} character. All whitespace characters other
 * than ASCII&nbsp;{@literal 0x20} will normalized to
 * ASCII&nbsp;{@literal 0x20}. Newlines were removed if possible. If the
 * character count of ASCII&nbsp;{@literal 0x22} is odd, the last
 * ASCII&nbsp;{@literal 0x22} will be part of a term or a single term.</p>
 *
 * @author Sascha Kohlmann
 */
public class UserQueryParser implements Function<String, List<String> > {

    /**
     * Splits the user query in different terms and phrases.
     *
     * @param query the users search query
     * @return Array with different search information
     */
    @SuppressWarnings("nls")
    final List<String> parseUserQuery(final String query) {

        if (query == null) {
            return new ArrayList<String>(0);
        }

        final String adjusted = " " + removeNewLines(query);
        if (! query.contains("\"")) {
            return splitSimpleTerms(adjusted);
        }
        
        final List<String> retval = new ArrayList<String>();
        boolean inQuote = false;
        for (final StringTokenizer st = new StringTokenizer(adjusted, "\"");
                st.hasMoreElements(); ) {
            final String part = st.nextToken();
            if (inQuote) {
                final String s = adjustWhiteSpace(part);
                if (s.length() != 0) {
                    retval.add(s);
                }
                inQuote = false;
            } else {
                final List<String> terms = splitSimpleTerms(part);
                retval.addAll(terms);
                inQuote = true;
            }
        }

        return retval;
    }

    @SuppressWarnings("nls")
    final static String adjustWhiteSpace(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (final StringTokenizer st = new StringTokenizer(s);
                st.hasMoreTokens(); ) {
            final String part = st.nextToken();
            sb.append(part);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    @SuppressWarnings("nls")
    final static String removeNewLines(final String s) {
        assert s != null;
        final StringBuilder sb = new StringBuilder();
        for (final StringTokenizer st = new StringTokenizer(s, "\n\r\f");
                st.hasMoreTokens(); ) {
            final String part = st.nextToken();
            sb.append(part);
        }
        return sb.toString();
    }

    @SuppressWarnings("nls")
    final static List<String> splitSimpleTerms(final String terms) {
        final List<String> retval = new ArrayList<String>();
        if (terms == null) {
            return retval;
        }
        for (final StringTokenizer st =
                    new StringTokenizer(terms, 
                               " \t\u001c\u001d\u001e\u001f\u00a0\u2007\u202f");
                st.hasMoreTokens(); ) {
            final String part = st.nextToken();
            retval.add(part);
        }
        return retval;
    }

    /**
     * Splits the user query in different terms and phrases.
     *
     * @param query the users search query
     * @return Array with different search information
     */
    public List<String> apply(final String query) {
        return parseUserQuery(query);
    }
}
