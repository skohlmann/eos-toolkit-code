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

import static org.junit.Assert.assertTrue;
import net.sf.eos.config.Configuration;

import org.junit.Test;

import java.util.List;


public class DefaultTextBuilderTest {

    @Test
    public void simpleFactoryCreation() throws Exception {
        final TextBuilder builder = TextBuilder.newInstance();
        assertTrue(builder.getClass().isInstance(TextBuilder.SPACE_BUILDER));
    }

    @Test
    public void notSpaceBuilderFactoryCreation() throws Exception {
        final Configuration conf = new Configuration();
        conf.set(TextBuilder.TEXT_BUILDER_IMPL_CONFIG_NAME,
                 DummyTextBuilder.class.getName());
        final TextBuilder builder = TextBuilder.newInstance(conf);
        assertTrue(builder instanceof DummyTextBuilder);
    }

    public final static class DummyTextBuilder extends TextBuilder {

        @Override
        public CharSequence buildText(List<Token> tokens) {
            return "";
        }

        @Override
        public CharSequence buildText(Token... tokens) {
            return "";
        }

        @Override
        public CharSequence buildText(CharSequence... seq) {
            return "";
        }
    }
    
}
