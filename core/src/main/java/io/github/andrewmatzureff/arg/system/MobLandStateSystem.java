package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animations.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.mob.MobIdleState;
import io.github.andrewmatzureff.arg.mob.MobLandState;
import io.github.andrewmatzureff.arg.mob.MobState;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.Optionals.none;
import static io.github.andrewmatzureff.arg.util.Optionals.some;

public class MobLandStateSystem extends AbstractMobStateIteratingSystem<MobLandState> {

    public MobLandStateSystem() {
        super(MobLandState.class);
    }

    @Override
    public void handle(Command command, Entity entity, float deltaTime) {
        switch (command) {
            case MOVE_LEFT -> Move.MAPPER.get(entity).left(0.5f);
            case MOVE_RIGHT -> Move.MAPPER.get(entity).right(0.5f);
        }
    }

    @Override
    protected ComponentMapper<MobLandState> mapper() {
        return MobLandState.MAPPER;
    }

    @Override
    public void enter(Entity entity, float deltaTime) {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(ac -> ac.setAnimation(Player.Clip.LAND.animation())))
            .ifPresent(ac -> ac.setLooping(false));
    }

    @Override
    public Optional<MobState> update(Entity entity, float deltaTime) {
        final var animationController = AnimationController.MAPPER.get(entity);
        if (animationController.isFinished()) return some(new MobIdleState());
        return none();
    }

    @Override
    public void exit(Entity entity, float deltaTime) {
    }
}
