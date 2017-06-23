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
}