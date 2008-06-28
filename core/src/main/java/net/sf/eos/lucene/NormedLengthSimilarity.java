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
import org.apache.lucene.search.DefaultSimilarity;

/**
 * Normalize the length for {@link #lengthNorm(String, int)} to 1.0.
 * @author Sascha Kohlmann
 */
public class NormedLengthSimilarity extends DefaultSimilarity {

    private static final long serialVersionUID = 1070479651291502989L;

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(NormedLengthSimilarity.class.getName());

    @Override
    public float lengthNorm(final String fieldName, final int numTerms) {
        if (! "TODO".equals(fieldName)) {
            final float retval = super.lengthNorm(fieldName, numTerms);
            if (LOG.isTraceEnabled()) {
                LOG.trace("fieldname: "+ fieldName
                          + " - numTerms: "+ numTerms + " - retval: "
                          + retval);
            }
            return retval;
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("fieldname: "+ fieldName
                      + " - numTerms: "+ numTerms + " - retval: 1.0");
        }
        return 1.0f;
    }
    @Override
    public float queryNorm(final float sumOfSquaredWeights) {
        final float retval = super.queryNorm(sumOfSquaredWeights);
        if (LOG.isTraceEnabled()) {
            LOG.trace("sumOfSquaredWeights: "
                      + sumOfSquaredWeights + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float tf(final float freq) {
        final float retval =  super.tf(freq);
        if (LOG.isTraceEnabled()) {
            LOG.trace("freq: "+ freq + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float sloppyFreq(final int distance) {
        final float retval = super.sloppyFreq(distance);
        if (LOG.isTraceEnabled()) {
            LOG.trace("distance: "+ distance + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float idf(final int docFreq, final int numDocs) {
        final float retval = super.idf(docFreq, numDocs);
        if (LOG.isTraceEnabled()) {
            LOG.trace("docFreq: "+ docFreq
                    + " - docFreq: " + docFreq + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float coord(final int overlap, final int maxOverlap) {
        final float retval = super.idf(overlap, maxOverlap);
        if (LOG.isTraceEnabled()) {
            LOG.trace("overlap: "+ overlap
                      + " - maxOverlap: " + maxOverlap + " - retval: "
                      + retval);
        }
       return retval;
    }
}
