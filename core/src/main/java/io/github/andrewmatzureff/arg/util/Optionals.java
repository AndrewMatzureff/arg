package io.github.andrewmatzureff.arg.util;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.*;

public interface Optionals {
    static <T> Optional<T> none() {return Optional.empty();}
    static <T> Optional<T> some(@NonNull T element) {return Optional.of(element);}
    static <T> Optional<T> maybe(T element) {return Optional.ofNullable(element);}

    static <T> UnaryOperator<T> peek(Consumer<T> observer) {
        return t -> {
            observer.accept(t);
            return t;
        };
    }

    static <T, U, R> Function<T, R> map(U arg, BiFunction<T, U, R> biFunction) {
        return self -> biFunction.apply(self, arg);
    }

    @SafeVarargs
    static <T, R> Function<T, R> remap(R target, BiConsumer<R, T> ...initializers) {
        return t -> {
            for (var initializer : initializers)
                initializer.accept(target, t);
            return target;
        };
    }
}
