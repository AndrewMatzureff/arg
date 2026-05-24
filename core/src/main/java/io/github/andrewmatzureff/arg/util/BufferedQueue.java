package io.github.andrewmatzureff.arg.util;

import java.util.*;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class BufferedQueue<T> implements Streamable<T> {

    private final List<T> queued = new LinkedList<>();
    private final List<T> pending = new LinkedList<>();
    private boolean streaming;

    public BufferedQueue<T> add(T data) {
        queued.add(data);
        return this;
    }

    public BufferedQueue<T> flush() {
        if (streaming) throw new IllegalStateException("Tried to flush the queue while actively streaming! Have you declared the Stream obtained from '%s::%s' in a try-with-resources block?"
            .formatted(getClass().getSimpleName(), "stream"));
        pending.addAll(queued);
        queued.clear();
        return this;
    }

    public Stream<T> stream() {
        streaming = true;
        return Stream.generate(() -> hasPending() ? next() : null)
            .takeWhile(Objects::nonNull)
            .onClose(() -> streaming = false);
    }

    public T next() {return pending.removeFirst();}

    public Optional<T> peek() {
        return Optional.of(pending)
            .filter(not(List::isEmpty))
            .map(List::getFirst);
    }

    public boolean hasPending() {return peek().isPresent();}
}
