package dk.belega.util.stream;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link List} class.
 */
public class ListTest {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Test cases

    @Test
    public void testNil() {

        // Given a nil list
        List<Integer> nil = List.nil();

        // Then the list reports empty
        assertTrue("Non-empty nil list", nil.isNil());

        // And the headOption is empty
        assertFalse("Non-empty nil list", nil.getHeadOption().isPresent());

        // And head throws an exception
        try {
            nil.getHead();
            fail("getHead() on nil list failed to throw exception");
        } catch (UnsupportedOperationException ignored) {
        }
    }

    @Test
    public void testUnit() {

        final Integer EXPECTED_RESULT = 73;

        // Given a unit list
        List<Integer> unitList = List.unit(EXPECTED_RESULT);

        // Then the unit value is the head
        assertEquals(EXPECTED_RESULT, unitList.getHead());

        // And the tail is nil
        assertTrue("Non-nil tail of unit list", unitList.getTail().isNil());
    }
}