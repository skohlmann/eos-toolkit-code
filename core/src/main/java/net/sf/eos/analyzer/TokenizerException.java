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

import net.sf.eos.EosException;

/**
 * Will throw if something goes wrong during tokenization.
 * @author Sascha Kohlmann
 */
public class TokenizerException extends EosException {

    private static final long serialVersionUID = -2130967708024026767L;

    /** Creates a new instance. */
    public TokenizerException() {
        super();
    }

    /** Creates a new instance.
     * @param message a message. */
    public TokenizerException(final String message) {
        super(message);
    }

    /** Creates a new instance.
     * @param cause a cause exception. */
    public TokenizerException(final Throwable cause) {
        super(cause);
    }

    /** Creates a new instance.
     * @param cause a cause exception.
     * @param message a message. */
    public TokenizerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
