package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.ComponentMapper;
import io.github.andrewmatzureff.arg.input.Command;

public class GameplayCommandBuffer extends AbstractBufferingComponent<Command> {
    public static final ComponentMapper<GameplayCommandBuffer> MAPPER = ComponentMapper.getFor(GameplayCommandBuffer.class);

    public void dispatch(Command command) {queue.add(command);}
    @Override protected void preflush() {}
}
