package dk.belega.util.stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
}
