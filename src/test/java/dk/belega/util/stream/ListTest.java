package dk.belega.util.stream;

import org.junit.Test;

import java.util.Arrays;

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

        // And headOption is empty
        assertFalse("Non-empty nil list", nil.getHeadOption().isPresent());

        // And head throws an exception
        try {
            nil.getHead();
            fail("getHead() on nil list failed to throw exception");
        } catch (UnsupportedOperationException ignored) {
        }

        // And setHead() throws an exception
        try {
            nil.setHead(42);
            fail("setHead() on nil list failed to throw exception");
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

    @Test
    public void testSetHead() {

        final Integer EXPECTED_RESULT = 73;

        // Given a non-nil list
        final List<Integer> list = List.unit(37);

        // When setting the head of the list
        final List<Integer> newList = list.setHead(EXPECTED_RESULT);

        // Then the new value is the head of the new list
        assertEquals(EXPECTED_RESULT, newList.getHead());
    }

    @Test
    public void testDropAll() {

        // Given a list
        final List<Integer> list = List.cons(1, List.cons(2, List.unit(3)));

        // When dropping all elements
        final List<Integer> actualResult = list.drop(3);

        // Then the result is a nil list
        assertTrue("Non-nil list returned after dropping all elements", actualResult.isNil());
    }

    @Test
    public void testDropNegative() {

        final List<String> NIL = List.nil();
        final List<String> UNIT = List.unit("some value");

        // Given a list
        for (List<String> list : Arrays.asList(NIL, UNIT)) {

            try {
                // When dropping a negative number of elements
                list.drop(-1);

                // Then the method throws an exception
                fail("drop() failed to throw exception for negative argument for list " + list);

            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    @Test
    public void testDropMore() {

        // Given a list
        final List<Object> unit = List.unit(new Object());

        // When dropping more elements than are in the list
        final List<Object> actualResult = unit.drop(10);

        // Then the result is the nil list
        assertTrue("Non-nil list returned after dropping more elements than in the list", actualResult.isNil());
    }

    @Test
    public void testDrop() {

        final List<Integer> EXPECTED_RESULT = List.unit(73);

        // Given a list with three elements
        final List<Integer> list = List.cons(42, List.cons(37, EXPECTED_RESULT));

        // When dropping the first two elements
        final List<Integer> actualResult = list.drop(2);

        // Then the remaining list is returned
        assertSame(EXPECTED_RESULT, actualResult);
    }
}
