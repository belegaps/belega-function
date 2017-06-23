package dk.belega.util.function;

import java.util.*;
import java.util.function.*;

/**
 * Represents a value of one of two possible types.  An instance of Either is an instance of
 * either {@link Left} or {@link Right}.
 * <p>A common use of Either is as an alternative to {@link java.util.Optional} for dealing with
 * possibly missing values.  In this case, an empty value is replaced by {@code Left}, which can
 * contain useful information.  Non-empty values are represented by {@code Right}.</p>
 * <p>Convention dictates that {@code Left} is used for failure and {@code Right} is used for
 * success.  This convention is applied in the design by standard algebra applying to
 * {@code Right} values and return {@code Left} values unchanged.</p>
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
        public boolean isLeft() {
            return true;
        }

        @Override
        public L getLeft() {
            return left;
        }

        @Override
        public Optional<L> getLeftOption() {
            return Optional.of(left);
        }

        @Override
        public boolean isRight() {
            return false;
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
        public R getOrElse(Supplier<? extends R> that) {
            return that.get();
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
        public boolean isLeft() {
            return false;
        }

        @Override
        public L getLeft() {
            throw new UnsupportedOperationException("calling getLeft() on right value");
        }

        @Override
        public Optional<L> getLeftOption() {
            return Optional.empty();
        }

        @Override
        public boolean isRight() {
            return true;
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
        public R getOrElse(Supplier<? extends R> that) {
            return right;
        }

        @Override
        public <T, U> Either<L, U> map2(Either<L, T> that, BiFunction<R, T, U> mapper) {
            return that.map(r -> mapper.apply(right, r));
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Construct, copy, destroy

    /**
     * Factory method for creating a Left value.
     *
     * @param left the value
     * @param <L>  type of left value
     * @param <R>  type of right value
     * @return a Left value
     */
    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    /**
     * Factory method for creating a Right value.
     *
     * @param right the value
     * @param <L>  type of left value
     * @param <R>  type of right value
     * @return a Right value
     */
    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    // Properties

    /**
     * Check if this is a Left.
     *
     * @return {@code true} if Left; {@code false} if Right
     */
    boolean isLeft();

    /**
     * Return the left value.
     *
     * @return left value
     * @throws UnsupportedOperationException if this is a Right value
     */
    L getLeft();

    /**
     * Return the left value.
     *
     * @return the value if this is a Left; otherwise an empty value
     */
    Optional<L> getLeftOption();

    /**
     * Check if this is a Right.
     *
     * @return {@code true} if Right; {@code false} if Left
     */
    boolean isRight();

    /**
     * Return the right value.
     *
     * @return right value
     * @throws UnsupportedOperationException if this is a Left value
     */
    R getRight();

    /**
     * Return the right value.
     *
     * @return the value is this is a Right; otherwise an empty value
     */
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
     * Returns the value from this Right or the given value if this is a Left.
     *
     * @param that the supplier of the alternative value
     * @return this Right value or that value if this is a Left.
     */
    R getOrElse(Supplier<? extends R> that);

    /**
     * Applies the given {@code mapper} function to Right values of {@code this} and {@code that}.
     * If either value is Left, that value is returned.  If both values are Left, {@code this}
     * Left value is returned.
     *
     * @param that   the other value
     * @param mapper the mapping function
     * @param <T>    the value type of {@code that value}
     * @param <U>    the return type of the {@code mapper} function
     * @return a Right value containing the result of the mapper function, if both values are
     * Right; otherwise the Left value of {@code this} or {@code that}
     */
    <T, U> Either<L, U> map2(Either<L, T> that, BiFunction<R, T, U> mapper);
}
