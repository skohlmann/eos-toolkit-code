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
 */
package net.sf.eos.util;

import net.sf.eos.Nullable;


/**
 * A class to store a pair of generic types. Use with care as keys in {@code maps}.
 *
 * @param <F> the type of the first value
 * @param <S> the type of the second value
 *
 * @since 0.1.0
 * @author Sascha Kohlmann
 */
public final class Pair<F, S> {

    /** The first value of the pair. */
    private final F first;

    /** The second value of the pair. */
    private final S second;

    /**
     * Creates a new pair.
     * @param first the first value of this pair
     * @param second the second value of this pair
     *
     */
    public Pair(@SuppressWarnings("hiding") @Nullable final F first,
                @SuppressWarnings("hiding") @Nullable final S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first value of the pair.
     * @return the first value of the pair
     */
    public F getFirst() {
        return this.first;
    }

    /**
     * Returns the second value of the pair.
     * @return the second value of the pair
     */
    public S getSecond() {
        return this.second;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = EqualsAndHashUtil.hash(this.first);
        return hash * EqualsAndHashUtil.hash(this.second);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }

        if (other == null) {
            return false;
        }

        if (!(this.getClass().equals(other.getClass()))) {
            return false;
        }

        final Pair<? super F, ? extends F> pair = (Pair<? super F, ? extends F>) other;
        return (EqualsAndHashUtil.isEqual(this.first, pair.first)
                && EqualsAndHashUtil.isEqual(this.second, pair.second));
    }
}
