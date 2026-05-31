package io.github.andrewmatzureff.arg.util;

import java.util.function.*;

public interface Optionals {
    static <T> UnaryOperator<T> peek(Consumer<T> observer) {
        return t -> {
            observer.accept(t);
            return t;
        };
    }

    static <T, U, R> Function<T, R> map(U arg, BiFunction<T, U, R> biFunction) {
        return self -> biFunction.apply(self, arg);
    }

    static <T, R> Function<T, R> remap(R target) {
        return __ -> target;
    }
}
