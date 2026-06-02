package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBodyBox;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

public record MobFallState(Entity entity) implements MobState {

    @Override
    public void enter() {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(ac -> ac.setAnimationState(AnimationController.FALL)))
            .ifPresent(ac -> ac.setLooping(true));
    }

    @Override
    public void handle(Command command) {
        switch (command) {
            case MOVE_LEFT -> Move.MAPPER.get(entity).accumulate(-0.5f, 0);
            case MOVE_RIGHT -> Move.MAPPER.get(entity).accumulate(0.5f, 0);
        }
    }

    @Override
    public void update() {
        final var rigidBodyBox = RigidBodyBox.MAPPER.get(entity);
        if (rigidBodyBox.getBody().getLinearVelocity().y >= 0) transition(MobLandState::new);
    }

    @Override
    public void exit() {
    }
}
