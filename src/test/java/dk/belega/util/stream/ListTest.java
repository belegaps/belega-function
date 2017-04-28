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
        assertEquals(EXPECTED_RESULT, actualResult);
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

    @Test
    public void testReverse() {

        final Integer[] EXPECTED_RESULT = new Integer[] { 0, 1, 2, 3 };

        // Given a list
        final List<Integer> list = listOf(reversed(EXPECTED_RESULT));

        // When reversing the list
        final List<Integer> actualResult = list.reverse();

        // Then the resulting list contains the elements in reverse
        assertEquals(listOf(EXPECTED_RESULT), actualResult);
    }

    @Test
    public void testMap() {

        // Given a list of integers
        final List<Integer> list = rangeOf(0, 10);

        // When mapping the elements to strings
        final List<String> actualResult = list.map(Object::toString);

        // Then the result is a list of strings
        List<Integer> e = list;
        for (List<String> a = actualResult; !a.isNil(); a = a.getTail(), e = e.getTail()) {
            assertEquals(e.getHead().toString(), a.getHead());
        }
    }

    @Test
    public void testFilter() {

        // Given a list of integers
        final List<Integer> list = rangeOf(0, 100);

        // When filtering out odd numbers
        final List<Integer> actualResult = list.filter(n -> (n % 2) == 0);

        // Then only even numbers remain
        assertEquals(rangeOf(0, 100, 2), actualResult);
    }

    @Test
    public void testEquals() {

        // Given a list
        final List<Integer> first = rangeOf(0, 5);

        // And an identical list
        final List<Integer> second =
                List.cons(0,
                        List.cons(1,
                                List.cons(2,
                                        List.cons(3,
                                                List.unit(4)))));

        // When comparing for equality
        final boolean actualResult = first.equals(second);

        // Then the result is true
        assertTrue("Equal list test failed", actualResult);
    }

    @Test
    public void testEqualsWithNull() {

        // Given a list with a null value
        List<Integer> nullList = List.unit(null);

        // And another list with a non-null value
        List<Integer> nonNullList = List.unit(5);

        // When testing the lists for equality
        final boolean actualResult = nullList.equals(nonNullList);

        // Then the answer should be false
        assertFalse("Non-equal lists report equality", actualResult);
    }

    @Test
    public void testToString() {

        final Integer[] VALUES = { 37, 42, 73 };

        // Given a list of integers
        final List<Integer> integerList = listOf(VALUES);

        // When converting to a string
        final String actualResult = integerList.toString();

        // Then the result is an array string
        assertEquals(Arrays.toString(VALUES), actualResult);
    }

    @Test
    public void testNilToString() {

        // Given the nil list
        List<Integer> integerList = List.nil();

        // When converting to a string
        final String actualResult = integerList.toString();

        // Then the result is an array string
        assertEquals(Arrays.toString(new Integer[0]), actualResult);
    }

    @Test
    public void testToStringWithNull() {

        final String[] VALUES = {
                "head",
                null,
                "tail"
        };

        // Given a string list with a null value
        List<String> list = listOf(VALUES);

        // When converting to a string
        final String actualResult = list.toString();

        // Then the result contains "null"
        assertEquals(Arrays.toString(VALUES), actualResult);
    }

    @Test
    public void testFlatMap() {

        final String[] VALUES = {
                "First",
                "Last"
        };

        // Given a list of strings
        final List<String> list = listOf(VALUES);

        // When flat-mapping to list of characters
        final List<Character> actualResult = list.flatMap(ListTest::charactersOf);

        // Then the result is a list of all characters
        assertEquals(listOf((VALUES[0] + VALUES[1]).toCharArray()), actualResult);
    }

    @Test
    public void testZipWith() {

        // Given a list of integers
        final List<Integer> integerList = rangeOf(0, 100, 10);

        // When zipping the list with itself using addition
        final List<Integer> actualResult = integerList.zipWith(integerList, (l,r) -> l + r);

        // Then the result is a list with double values
        assertEquals(integerList.map(n -> n * 2), actualResult);
    }

    @Test
    public void testZipWithDifferentLengths() {

        final String[] FIRST_VALUES = { "a", "b", "c", "d", "e" };
        final String[] SECOND_VALUES = { "f", "g", "h", "i" };

        // Given two lists of different lengths
        final List<String> first = listOf(FIRST_VALUES);
        final List<String> second = listOf(SECOND_VALUES);

        // When zipping the two lists
        final List<String> actualResult = first.zipWith(second, String::concat);

        // Then the length of the result is the minimum length of the two lists
        assertEquals(Math.min(first.getLength(), second.getLength()), actualResult.getLength());
    }

    @Test
    public void testLength() {

        final String SOME_VALUE = "Some value";

        // Given the nil list
        final List<Object> nil = List.nil();

        // Then the length is zero (0)
        assertEquals(0L, nil.getLength());


        // Given a unit list
        final List<String> unit = List.unit(SOME_VALUE);

        // Then the length is one (1)
        assertEquals(1L, unit.getLength());


        // Given a list of values
        final List<Character> characterList = listOf(SOME_VALUE.toCharArray());

        // Then the length is the number of elements in the list
        assertEquals(SOME_VALUE.length(), characterList.getLength());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation

    private List<Character> listOf(char[] elements) {
        List<Character> list = List.nil();
        for (int i = elements.length; i > 0; --i) {
            list = List.cons(elements[i - 1], list);
        }
        return list;
    }

    private <T> List<T> listOf(T[] elements) {
        List<T> list = List.nil();
        for (int i = elements.length; i > 0; --i) {
            list = List.cons(elements[i - 1], list);
        }
        return list;
    }

    private <E> E[] reversed(E[] values) {
        final E[] result = Arrays.copyOf(values, values.length);
        for (int i = 0, j = values.length - 1; i < j; ++i, --j) {
            E tmp = values[i];
            values[i] = values[j];
            values[j] = tmp;
        }
        return result;
    }

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

    private static List<Integer> rangeOf(int start, int end) {
        return rangeOf(start, end, Integer.signum(end - start));
    }

    private static List<Integer> rangeOf(int start, int end, int step) {

        if (start == end) {
            return List.nil();
        }

        if (Integer.signum(end - start) != Integer.signum(step)) {
            throw new IllegalArgumentException("illegal range/step");
        }

        List<Integer> list = List.nil();
        for (int i = start; i < end; i += step) {
            list = List.cons(i, list);
        }

        return list.reverse();
    }
}
