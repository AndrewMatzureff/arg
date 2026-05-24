package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import io.github.andrewmatzureff.arg.util.BufferedQueue;
import io.github.andrewmatzureff.arg.util.Streamable;

import java.util.stream.Stream;

public abstract class AbstractBufferingComponent<T> implements Streamable<T>, Component {

    protected final BufferedQueue<T> queue = new BufferedQueue<>();
    private int frame = 1;

    public Stream<T> stream() {
        if (frame == Gdx.graphics.getFrameId()) {
            frame++;
            preflush();
            return queue.flush().stream();
        }

        throw new IllegalStateException("Tried to flush incremental queue data %s than once per frame! '%s::%s' must be called exactly once per frame (1 Frame = 1 Flush)."
            .formatted(
                frame > Gdx.graphics.getFrameId() ? "more" : "less",
                getClass().getSimpleName(),
                "stream"));
    }

    protected abstract void preflush();
}
