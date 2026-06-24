package io.github.andrewmatzureff.arg.util;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;

import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.github.andrewmatzureff.arg.util.Optionals.some;

public interface Streams {
    static <T, U> Consumer<T> consumer(U arg, BiConsumer<T, U> biConsumer) {
        return self -> biConsumer.accept(self, arg);
    }

    static <T, U> Predicate<T> predicate(U arg, BiPredicate<T, U> biPredicate) {
        return self -> biPredicate.test(self, arg);
    }

    static Stream<MapObject> stream(MapObjects mapObjects) {
        return some(mapObjects)
            .map(MapObjects::spliterator)
            .map(split -> StreamSupport.stream(split, false))
            .orElseThrow(); // TODO: throw a more descriptive error
    }
}
