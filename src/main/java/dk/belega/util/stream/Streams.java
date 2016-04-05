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
     * @param elements the elements for the stream
     * @param <T> the stream value type
     * @return a stream
     */
    @SafeVarargs
    public static <T> Stream<T> of(T... elements) {
        return arrayFromOffset(0, elements);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    @SafeVarargs
    private static <T> Stream<T> arrayFromOffset(int offset, T... elements) {
        if (offset < elements.length) {
            return Stream.cons(() -> elements[offset], () -> arrayFromOffset(offset + 1, elements));
        } else {
            return Stream.nil();
        }
    }
}
