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

import net.sf.eos.Experimental;

/**
 * Thrown if it is not possible to inject a resource.
 * @author Sascha Kohlmann
 * @since 0.2.0
 */
@Experimental
public class InjectionException extends RuntimeException {

	private static final long serialVersionUID = 6750305815007157490L;

    /** Creates a new empty exception. */
    public InjectionException() {
        super();
    }

    /** Creates a new exception with a message explaining the exception in more detail.
     * @param message a detailed message of the type of the exception */
    public InjectionException(final String message) {
        super(message);
    }

    /** Creates a new exception with a caused exception.
     * @param cause the caused exception */
    public InjectionException(final Throwable cause) {
        super(cause);
    }

    /** Creates a new exception with a message explaining the exception in more detail and a
     * caused exception.
     * @param message a detailed message of the type of the exception
     * @param cause the caused exception */
    public InjectionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
