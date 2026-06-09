package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBodyBox;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.component.StateManager;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

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
        final var move = Move.MAPPER.get(entity);
        final var rigidBodyBox = RigidBodyBox.MAPPER.get(entity);
        final var body = rigidBodyBox.getBody();
        switch (command) {
            case MOVE_LEFT -> {
                move.accumulate(-1f, 0);
                body.applyTorque(move.getDirection().x * 255f, true);
            }
            case MOVE_RIGHT -> {
                move.accumulate(1f, 0);
                body.applyTorque(move.getDirection().x * 255f, true);
            }
            case JUMP -> {
                transition(MobJumpState::new).handle(command);
            }
            default -> {}
        }
    }

    @Override
    public void update() {
        Optional.of(entity)
            .map(Move.MAPPER::get)
            .map(Move::getDirection)
//            .filter(v -> isZero(v.x, 0.1f))
//            .ifPresent(__ -> transition(MobIdleState::new));
            .<Function<Entity, MobState>>map(v -> {
                if (v.y < 0) return MobFallState::new;
                if (isZero(v.x, 0.1f)) return MobIdleState::new;
                return null;
            })
            .ifPresent(this::transition);
    }

    @Override
    public void exit() {

    }
}
