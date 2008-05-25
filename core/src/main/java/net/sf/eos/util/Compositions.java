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
import net.sf.eos.Supplier;
import static net.sf.eos.util.Conditions.checkArgumentNotNull;

/**
 * Defines some useful compositions for handling with {@link Function Functions},
 * {@link Predicate Predicates} and {@link Supplier Providers}.
 *
 * <p>Each {@code composition} follows the rule of a {@code from} or <em>source</em>
 * entity, propagating its result value to a {@code to} or <em>target</em> entity.</p>
 *
 * @author Sascha Kohlmann
 */
@Experimental
public class Compositions {

    /**
     * Returns the composition of two functions. For {@code f: F→I} and
     * {@code g: I→T}, composition is defined as the function {@code h}
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

    /**
     * Returns a composition of a <em>from</em> {@code Function} and an evaluating
     * <em>to</em> {@code Predicate}. The {@code predicate} retrieves the value from
     * the {@code function}.
     *
     * <p>The usage interface is designed as a {@code from} function which
     * propagates its result to the {@code to} predicate.</p>
     *
     * @param <F> the {@code from} or <em>source</em> type
     * @param <T> the {@code to} or <em>target</em> type
     * @param fromFunction the {@code from} or <em>source</em> {@code function}
     * @param toPredicate the {@code to} or <em>target</em> {@code predicate}
     * @return a {@code predicate}
     * @throws IllegalArgumentException if one parameter is {@code null}
     * @since 0.7.0
     */
    public final static <F, T> Predicate<F> compose(
            final Function<? super F, ? extends T> fromFunction,
            final Predicate<T> toPredicate) {
        return new FunctionPredicateComposition<F, T>(fromFunction, toPredicate);
    }

    /** @see #compose(Function, Predicate) */
    private final static class FunctionPredicateComposition<F, T>
            implements Predicate<F> {

        private final Predicate<? super T> to;
        private final Function<? super F, ? extends T> from;

        /**
         * Constructs the composition of the given {@code Function} and
         * {@code Predicate}.
         *
         * @param fromFunction the inner {@link Function}. Must not be {@code null}.
         * @param toPredicate the outer {@link Predicate}. Must not be {@code null}.
         */
        @SuppressWarnings("nls")
        public FunctionPredicateComposition(
                final Function<? super F, ? extends T> fromFunction,
                final Predicate<T> toPredicate) {
          this.from = checkArgumentNotNull(fromFunction, "fromFunction is null");
          this.to = checkArgumentNotNull(toPredicate, "toPredicate is null");
        }

        /** {@inheritDoc} */
        public boolean evaluate(final F source) {
            return this.to.evaluate(this.from.apply(source));
        }
    }
    
    /**
     * Returns a composition of a <em>from</em> {@code Provider} and an applied
     * <em>to</em> {@code Function}. The {@code function} retrieves the value from
     * the {@code supplier}.
     *
     * <p>The usage interface is designed as a {@code from} supplier which
     * propagates its result to the {@code to} function.</p>
     *
     * @param <F> the {@code from} or <em>source</em> type
     * @param <T> the {@code to} or <em>target</em> type
     * @param fromSupplier the {@code from} or <em>source</em> {@code supplier}
     * @param toFunction the {@code to} or <em>target</em> {@code function}
     * @return a {@code supplier} with the value from {@code function}
     * @throws IllegalArgumentException if one parameter is {@code null}
     * @since 0.7.0
     */
    public final static <F, T> Supplier<T> compose(
            final Supplier<? extends F> fromSupplier,
            final Function<F, T> toFunction) {
        return new SupplierFunctionComposition<F, T>(fromSupplier, toFunction);
    }

    /** @see #compose(Supplier, Function) */
    private final static class SupplierFunctionComposition<F, T>
            implements Supplier<T> {

        private final Supplier<? extends F> from;
        private final Function<? super F, ? extends T> to;

        /**
         * Constructs the composition of the given {@code Supplier} and
         * {@code Function}.
         *
         * @param fromSupplier the inner {@link Supplier}. Must not be {@code null}.
         * @param toFunction the outer {@link Function}. Must not be {@code null}.
         */
        @SuppressWarnings("nls")
        public SupplierFunctionComposition(final Supplier<? extends F> fromSupplier,
                                           final Function<F, T> toFunction) {
          this.from = checkArgumentNotNull(fromSupplier, "fromSupplier is null");
          this.to = checkArgumentNotNull(toFunction, "toFunction is null");
        }

        /** {@inheritDoc} */
        public T get() {
          return this.to.apply(this.from.get());
        }
    }


    /** Never used. */
    private Compositions() { }
}
