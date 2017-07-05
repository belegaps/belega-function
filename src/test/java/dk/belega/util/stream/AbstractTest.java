package dk.belega.util.stream;

import dk.belega.util.function.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Base class for stream tests
 */
class AbstractTest {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    static <T> void assertStreamEquals(T[] expected, Stream<T> actual) {

        Stream<T> s = actual;
        for (T t : expected) {
            assertEquals(null, t, s.getHead());
            s = s.getTail();
        }

        assertTrue("Stream length doesn't match expected", s.isNil());
    }

    static <T> void assertStreamEquals(Traversable<T> expected, Stream<T> actual) {

        Stream<T> s = actual;
        for (T t : expected) {
            assertEquals(null, t, s.getHead());
            s = s.getTail();
        }

        assertTrue("Stream length doesn't match expected", s.isNil());
    }
}
