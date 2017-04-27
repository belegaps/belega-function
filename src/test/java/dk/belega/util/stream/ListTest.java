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

    @Test
    public void testDropWhile() {

        final Integer EXPECTED_RESULT = 5;

        // Given a range of integers from 0 to 9
        final List<Integer> list = rangeOf(0, 10);

        // When dropping prefix values under 5
        List<Integer> actualResult = list.dropWhile(n -> n < EXPECTED_RESULT);

        // Then the head value of the result is 5
        assertEquals(EXPECTED_RESULT, actualResult.getHead());
    }

    @Test
    public void testDropWhileAllMatch() {

        final Integer UNMATCHED_VALUE = 74;

        // Given a list of integers
        final List<Integer> list = rangeOf(37, UNMATCHED_VALUE);

        // When dropping prefix elements without finding a failing match
        final List<Integer> actualResult = list.dropWhile(n -> n < UNMATCHED_VALUE);

        // Then the result is the nil list
        assertTrue("Non-nil list returned after dropping all elements", actualResult.isNil());
    }

    @Test
    public void testAppend() {

        final List<Integer> EXPECTED_RESULT = rangeOf(0, 10);

        // Given two lists
        final List<Integer> first = rangeOf(0, 5);
        final List<Integer> second = rangeOf(5, 10);

        // When appending one to the other
        final List<Integer> actualResult = first.append(second);

        // Then the result contains all elements from both lists
        assertListEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    public void testAppendNil() {

        // Given a list
        final List<Integer> list = rangeOf(4, 30);

        // When appending the nil list
        final List<Integer> actualResult = list.append(List.nil());

        // Then the result is the same list
        assertSame(list, actualResult);
    }

    @Test
    public void testFoldRight() {

        // Given a list of integer values
        final List<Integer> list = rangeOf(5, 14);

        // When folding the list right on addition
        final long actualResult = list.foldRight(0L, (i,z) -> i + z);

        // Then the result is the sum of all integer values in the list
        assertEquals(sumOf(list), actualResult);
    }

    @Test
    public void testFoldLeft() {

        final String EXPECTED_RESULT = "some string";

        // Given a list of the characters of a string
        List<Character> list = charactersOf(EXPECTED_RESULT);

        // When folding the characters into a list from left to right
        final String actualResult = list.foldLeft("", (s,c) -> s + c);

        // Then the result is the string
        assertEquals(EXPECTED_RESULT, actualResult);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    private static List<Character> charactersOf(String str) {

        List<Character> list = List.nil();

        int idx = str.length();
        while (idx > 0) {
            list = List.cons(str.charAt(--idx), list);
        }

        return list;
    }

    private static long sumOf(List<Integer> ints) {
        long sum = 0;
        while (!ints.isNil()) {
            sum += ints.getHead();
            ints = ints.getTail();
        }
        return sum;
    }

    private static void assertListEquals(List<?> expected, List<?> actual) {

        List<?> e = expected;
        List<?> a = actual;

        for (; ; ) {
            if (e.isNil()) {
                if (!a.isNil()) {
                    fail("List sizes differ");
                }
                break;
            } else if (a.isNil()) {
                fail("List sizes differ");
            }

            final Object eHead = e.getHead();
            final Object aHead = a.getHead();

            if (null == eHead) {
                if (null != aHead) {
                    fail("List elements differ; expected null, got " + aHead);
                }
            } else if (!eHead.equals(aHead)) {
                fail("List elements differ; expected " + eHead + ", got " + aHead);
            }

            e = e.getTail();
            a = a.getTail();
        }
    }

    private static List<Integer> rangeOf(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("illegal range (end < start)");
        }
        List<Integer> list = List.nil();
        while (end > start) {
            list = List.cons(--end, list);
        }
        return list;
    }
}
