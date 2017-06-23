package dk.belega.util.function;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Either} class.
 */
public class EitherTest {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Test cases

    @Test
    public void testLeft() {

        final String EXPECTED_RESULT = "some string";

        // Given a left value
        final Either<String, ?> left = Either.left(EXPECTED_RESULT);

        // Then getLeftOption() returns the value
        assertTrue("expected value from getLeftOption()", left.getLeftOption().isPresent());
        assertEquals(EXPECTED_RESULT, left.getLeftOption().get());

        // And getLeft() returns the value
        assertEquals(EXPECTED_RESULT, left.getLeft());

        // And getRightOption() is absent
        assertFalse("expected absent value for right", left.getRightOption().isPresent());

        // And getRight() throws exception
        try {
            left.getRight();
            fail("expected exception from getRight()");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testRight() {

        final Integer EXPECTED_RESULT = 73;

        // Given a left value
        final Either<?, Integer> right = Either.right(EXPECTED_RESULT);

        // Then getRightOption() returns the value
        assertTrue("expected value from getRightOption()", right.getRightOption().isPresent());
        assertEquals(EXPECTED_RESULT, right.getRightOption().get());

        // And getRight() returns the value
        assertEquals(EXPECTED_RESULT, right.getRight());

        // And getLeftOption() is absent
        assertFalse("expected absent value for left", right.getLeftOption().isPresent());

        // And getLeft() throws exception
        try {
            right.getLeft();
            fail("expected exception from getLeft()");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testMap() {

        final Integer EXPECTED_RESULT = 73;

        // Given a right value
        final Either<?, String> right = Either.right(EXPECTED_RESULT.toString());

        // When mapping the value
        final Either<?, Integer> actualResult = right.map(Integer::parseInt);

        // Then the result is the mapped right value
        assertEquals(EXPECTED_RESULT, actualResult.getRight());
    }

    @Test
    public void testMapOnLeft() {

        final NumberFormatException EXPECTED_RESULT = new NumberFormatException();

        // Given a left value
        Either<Exception, String> left = Either.left(EXPECTED_RESULT);

        // When mapping the value
        final Either<Exception, Integer> actualResult = left.map(Integer::parseInt);

        // Then the result is the same left value
        assertSame(EXPECTED_RESULT, actualResult.getLeft());
    }

    @Test
    public void testFlatMap() {

        final Integer EXPECTED_RESULT = 41;

        // Given a right value
        Either<Exception, String> right = Either.right(EXPECTED_RESULT.toString());

        // When flat mapping the value with success
        final Either<Exception, Integer> actualResult = right.flatMap(EitherTest::parseInt);

        // Then the result is a right value
        assertEquals(EXPECTED_RESULT, actualResult.getRight());
    }

    @Test
    public void testFlatMapWithFailure() {

        // Given a right value
        final Either<Exception, String> right = Either.right("not a number");

        // When mapping the value with failure
        final Either<Exception, Integer> actualResult = right.flatMap(EitherTest::parseInt);

        // Then the result is a left value
        assertTrue("expected left value from failing mapper",
                actualResult.getLeftOption().isPresent());
    }

    @Test
    public void testFlatMapOnLeft() {

        final NumberFormatException EXPECTED_RESULT = new NumberFormatException();

        // Given a left value
        Either<Exception, String> left = Either.left(EXPECTED_RESULT);

        // When flat-mapping the value
        final Either<Exception, Integer> actualResult = left.flatMap(EitherTest::parseInt);

        // Then the result is the same left value
        assertSame(EXPECTED_RESULT, actualResult.getLeft());
    }

    @Test
    public void testOrElse() {

        final String EXPECTED_RESULT = "some text";

        // Given a left value
        final Either<Exception, ?> left = Either.left(new NumberFormatException(EXPECTED_RESULT));

        // When flat mapping on left
        final Either<String, ?> actualResult = left.orElse(e -> Either.left(e.getMessage()));

        // Then the result is the converted value
        assertEquals(EXPECTED_RESULT, actualResult.getLeft());
    }

    @Test
    public void testOrElseMappingRight() {

        final Integer EXPECTED_RESULT = 42;

        // Given a left value
        final Either<Exception, Integer> left =
                Either.left(new NumberFormatException());

        // When flat mapping on left with success
        final Either<Exception, Integer> actualResult =
                left.orElse(e -> Either.right(EXPECTED_RESULT));

        // Then the result is the converted value
        assertSame(EXPECTED_RESULT, actualResult.getRight());
    }

    @Test
    public void testOrElseOnRight() {

        final String EXPECTED_RESULT = "some text";

        // Given a right value
        final Either<Exception, String> right =
                Either.right(EXPECTED_RESULT);

        // When flat-mapping on left
        final Either<String, String> actualResult =
                right.orElse(e -> Either.left(e.getMessage()));

        // Then the result is the same right value
        assertEquals(EXPECTED_RESULT, actualResult.getRight());
    }

    @Test
    public void testMap2() {

        final Integer EXPECTED_RESULT = 73;

        // Given two right values
        final Either<Exception, Integer> first = Either.right(EXPECTED_RESULT / 2);
        final Either<Exception, Integer> second = Either.right(EXPECTED_RESULT - first.getRight());

        // When mapping function over both values
        final Either<Exception, Integer> actualResult = first.map2(second, (a, b) -> a + b);

        // Then the result is a right value
        assertEquals(EXPECTED_RESULT, actualResult.getRight());
    }

    @Test
    public void testMap2OnThisLeft() {

        final Exception EXPECTED_RESULT = new NumberFormatException();

        // Given a left value
        final Either<Exception, Integer> first = Either.left(EXPECTED_RESULT);

        // And a right value
        final Either<Exception, Integer> second = Either.right(73);

        // When mapping function over both values
        final Either<Exception, Integer> actualResult = first.map2(second, (a, b) -> a + b);

        // Then the return value is the left value
        assertSame(EXPECTED_RESULT, actualResult.getLeft());
    }

    @Test
    public void testMap2OnThatLeft() {

        final Exception EXPECTED_RESULT = new NumberFormatException();

        // Given a right value
        final Either<Exception, Integer> first = Either.right(73);

        // And a left value
        final Either<Exception, Integer> second = Either.left(EXPECTED_RESULT);

        // When mapping function over both values
        final Either<Exception, Integer> actualResult = first.map2(second, (a, b) -> a + b);

        // Then the return value is the left value
        assertSame(EXPECTED_RESULT, actualResult.getLeft());
    }

    @Test
    public void testMap2OnBothLeft() {

        final Exception EXPECTED_RESULT = new NumberFormatException();

        // Given two left value
        final Either<Exception, Integer> first = Either.left(EXPECTED_RESULT);
        final Either<Exception, Integer> second = Either.left(new NumberFormatException());

        // When mapping function over both values
        final Either<Exception, Integer> actualResult = first.map2(second, (a, b) -> a + b);

        // Then the return value is the first left value
        assertSame(EXPECTED_RESULT, actualResult.getLeft());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    private static Either<Exception, Integer> parseInt(String str) {
        try {
            return Either.right(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            return Either.left(e);
        }
    }
}