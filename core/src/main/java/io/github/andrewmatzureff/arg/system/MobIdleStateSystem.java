package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animation.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.state.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static io.github.andrewmatzureff.arg.util.ComponentMappers.get;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobIdleStateSystem extends AbstractMobStateIteratingSystem<MobIdleState> {
    private final Set<Entity> jumps = new HashSet<>();

    public MobIdleStateSystem() {
        super(MobIdleState.class);
    }

    @Override
    public void handle(Command command, Entity entity, float deltaTime) {
        switch (command) {
            case NO_OP -> {}
            case MOVE_LEFT -> {
                get(entity, RigidBody.class)
                    .addLinearMotion(-1f, 0f);
            }
            case MOVE_RIGHT -> {
                get(entity, RigidBody.class)
                    .addLinearMotion(1f, 0f);
            }
            case JUMP -> {
                jumps.add(entity);
            }
        }
    }

    @Override
    protected ComponentMapper<MobIdleState> mapper() {
        return MobIdleState.MAPPER;
    }

    @Override
    public void enter(Entity entity, float deltaTime) {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(peek(ac -> ac.setAnimation(Player.Clip.IDLE.animation())))
            .ifPresent(controller -> controller.setLooping(true));
    }

    @Override
    public Optional<MobState> update(Entity entity, float deltaTime) {
        final var rigidBody = get(entity, RigidBody.class);
        if (!rigidBody.isGrounded())
            return some(new MobFallState());
        if (jumps.remove(entity))
            return some(new MobJumpState());
        if (rigidBody.hasLinearMotion())
            return some(new MobWalkState());
        return none();
    }

    @Override
    public void exit(Entity entity, float deltaTime) {
        jumps.clear();
    }
}
