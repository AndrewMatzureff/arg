package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.GameplayCommandBuffer;
import io.github.andrewmatzureff.arg.component.Move;

public class StateManagerSystem extends IteratingSystem {

    public StateManagerSystem() {
        super(Family.all(GameplayCommandBuffer.class, Move.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var controller = GameplayCommandBuffer.MAPPER.get(entity);
        final var move = Move.MAPPER.get(entity);
        final var moveDelta = (float) Math.sqrt(25);
        try (final var stream = controller.stream()) {
            stream.forEach(command -> {
                switch (command) {
                    case MOVE_LEFT -> move.accumulate(-moveDelta, 0);
                    case MOVE_RIGHT -> move.accumulate(moveDelta, 0);
                    case MOVE_UP -> move.accumulate(0, moveDelta);
                    case MOVE_DOWN -> move.accumulate(0, -moveDelta);
                    default -> {}
                }
            });
        }
    }
}
