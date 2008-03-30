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


import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.medline.MedlineTokenizerBuilder;

public abstract class TokenizerBuilder extends Configured {

    public final static String BUILDER_IMPL_CONFIG_NAME =
        "net.sf.eos.analyzer.TokenizerBuilder.impl";

    public final static TokenizerBuilder newInstance() throws TokenizerException
    {
        final Configuration config = new Configuration();
        return newInstance(config);
    }

    public final static TokenizerBuilder newInstance(final Configuration config)
            throws TokenizerException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = TokenizerBuilder.class.getClassLoader();
        }

        final String clazzName = config.get(BUILDER_IMPL_CONFIG_NAME,
                                      MedlineTokenizerBuilder.class.getName());

        try {
            final Class<? extends TokenizerBuilder> clazz = 
                (Class<? extends TokenizerBuilder>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final TokenizerBuilder builder = clazz.newInstance();
                builder.configure(config);
                return builder;

            } catch (final InstantiationException e) {
                throw new TokenizerException(e);
            } catch (final IllegalAccessException e) {
                throw new TokenizerException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new TokenizerException(e);
        }
    }

    public abstract ResettableTokenizer newResettableTokenizer()
        throws TokenizerException;
}
