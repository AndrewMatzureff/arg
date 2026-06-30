package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animations.reels.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.mob.*;

import java.util.Optional;

import static com.badlogic.gdx.math.MathUtils.isZero;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobIdleStateSystem extends AbstractMobStateIteratingSystem<MobIdleState> {

    public MobIdleStateSystem() {
        super(MobIdleState.class);
    }

    @Override
    public void handle(Command command, Entity entity, float deltaTime) {
        final var move = Move.MAPPER.get(entity);
        final var rigidBodyBox = RigidBody.MAPPER.get(entity);
        final var body = rigidBodyBox.getBody();
        switch (command) {
            case MOVE_LEFT -> {
                move.left(1f);
                body.applyTorque(move.getDirection().x * 255f, true);
            }
            case MOVE_RIGHT -> {
                move.right(1f);
                body.applyTorque(move.getDirection().x * 255f, true);
            }
            case JUMP -> {
                Jump.MAPPER.get(entity)
                    .begin();
            }
            default -> {}
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
        final var jump = Jump.MAPPER.get(entity);
        if (jump.isJumping()) return some(new MobJumpState());
        final var move = Move.MAPPER.get(entity);
        if (isZero(move.getDirection().x)) return none();
        return some(new MobWalkState());
    }

    @Override
    public void exit(Entity entity, float deltaTime) {

    }
}
