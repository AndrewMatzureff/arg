package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.GameplayCommandBuffer;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.component.StateManager;

public class CommandHandlerSystem extends IteratingSystem {

    public CommandHandlerSystem() {
        super(Family.all(GameplayCommandBuffer.class, Move.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
//        final var controller = GameplayCommandBuffer.MAPPER.get(entity);
//        final var stateManager = StateManager.MAPPER.get(entity);
//        try (final var stream = controller.stream()
//            .onClose(() -> stateManager.getState().update())) {
//            stream.forEach(command -> stateManager.getState().handle(command));
//            // TODO: move state handling logic from within states into state handling systems
//        }
    }
}
