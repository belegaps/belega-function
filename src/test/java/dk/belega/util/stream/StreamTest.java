package dk.belega.util.stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Stream} class.
 */
@RunWith(JUnit4.class)
public class StreamTest extends AbstractTest {

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


    @Test
    public void testLazyAppend() {

        final String FIRST_VALUE = "first-value";
        final String SECOND_VALUE = "second-value";

        final String[] EXPECTED_RESULT = {
                FIRST_VALUE,
                SECOND_VALUE
        };

        // Given a unit stream
        final Stream<String> firstStream = Stream.unit(FIRST_VALUE);

        // When appending another unit stream to it
        final Stream<String> actualResult = firstStream.append(() -> Stream.unit(SECOND_VALUE));

        // Then the resulting stream has two elements, in order
        assertStreamEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    public void testFlatMap() {

        final String[] EXPECTED_RESULT = {
                "first",
                "second"
        };

        // Given a unit stream with a comma-separated string value
        final Stream<String> stream =
                Stream.unit(String.join(",", Arrays.asList(EXPECTED_RESULT)));

        // When flat-mapping the stream with a mapper converting elements to streams
        Stream<String> actualResult = stream.flatMap(str -> Streams.of(str.split(",")));

        // Then the result is a stream with the mapped elements
        assertStreamEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    public void testFoldLeft() {

        final String[] values = {
                "first",
                "second"
        };

        // Given a stream with two elements
        final Stream<String> stream = Streams.of(values);

        // When foldLeft'ing the values
        final Integer actualResult = stream.foldLeft(0, (index, elem) -> {

            // Then the values are given to the collector, in order
            assertSame(values[index], elem);
            return index + 1;
        });

        // And the return value is the result of the last call
        assertEquals(values.length, (int)actualResult);
    }

    @Test
    public void testAsList() {

        final Object[] EXPECTED_RESULT = {
                "first",
                "second",
                "third"
        };

        // Given a stream with multiple elements
        final Stream<Object> stream = Streams.of(EXPECTED_RESULT);

        // When collecting the elements as a list
        final List<Object> actualResult = stream.asList();

        // Then the result matches the order of the stream elements
        assertEquals(Arrays.asList(EXPECTED_RESULT), actualResult);
    }

    @Test
    public void testFilter() {

        final Integer[] EVEN_VALUES = { 0, 2, 4, 6, 8 };
        final Integer[] ODD_VALUES = { 1, 3, 5, 7, 9 };

        // Given a stream of integer elements
        final Stream<Integer> stream = Streams.of(EVEN_VALUES).append(Streams.of(ODD_VALUES));

        // When filtering out the odd elements
        final Stream<Integer> actualResult = stream.filter(StreamTest::isEven);

        // Then the resulting list contains only even numbers
        assertStreamEquals(EVEN_VALUES, actualResult);
    }

    @Test
    public void testEmptyHeadOption() {

        // Given an empty stream
        Stream<String> emptyStream = Stream.nil();

        // When getting the head value
        final Optional<String> actualResult = emptyStream.getHeadOption();

        // Then the return value is empty
        assertFalse("Empty stream returned head option", actualResult.isPresent());
    }

    @Test
    public void testNonEmptyHeadOption() {

        final String EXPECTED_RESULT = "some-value";

        // Given a non-empty stream
        final Stream<String> nonEmptyStream = Stream.unit(EXPECTED_RESULT);

        // When getting the head value
        final Optional<String> actualResult = nonEmptyStream.getHeadOption();

        // Then the return value is non-empty
        assertTrue("Non-empty stream returned empty head", actualResult.isPresent());

        // And the value is the stream's first value
        assertEquals(EXPECTED_RESULT, actualResult.get());
    }

    @Test
    public void testSkipNil() {

        // Given a nil stream
        Stream<Integer> stream = Stream.nil();

        // When skipping any number of elements
        Stream<Integer> actualResult = stream.skip(5);

        // Then the resulting stream is still nil
        assertTrue("skip() on nil stream returned non-nil stream", actualResult.isNil());

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    private static boolean isEven(Integer value) {
        return 0 == value % 2;
    }
}
