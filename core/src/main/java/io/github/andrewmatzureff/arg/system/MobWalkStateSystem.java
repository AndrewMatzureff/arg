package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animation.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.state.*;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.ComponentMappers.get;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobWalkStateSystem extends AbstractMobStateIteratingSystem<MobWalkState> {

    public MobWalkStateSystem() {
        super(MobWalkState.class);
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
            case JUMP -> {
                rigidBody.addLinearThrust(0f, 1f);
            }
        }
    }

    @Override
    protected ComponentMapper<MobWalkState> mapper() {
        return MobWalkState.MAPPER;
    }

    @Override
    public void enter(Entity entity, float deltaTime) {
        Optional.of(entity)
            .map(AnimationController.MAPPER::get)
            .map(peek(controller -> controller.setAnimation(Player.Clip.WALK.animation())))
            .ifPresent(controller -> controller.setLooping(true));
    }

    @Override
    public Optional<MobState> update(Entity entity, float deltaTime) {
        final var rigidBody = get(entity, RigidBody.class);
        if (!rigidBody.isGrounded() ||
            rigidBody.hasAngularMotion())
            return some(new MobFallState());
        if (rigidBody.hasLinearThrust())
            return some(new MobJumpState());
        if (!rigidBody.hasLinearMotion())
            return some(new MobIdleState());
        return none();
    }

    @Override
    public void exit(Entity entity, float deltaTime) {
    }
}
