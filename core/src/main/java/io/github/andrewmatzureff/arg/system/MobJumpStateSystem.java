package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animations.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.*;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.mob.*;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.Optionals.none;
import static io.github.andrewmatzureff.arg.util.Optionals.some;

public class MobJumpStateSystem extends AbstractMobStateIteratingSystem<MobJumpState> {

    public MobJumpStateSystem() {
        super(MobJumpState.class);
    }

    @Override
    public void handle(Command command, Entity entity, float deltaTime) {
        switch (command) {
            case MOVE_LEFT -> Move.MAPPER.get(entity).left(0.5f);
            case MOVE_RIGHT -> Move.MAPPER.get(entity).right(0.5f);
        }
    }

    @Override
    protected ComponentMapper<MobJumpState> mapper() {
        return MobJumpState.MAPPER;
    }

    @Override
    public void enter(Entity entity, float deltaTime) {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(ac -> ac.setAnimation(Player.Clip.JUMP.animation())))
            .ifPresent(ac -> ac.setLooping(false));
    }

    @Override
    public Optional<MobState> update(Entity entity, float deltaTime) {
        final var jump = Jump.MAPPER.get(entity);
        final var move = Move.MAPPER.get(entity);
        final var traits = MobTraits.MAPPER.get(entity);
        final var rigidBodyBox = RigidBody.MAPPER.get(entity);
        final var animationController = AnimationController.MAPPER.get(entity);
//        if (traits.have(Maneuver.POST_JUMP)) move.up(75f);
        if (traits.have(Timing.ANIMATION_FINISHED) && rigidBodyBox.getBody().getLinearVelocity().y <= 0) return  some(new MobFallState());
        return none();
    }

    @Override
    public void exit(Entity entity, float deltaTime) {
    }
}
