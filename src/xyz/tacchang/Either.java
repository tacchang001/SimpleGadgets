/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

/*
    @authority https://github.com/jbock-java/either
*/
package xyz.tacchang;

import static java.util.Objects.requireNonNull;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 */
public abstract class Either<L, R> {
    public static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }
    
    public static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }
    
    public abstract <R2> Either<L, R2> map(
            Function<? super R, ? extends R2> mapper);
    
    public abstract <R2> Either<L, R2> flatMap(
            Function<? super R, ? extends Either<? extends L, ? extends R2>> mapper);
    
    public abstract Either<L, R> filter(
            Function<? super R, Optional<? extends L>> predicate);
    
    public abstract <L2> Either<L2, R> mapLeft(
            Function<? super L, ? extends L2> mapper);
    
    public abstract <L2> Either<L2, R> flatMapLeft(
            Function<? super L, ? extends Either<? extends L2, ? extends R>> mapper);
    
    public abstract Either<L, R> filterLeft(
            Function<? super L, Optional<? extends R>> predicate);
    
    public abstract <U> U fold(
            Function<? super L, ? extends U> leftMapper,
            Function<? super R, ? extends U> rightMapper);
    
    public abstract void ifLeftOrElse(
            Consumer<? super L> leftAction,
            Consumer<? super R> rightAction);
    
    public abstract <X extends Throwable> R orElseThrow(
            Function<? super L, ? extends X> exceptionSupplier) throws X;
    
    public abstract boolean isLeft();
    
    public final boolean isRight() {
        return !isLeft();
    }
    
    public abstract Optional<L> getLeft();
    
    public abstract Optional<R> getRight();
    
    @Override
    public abstract String toString();
    
    /**
     * 
     * @param <L>
     * @param <R> 
     */
    public static class Left<L, R> extends Either<L, R> {

        private final L value;

        Left(L value) {
            this.value = requireNonNull(value);
        }

        @Override
        public Optional<L> getLeft() {
            return Optional.of(value);
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public Optional<R> getRight() {
            return Optional.empty();
        }

        @Override
        public <R2> Either<L, R2> map(Function<? super R, ? extends R2> mapper) {
            @SuppressWarnings("unchecked")
            Either<L, R2> result = (Either<L, R2>) this;
            return result;
        }

        @Override
        public <R2> Either<L, R2> flatMap(Function<? super R, ? extends Either<? extends L, ? extends R2>> mapper) {
            @SuppressWarnings("unchecked")
            Either<L, R2> result = (Either<L, R2>) this;
            return result;
        }

        @Override
        public Either<L, R> filter(Function<? super R, Optional<? extends L>> predicate) {
            return this;
        }

        @Override
        public <L2> Either<L2, R> mapLeft(Function<? super L, ? extends L2> mapper) {
            return new Left<>(mapper.apply(value));
        }

        @Override
        public <L2> Either<L2, R> flatMapLeft(Function<? super L, ? extends Either<? extends L2, ? extends R>> mapper) {
            @SuppressWarnings("unchecked")
            Either<L2, R> result = (Either<L2, R>) mapper.apply(value);
            return result;
        }

        @Override
        public Either<L, R> filterLeft(Function<? super L, Optional<? extends R>> predicate) {
            Optional<? extends R> test = predicate.apply(value);
            if (test == Optional.empty()) {
                return this;
            } else {
                return new Right<>(test.get());
            }
        }

        @Override
        public <U> U fold(
                Function<? super L, ? extends U> leftMapper,
                Function<? super R, ? extends U> rightMapper) {
            return leftMapper.apply(value);
        }

        @Override
        public void ifLeftOrElse(Consumer<? super L> leftAction, Consumer<? super R> rightAction) {
            leftAction.accept(value);
        }

        @Override
        public <X extends Throwable> R orElseThrow(Function<? super L, ? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.apply(value);
        }

        @Override
        public String toString() {
            return String.format("Left[%s]", value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Left)) {
                return false;
            }

            Left<?, ?> other = (Left<?, ?>) obj;
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return 31 * value.hashCode();
        }
    }
    
    /**
     * 
     * @param <L>
     * @param <R> 
     */
    public static class Right<L, R> extends Either<L, R> {

        private final R value;

        Right(R value) {
            this.value = requireNonNull(value);
        }

        @Override
        public Optional<L> getLeft() {
            return Optional.empty();
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public Optional<R> getRight() {
            return Optional.of(value);
        }

        @Override
        public <R2> Either<L, R2> map(Function<? super R, ? extends R2> mapper) {
            return new Right<>(mapper.apply(value));
        }

        @Override
        public <R2> Either<L, R2> flatMap(Function<? super R, ? extends Either<? extends L, ? extends R2>> mapper) {
            @SuppressWarnings("unchecked")
            Either<L, R2> result = (Either<L, R2>) mapper.apply(value);
            return result;
        }

        @Override
        public Either<L, R> filter(Function<? super R, Optional<? extends L>> predicate) {
            Optional<? extends L> test = predicate.apply(value);
            if (test == Optional.empty()) {
                return this;
            } else {
                return new Left<>(test.get());
            }
        }

        @Override
        public <L2> Either<L2, R> mapLeft(Function<? super L, ? extends L2> mapper) {
            @SuppressWarnings("unchecked")
            Either<L2, R> result = (Either<L2, R>) this;
            return result;
        }

        @Override
        public <L2> Either<L2, R> flatMapLeft(Function<? super L, ? extends Either<? extends L2, ? extends R>> mapper) {
            @SuppressWarnings("unchecked")
            Either<L2, R> result = (Either<L2, R>) this;
            return result;
        }

        @Override
        public Either<L, R> filterLeft(Function<? super L, Optional<? extends R>> predicate) {
            return this;
        }

        @Override
        public <U> U fold(
                Function<? super L, ? extends U> leftMapper,
                Function<? super R, ? extends U> rightMapper) {
            return rightMapper.apply(value);
        }

        @Override
        public void ifLeftOrElse(Consumer<? super L> leftAction, Consumer<? super R> rightAction) {
            rightAction.accept(value);
        }

        @Override
        public <X extends Throwable> R orElseThrow(Function<? super L, ? extends X> exceptionSupplier) {
            return value;
        }

        @Override
        public String toString() {
            return String.format("Right[%s]", value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Right)) {
                return false;
            }

            Right<?, ?> other = (Right<?, ?>) obj;
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
}
}
