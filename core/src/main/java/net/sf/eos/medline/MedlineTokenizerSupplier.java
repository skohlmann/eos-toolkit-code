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
package net.sf.eos.medline;

import net.sf.eos.analyzer.CaseTokenFilter;
import net.sf.eos.analyzer.ResettableTokenFilter;
import net.sf.eos.analyzer.ResettableTokenizer;
import net.sf.eos.analyzer.StopTokenFilter;
import net.sf.eos.analyzer.SurroundingTokenFilter;
import net.sf.eos.analyzer.Tokenizer;
import net.sf.eos.analyzer.TokenizerSupplier;
import net.sf.eos.analyzer.WhitespaceTokenizer;

import static net.sf.eos.medline.MedlineAbstractStructureWords.STRUCTURE_WORDS_UPPER;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MedlineTokenizerSupplier extends TokenizerSupplier {

    @Override
    public ResettableTokenizer get() {

        final ResettableTokenizer whitespace = new WhitespaceTokenizer();
        final Set<CharSequence> stopWords =
            new HashSet<CharSequence>(Arrays.asList(STRUCTURE_WORDS_UPPER));
        final Tokenizer stop = new StopTokenFilter(whitespace, stopWords);
        final Tokenizer lower = new CaseTokenFilter(stop);
        final Tokenizer surround = new SurroundingTokenFilter(lower);
        final ResettableTokenizer retval =
            new ResettableTokenFilter(surround, whitespace);

        return retval;
    }
}
