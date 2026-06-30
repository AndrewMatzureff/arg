package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animations.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.mob.MobFallState;
import io.github.andrewmatzureff.arg.mob.MobLandState;
import io.github.andrewmatzureff.arg.mob.MobState;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobFallStateSystem extends AbstractMobStateIteratingSystem<MobFallState> {

    public MobFallStateSystem() {
        super(MobFallState.class);
    }

    @Override
    protected void handle(Command command, Entity entity, float deltaTime) {
        switch (command) {
            case MOVE_LEFT -> Move.MAPPER.get(entity).left(0.5f);
            case MOVE_RIGHT -> Move.MAPPER.get(entity).right(0.5f);
        }
    }

    @Override
    protected ComponentMapper<MobFallState> mapper() {
        return MobFallState.MAPPER;
    }

    @Override
    protected void enter(Entity entity, float deltaTime) {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .ifPresent(ac -> ac.setAnimation(Player.Clip.FALL.animation()));
    }

    @Override
    protected Optional<MobState> update(Entity entity, float deltaTime) {
        final var rigidBodyBox = RigidBody.MAPPER.get(entity);
        if (rigidBodyBox.getBody().getLinearVelocity().y >= 0) return some(new MobLandState());
        return none();
    }

    @Override
    protected void exit(Entity entity, float deltaTime) {

    }
}
