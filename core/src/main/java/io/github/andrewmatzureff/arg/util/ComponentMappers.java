package io.github.andrewmatzureff.arg.util;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.Optionals.some;

public class ComponentMappers {
    private static final Map<Class<? extends Component>, ComponentMapper<?>> mappers = new HashMap<>();

    public static <T extends Component> T get(Entity entity, Class<T> type) {
        return some(mappers) // TODO: eliminate capturing lambdas
            .map(m -> m.computeIfAbsent(type, ComponentMapper::getFor))
            .map(cm -> cm.get(entity))
            .map(type::cast)
            .orElseThrow(); // TODO: throw a more descriptive error
    }

    public static <T extends Component> Optional<T> maybe(Entity entity, Class<T> type) {
        return some(mappers)
            .map(m -> m.computeIfAbsent(type, ComponentMapper::getFor))
            .map(cm -> cm.get(entity))
            .map(type::cast);
    }

    public static <T extends Component> boolean has(Entity entity, Class<T> type) {
        return mappers.computeIfAbsent(type, ComponentMapper::getFor)
            .has(entity);
    }

    @SafeVarargs
    public static <T extends Component> boolean hasAny(Entity entity, Class<T>...types) {
        return Arrays.stream(types)
            .anyMatch(type -> has(entity, type));
    }

    @SafeVarargs
    public static <T extends Component> boolean hasAll(Entity entity, Class<T>...types) {
        return Arrays.stream(types)
            .allMatch(type -> has(entity, type));
    }

    @SafeVarargs
    public static <T extends Component> boolean hasNone(Entity entity, Class<T>...types) {
        return Arrays.stream(types)
            .noneMatch(type -> has(entity, type));
    }

    public static <T extends Component> boolean lacks(Entity entity, Class<T> type) {
        return !mappers.computeIfAbsent(type, ComponentMapper::getFor)
            .has(entity);
    }

    @SafeVarargs
    public static <T extends Component> boolean lacksAny(Entity entity, Class<T>...types) {
        return Arrays.stream(types)
            .anyMatch(type -> lacks(entity, type));
    }

    @SafeVarargs
    public static <T extends Component> boolean lacksAll(Entity entity, Class<T>...types) {
        return Arrays.stream(types)
            .allMatch(type -> lacks(entity, type));
    }

    @SafeVarargs
    public static <T extends Component> boolean lacksNone(Entity entity, Class<T>...types) {
        return Arrays.stream(types)
            .noneMatch(type -> lacks(entity, type));
    }
}
