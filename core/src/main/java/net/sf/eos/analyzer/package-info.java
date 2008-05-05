/**
 * <p>The package contains classes and patterns to support analyzing of
 *  {@link java.lang.CharSequences}. Implementation of
 *  {@link Tokenizer} are the base to
 *  disassemble <code>CharSequences</code> into
 *  {@link Token}.
 *  {@link TextBuilder} rebuilds a new
 *  <code>CharSequence</code> from an list of <code>CharSequences</code> or
 *  <code>Token</code>.</p>
 * <p>The {@link TokenFilter} implements
 *  the <a href='http://en.wikipedia.org/wiki/Decorator_pattern' title='Wikipedia'>decorator pattern</a>.
 *  With the {@link TokenizerBuilder}
 *  it is possible to implement classes that returns a complete chain.</p>
 * <p>Classes that implements the
 *  {@link ResettableTokenizer}
 *  should be reused.</p>
 *  
 * @since 0.1.0
 */
package net.sf.eos.analyzer;
