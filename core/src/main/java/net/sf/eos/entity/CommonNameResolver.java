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
package net.sf.eos.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Locale;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.config.FactoryMethod;

/**
 * The common name resolver maps an ID of an entity to the common name of
 * the entity.
 * @author Sascha Kohlmann
 */
public abstract class CommonNameResolver extends Configured {

    /** For logging. */
    private static final Log LOG =
        LogFactory.getLog(CommonNameResolver.class.getName());

    /** The configuration key name for the classname of the factory.
     * @see #newInstance(Configuration) */
    @SuppressWarnings("nls")
    public final static String COMMON_NAME_RESOLVER_IMPL_CONFIG_NAME =
            "net.sf.eos.entity.CommonNameResolver.impl";

    /**
     * Creates a new instance of a of the recognizer. If the
     * <code>Configuration</code> contains a key
     * {@link #COMMON_NAME_RESOLVER_IMPL_CONFIG_NAME}
     * a new instance of the classname in the value will instantiate.
     * @param config the configuration
     * @return a new instance
     * @throws EosException if it is not possible to instantiate an instance
     */
    @FactoryMethod(key=COMMON_NAME_RESOLVER_IMPL_CONFIG_NAME)
    public final static CommonNameResolver newInstance(final Configuration config)
                throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader =
                AbstractDictionaryBasedEntityRecognizer.class.getClassLoader();
        }

        final String clazzName = config.get(COMMON_NAME_RESOLVER_IMPL_CONFIG_NAME);

        try {
            final Class<? extends CommonNameResolver> clazz = 
                (Class<? extends CommonNameResolver>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final CommonNameResolver instance = clazz.newInstance();
                instance.configure(config);
                return instance;

            } catch (final InstantiationException e) {
                throw new EosException(e);
            } catch (final IllegalAccessException e) {
                throw new EosException(e);
            }
        } catch (final ClassNotFoundException e) {
            throw new EosException(e);
        }
    }

    /**
     * Resolves the common enitity name for the default locale of the platform.
     * @param id the ID of the entity
     * @return a commond name or <code>null</code> if the implementation is
     *         unable to resolve a commmon name.
     * @throws EosException if an error occurs
     */
    public String resolveCommonName(final String id) throws EosException {
        final Locale locale = Locale.getDefault();
        return resolveCommonName(id, locale);
    }

    /**
     * Resolves the common enitity name for the given locale if available.
     * @param id the ID of the entity
     * @param locale to get the right language of the common name.
     * @return a commond name or <code>null</code> if the implementation is
     *         unable to resolve a commmon name.
     * @throws EosException if an error occurs
     */
    public abstract String resolveCommonName(final String id,
                                             final Locale locale) throws EosException;
}
