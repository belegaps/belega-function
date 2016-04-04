package dk.belega.util.function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link Lazy} class.
 */
@RunWith(JUnit4.class)
public class LazyTest {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Test cases

    @Test
    public void testLazyInit() {

        final String EXPECTED_RESULT = "some-string-value";

        final AtomicBoolean supplierCalled = new AtomicBoolean(false);

        // Given a lazy variable reference
        final Lazy<String> lazy = Lazy.of(() -> {
            supplierCalled.set(true);
            return EXPECTED_RESULT;
        });

        // And not yet initialized
        assertFalse("Lazy variable initialized after instantiation", lazy.isInitialized());
        assertFalse("Supplier called before call to Lazy#get()", supplierCalled.get());

        // When getting the variable value
        final String actualResult = lazy.get();

        // Then the variable is initialized
        assertTrue("Lazy variable not marked as initialized", lazy.isInitialized());

        // And the value is the value returned by the supplier
        assertEquals(EXPECTED_RESULT, actualResult);
    }

    @Test
    public void testMemoization() {

        final Integer EXPECTED_RESULT = 42;

        final AtomicInteger atomicInteger = new AtomicInteger(0);

        // Given a lazy variable
        final Lazy<Integer> lazy = Lazy.of(() -> {
            atomicInteger.incrementAndGet();
            return EXPECTED_RESULT;
        });

        // When querying the variable two times
        lazy.get();
        lazy.get();

        // Then the initializer is only called once
        assertEquals("Supplier called multiple times;", 1, atomicInteger.get());
    }

    @Test
    public void testUnit() {

        final Object EXPECTED_VALUE = new Object();

        // Given a lazy variable with a unit (constant) value
        Lazy<Object> lazy = Lazy.unit(EXPECTED_VALUE);

        // Then the value is initialized
        assertTrue("Unit value not initialized immediately", lazy.isInitialized());

        // And the variable value matches the defined value
        assertEquals(EXPECTED_VALUE, lazy.get());
    }
}
