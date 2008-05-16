package net.sf.eos;

/**
 * <p>Represents a {@code Predicate}, that is, a function with
 * one argument returning a {@code boolean} value. It not extends
 * function since implementing this would entail
 * changing the signature of the {@link Function#apply} method
 * to return a {@link Boolean} instead of a {@code boolean}, which
 * would in turn allow people to return {@code null} from their
 * predicate, which would in turn enable code that looks like
 * {@code if (myPredicate.apply(myObject)) ... } to throw a
 * {@link NullPointerException}.<p>
 *
 * <p>strong>Note:</strong> experimental</p>
 *
 * @author Sascha Kohlmann
 * @since 0.1.0
 * @param <T> the value type
 */
public interface Predicate<T> {

   /**
    * Evaluates this predicate for the given value.
    *
    * @param value a value to be evaluated; should not be changed.
    *
    * @return a boolean value.
    */
   boolean evaluate(@Nullable T value);

   /**
    * Indicates whether some other object is equal to this {@code Predicate}.
    * This method can return {@code true} <em>only</em> if the specified object
    * is also a {@code Predicate} and, for every input object {@code o}, it
    * returns exactly the same value.  Thus,
    * {@code predicate1.equals(predicate2)} implies that either
    * {@code predicate1.apply(o)} and {@code predicate2.apply(o)} are both
    * {@code true}, or both {@code false}.
    *
    * <p>Note that it is <em>always</em> safe <em>not</em> to override
    * {@code Object.equals(Object)}.
    */
   boolean equals(Object obj);
}
