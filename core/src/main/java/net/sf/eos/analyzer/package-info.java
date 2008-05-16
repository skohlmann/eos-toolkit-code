/**
 * <p>The package contains classes and patterns to support analyzing of
 *  {@link java.lang.CharSequence}. Implementation of
 *  {@link net.sf.eos.analyzer.Tokenizer} are the base to disassemble
 *  {@code CharSequences} into {@link net.sf.eos.analyzer.Token}.
 *  {@link net.sf.eos.analyzer.TextBuilder} rebuilds a new 
 *  {@code CharSequence} from an list of {@code CharSequences} or 
 *  {@link net.sf.eos.analyzer.Token}.</p>
 * <p>The {@link net.sf.eos.analyzer.TokenFilter} implements
 *  the <a href='http://en.wikipedia.org/wiki/Decorator_pattern' title='Wikipedia'>decorator pattern</a>.
 *  With the {@link net.sf.eos.analyzer.TokenizerProvider} it is possible to
 *  implement classes that returns a complete chain.</p>
 * <p>Classes that implements the {@link net.sf.eos.analyzer.ResettableTokenizer}
 *  should be reused by there clients.</p>
 *
 * @since 0.1.0
 */
package net.sf.eos.analyzer;
