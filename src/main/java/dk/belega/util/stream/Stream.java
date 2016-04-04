package dk.belega.util.stream;

import dk.belega.util.function.Lazy;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Abstraction of a value stream.
 */
@SuppressWarnings("WeakerAccess")
public interface Stream<T> {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Types, constants

    /**
     * Implementation of the {@link Stream} interface for an empty stream.
     *
     * @param <T> the stream value type
     */
    class Nil<T> implements Stream<T> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Properties

        @Override
        public boolean isNil() {
            return true;
        }

        @Override
        public T getHead() {
            throw new UnsupportedOperationException("Cannot call getHead() on nil list");
        }

        @Override
        public Stream<T> getTail() {
            throw new UnsupportedOperationException("Cannot call getTail() on nil list");
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Operations

        /**
         * Map the values of this stream using the given mapping function and return it as a
         * stream.
         *
         * @param mapper the mapping function
         * @param <R> the mapped type
         * @return the mapped stream
         */
        @Override
        public <R> Stream<R> map(Function<T, R> mapper) {
            return nil();
        }
    }

    /**
     * An implementation of the {@link Stream} interface for a non-empty stream.
     *
     * @param <T> the stream value type
     */
    class Cons<T> implements Stream<T> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Data

        private final Lazy<T> head;

        private final Lazy<Stream<T>> tail;

        //////////////////////////////////////////////////////////////////////////////////////////
        // Construct, copy, destroy

        Cons(Supplier<? extends T> head, Supplier<Stream<T>> tail) {
            this.head = Lazy.of(head);
            this.tail = Lazy.of(tail);
        }

        Cons(T head, Stream<T> tail) {
            this.head = Lazy.unit(head);
            this.tail = Lazy.unit(tail);
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Properties

        @Override
        public boolean isNil() {
            return false;
        }

        @Override
        public T getHead() {
            return head.get();
        }

        @Override
        public Stream<T> getTail() {
            return tail.get();
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Operations

        /**
         * Map the values of this stream using the given mapping function and return it as a
         * stream.
         *
         * @param mapper the mapping function
         * @param <R> the mapped type
         * @return the mapped stream
         */
        @Override
        public <R> Stream<R> map(Function<T, R> mapper) {
            return cons(() -> mapper.apply(getHead()), () -> getTail().map(mapper));
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Construct, copy, destroy

    /**
     * Create a nil (empty) stream.
     *
     * @param <T> the stream type
     * @return the nil stream
     */
    static <T> Stream<T> nil() {
        return new Nil<>();
    }

    /**
     * Create a non empty stream from the given head and tail suppliers.  When/if the head value
     * is requested, the head supplier will be asked to supply it.  When/if the tail stream is
     * requested, the tail supplier will be asked to supply it.
     *
     * @param head the head value supplier
     * @param tail the tail supplier
     * @param <T>  the stream value type
     * @return the stream
     */
    static <T> Stream<T> cons(Supplier<? extends T> head, Supplier<Stream<T>> tail) {
        return new Cons<>(head, tail);
    }

    /**
     * Create a unit stream, a stream with only the given value.
     *
     * @param head the single value of the stream
     * @param <T>  the stream value type
     * @return the stream
     */
    static <T> Stream<T> unit(T head) {
        return new Cons<>(head, nil());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    /**
     * Return {@code true} if the stream is nil (empty).
     *
     * @return {@code true} if nil; {@code false} if not
     */
    boolean isNil();

    /**
     * Return the head value of the stream.
     *
     * @return the head value.
     */
    T getHead();

    /**
     * Return the tail of the stream (the stream except the head value).
     *
     * @return the stream tail.
     */
    Stream<T> getTail();

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * Map the values of this stream using the given mapping function and return it as a stream.
     */
    <R> Stream<R> map(Function<T,R> mapper);
}
