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
package net.sf.eos;

/**
 * Instances supply objects of a single type.
 *
 * <p><strong>Note:</strong> experimental - inspired by <em>guice</em></p>
 *
 * @author Sascha Kohlmann
 * @since 0.1.0
 * @param <T> the provided type to supply
 */
@Experimental
public interface Supplier<T> {

    /**
     * Returns an instance of the expected type. The returned type may or
     * may not be a new instance, depending on the implementation.
     * @return an instance of the expected type
     */
    T get();
}
