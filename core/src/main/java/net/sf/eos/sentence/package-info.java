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


/**
 * <p>It is essential to eleminate double sentences in a corpus with respect
 *  to additional metadata. The additional metadata are not supported by the
 *  implementation of this package. The
 *  {@link net.sf.eos.sentence.Sentencer} supports a
 *  {@link net.sf.eos.sentence.Sentencer#newInstance(net.sf.eos.config.Configuration)}
 *  factory method. It returns a 
 *  {@link net.sf.eos.sentence.DefaultSentencer} if no other
 *  configuration is set.</p>
 *
 * @since 0.1.0
 * @author Sascha Kohlmann
 */
package net.sf.eos.sentence;
