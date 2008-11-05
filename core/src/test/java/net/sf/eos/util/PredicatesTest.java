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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static net.sf.eos.util.CompositionsTest.truePredicate;
import static net.sf.eos.util.CompositionsTest.falsePredicate;

import net.sf.eos.util.Predicates.AlwaysTruePredicate;
import net.sf.eos.util.Predicates.AndPredicate;
import net.sf.eos.util.Predicates.OrPredicate;

import net.sf.eos.Predicate;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PredicatesTest {


    @SuppressWarnings({ "unchecked", "nls" })
    @Test
    public void simpleAndTestWithTwoTruePredicates() {
        final Predicate<String>[] predicates =
            new Predicate[] {truePredicate, truePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> predicate = new AndPredicate<String>(asList);
        assertTrue(predicate.evaluate(""));
    }

    @SuppressWarnings({ "unchecked", "nls" })
    @Test
    public void simpleAndTestWithTwoTruePredicatesOneReturnsFalse() {
        final Predicate<String>[] predicates =
            new Predicate[] {truePredicate, falsePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> predicate = new AndPredicate<String>(asList);
        assertFalse(predicate.evaluate(""));
    }


    @SuppressWarnings({ "unchecked", "nls" })
    @Test
    public void simpleOrTestWithTwoTruePredicates() {
        final Predicate<String>[] predicates =
            new Predicate[] {truePredicate, truePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> predicate = new OrPredicate<String>(asList);
        assertTrue(predicate.evaluate(""));
    }

    @SuppressWarnings({ "unchecked", "nls" })
    @Test(expected=NullPointerException.class)
    public void simpleOrTestWithTwoPredicatesOneIsNull() {
        final Predicate<String>[] predicates =
            new Predicate[] {truePredicate, null};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        new OrPredicate<String>(asList);
    }

    @SuppressWarnings({ "unchecked", "nls" })
    @Test(expected=NullPointerException.class)
    public void simpleOrTestNull() {
        new OrPredicate<String>(null);
    }


    @SuppressWarnings({ "unchecked", "nls" })
    @Test(expected=NullPointerException.class)
    public void simpleAndTestWithTwoPredicatesOneIsNull() {
        final Predicate<String>[] predicates =
            new Predicate[] {truePredicate, null};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        new AndPredicate<String>(asList);
    }

    @SuppressWarnings({ "unchecked", "nls" })
    @Test(expected=NullPointerException.class)
    public void simpleAndTestNull() {
        new AndPredicate<String>(null);
    }

    @SuppressWarnings({ "unchecked", "nls" })
    @Test
    public void simpleOrTestWithTwoTruePredicatesOneReturnsFalse() {
        final Predicate<String>[] predicates =
            new Predicate[] {truePredicate, falsePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> predicate = new OrPredicate<String>(asList);
        assertTrue(predicate.evaluate(""));
    }

    @SuppressWarnings({ "unchecked", "nls" })
    @Test
    public void simpleOrTestWithTwoTruePredicatesAllReturnsFalse() {
        final Predicate<String>[] predicates =
            new Predicate[] {falsePredicate, falsePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> predicate = new OrPredicate<String>(asList);
        assertFalse(predicate.evaluate(""));
    }

    @Test
    public void andTestWithIterable() {
        final Predicate<String>[] predicates = new Predicate[] {truePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> and = Predicates.and(asList);
        assertTrue(and.evaluate(""));
    }

    @Test
    public void andTestWithParameterList() {
        final Predicate<String> and = Predicates.and(truePredicate, truePredicate);
        assertTrue(and.evaluate(""));
    }

    @Test
    public void orTestWithIterable() {
        final Predicate<String>[] predicates = new Predicate[] {truePredicate};
        final List<Predicate<String>> asList = Arrays.asList(predicates);
        final Predicate<String> or = Predicates.or(asList);
        assertTrue(or.evaluate(""));
    }

    @Test
    public void orTestWithParameterList() {
        final Predicate<String> or = Predicates.or(truePredicate, falsePredicate);
        assertTrue(or.evaluate(""));
    }

    @Test
    public void orWithEmptyParameterList() {
        final Predicate<String> or = Predicates.or();
        assertTrue(or.evaluate(""));
    }

    @Test
    public void andWithEmptyParameterList() {
        final Predicate<String> and = Predicates.and();
        assertTrue(and.evaluate(""));

        Class<? extends Predicate<String>> class1 =
            (Class<? extends Predicate<String>>) new Predicates.AlwaysTruePredicate<String>().getClass();
    }

    @Test
    public void not() {
        final Predicate<String> truePredicate = new Predicate<String>() {
            public boolean evaluate(final String t) {
                return true;
            }
        };
        final Predicate<String> not = Predicates.not(truePredicate);
        assertFalse(not.evaluate(""));
    }

    @Test
    public void notThrowsIllegalArgumentException() {
        try {
            Predicates.not(null);
            fail("Oops... Missing IllegalArgumentException");
        } catch (final IllegalArgumentException e) {}
    }

    @Test
    public void alwaysTrue() {
        assertTrue(new AlwaysTruePredicate().evaluate(""));
    }


    @Test
    public void alwaysTrueWithNull() {
        assertTrue(new AlwaysTruePredicate().evaluate(null));
    }
}

