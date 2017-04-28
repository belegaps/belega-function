package dk.belega.util.stream;

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
     * @param z the starting value
     * @param op the binary operator
     * @param <Z> the type of the result of the binary operator
     * @return the result of applying the binary operator to each element of the list, going from
     * left to right
     */
    <Z> Z foldLeft(Z z, BiFunction<Z, T, Z> op);

    /**
     * Return a list with all elements from this list in reverse order.
     * @return list with elements from this list in reverse order
     */
    default List<T> reverse() {
        return foldLeft(nil(), (t,h) -> cons(h, t));
    }

    /**
     * Return a list with the result of applying a function to all elements in this list.
     * @param mapper the mapper function
     * @param <R> the return type of the function
     * @return list of the result of applying mapper function to all elements of this list
     */
    default <R> List<R> map(Function<T,R> mapper) {
        return foldRight(nil(), (t,l) -> cons(mapper.apply(t), l));
    }
}
