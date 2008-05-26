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
package net.sf.eos.util.functions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import net.sf.eos.Experimental;
import net.sf.eos.Function;
import net.sf.eos.Nullable;
import net.sf.eos.config.ConfigurationKey;

import net.sf.eos.util.functions.ConfigurationKeyIntrospectorFunction.ConfigurationKeySupport;

/**
 * Extracts a {@link ConfigurationKey} from a given type with a specified value.
 * The implementation only intrspects field which are {@code public}, {@code final}
 * and {@code static}. Otherwise the key will be ignored. Use this function with
 * {@link DefaultValueConfigurationKeyFunction} to get the default value
 * of a {@code ConfigurationKey}.
 * @author Sascha Kohlmann
 * @since 0.1.0
 * @version 0.1.0
 */
@Experimental
public class ConfigurationKeyIntrospectorFunction
        implements Function<ConfigurationKeySupport, ConfigurationKey> {

    /** for logging */
    static final Log LOG = LogFactory.getLog(ConfigurationKeyIntrospectorFunction.class.getName());

    /**
     * Simple holder for reflection data.
     * @author Sascha Kohlmann
     * @since 0.1.0
     */
    public static class ConfigurationKeySupport {

        /** The class to introspect. */
        final Class<?> clazz;
        /** The value of the field. */
        final String configKeyValue;

        /**
         * Creates a new instance.
         * @param clazz the {@code class} to introspect
         * @param configKeyValue the value of the configuration key field
         */
        public ConfigurationKeySupport(
                @SuppressWarnings("hiding") @Nullable final Class<?> clazz,
                @SuppressWarnings("hiding") @Nullable final String configKeyValue) {
            this.clazz = clazz;
            this.configKeyValue = configKeyValue;
        }

        /**
         * Returns the class to refelect.
         * @return the class to refelect. May be {@code null}
         */
        public Class<?> getConfigKeyHolderClass() {
            return this.clazz;
        }

        /**
         * Returns the value of the configuration key field.
         * @return the value of the configuration key field. May be {@code null}
         */
        public String getConfigKeyValue() {
            return this.configKeyValue;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("nls")
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ConfigurationKeySupport[configKeyHolderClass=");
            sb.append(this.getConfigKeyHolderClass());
            sb.append("|configKeyValue=");
            sb.append(this.getConfigKeyValue());
            sb.append("]");

            return sb.toString();
        }
    }

    /**
     * Extracts the {@code ConfigurationKey} from the given {@code class}
     * with the specified value of the key.
     * @param from the instance holds the data to refelect the
     *             {@code ConfigurationKey}
     * @return a {@code ConfigurationKey} if the function is able to reflect
     *         it. Otherwise {@code null}.
     */
    @SuppressWarnings("nls")
    public ConfigurationKey apply(@Nullable ConfigurationKeySupport from) {
        if (from == null
                || from.getConfigKeyHolderClass() == null
                || from.getConfigKeyValue() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("from value or values of from are null: " + from);
            }
            return null;
        }

        ConfigurationKey retval = null;

        final Field[] fields = from.getConfigKeyHolderClass().getFields();
        for (final Field field : fields) {

            final ConfigurationKey fieldKey =
                field.getAnnotation(ConfigurationKey.class);

            if (fieldKey != null) {
                final int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)
                        && Modifier.isFinal(modifiers)
                        && Modifier.isPublic(modifiers)) {
                    try {
                        final Object o = field.get(null);
                        if (from.getConfigKeyValue().equals(o)) {
                            retval = fieldKey;
                            // Found, nothing elese todo.
                            break;
                        }
                    } catch (final IllegalAccessException e) {
                        // Never happen causse rules for this block demands
                        // for 'public final static'
                    }
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            if (retval != null) {
                LOG.debug("found configuration key " + retval + " for " + from);
            } else {
                LOG.debug("No configuration key found for " + from);
            }
        }

        return retval;
    }
}
