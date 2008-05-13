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
package net.sf.eos.trie;


import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;
import net.sf.eos.analyzer.TokenizerException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.FactoryMethod;

import java.io.InputStream;

public abstract class AbstractTrieLoader<K, V> implements TrieLoader<K, V> {

    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
            description="Configuration key of the trie loader"
                        + " factory.")
    public final static String TRIE_LOADER_IMPL_CONFIG_NAME =
        "net.sf.eos.trie.AbstractTrieLoader.impl";

    /**
     * Default {@code TrieLoader} is {@link XmlTrieLoader}.
     * @return a loader
     * @throws TokenizerException
     */
    @FactoryMethod(key=TRIE_LOADER_IMPL_CONFIG_NAME,
                   implementation=XmlTrieLoader.class)
    public final static TrieLoader newInstance() throws TokenizerException
    {
        final Configuration config = new Configuration();
        return newInstance(config);
    }

    @FactoryMethod(key=TRIE_LOADER_IMPL_CONFIG_NAME,
                   implementation=XmlTrieLoader.class)
    public final static TrieLoader newInstance(final Configuration config)
            throws TokenizerException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = AbstractTrieLoader.class.getClassLoader();
        }

        final String clazzName =
            config.get(TRIE_LOADER_IMPL_CONFIG_NAME,
                       XmlTrieLoader.class.getName());

        try {
            final Class<? extends TrieLoader> clazz = 
                (Class<? extends TrieLoader>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final TrieLoader loader = clazz.newInstance();
                return loader;

            } catch (final InstantiationException e) {
                throw new TokenizerException(e);
            } catch (final IllegalAccessException e) {
                throw new TokenizerException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new TokenizerException(e);
        }
    }

    public abstract void loadTrie(final InputStream trieData,
                                  final Trie<K, V> trie)
            throws Exception;
}
