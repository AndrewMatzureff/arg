package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.StateManager;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

public record MobIdleState(Entity entity) implements MobState {

    @Override
    public void enter() {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(controller -> controller.setAnimationState(AnimationController.IDLE_WALK)))
            .ifPresent(controller -> controller.setLooping(true));
    }

    @Override
    public void handle(Command command) {
        final var stateManager  = StateManager.MAPPER.get(entity);
        if (command == Command.MOVE_LEFT || command == Command.MOVE_RIGHT) {
            stateManager.setState(create(MobWalkState::new));
            stateManager.getState().handle(command);
        } else if (command == Command.JUMP) {
            stateManager.setState(create(MobJumpState::new));
            stateManager.getState().handle(command);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void exit() {

    }
}
