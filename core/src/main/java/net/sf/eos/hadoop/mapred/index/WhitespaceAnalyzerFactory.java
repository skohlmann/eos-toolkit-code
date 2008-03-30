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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;

/**
 * Supprt for Lucene {@link org.apache.lucene.analysis.WhitespaceAnalyzer}.
 * @author Sascha Kohlmann
 */
public class WhitespaceAnalyzerFactory extends AnalyzerFactory {

    /**
     * @return a Lucene {@link org.apache.lucene.analysis.WhitespaceAnalyzer}
     */
    @Override
    public Analyzer newAnalyzer() {
        return new WhitespaceAnalyzer();
    }
}
