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

import net.sf.eos.analyzer.TextBuilder;

import java.util.Map;
import java.util.Set;

/**
 * The <code>DictionaryBasedEntityRecognizer</code> uses a {@link Map} to
 * recognize entites in a text. An entity is identified thru the 
 * {@link net.sf.eos.analyzer.Token#getType()} {@link #ENTITY_TYPE}. The ID
 * comming in the map ist stored in the meta data with the key
 * {@link #ENTITY_ID_KEY}.
 * @author Sascha Kohlmann
 */
public interface DictionaryBasedEntityRecognizer extends EntityRecognizer {

    /** The default length of token to check against the entries in the map. */
    public final static int DEFAULT_MAX_TOKEN = 5;

    /** ID meta key. */
    @SuppressWarnings("nls")
    public final static String ENTITY_ID_KEY = 
        DictionaryBasedEntityRecognizer.class.getName() + "." + ENTITY_TYPE;

    /** Configuration key for the maximum token. The value of the key must
     * follow the rules of an Java integer. */
    @SuppressWarnings("nls")
    public final static String MAX_TOKEN_CONFIG_NAME = 
        "net.sf.eos.entity.DictionaryBasedEntityRecognizer.maxToken";

    /**
     * Set the entity map.
     * @param entities the entity map
     * @see net.sf.eos.trie.Trie
     */
    void setEntityMap(final Map<CharSequence, Set<CharSequence>> entities);

    /**
     * Return the entity map.
     * @return the entity map. May be <code>null</code>
     */
    Map<CharSequence, Set<CharSequence>> getEntityMap();

    /**
     * Sets a builder. The implementation has default builder of instance
     * {@link TextBuilder#SPACE_BUILDER} setted at construction time.
     * @param builder a builder to set or <code>null</code>
     */
    void setTextBuilder(final TextBuilder builder);

    /**
     * Returns a setted builder.
     * @return a setted builder or <code>null</code>.
     */
    TextBuilder getTextBuilder();

    /**
     * @return the maxToken
     */
    int getMaxToken();

    /**
     * @param maxToken the maxToken to set
     * @throws IllegalArgumentException if and only if 
     *                                  <code><em>token</em> &gt; 1</code>
     */
    void setMaxToken(final int maxToken);
}
