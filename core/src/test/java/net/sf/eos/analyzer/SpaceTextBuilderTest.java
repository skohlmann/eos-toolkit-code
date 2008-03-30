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
package net.sf.eos.analyzer;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SpaceTextBuilderTest {

    @Test(expected=RuntimeException.class)
    public void tokenBuilderWithNullTokenList() {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText((List<Token>) null);
    }

    @Test
    public void tokenBuilderWithTwoTokenList() throws Exception {
        final Token t1 = new AbstractToken("z") {};
        final Token t2 = new AbstractToken("3") {};
        final List<Token> tokenList = new ArrayList<Token>();
        tokenList.add(t1);
        tokenList.add(t2);
        
        final CharSequence cs = TextBuilder.SPACE_BUILDER.buildText(tokenList);
        assertEquals("z 3", cs);
    }

    @Test
    public void tokenBuilderWithTwoTokenArray() throws Exception {
        final Token t1 = new AbstractToken("a") {};
        final Token t2 = new AbstractToken("1") {};
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText(new Token[] {t1, t2});
        assertEquals("a 1", cs);
    }

    @Test
    public void tokenBuilderWithOneTokenArray() throws Exception {
        final Token t = new AbstractToken("a") {};
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText(new Token[] {t});
        assertEquals("a", cs);
    }

    @Test(expected=RuntimeException.class)
    public void tokenBuilderWithNullTokenArray() {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText((Token[]) null);
    }

    @Test
    public void tokenBuilderWithEmptyTokenArray() throws Exception {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText(new Token[] {});
        assertEquals("", cs);
    }

    @Test(expected=RuntimeException.class)
    public void charSequenceBuilderWithNullCharSequenceArray() {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText((CharSequence[]) null);
    }

    @Test
    public void charSequenceBuilderWithEmptySequences() throws Exception {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText(new CharSequence[] {});
        assertEquals("", cs);
    }

    @Test
    public void charSequenceBuilderWithOneSequences() throws Exception {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText(new CharSequence[] {"a"});
        assertEquals("a", cs);
    }

    @Test
    public void charSequenceBuilderWithTwoSequences() throws Exception {
        final CharSequence cs =
            TextBuilder.SPACE_BUILDER.buildText(new CharSequence[] {"a", "b"});
        assertEquals("a b", cs);
    }
}
