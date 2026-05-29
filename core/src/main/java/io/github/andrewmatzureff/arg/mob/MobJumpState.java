package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.StateManager;
import io.github.andrewmatzureff.arg.component.Transform;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

public record MobJumpState(Entity entity) implements MobState {

    @Override
    public void enter() {
        if (StateManager.MAPPER.get(entity).hasState(this)) return;
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(controller -> controller.setAnimationState(AnimationController.JUMP)))
            .ifPresent(controller -> controller.setLooping(false));
        Optional.of(entity)
            .map(Jump.MAPPER::get)
            .ifPresent(jump -> jump.setTriggered(true));
    }

    @Override
    public void handle(Command command) {
        final var stateManager  = StateManager.MAPPER.get(entity);
        if (command == Command.JUMP) {
        }

        switch (command) {
//            stateManager.setState(create(MobWalkState::new));
//            stateManager.getState().handle(command);
            case MOVE_LEFT -> Move.MAPPER.get(entity).accumulate(-0.5f, 0);
            case MOVE_RIGHT -> Move.MAPPER.get(entity).accumulate(0.5f, 0);
        }
    }

    @Override
    public void update() {
        final var jump = Jump.MAPPER.get(entity);
        final var move = Move.MAPPER.get(entity);
        final var animationController = AnimationController.MAPPER.get(entity);
        final var stateManager = StateManager.MAPPER.get(entity);
        if (jump.isTriggered()) move.accumulate(0, 100);
        if (animationController.isFinished()) stateManager.setState(create(MobIdleState::new));
        jump.setTriggered(false);
    }

    @Override
    public void exit() {

    }
}
