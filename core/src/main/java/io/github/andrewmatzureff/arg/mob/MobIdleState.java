package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBodyBox;
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
        if (command == Command.MOVE_LEFT || command == Command.MOVE_RIGHT) {
            transition(MobWalkState::new)
                .handle(command);
        } else if (command == Command.JUMP) {
            transition(MobJumpState::new)
                .handle(command);
        }
    }

    @Override
    public void update() {
        Optional.of(entity)
            .map(RigidBodyBox.MAPPER::get)
            .map(RigidBodyBox::getBody)
            .map(Body::getLinearVelocity)
            .filter(v -> v.y < -2f)
            .ifPresent(v -> transition(MobFallState::new));
    }

    @Override
    public void exit() {

    }
}
