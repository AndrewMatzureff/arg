package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

public record MobJumpState(Entity entity) implements MobState {

    @Override
    public void enter() {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(ac -> ac.setAnimationState(AnimationController.JUMP)))
            .ifPresent(ac -> ac.setLooping(false));

        Optional.of(entity)
            .map(Jump.MAPPER::get)
            .ifPresent(jump -> jump.setTriggered(true));
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
        final var jump = Jump.MAPPER.get(entity);
        final var move = Move.MAPPER.get(entity);
        final var rigidBodyBox = RigidBody.MAPPER.get(entity);
        final var animationController = AnimationController.MAPPER.get(entity);
        if (jump.isTriggered()) move.accumulate(0, 75);
        if (animationController.isFinished() && rigidBodyBox.getBody().getLinearVelocity().y <= 0) transition(MobFallState::new);
        jump.setTriggered(false);
    }

    @Override
    public void exit() {
    }
}
