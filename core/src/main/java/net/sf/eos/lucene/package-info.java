/**
 * <p>&#949;&#959;s based on <a href='http://lucene.apache.org/' title='Homepage'>Lucene</a>.
 *  This package contains abstractions for the index creation and index lokk up.
 *  To support a better decoupling and configuration, the core services will
 *  create over factories.</p>
 * <h1>SimilarityFactory</h1>
 * <p>In a concordance system the length of a document may not be crucial.
 *  The main implementation of the Lucene length of the document is considered.
 *  The shorter document is the more important document. To suppress this
 *  attitude the default similarity implementation is
 *  {@link NormedLengthSimilarity}
 *  which sets the
 *  {@link NormedLengthSimilarity#lengthNorm(java.lang.String, int)}
 *  always to 1.0f.</p>
 * <h1>LuceneDocumentCreator</h1>
 * <p>To decouple the creation of the Lucence documents use instances of
 *  {@link LuceneDocumentCreator}.
 *  The 
 *  {@link LuceneDocumentCreator#newInstance(net.sf.eos.config.Configuration)}
 *  factory method creates a new instance.</p>
 * <h1>AnalyzerFactory</h1>
 * <p>Its also possible to replace the Lucene analyzer. User the
 *  {@link AnalyzerFactory} with the
 *  {@ink AnalyzerFactory#newInstance(net.sf.eos.config.Configuration)}
 *  factory method to create a new instance.</p>
 */
package net.sf.eos.lucene;
