package io.github.andrewmatzureff.arg.util;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface Optionals {
    static <T> UnaryOperator<T> peek(Consumer<T> observer) {
        return t -> {
            observer.accept(t);
            return t;
        };
    }
}
