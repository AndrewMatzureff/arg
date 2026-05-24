package io.github.andrewmatzureff.arg.util;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

@AllArgsConstructor
public class CopyPool<T> {
    private final UnaryOperator<T> copy;
    private final BiConsumer<T, T> sync;
    private final Map<T, T> pool;
    public CopyPool(boolean useReferenceIdentity, UnaryOperator<T> copy, BiConsumer<T, T> sync) {
        this(useReferenceIdentity, copy, sync, Map.of());
    }
    public CopyPool(boolean useReferenceIdentity, UnaryOperator<T> copy, BiConsumer<T, T> sync, Map<T, T> pool) {
        this(copy, sync, useReferenceIdentity ? new IdentityHashMap<>(pool) : new HashMap<>(pool));
    }
    public T obtain(T original) {
        final var copy = pool.computeIfAbsent(original, this.copy);
        sync.accept(copy, original);
        return copy;
    }
}
