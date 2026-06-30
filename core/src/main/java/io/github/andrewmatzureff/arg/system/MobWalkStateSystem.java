package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.animations.Player;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.mob.*;

import java.util.Optional;

import static com.badlogic.gdx.math.MathUtils.isZero;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public class MobWalkStateSystem extends AbstractMobStateIteratingSystem<MobWalkState> {

    public MobWalkStateSystem() {
        super(MobWalkState.class);
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
        final var jump = Jump.MAPPER.get(entity);
        if (jump.isJumping()) return some(new MobJumpState());
        final var move = Move.MAPPER.get(entity);
        if (isZero(move.getDirection().x)) return some(new MobIdleState());
        return none();
    }

    @Override
    public void exit(Entity entity, float deltaTime) {
    }
}
