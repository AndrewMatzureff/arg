package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.component.StateManager;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

import static com.badlogic.gdx.math.MathUtils.isZero;

public record MobWalkState(Entity entity) implements MobState {

    @Override
    public void enter() {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(controller -> controller.setAnimationState(AnimationController.WALK)))
            .ifPresent(controller -> controller.setLooping(true));
    }

    @Override
    public void handle(Command command) {
        final var stateManager = StateManager.MAPPER.get(entity);
        switch (command) {
            case MOVE_LEFT -> Move.MAPPER.get(entity).accumulate(-1, 0);
            case MOVE_RIGHT -> Move.MAPPER.get(entity).accumulate(1, 0);
            case JUMP -> {
                stateManager.setState(create(MobJumpState::new));
                stateManager.getState().handle(command);
            }
            default -> {}
        }
    }

    @Override
    public void update() {
        Optional.of(entity)
            .map(Move.MAPPER::get)
            .map(Move::getDirection)
            .filter(v -> isZero(v.x, 0.1f))
            .ifPresent(__ -> StateManager.MAPPER.get(entity)
                .setState(create(MobIdleState::new)));
    }

    @Override
    public void exit() {

    }
}
