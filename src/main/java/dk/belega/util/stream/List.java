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
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Construct, copy, destroy

    static <T> List<T> nil() {
        return new Nil<>();
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
}
