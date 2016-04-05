package dk.belega.util.stream;

import dk.belega.util.function.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
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
         * @param <R>    the mapped type
         * @return the mapped stream
         */
        @Override
        public <R> Stream<R> map(Function<T, R> mapper) {
            return nil();
        }

        /**
         * Map the elements of this stream into sub-streams and return the combined stream.
         *
         * @param mapper the mapping function
         * @return a combined stream of mapped values
         */
        @Override
        public <R> Stream<R> flatMap(Function<T, Stream<R>> mapper) {
            return nil();
        }

        /**
         * Return a stream with combined elements of this and the given stream.
         *
         * @param tail the stream to append
         * @return a combined stream
         */
        @Override
        public Stream<T> append(Stream<T> tail) {
            return tail;
        }

        /**
         * Return a stream with combined elements of this and the stream returned by the given
         * supplier.
         *
         * @param tail the supplier of the appended stream
         * @return a combined stream
         */
        @Override
        public Stream<T> append(Supplier<Stream<T>> tail) {
            return tail.get();
        }

        /**
         * Fold the elements of the stream into a single value.
         *
         * @param seed      the seed value
         * @param collector the function combining elements
         * @return the combined result
         */
        @Override
        public <R> R foldLeft(R seed, BiFunction<R, T, R> collector) {
            return seed;
        }

        /**
         * Filter the stream elements using the given predicate.
         *
         * @param predicate the predicate for the filter
         * @return a filtered stream
         */
        @Override
        public Stream<T> filter(Predicate<T> predicate) {
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
         * @param <R>    the mapped type
         * @return the mapped stream
         */
        @Override
        public <R> Stream<R> map(Function<T, R> mapper) {
            return cons(() -> mapper.apply(getHead()), () -> getTail().map(mapper));
        }

        /**
         * Map the elements of this stream into sub-streams and return the combined stream.
         *
         * @param mapper the mapping function
         * @return a combined stream of mapped values
         */
        @Override
        public <R> Stream<R> flatMap(Function<T, Stream<R>> mapper) {
            return mapper.apply(getHead()).append(() -> getTail().flatMap(mapper));
        }

        /**
         * Return a stream with combined elements of this and the given stream.
         *
         * @param tail the stream to append
         * @return a combined stream
         */
        @Override
        public Stream<T> append(Stream<T> tail) {
            return cons(this::getHead, () -> getTail().append(tail));
        }

        /**
         * Return a stream with combined elements of this and the stream returned by the given
         * supplier.
         *
         * @param tail the supplier of the appended stream
         * @return a combined stream
         */
        @Override
        public Stream<T> append(Supplier<Stream<T>> tail) {
            return cons(this::getHead, () -> getTail().append(tail));
        }

        /**
         * Fold the elements of the stream into a single value.
         *
         * @param seed      the seed value
         * @param collector the function combining elements
         * @return the combined result
         */
        @Override
        public <R> R foldLeft(R seed, BiFunction<R, T, R> collector) {
            return getTail().foldLeft(collector.apply(seed, getHead()), collector);
        }

        /**
         * Filter the stream elements using the given predicate.
         *
         * @param predicate the predicate for the filter
         * @return a filtered stream
         */
        @Override
        public Stream<T> filter(Predicate<T> predicate) {
            if (predicate.test(getHead())) {
                return cons(this::getHead, () -> getTail().filter(predicate));
            } else {
                return getTail().filter(predicate);
            }
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
    <R> Stream<R> map(Function<T, R> mapper);

    /**
     * Map the elements of this stream into sub-streams and return the combined stream.
     *
     * @param mapper the mapping function
     * @param <R>    the resulting stream value type
     * @return a combined stream of mapped values
     */
    <R> Stream<R> flatMap(Function<T, Stream<R>> mapper);

    /**
     * Return a stream with combined elements of this and the given stream.
     *
     * @param tail the stream to append
     * @return a combined stream
     */
    Stream<T> append(Stream<T> tail);

    /**
     * Return a stream with combined elements of this and the stream returned by the given
     * supplier.
     *
     * @param tail the supplier of the appended stream
     * @return a combined stream
     */
    Stream<T> append(Supplier<Stream<T>> tail);

    /**
     * Fold the elements of the stream into a single value.
     *
     * @param seed      the seed value
     * @param collector the function combining elements
     * @param <R>       the result value type
     * @return the combined result
     */
    <R> R foldLeft(R seed, BiFunction<R, T, R> collector);

    /**
     * Filter the stream elements using the given predicate.
     *
     * @param predicate the predicate for the filter
     * @return a filtered stream
     */
    Stream<T> filter(Predicate<T> predicate);

    /**
     * Collect the elements of the stream as a list.
     *
     * @return list of elements
     */
    default List<T> asList() {
        return foldLeft(new ArrayList<>(), (list, elem) -> {
            list.add(elem);
            return list;
        });
    }
}
