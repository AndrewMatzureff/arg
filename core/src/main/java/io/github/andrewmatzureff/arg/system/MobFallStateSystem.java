package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animation.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.component.mob.Motion;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.state.MobFallState;
import io.github.andrewmatzureff.arg.state.MobLandState;
import io.github.andrewmatzureff.arg.state.MobState;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobFallStateSystem extends AbstractMobStateIteratingSystem<MobFallState> {

    public MobFallStateSystem() {
        super(MobFallState.class, MobTraits.class);
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
        final var traits = MobTraits.MAPPER.get(entity);
        if (traits.have(Motion.MOVING_Y_POSITIVE) || traits.lack(Motion.MOVING)) return some(new MobLandState());
        return none();
    }

    @Override
    protected void exit(Entity entity, float deltaTime) {

    }
}
