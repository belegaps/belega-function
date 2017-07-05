package dk.belega.util.function;

import java.util.*;

/**
 * Function interface for traversable structures.
 * @param <T> type of traversable elements
 */
public interface Traversable<T> extends Iterable<T> {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Types, constants

    class TraversableIterator<T> implements Iterator<T> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Data

        private Traversable<T> traversable;

        //////////////////////////////////////////////////////////////////////////////////////////
        // Construct, copy, destroy

        private TraversableIterator(Traversable<T> traversable) {
            this.traversable = traversable;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Operations

        @Override
        public boolean hasNext() {
            return !traversable.isNil();
        }

        @Override
        public T next() {
            if (traversable.isNil())
                throw new NoSuchElementException("next() called with no more elements");

            final T result = traversable.getHead();
            traversable = traversable.getTail();
            return result;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    /**
     * Return {@code true} if the traversable structure is nil (empty).
     *
     * @return {@code true} if nil; {@code false} if not
     */
    boolean isNil();

    /**
     * Return the head element of the structure.
     *
     * @return head element
     * @throws UnsupportedOperationException if the structure is nil
     */
    T getHead();

    /**
     * Return the structure's head element, if present.
     *
     * @return head element or empty
     */
    Optional<T> getHeadOption();

    /**
     * Return a traversable structure that contains all elements but the first of this element.
     *
     * @return the structure's tail
     */
    Traversable<T> getTail();

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Operations

    @Override
    default Iterator<T> iterator() {
        return new TraversableIterator<>(this);
    }
}
