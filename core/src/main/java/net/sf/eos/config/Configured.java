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
package net.sf.eos.config;

/**
 * Support class for configurable objects.
 * @author Sascha Kohlmann
 */
public abstract class Configured implements Configurable {

    private Configuration config;

    /** Construct a Configured. */
    public Configured() {
        super();
    }

    /**
     * Creates a copy of the given {@code Configuration} and stores it in
     * a manner that {@link #getConfiguration()} can reach it.
     * <p>If override the method first call {@code super} in the
     * overriding method.</p>
     * @param config the {@code Configuration} to copy.
     */
    public void configure(
            @SuppressWarnings("hiding") final Configuration config) {
        this.config = new Configuration(config);
    }

    /**
     * Returns the configuration.
     * @return the configuration holder or {@code null}
     */
    protected final Configuration getConfiguration() {
        return this.config;
    }
}
