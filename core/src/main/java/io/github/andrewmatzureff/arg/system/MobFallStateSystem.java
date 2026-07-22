package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animation.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.state.MobFallState;
import io.github.andrewmatzureff.arg.state.MobLandState;
import io.github.andrewmatzureff.arg.state.MobState;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.ComponentMappers.*;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobFallStateSystem extends AbstractMobStateIteratingSystem<MobFallState> {

    public MobFallStateSystem() {
        super(MobFallState.class);
    }

    @Override
    protected void handle(Command command, Entity entity, float deltaTime) {

        switch (command) {
            case NO_OP -> {}
            case MOVE_LEFT -> {
                final var rigidBody = get(entity, RigidBody.class);
                rigidBody.addLinearMotion(-1f, 0f);
            }
            case MOVE_RIGHT -> {
                final var rigidBody = get(entity, RigidBody.class);
                rigidBody.addLinearMotion(1f, 0f);
            }
            case JUMP -> {}
        }
    }

    @Override
    protected ComponentMapper<MobFallState> mapper() {
        return MobFallState.MAPPER;
    }

    @Override
    protected void enter(Entity entity, float deltaTime) {
        // TODO: implement animation queueing so that non-interrupting transitions do not need to be orchestrated by state handlers
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .ifPresent(ac -> ac.setAnimation(Player.Clip.FALL.animation()));
    }

    @Override
    protected Optional<MobState> update(Entity entity, float deltaTime) {
        // TODO: remove mob traits construct and go back to raw component manipulation...?
        final var rigidBody = get(entity, RigidBody.class);
        if (rigidBody.isGrounded())
            return some(new MobLandState());
        return none();
    }

    @Override
    protected void exit(Entity entity, float deltaTime) {

    }
}
