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
package net.sf.eos.util;

import net.sf.eos.Experimental;
import net.sf.eos.Function;
import net.sf.eos.Predicate;
import net.sf.eos.Provider;
import static net.sf.eos.util.Conditions.checkArgumentNotNull;

/**
 * Defines some useful compositions for handling with {@link Function Functions},
 * {@link Predicate Predicates} and {@link Provider Providers}.
 *
 * <p>Each {@code composition} follows the rule of a {@code from} or <em>source</em>
 * entity, propagating its result value to a {@code to} or <em>target</em> entity.</p>
 *
 * @author Sascha Kohlmann
 */
@Experimental
public class Compositions {

    /**
     * Returns the composition of two functions. For {@code f: F&#8594;I} and
     * {@code g: I&#8594;T}, composition is defined as the function {@code h}
     * such that {@code h(x) == g(f(x))} for each {@code x}.
     *
     * <p>The usage interface is designed as a {@code from} function which
     * propagates its result to the {@code to} function.</p>
     *
     * @param <F> the {@code from} or <em>source</em> type
     * @param <I> the intermediate type
     * @param <T> the {@code to} or <em>target</em> type
     * @param from the {@code from} or <em>source</em> {@code function}
     * @param to the {@code to} or <em>target</em> {@code function}
     * @throws IllegalArgumentException if one parameter is {@code null}
     * @since 0.1.0
     */
    public final static <F, I, T> Function<F, T> compose(
            final Function<? super F, ? extends I> from,
            final Function<? super I, ? extends T> to) {
        return new FunctionComposition<F, I, T>(from, to);
    }

    /**
     * @see #compose(Function, Function)
     */
    private final static class FunctionComposition<F, I, T> implements Function<F, T> {

        private final Function<? super F, ? extends I> from;
        private final Function<? super I, ? extends T> to;
        
        public FunctionComposition(final Function<? super F, ? extends I> from,
                                   final Function<? super I, ? extends T> to) {
            this.from = checkArgumentNotNull(from, "from is null");
            this.to = checkArgumentNotNull(to, "to is null");
        }
        public T apply(final F source) {
              return to.apply(from.apply(source));
            }
    }

    /** Never used. */
    private Compositions() { }
}
