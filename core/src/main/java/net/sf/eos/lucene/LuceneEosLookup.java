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
package net.sf.eos.lucene;

import static net.sf.eos.config.ConfigurationKey.Type.CLASSNAME;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.ConfigurationKey;
import net.sf.eos.config.Configured;
import net.sf.eos.search.EosLookup;

/**
 * A simple <a href='http://lucene.apache.org/' title='Homepage'>Lucene</a>
 * entity oriented search lookup implementation.
 * @author Sascha Kohlmann
 */
public abstract class LuceneEosLookup extends Configured implements EosLookup {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(LuceneEosLookup.class.getName());

    /** The configuration key name for the classname of the creator.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    @ConfigurationKey(type=CLASSNAME,
                      description="Configuration key of the Lucene look up "
                                  + " factory.")
    public final static String LUCENE_EOS_LOOKUP_IMPL_CONFIG_NAME = 
        "net.sf.eos.lucene.LuceneEosLookup.impl";

//    /**
//     * Creates a new instance of a of the lookup. If the
//     * {@code Configuration} contains a key
//     * {@link #LUCENE_EOS_LOOKUP_IMPL_CONFIG_NAME} a new instance of the
//     * classname in the value will instantiate. The 
//     * {@link DefaultLuceneEosLookup} will instantiate if there is no
//     * value setted.
//     * @param config the configuration
//     * @return a new instance
//     * @throws EosException if it is not possible to instantiate an instance
//     */
//    @FactoryMethod(key=LUCENE_EOS_LOOKUP_IMPL_CONFIG_NAME,
//                   implementation=DefaultLuceneEosLookup.class)
//    public final static LuceneEosLookup
//            newInstance(final Configuration config) throws EosException {
//
//        final Thread t = Thread.currentThread();
//        ClassLoader classLoader = t.getContextClassLoader();
//        if (classLoader == null) {
//            classLoader = LuceneEosLookup.class.getClassLoader();
//        }
//
//        final String clazzName =
//            config.get(LUCENE_EOS_LOOKUP_IMPL_CONFIG_NAME,
//                       DefaultLuceneEosLookup.class.getName());
//
//        try {
//            final Class<? extends LuceneEosLookup> clazz =
//                (Class<? extends LuceneEosLookup>) Class
//                    .forName(clazzName, true, classLoader);
//            try {
//
//                final LuceneEosLookup lookup = clazz.newInstance();
//                lookup.configure(config);
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("LuceneEosLookup instance: "
//                              + lookup.getClass().getName());
//                }
//                return lookup;
//
//            } catch (final InstantiationException e) {
//                throw new EosException(e);
//            } catch (final IllegalAccessException e) {
//                throw new EosException(e);
//            }
//        } catch (final ClassNotFoundException e) {
//            throw new EosException(e);
//        }
//    }
}
