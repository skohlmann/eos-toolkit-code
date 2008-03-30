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
package net.sf.eos.hadoop.mapred.index;

import org.apache.lucene.search.DefaultSimilarity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Normalize the lengh for the goven field to 1.0.
 * @author Sascha Kohlmann
 */
public class NormedLengthSimilarity extends DefaultSimilarity {

    /** For logging. */
    private static final Logger LOG =
            Logger.getLogger(NormedLengthSimilarity.class.getName());
    private static final Level LEVEL = Level.FINER;

    @Override
    public float lengthNorm(final String fieldName, final int numTerms) {
        if (! "TODO".equals(fieldName)) {
            final float retval = super.lengthNorm(fieldName, numTerms);
            if (LOG.isLoggable(LEVEL)) {
                LOG.log(LEVEL, "fieldname: "+ fieldName
                               + " - numTerms: "+ numTerms + " - retval: "
                               + retval);
            }
            return retval;
        }
        if (LOG.isLoggable(LEVEL)) {
            LOG.log(LEVEL, "fieldname: "+ fieldName
                           + " - numTerms: "+ numTerms + " - retval: 1.0");
        }
        return 1.0f;
    }
    @Override
    public float queryNorm(final float sumOfSquaredWeights) {
        final float retval = super.queryNorm(sumOfSquaredWeights);
        if (LOG.isLoggable(LEVEL)) {
            LOG.log(LEVEL, "sumOfSquaredWeights: "
                           + sumOfSquaredWeights + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float tf(final float freq) {
        final float retval =  super.tf(freq);
        if (LOG.isLoggable(LEVEL)) {
            LOG.log(LEVEL, "freq: "+ freq + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float sloppyFreq(final int distance) {
        final float retval = super.sloppyFreq(distance);
        if (LOG.isLoggable(LEVEL)) {
            LOG.log(LEVEL, "distance: "+ distance + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float idf(final int docFreq, final int numDocs) {
        final float retval = super.idf(docFreq, numDocs);
        if (LOG.isLoggable(LEVEL)) {
            LOG.log(LEVEL, "docFreq: "+ docFreq
                           + " - docFreq: " + docFreq + " - retval: " + retval);
        }
        return retval;
    }
    @Override
    public float coord(final int overlap, final int maxOverlap) {
        final float retval = super.idf(overlap, maxOverlap);
        if (LOG.isLoggable(LEVEL)) {
            LOG.log(LEVEL, "overlap: "+ overlap
                           + " - maxOverlap: " + maxOverlap + " - retval: "
                           + retval);
        }
       return retval;
    }
}
