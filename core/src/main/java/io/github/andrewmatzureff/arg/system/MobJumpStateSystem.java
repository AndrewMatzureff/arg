package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Timer;
import io.github.andrewmatzureff.arg.animation.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.*;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.state.*;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.ComponentMappers.get;
import static io.github.andrewmatzureff.arg.util.ComponentMappers.maybe;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobJumpStateSystem extends AbstractMobStateIteratingSystem<MobJumpState> {

    public MobJumpStateSystem() {
        super(MobJumpState.class);
    }

    @Override
    public void handle(Command command, Entity entity, float deltaTime) {
        final var rigidBody = get(entity, RigidBody.class);
        switch (command) {
            case NO_OP -> {}
            case MOVE_LEFT -> {
                rigidBody.addLinearMotion(-1f, 0f);
            }
            case MOVE_RIGHT -> {
                rigidBody.addLinearMotion(1f, 0f);
            }
            case JUMP -> {}
        }
    }

    @Override
    protected ComponentMapper<MobJumpState> mapper() {
        return MobJumpState.MAPPER;
    }

    @Override
    public void enter(Entity entity, float deltaTime) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                maybe(entity, RigidBody.class)
                    .ifPresent(rigidBody -> rigidBody.addLinearThrust(0f, 1f));
            }
        }, 0.2f);
        some(entity)
            .map(AnimationController.MAPPER::get)
            .map(Optionals.peek(ac -> ac.setAnimation(Player.Clip.JUMP.animation())))
            .ifPresent(ac -> ac.setLooping(false));
    }

    @Override
    public Optional<MobState> update(Entity entity, float deltaTime) {
        final var animationController = AnimationController.MAPPER.get(entity);
        if (animationController.isFinished())
            return some(new MobFallState());
        return none();
    }

    @Override
    public void exit(Entity entity, float deltaTime) {
    }
}
