package io.github.andrewmatzureff.arg.util;

import java.util.Collection;
import java.util.stream.Stream;

public interface Streamable<T> {
    /**
     * Returns a {@code Stream} of {@code T} elements.
     *
     * <p>This method makes no guarantees as to the purity of its interactions with the implementing class instance, its members and their collective state.
     *
     * <p>Instances of {@link Collection}-backed {@link Streamable} implementations upon which this method is invoked may be subject to mutation of their streamed {@code Collections} prior to completion of this method, during the lifespan of the returned {@link Stream} and/or as a result of terminating the {@code Stream}. Thus, implementors of the {@code Collection} interface should not simultaneously implement {@code Streamable} since doing so would violate the contract of {@link Collection#stream()}.
     *
     * @return a sequential {@code Stream} over the elements from this data source
     */
    Stream<T> stream();
}
