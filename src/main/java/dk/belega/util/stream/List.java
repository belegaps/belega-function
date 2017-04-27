package dk.belega.util.stream;

import java.util.Optional;

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
}
