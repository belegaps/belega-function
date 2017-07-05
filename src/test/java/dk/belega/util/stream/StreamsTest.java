package dk.belega.util.stream;

import dk.belega.util.function.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link Streams} class.
 */
@RunWith(JUnit4.class)
public class StreamsTest extends AbstractTest {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Test cases

    @Test
    public void testOfArray() {

        final String[] EXPECTED_RESULT = {
                "first value",
                "second value",
                "third value"
        };

        // When creating a stream of array elements
        final Stream<String> actualResult = Streams.of(EXPECTED_RESULT);

        // Then the result is a stream returning the values in order
        assertStreamEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    public void testOfNegativeLength() {

        try {
            // When creating a stream with negative length
            Streams.of(new String[0], 0, -1);

            // Then the call fails
            fail("expected Streams::of to fail for negative length");

        } catch (IllegalArgumentException ignored) {
            // And throws an exception
        }
    }

    @Test
    public void testOfOutOfBounds() {

        // Given a stream created with invalid offset
        final Stream<String> stream = Streams.of(new String[1], 1, 1);

        try {
            // When accessing an invalid element
            stream.getHead();

            // Then the call fails
            fail("expected Stream::getHead to fail on invalid element");
        } catch (ArrayIndexOutOfBoundsException ignored) {
            // And throws an exception
        }
    }

    @Test
    public void testOfEmptyList() {

        // When creating a stream from a nil list
        Stream<Integer> stream = Streams.of(List.nil());

        // Then the result is a nil stream
        assertTrue(stream.isNil());
    }

    @Test
    public void testOfList() {

        final List<Character> EXPECTED_RESULT =
                List.of("the quick brown bug jumped over the lazy firewall");

        // When creating a stream from a list
        final Stream<Character> stream = Streams.of(EXPECTED_RESULT);

        // Then the stream contains the same elements in the same order
        assertStreamEquals(EXPECTED_RESULT, stream);
    }
}
