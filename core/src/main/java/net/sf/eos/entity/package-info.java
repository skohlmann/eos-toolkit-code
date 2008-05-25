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
 * <p>Support for named entity recognizer.</p>
 * <p>At this time &#949;&#959;s supports only dictionary based named entity
 *  recognition. To create an instance use the factory method in
 *  {@link net.sf.eos.entity.AbstractDictionaryBasedEntityRecognizer}. The default
 *  implementation is
 *  {@link net.sf.eos.entity.SimpleLongestMatchDictionaryBasedEntityRecognizer}
 *  which supports a longest match named entity recognition.</p>
 *
 * @since 0.1.0
 * @author Sascha Kohlmann
 */
package net.sf.eos.entity;
