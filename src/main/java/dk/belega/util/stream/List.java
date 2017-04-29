package dk.belega.util.stream;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Abstraction of an immutable list
 */
public interface List<T> {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Types, constants

    class Nil<T> implements List<T> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Properties

        /**
         * Return {@code true} if the list is nil (empty).
         *
         * @return {@code true} if nil; {@code false} if not
         */
        @Override
        public boolean isNil() {
            return true;
        }

        /**
         * Return the list head, if present.
         *
         * @return list head value or empty
         */
        @Override
        public Optional<T> getHeadOption() {
            return Optional.empty();
        }

        /**
         * Return the head value of the list.
         *
         * @return the head value.
         */
        @Override
        public T getHead() {
            throw new UnsupportedOperationException("Cannot call getHead() on nil list");
        }

        /**
         * Return a new list with the given value as head and the same tail as this list.
         *
         * @param head head value of new list
         * @return new list with given head and existing tail values
         */
        @Override
        public List<T> setHead(T head) {
            throw new UnsupportedOperationException("Cannot call setHead() on nil list");
        }

        /**
         * Return the tail of the list.
         *
         * @return the list tail
         */
        @Override
        public List<T> getTail() {
            return this;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Operations

        /**
         * Indicates whether some other object is "equal to" this one.
         *
         * @param obj the reference object with which to compare.
         * @return {@code true} if this object is the same as the obj
         * argument; {@code false} otherwise.
         * @see #hashCode()
         * @see HashMap
         */
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof List)) {
                return false;
            }
            final List<?> other = (List<?>) obj;
            return other.isNil();
        }

        /**
         * Returns a string representation of the object. In general, the
         * {@code toString} method returns a string that
         * "textually represents" this object. The result should
         * be a concise but informative representation that is easy for a
         * person to read.
         * It is recommended that all subclasses override this method.
         * <p>
         * The {@code toString} method for class {@code Object}
         * returns a string consisting of the name of the class of which the
         * object is an instance, the at-sign character `{@code @}', and
         * the unsigned hexadecimal representation of the hash code of the
         * object. In other words, this method returns a string equal to the
         * value of:
         * <blockquote>
         * <pre>
         * getClass().getName() + '@' + Integer.toHexString(hashCode())
         * </pre></blockquote>
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return "[]";
        }

        /**
         * Drop the first {@code n} elements of this list and return a list of the remaining
         * elements.
         *
         * @param n the number of elements to drop
         * @return a list consisting of all elements of this list, except the first {@code n}
         * elements
         */
        @Override
        public List<T> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("n cannot be negative");
            }
            return this;
        }

        /**
         * Drop the initial elements of this list, as long as they match the given predicate and
         * return the list of elements remaining.
         *
         * @param predicate the predicate
         * @return list of remaining elements
         */
        @Override
        public List<T> dropWhile(Predicate<? super T> predicate) {
            return this;
        }

        /**
         * Return a list containing all elements from this list, followed by all elements of the
         * given list.
         *
         * @param other the list to append to this list
         * @return list containing all elements from both lists
         */
        @Override
        public List<T> append(List<T> other) {
            return other;
        }

        /**
         * Apply a binary operator to all elements of this list and a start value, going from right
         * to left.
         *
         * @param z  the start value
         * @param op the binary operator
         * @return the result of applying the binary operator to consecutive elements in the list and
         * the start value, going from right to left.
         */
        @Override
        public <Z> Z foldRight(Z z, BiFunction<T, Z, Z> op) {
            return z;
        }

        /**
         * Apply a binary operator to consecutive elements of the list, from left to right, and a
         * starting value.
         *
         * @param z  the starting value
         * @param op the binary operator
         * @return the result of applying the binary operator to each element of the list, going from
         * left to right
         */
        @Override
        public <Z> Z foldLeft(Z z, BiFunction<Z, T, Z> op) {
            return z;
        }

        /**
         * Generates a list formed from this list and {@code that} list by combining corresponding
         * elements using a binary operations.  The length of the generated list is the minimum length
         * of this list and {@code that}.
         *
         * @param that   that list
         * @param mapper the binary mapper operation
         * @return list of combined elements
         */
        @Override
        public <B, R> List<R> zipWith(List<B> that, BiFunction<? super T, ? super B, R> mapper) {
            return nil();
        }

        /**
         * Return a list consisting of the first {@code n} elements of this list.
         *
         * @param n the number of elements
         * @return list of initial elements
         */
        @Override
        public List<T> take(long n) {
            return this;
        }

        /**
         * Return the longest prefix of this list where all elements satisfies the given
         * {@code predicate}.
         *
         * @param predicate the predicate
         * @return list of initial elements satifying predicate
         */
        @Override
        public List<T> takeWhile(Predicate<? super T> predicate) {
            return this;
        }

        /**
         * Return {@code true} if all elements of the list match the given {@code predicate}.
         *
         * @param predicate the predicate to use for testing elements
         * @return {@code true} if all elements match; {@code false} if at least one element doesn't
         * match
         */
        @Override
        public boolean forall(Predicate<? super T> predicate) {
            return true;
        }

        /**
         * Return {@code true} if any element of this list satisfies the given {@code predicate}.
         *
         * @param predicate the predicate to use for testing
         * @return {@code true} if any element satisfies predicate
         */
        @Override
        public boolean exists(Predicate<? super T> predicate) {
            return false;
        }
    }

    class Cons<T> implements List<T> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Data

        /**
         * The head value of this list
         */
        private final T head;

        /**
         * The tail of this list
         */
        private final List<T> tail;

        //////////////////////////////////////////////////////////////////////////////////////////
        // Construct, copy, destroy

        Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Properties

        /**
         * Return {@code true} if the list is nil (empty).
         *
         * @return {@code true} if nil; {@code false} if not
         */
        @Override
        public boolean isNil() {
            return false;
        }

        /**
         * Return the list head, if present.
         *
         * @return list head value or empty
         */
        @Override
        public Optional<T> getHeadOption() {
            return Optional.of(head);
        }

        /**
         * Return the head value of the list.
         *
         * @return the head value.
         */
        @Override
        public T getHead() {
            return head;
        }

        /**
         * Return a new list with the given value as head and the same tail as this list.
         *
         * @param head head value of new list
         * @return new list with given head and existing tail values
         */
        @Override
        public List<T> setHead(T head) {
            return cons(head, getTail());
        }

        /**
         * Return the tail of the list.
         *
         * @return the list tail
         */
        @Override
        public List<T> getTail() {
            return tail;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Operations

        /**
         * Indicates whether some other object is "equal to" this one.
         *
         * @param obj the reference object with which to compare.
         * @return {@code true} if this object is the same as the obj
         * argument; {@code false} otherwise.
         * @see #hashCode()
         * @see HashMap
         */
        @Override
        public boolean equals(Object obj) {

            if (!(obj instanceof List)) {
                return false;
            }

            final List<?> other = (List<?>) obj;
            return !other.isNil() &&
                    equal(getHead(), other.getHead()) &&
                    getTail().equals(other.getTail());
        }

        /**
         * Returns a string representation of the object.
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString() {
            return getTail().foldLeft(
                    new StringBuilder("[").append(stringOf(getHead())),
                    (sb, t) -> sb.append(", ").append(stringOf(t)))
                    .append("]").toString();
        }

        /**
         * Drop the first {@code n} elements of this list and return a list of the remaining elements.
         *
         * @param n the number of elements to drop
         * @return a list consisting of all elements of this list, except the first {@code n} elements
         */
        @Override
        public List<T> drop(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("n cannot be negative");
            } else if (0 == n) {
                return this;
            } else {
                return getTail().drop(n - 1);
            }
        }

        /**
         * Drop the initial elements of this list, as long as they match the given predicate and
         * return the list of elements remaining.
         *
         * @param predicate the predicate
         * @return list of remaining elements
         */
        @Override
        public List<T> dropWhile(Predicate<? super T> predicate) {
            if (predicate.test(getHead())) {
                return getTail().dropWhile(predicate);
            } else {
                return this;
            }
        }

        /**
         * Return a list containing all elements from this list, followed by all elements of the
         * given list.
         *
         * @param other the list to append to this list
         * @return list containing all elements from both lists
         */
        @Override
        public List<T> append(List<T> other) {
            if (other.isNil()) {
                return this;
            } else {
                return cons(getHead(), getTail().append(other));
            }
        }

        /**
         * Apply a binary operator to all elements of this list and a start value, going from right
         * to left.
         *
         * @param z  the start value
         * @param op the binary operator
         * @return the result of applying the binary operator to consecutive elements in the list and
         * the start value, going from right to left.
         */
        @Override
        public <Z> Z foldRight(Z z, BiFunction<T, Z, Z> op) {
            return op.apply(getHead(), getTail().foldRight(z, op));
        }

        /**
         * Apply a binary operator to consecutive elements of the list, from left to right, and a
         * starting value.
         *
         * @param z  the starting value
         * @param op the binary operator
         * @return the result of applying the binary operator to each element of the list, going from
         * left to right
         */
        @Override
        public <Z> Z foldLeft(Z z, BiFunction<Z, T, Z> op) {
            return getTail().foldLeft(op.apply(z, getHead()), op);
        }

        /**
         * Generates a list formed from this list and {@code that} list by combining corresponding
         * elements using a binary operations.  The length of the generated list is the minimum length
         * of this list and {@code that}.
         *
         * @param that   that list
         * @param mapper the binary mapper operation
         * @return list of combined elements
         */
        @Override
        public <B, R> List<R> zipWith(List<B> that, BiFunction<? super T, ? super B, R> mapper) {
            return that.isNil() ? nil() :
                    cons(mapper.apply(getHead(), that.getHead()),
                            getTail().zipWith(that.getTail(), mapper));
        }

        /**
         * Return a list consisting of the first {@code n} elements of this list.
         *
         * @param n the number of elements
         * @return list of initial elements
         */
        @Override
        public List<T> take(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n cannot be negative");
            } else if (0 == n) {
                return nil();
            } else {
                final List<T> tail = getTail().take(n - 1);
                if (tail == getTail()) {
                    return this;
                } else {
                    return cons(getHead(), tail);
                }
            }
        }

        /**
         * Return the longest prefix of this list where all elements satisfies the given
         * {@code predicate}.
         *
         * @param predicate the predicate
         * @return list of initial elements satifying predicate
         */
        @Override
        public List<T> takeWhile(Predicate<? super T> predicate) {

            if (predicate.test(getHead())) {
                final List<T> tail = getTail().takeWhile(predicate);
                if (tail == getTail()) {
                    return this;
                } else {
                    return cons(getHead(), tail);
                }
            } else {
                return nil();
            }
        }

        /**
         * Return {@code true} if all elements of the list match the given {@code predicate}.
         *
         * @param predicate the predicate to use for testing elements
         * @return {@code true} if all elements match; {@code false} if at least one element doesn't
         * match
         */
        @Override
        public boolean forall(Predicate<? super T> predicate) {
            return predicate.test(getHead()) && getTail().forall(predicate);
        }

        /**
         * Return {@code true} if any element of this list satisfies the given {@code predicate}.
         *
         * @param predicate the predicate to use for testing
         * @return {@code true} if any element satisfies predicate
         */
        @Override
        public boolean exists(Predicate<? super T> predicate) {
            return predicate.test(getHead()) || getTail().exists(predicate);
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Implementation

        /**
         * Return string representation of given object or "null" if given a {@code null} value.
         *
         * @param obj the object reference
         * @return string representation of object or {@code null}
         */
        private static String stringOf(Object obj) {
            return null == obj ? "null" : obj.toString();
        }

        /**
         * Return true if both values are {@code null} or if equal.
         *
         * @param left  left value to compare
         * @param right right value to compare
         * @return {@code true} if objects are equal or both {@code null}
         */
        private static boolean equal(Object left, Object right) {
            if (null == left) {
                return null == right;
            } else {
                return left.equals(right);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Construct, copy, destroy

    static <T> List<T> nil() {
        return new Nil<>();
    }

    /**
     * Create an immutable list consisting of the given head value and the given tail.
     *
     * @param head the head value
     * @param tail the list tail
     * @param <T>  the value type of the list
     * @return immutable list with the given elements
     */
    static <T> List<T> cons(T head, List<T> tail) {
        return new Cons<>(head, tail);
    }

    /**
     * Create a list with the given value as it's only element.
     *
     * @param unit the unit value
     * @param <T>  the value type of the list
     * @return immutable list with the given value
     */
    static <T> List<T> unit(T unit) {
        return cons(unit, nil());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    /**
     * Return {@code true} if the list is nil (empty).
     *
     * @return {@code true} if nil; {@code false} if not
     */
    boolean isNil();


    /**
     * Return the list head, if present.
     *
     * @return list head value or empty
     */
    Optional<T> getHeadOption();

    /**
     * Return the head value of the list.
     *
     * @return the head value.
     */
    @SuppressWarnings("UnusedReturnValue")
    T getHead();

    /**
     * Return a new list with the given value as head and the same tail as this list.
     *
     * @param head head value of new list
     * @return new list with given head and existing tail values
     */
    List<T> setHead(T head);

    /**
     * Return the tail of the list.
     *
     * @return the list tail
     */
    List<T> getTail();

    /**
     * Return the number of elements in the list.
     *
     * @return number of elements in this list
     */
    default long getLength() {
        long length = 0;
        for (List<T> tList = this; !tList.isNil(); tList = tList.getTail()) {
            length += 1;
        }
        return length;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * Drop the first {@code n} elements of this list and return a list of the remaining elements.
     *
     * @param n the number of elements to drop
     * @return a list consisting of all elements of this list, except the first {@code n} elements
     */
    List<T> drop(int n);

    /**
     * Drop the initial elements of this list, as long as they match the given predicate and
     * return the list of elements remaining.
     *
     * @param predicate the predicate
     * @return list of remaining elements
     */
    List<T> dropWhile(Predicate<? super T> predicate);

    /**
     * Return a list containing all elements from this list, followed by all elements of the
     * given list.
     *
     * @param other the list to append to this list
     * @return list containing all elements from both lists
     */
    List<T> append(List<T> other);

    /**
     * Apply a binary operator to all elements of this list and a start value, going from right
     * to left.
     *
     * @param z   the start value
     * @param op  the binary operator
     * @param <Z> the result type of the binary operator
     * @return the result of applying the binary operator to consecutive elements in the list and
     * the start value, going from right to left.
     */
    <Z> Z foldRight(Z z, BiFunction<T, Z, Z> op);

    /**
     * Apply a binary operator to consecutive elements of the list, from left to right, and a
     * starting value.
     *
     * @param z   the starting value
     * @param op  the binary operator
     * @param <Z> the type of the result of the binary operator
     * @return the result of applying the binary operator to each element of the list, going from
     * left to right
     */
    <Z> Z foldLeft(Z z, BiFunction<Z, T, Z> op);

    /**
     * Return a list with all elements from this list in reverse order.
     *
     * @return list with elements from this list in reverse order
     */
    default List<T> reverse() {
        return foldLeft(nil(), (t, h) -> cons(h, t));
    }

    /**
     * Return a list with the result of applying a function to all elements in this list.
     *
     * @param mapper the mapper function
     * @param <R>    the return type of the function
     * @return list of the result of applying mapper function to all elements of this list
     */
    default <R> List<R> map(Function<T, R> mapper) {
        return foldRight(nil(), (t, l) -> cons(mapper.apply(t), l));
    }

    /**
     * Return a list of all elements of this list that satisfies the given predicate.
     *
     * @param predicate the predicate
     * @return list of satisfying elements
     */
    default List<T> filter(Predicate<? super T> predicate) {
        return foldRight(nil(), (t, l) -> predicate.test(t) ? cons(t, l) : l);
    }

    /**
     * Apply the given mapper function to all elements of this list and return a list containing
     * the combined set of elements of the mapper function results.
     *
     * @param mapper the mapper function
     * @param <R>    the element type of the returned collection
     * @return the combined list of elements from the function's return values
     */
    default <R> List<R> flatMap(Function<T, List<R>> mapper) {
        return foldRight(nil(), (t, l) -> mapper.apply(t).append(l));
    }

    /**
     * Generates a list formed from this list and {@code that} list by combining corresponding
     * elements using a binary operations.  The length of the generated list is the minimum length
     * of this list and {@code that}.
     *
     * @param that   that list
     * @param mapper the binary mapper operation
     * @param <B>    type of elements in that list
     * @param <R>    return value of the mapper operation
     * @return list of combined elements
     */
    <B, R> List<R> zipWith(List<B> that, BiFunction<? super T, ? super B, R> mapper);

    /**
     * Return a list consisting of the first {@code n} elements of this list.
     *
     * @param n the number of elements
     * @return list of initial elements
     */
    List<T> take(long n);

    /**
     * Return the longest prefix of this list where all elements satisfies the given
     * {@code predicate}.
     *
     * @param predicate the predicate
     * @return list of initial elements satifying predicate
     */
    List<T> takeWhile(Predicate<? super T> predicate);

    /**
     * Return {@code true} if all elements of the list match the given {@code predicate}.
     *
     * @param predicate the predicate to use for testing elements
     * @return {@code true} if all elements match; {@code false} if at least one element doesn't
     * match
     */
    boolean forall(Predicate<? super T> predicate);

    /**
     * Return {@code true} if any element of this list satisfies the given {@code predicate}.
     *
     * @param predicate the predicate to use for testing
     * @return {@code true} if any element satisfies predicate
     */
    boolean exists(Predicate<? super T> predicate);

    /**
     * Return a list of cumulative results of applying the given {@code operator} to consecutive
     * elements of this list and a starting value, going from left to right.
     *
     * @param z        the starting value
     * @param operator the operator
     * @param <Z>      the return value of the operator
     * @return list of cumulative results
     */
    default <Z> List<Z> scanLeft(Z z, BiFunction<Z, T, Z> operator) {
        return foldLeft(
                List.unit(z),
                (l, h) -> cons(operator.apply(l.getHead(), h), l));
    }
}
