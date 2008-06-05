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


import net.sf.eos.Function;
import net.sf.eos.Predicate;
import net.sf.eos.Supplier;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompositionsTest {

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void functionCompositionWithNullAsFromEntity() {
        Compositions.compose((Function) null, new Function() {
            public Object apply(Object from) {
                return null;
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void functionCompositionWithNullAsToEntity() {
        Compositions.compose(new Function() {
            public Object apply(Object from) {
                return null;
            }
        }, (Function) null);
    }

    @Test
    public void validFunctionComposition() {
        final Function<String, String> f =
            Compositions.compose(new Function<String, StringBuilder>() {
                public StringBuilder apply(final String from) {
                    return new StringBuilder(from + "-");
                }
            }, new Function<StringBuilder, String>() {
                public String apply(final StringBuilder i) {
                    return i.append("to").toString();
                }
            });
        final String result = f.apply("from");
        assertEquals("from-to", result);
    }


    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void supplierCompositionWithNullProvider() {
        Compositions.compose(new Supplier() {
            public Object get() {
                return null;
            }
        }, (Function) null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void supplierCompositionWithNullFunction() {
        Compositions.compose((Supplier) null,
            new Function() {
                public Object apply(final Object from) {
                    return null;
                }
            }
        );
    }

    @SuppressWarnings("nls")
    @Test
    public void validSupplierComposition() {
        final Supplier<String> p =
            Compositions.compose(
                new Supplier<Integer>() {
                    public Integer get() {
                        return 1;
                    }
                },
                new Function<Integer, String>() {
                    public String apply(final Integer i) {
                        return i.toString();
                    }
                }
            );
        assertEquals("1", p.get());
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void predicateCompositionWithNullFunction() {
        Compositions.compose(new Function() {
                public Object apply(Object from) {
                    return null;
                }
            }, (Predicate) null);
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void preciateCompositionWithNulPredicate() {
        Compositions.compose((Function) null,
            new Predicate() {
                public boolean evaluate(Object t) {
                    return false;
                }
            }
        );
    }

    @SuppressWarnings("nls")
    @Test
    public void validPredicateComposition() {
        final Predicate<String> p =
            Compositions.compose(
                new Function<String, Integer>() {
                    public Integer apply(final String from) {
                        return Integer.valueOf(from);
                    }
                },
                new Predicate<Integer>() {
                    @SuppressWarnings("boxing")
                    public boolean evaluate(final Integer t) {
                        return t == 2;
                    }
                }
            );
        assertTrue(p.evaluate("2"));
    }

    @Test(expected=IllegalArgumentException.class)
    public void composeRuleWithNullPredicate() {
        final Function<Object, Object> dummy = new Function<Object, Object>() {
            public Object apply(final Object from) {
                return from;
            }
        };
        Compositions.composeRule(null, dummy, dummy);
    }

    @Test(expected=IllegalArgumentException.class)
    public void composeRuleWithNullTrueFunction() {
        final Function<Object, Object> fDummy = new Function<Object, Object>() {
            public Object apply(final Object from) {
                return from;
            }
        };
        Compositions.composeRule(truePredicate, null, fDummy);
    }

    @Test(expected=IllegalArgumentException.class)
    public void composeRuleWithNullFalseFunction() {
        final Function<Object, Object> fDummy = new Function<Object, Object>() {
            public Object apply(final Object from) {
                return from;
            }
        };
        Compositions.composeRule(truePredicate, fDummy, null);
    }

    @SuppressWarnings("nls")
    @Test
    public void composeRuleWithTruePredicate() {
        final List<String> trueValue = new ArrayList<String>();
        final List<String> falseValue = new ArrayList<String>();
        final Function<String, Collection<String>> fTrue =
            new Function<String, Collection<String>>() {
                public Collection<String> apply(final String from) {
                    trueValue.add(from);
                    return trueValue;
                }
        };
        final Function<String, Collection<String>> fFalse =
            new Function<String, Collection<String>>() {
                public Collection<String> apply(final String from) {
                    falseValue.add(from);
                    return falseValue;
                }
        };
        final Function <String, Collection<String>> retval =
            Compositions.composeRule(truePredicate, fTrue, fFalse);
        retval.apply("");
        assertEquals(1, trueValue.size());
        assertEquals(0, falseValue.size());
    }

    @SuppressWarnings("nls")
    @Test
    public void composeRuleWithFalsePredicate() {
        final List<String> trueValue = new ArrayList<String>();
        final List<String> falseValue = new ArrayList<String>();
        final Function<String, Collection<String>> fTrue =
            new Function<String, Collection<String>>() {
                public Collection<String> apply(final String from) {
                    trueValue.add(from);
                    return trueValue;
                }
        };
        final Function<String, Collection<String>> fFalse =
            new Function<String, Collection<String>>() {
                public Collection<String> apply(final String from) {
                    falseValue.add(from);
                    return falseValue;
                }
        };
        final Function <String, Collection<String>> retval =
            Compositions.composeRule(falsePredicate, fTrue, fFalse);
        retval.apply("");
        assertEquals(0, trueValue.size());
        assertEquals(1, falseValue.size());
    }

    public final static Predicate<String> truePredicate =
        new Predicate<String>() {
            public boolean evaluate(final String s) {
                return true;
            };
        };

    public final static Predicate<String> falsePredicate =
        new Predicate<String>() {
            public boolean evaluate(final String s) {
                return false;
            };
        };
}
