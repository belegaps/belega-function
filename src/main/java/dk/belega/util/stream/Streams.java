package dk.belega.util.stream;

/**
 * Utility functions for {@link Stream}s.
 */
@SuppressWarnings("WeakerAccess")
public class Streams {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * Return a stream containing the given elements, in order.
     *
     * @param elements the elements for the stream
     * @param <T>      the stream value type
     * @return a stream
     */
    @SafeVarargs
    public static <T> Stream<T> of(T... elements) {
        return of(elements, 0, elements.length);
    }

    /**
     * Return a stream containing {@code length} elements from the array, starting at the given
     * {@code offset}.
     *
     * @param elements the array containing elements
     * @param offset   the starting offset
     * @param length   the length of the stream
     * @param <T>      the stream value type
     * @return stream of elements from the array
     */
    public static <T> Stream<T> of(T[] elements, int offset, int length) {

        if (length < 0)
            throw new IllegalArgumentException("length cannot be negative");

        if (0 == length) {
            return Stream.nil();
        } else {
            return Stream.cons(
                    () -> elements[offset],
                    () -> of(elements, offset + 1, length - 1));
        }
    }
}
