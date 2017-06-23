package dk.belega.util.function;

import java.util.*;
import java.util.function.*;

/**
 * Represents a value of one of two types.
 */
@SuppressWarnings("WeakerAccess")
public interface Either<L, R> {

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Types, constants

    class Left<L, R> implements Either<L, R> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Data

        private final L left;

        //////////////////////////////////////////////////////////////////////////////////////////
        // Construct, copy, destroy

        public Left(L left) {
            this.left = left;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Properties

        @Override
        public L getLeft() {
            return left;
        }

        @Override
        public Optional<L> getLeftOption() {
            return Optional.of(left);
        }

        @Override
        public R getRight() {
            throw new UnsupportedOperationException("calling getRight() on left value");
        }

        @Override
        public Optional<R> getRightOption() {
            return Optional.empty();
        }

        @Override
        public <T> Either<L, T> map(Function<R, T> mapper) {
            return left(left);
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
            return left(left);
        }

        @Override
        public <T> Either<T, R> orElse(Function<L, Either<T, R>> mapper) {
            return mapper.apply(left);
        }

        @Override
        public <T, U> Either<L, U> map2(Either<L, T> that, BiFunction<R, T, U> mapper) {
            return left(left);
        }
    }

    class Right<L, R> implements Either<L, R> {

        //////////////////////////////////////////////////////////////////////////////////////////
        // Data

        private final R right;

        //////////////////////////////////////////////////////////////////////////////////////////
        // Construct, copy, destroy

        public Right(R right) {
            this.right = right;
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Properties

        @Override
        public L getLeft() {
            throw new UnsupportedOperationException("calling getLeft() on right value");
        }

        @Override
        public Optional<L> getLeftOption() {
            return Optional.empty();
        }

        @Override
        public R getRight() {
            return right;
        }

        @Override
        public Optional<R> getRightOption() {
            return Optional.of(right);
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        // Operations

        @Override
        public <T> Either<L, T> map(Function<R, T> mapper) {
            return right(mapper.apply(right));
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
            return mapper.apply(right);
        }

        @Override
        public <T> Either<T, R> orElse(Function<L, Either<T, R>> mapper) {
            return right(right);
        }

        @Override
        public <T, U> Either<L, U> map2(Either<L, T> that, BiFunction<R, T, U> mapper) {
            return that.map(r -> mapper.apply(right, r));
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Construct, copy, destroy

    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    L getLeft();

    Optional<L> getLeftOption();

    R getRight();

    Optional<R> getRightOption();

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Operations

    /**
     * Returns a new right value with the result of applying the given mapper function to the
     * right value of this object.  Returns the existing value for a left value.
     *
     * @param mapper the mapping function
     * @param <T>    the result type of the mapping function
     * @return mapped right value or same left value
     */
    <T> Either<L, T> map(Function<R, T> mapper);

    /**
     * Applies the given {@code mapper} function to Right values and returns the result.  If Left,
     * the value is returned unchanged.
     *
     * @param mapper the mapping function
     * @param <T>    the return type of the mapping function
     * @return Result of mapping Right value; or Left unchanged
     */
    <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper);

    /**
     * Applies the given {@code mapper} function to Left values and returns the result.  If Right,
     * the value is returned unchanged.
     *
     * @param mapper the mapping function
     * @param <T>    the return type of the mapping function
     * @return Result of mapping Left value; or Right value unchanged
     */
    <T> Either<T, R> orElse(Function<L, Either<T, R>> mapper);

    /**
     * Applies the given {@code mapper} function to Right values of {@code this) and {@code that}.
     * If either value is Left, that value is returned.  If both values are Left, {@code this}
     * Left value is returned.
     *
     * @param that the other value
     * @param mapper the mapping function
     * @param <T> the value type of {@code that value}
     * @param <U> the return type of the {@code mapper} function
     * @return a Right value containing the result of the mapper function, if both values are
     * Right; otherwise the Left value of {@code this} or {@code that}
     */
    <T, U> Either<L, U> map2(Either<L, T> that, BiFunction<R, T, U> mapper);
}
