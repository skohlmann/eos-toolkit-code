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


/**
 * <p>&#949;&#959;s based on <a href='http://lucene.apache.org/' title='Homepage'>Lucene</a>.
 *  This package contains abstractions for the index creation and index look up.
 *  To support a better decoupling and configuration, the core services will
 *  create over factories.</p>
 * <h1>SimilarityFactory</h1>
 * <p>In a concordance system the length of a document may not be crucial.
 *  The main implementation of the Lucene length of the document is considered.
 *  The shorter document is the more important document. To suppress this
 *  attitude the default similarity implementation is
 *  {@link net.sf.eos.lucene.NormedLengthSimilarity} which sets the
 *  {@link net.sf.eos.lucene.NormedLengthSimilarity#lengthNorm(java.lang.String, int)}
 *  always to 1.0f.</p>
 * <h1>LuceneDocumentCreator</h1>
 * <p>To decouple the creation of the Lucence documents use instances of
 *  {@link net.sf.eos.lucene.LuceneDocumentCreator}. The 
 *  {@link net.sf.eos.lucene.LuceneDocumentCreator#newInstance(net.sf.eos.config.Configuration)}
 *  factory method creates a new instance.</p>
 * <h1>AnalyzerFactory</h1>
 * <p>Its also possible to replace the Lucene analyzer. User the
 *  {@link net.sf.eos.lucene.AnalyzerFactory} with the
 *  {@link net.sf.eos.lucene.AnalyzerFactory#newInstance(net.sf.eos.config.Configuration)}
 *  factory method to create a new instance.</p>
 *
 * @since 0.1.0
 */
package net.sf.eos.lucene;
