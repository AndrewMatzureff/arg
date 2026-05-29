package io.github.andrewmatzureff.arg.util;

import java.util.function.*;

public interface Streams {
    static <T, U> Consumer<T> consumer(U arg, BiConsumer<T, U> biConsumer) {
        return self -> biConsumer.accept(self, arg);
    }

    static <T, U> Predicate<T> predicate(U arg, BiPredicate<T, U> biPredicate) {
        return self -> biPredicate.test(self, arg);
    }
}
