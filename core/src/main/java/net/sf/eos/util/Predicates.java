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

import static net.sf.eos.util.Conditions.checkContainsNoNull;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.eos.Nullable;
import net.sf.eos.Predicate;


/**
 * Defines useful method for handling with {@link Predicate Predicates}.
 *
 * @author Sascha Kohlmann
 *
 * @since 0.2.0
 */
public final class Predicates {

    /** The logger for the given class. */
    private static final Logger LOG = Logger.getLogger(Predicates.class.getName());

    /**
     * Returns a {@code Predicate} that evaluates all to {@code true} if each
     * {@code predicate} of the <em>predicates</em> evaluates to {@code true}.
     * Also evaluates to {@code true} if <em>predicates</em> is empty.
     *
     * @param <T> the type of the predicates.
     * @param predicates the {@code predicates} to evaluate
     * @return a predicate evaluating all <em>predicates</em>
     * @throws NullPointerException if <em>predicates</em> is {@code null}
     *                              or contains a {@code null}-reference
     * @since 0.8.0
     */
    public static <T> Predicate<T> and(final Iterable<? extends Predicate<? super T>> predicates) {
        return new AndPredicate<T>(predicates);
    }


    /**
     * Returns a {@code Predicate} that evaluates all to {@code true} if each
     * {@code predicate} of the <em>predicates</em> evaluates to {@code true}.
     * Also evaluates to {@code true} if <em>predicates</em> is empty.
     *
     * @param <T> the type of the predicates.
     * @param predicates the {@code predicates} to evaluate
     * @return a predicate evaluating all <em>predicates</em>
     * @throws NullPointerException if <em>predicates</em> is {@code null}
     *                              or contains a {@code null}-reference
     * @since 0.8.0
     */
    public static <T> Predicate<T> and(final Predicate<? super T>... predicates) {
        final List<Predicate<? super T>> list = Arrays.asList(predicates);
        return new AndPredicate<T>(list);
    }

    /**
     * Returns a {@code Predicate} that evaluates all to {@code true} if one
     * {@code predicate} of the <em>predicates</em> evaluates to {@code true}.
     * Also evaluates to {@code true} if <em>predicates</em> is empty.
     *
     * @param <T> the type of the predicates.
     * @param predicates the {@code predicates} to evaluate
     * @return a predicate evaluating all <em>predicates</em>
     * @throws NullPointerException if <em>predicates</em> is {@code null}
     *                              or contains a {@code null}-reference
     * @since 0.8.0
     */
    public static <T> Predicate<T> or(final Iterable<? extends Predicate<? super T>> predicates) {
        return new OrPredicate<T>(predicates);
    }

    /**
     * Returns a {@code Predicate} that evaluates all to {@code true} if one
     * {@code predicate} of the <em>predicates</em> evaluates to {@code true}.
     * Also evaluates to {@code true} if <em>predicates</em> is empty.
     *
     * @param <T> the type of the predicates.
     * @param predicates the {@code predicates} to evaluate
     * @return a predicate evaluating all <em>predicates</em>
     * @throws NullPointerException if <em>predicates</em> is {@code null}
     *                              or contains a {@code null}-reference
     * @since 0.8.0
     */
    public static <T> Predicate<T> or(final Predicate<? super T>... predicates) {
        final List<Predicate<? super T>> list = Arrays.asList(predicates);
        return new OrPredicate<T>(list);
    }

    /**
     * A {@link Predicate} which always evaluates to {@code true} regardless of the provided input.
     * {@code null} is a valid input parameter for the {@link Predicate#evaluate(Object)} method.
     *
     * @param <T> the generic type of the predicate.
     * @since 0.9.0
     */
    @SuppressWarnings("unchecked")
    public static class AlwaysTruePredicate<T> implements Predicate<T>  {
        /** {@inheritDoc} */
        public boolean evaluate(@Nullable final T t) {
            return true;
        }
    }


    /** @see #and(Iterable) */
    static final class AndPredicate<T> implements Predicate<T> {

        private final Iterable<? extends Predicate<? super T>> predicates;

        /**
         * Creates a new instance.
         * @param predicates the predicates to evaluate
         * @throws NullPointerException if <em>predicates</em> is {@code null}
         *                              or contains a {@code null}-reference
         */
        public AndPredicate(
                @SuppressWarnings("hiding") Iterable<? extends Predicate<? super T>> predicates) {
            this.predicates = checkContainsNoNull(predicates);
        }

        /**
         * Evaluates all given <em>predicates</em>.
         * @param t the value to test
         * @return see {@link Predicates#and(Iterable)}
         */
        public boolean evaluate(final T t) {
            for (Predicate<? super T> predicate : this.predicates) {
                if (! predicate.evaluate(t)) {
                    return false;
                }
            }
            return true;
        }
    }

    /** @see #or(Iterable) */
    static final class OrPredicate<T> implements Predicate<T> {

        private final Iterable<? extends Predicate<? super T>> predicates;

        /**
         * Creates a new instance.
         * @param predicates the predicates to evaluate
         * @throws NullPointerException if <em>predicates</em> is {@code null}
         *                              or contains a {@code null}-reference
         */
        public OrPredicate(
                @SuppressWarnings("hiding") Iterable<? extends Predicate<? super T>> predicates) {
            this.predicates = checkContainsNoNull(predicates);
        }

        /**
         * Evaluates all given <em>predicates</em>.
         * @param t the value to test
         * @return see {@link #or(Iterable)}
         */
        public boolean evaluate(final T t) {
            boolean oneOrMore = false;
            for (Predicate<? super T> predicate : this.predicates) {
                oneOrMore = true;
                if (predicate.evaluate(t)) {
                    return true;
                }
            }
            if (! oneOrMore) {
                return true;
            }
            return false;
        }
    }

    public static <T> Predicate<T> not(final Predicate<? super T> predicate) {
        if (LOG.isLoggable(Level.FINER)) {
            LOG.warning("use package private method");
        }
        return new NotPredicate<T>(predicate);
    }

    static final class NotPredicate<T> implements Predicate<T> {
        private final Predicate<? super T> p;
        public NotPredicate(final Predicate<? super T> p) {
            this.p = Conditions.checkArgumentNotNull(p);
        }
        public boolean evaluate(final T t) {
            return ! this.p.evaluate(t);
        }
    }


    /** Never used. */
    @SuppressWarnings("nls")
    private Predicates() {
        throw new AssertionError("never call direct");
    }
}
