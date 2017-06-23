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
            return null;
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
            return null;
        }

        @Override
        public <T> Either<T, R> orElse(Function<L, Either<T, R>> mapper) {
            return null;
        }

        @Override
        public <T, U> Either<L, R> map2(Either<L, T> that, BiFunction<R, T, U> mapper) {
            return null;
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
            return null;
        }

        @Override
        public <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper) {
            return null;
        }

        @Override
        public <T> Either<T, R> orElse(Function<L, Either<T, R>> mapper) {
            return null;
        }

        @Override
        public <T, U> Either<L, R> map2(Either<L, T> that, BiFunction<R, T, U> mapper) {
            return null;
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

    <T> Either<L, T> map(Function<R, T> mapper);

    <T> Either<L, T> flatMap(Function<R, Either<L, T>> mapper);

    <T> Either<T, R> orElse(Function<L, Either<T, R>> mapper);

    <T, U> Either<L, R> map2(Either<L, T> that, BiFunction<R, T, U> mapper);
}
