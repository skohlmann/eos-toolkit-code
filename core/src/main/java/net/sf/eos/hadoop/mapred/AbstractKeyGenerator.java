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
package net.sf.eos.hadoop.mapred;

import net.sf.eos.EosException;
import net.sf.eos.config.Configuration;
import net.sf.eos.config.Configured;
import net.sf.eos.sentence.Sentencer;

import org.apache.hadoop.io.WritableComparable;

/**
 * Simple implementation with a factory.
 * @author Sascha Kohlmann
 */
public abstract class AbstractKeyGenerator<K extends WritableComparable>
        extends Configured implements KeyGenerator<K> {

    @SuppressWarnings("nls")
    public final static String ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME =
        "net.sf.eos.hadoop.mapred.AbstractKeyGenerator.impl";

    /**
     * Creates a new instance.
     * @param conf the configuration with the configuration data for the
     *            implementation
     * @return a new instance
     * @throws EosException if an error occurs
     */
    public final static AbstractKeyGenerator<? extends WritableComparable> 
            newInstance(final Configuration conf) throws EosException {

        final Thread t = Thread.currentThread();
        ClassLoader classLoader = t.getContextClassLoader();
        if (classLoader == null) {
            classLoader = Sentencer.class.getClassLoader();
        }

        final String clazzName =
            conf.get(ABSTRACT_KEY_GENERATOR_IMPL_CONFIG_NAME);

        try {
            final Class<? extends AbstractKeyGenerator> clazz = 
                (Class<? extends AbstractKeyGenerator>) 
                    Class.forName(clazzName, true, classLoader);
            try {

                final AbstractKeyGenerator instance = clazz.newInstance();
                instance.configure(conf);
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
}
