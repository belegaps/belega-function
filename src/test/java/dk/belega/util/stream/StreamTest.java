package dk.belega.util.stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Stream} class.
 */
@RunWith(JUnit4.class)
public class StreamTest {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Test cases

    @Test
    public void testEmpty() {

        // Given an empty stream
        Stream<?> stream = Stream.nil();

        // Then the stream is nil
        assertTrue("Empty stream is not nil", stream.isNil());
    }

    @Test
    public void testUnit() {

        final String EXPECTED_RESULT = "some-string-value";

        // Given a unit stream
        Stream<String> stream = Stream.unit(EXPECTED_RESULT);

        // Then the head value is the unit value
        assertEquals(EXPECTED_RESULT, stream.getHead());

        // And the tail is nil
        assertTrue("Unit stream tail is not nil", stream.getTail().isNil());
    }

    @Test
    public void testMemoization() {

        final Integer EXPECTED_RESULT = 73;

        // Given a stream from one value supplier and one nil supplier
        Stream<Integer> stream = Stream.cons(() -> EXPECTED_RESULT, Stream::nil);

        // Then the head value is the value of the supplier
        assertEquals(EXPECTED_RESULT, stream.getHead());

        // And the tail is nil
        assertTrue("Single value stream is not nil", stream.getTail().isNil());
    }

    @Test
    public void testMap() {

        final Integer EXPECTED_RESULT = 42;

        // Given a stream with one string value
        final Stream<String> stream = Stream.unit(EXPECTED_RESULT.toString());

        // When mapping the string values to integer values
        final Stream<Integer> mapped = stream.map(Integer::parseInt);

        // Then the resulting stream contains one value
        final Integer actualResult = mapped.getHead();
        assertTrue("Multiple values in single value stream", mapped.getTail().isNil());

        // And the value is the mapped value
        assertEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    public void testAppend() {

        final String FIRST_VALUE = "first-value";
        final String SECOND_VALUE = "second-value";

        final String[] EXPECTED_RESULT = {
                FIRST_VALUE,
                SECOND_VALUE
        };

        // Given two unit streams
        final Stream<String> firstStream = Stream.unit(FIRST_VALUE);
        final Stream<String> secondStream = Stream.unit(SECOND_VALUE);

        // When appending one to the other
        final Stream<String> actualResult = firstStream.append(secondStream);

        // Then the resulting stream has two elements, in order
        assertStreamEquals(EXPECTED_RESULT, actualResult);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    private static <T> void assertStreamEquals(T[] expected, Stream<T> actual) {

        Stream<T> s = actual;
        for (T t : expected) {
            assertEquals(null, t, s.getHead());
            s = s.getTail();
        }

        assertTrue("Stream length doesn't match expected", s.isNil());
    }
}
