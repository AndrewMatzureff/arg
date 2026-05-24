package io.github.andrewmatzureff.arg.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Streams {
    static <T, U> Consumer<T> consumer(U arg, BiConsumer<T, U> biConsumer) {
        return self -> biConsumer.accept(self, arg);
    }
}
