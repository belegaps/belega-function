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
}
