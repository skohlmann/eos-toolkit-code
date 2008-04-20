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
package net.sf.eos.lucene;

import java.util.List;
import java.util.Map;

/**
 * Bean holder for lookup data.
 * @author Sascha Kohlmann
 */
public class LookupEntry {

    private String id;
    private String commonName;
    private float relevance;

    private Map<String, List<String>> meta;

    /**
     * Returns the ID of the entry.
     * @return the ID of the entry
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the common name of a named entity.
     * @return the common name of a named entity
     */
    public String getCommonName() {
        return this.commonName;
    }

    /**
     * Returns the relevance of the entry.
     * @return the relevance of the entry
     */
    public float getRelevance() {
        return this.relevance;
    }

    /**
     * Returns the metadata of an entry.
     * @return the metadata of an entry.
     */
    public Map<String, List<String>> getMeta() {
        return this.meta;
    }

    /**
     * Sets the ID of the entry.
     * @param id the ID of the entry
     */
    public void setId(@SuppressWarnings("hiding") final String id) {
        this.id = id;
    }

    /**
     * Sets the common name of the entry.
     * @param commonName the common name of the entry
     */
    public void setCommonName(@SuppressWarnings("hiding")
            final String commonName) {
        this.commonName = commonName;
    }

    /**
     * Sets the relevance of the entry.
     * @param relevance the relevane of the entry
     */
    public void setRelevance(@SuppressWarnings("hiding")
            final float relevance) {
        this.relevance = relevance;
    }

    /**
     * Sets the metadata of the entry.
     * @param meta the metadata of the entry
     */
    public void setMeta(@SuppressWarnings("hiding")
            final Map<String, List<String>> meta) {
        this.meta = meta;
    }
}
