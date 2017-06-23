package dk.belega.util.function;

import java.util.function.Supplier;

/**
 * Lazy variable initialization.
 */
@SuppressWarnings("WeakerAccess")
public class Lazy<T> implements Supplier<T> {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Data

    /**
     * Value expression; supplier of variable value.
     */
    private final Supplier<? extends T> supplier;

    /**
     * Set to true when variable has been initialized.
     */
    private boolean initialized = false;

    /**
     * Variable value, when initialized.
     */
    private T value;

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Construct, copy, destroy

    /**
     * Construct a lazy variable where the given supplier is called to provide the variable value
     * the first time it's requested.
     *
     * @param supplier the value expression
     */
    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * Construct a lazy variable with a unit value.
     *
     * @param value the variable value
     */
    private Lazy(T value) {

        // No supplier for unit values
        this.supplier = null;

        // Immediately initialized
        this.initialized = true;
        this.value = value;
    }

    /**
     * Construct a lazy variable where the given supplier is called to provide the variable value
     * the first time it's requested.
     *
     * @param supplier the value expression
     * @param <T>      the variable type
     * @return the uninitialized lazy variable
     */
    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<>(supplier);
    }

    /**
     * Construct a lazy variable with a unit value.
     *
     * @param value the variable value
     * @param <T>   the variable type
     * @return the initialized lazy variable
     */
    public static <T> Lazy<T> unit(T value) {
        return new Lazy<>(value);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    /**
     * Returns true if the variable value has been calculated.
     *
     * @return {@code true} when initialized; {@code false} when not.
     */
    boolean isInitialized() {
        return initialized;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * Return the variable value.  If the value hasn't been calculated yet, the supplier is called
     * to provide the value.
     *
     * @return the variable value.
     */
    @Override
    public T get() {

        if (!isInitialized()) {
            synchronized (this) {
                if (!isInitialized()) {
                    //noinspection ConstantConditions
                    value = supplier.get();
                    initialized = true;
                }
            }
        }

        return value;
    }
}
