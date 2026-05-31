package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.component.Transform;
import io.github.andrewmatzureff.arg.util.Optionals;

import java.util.Optional;

public class GameplayMovementHandlerSystem extends IteratingSystem {

    public GameplayMovementHandlerSystem() {
        super(Family.all(Move.class, Transform.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var move = Move.MAPPER.get(entity);
        move.setMultiplicativeDamping(0.9f);
        final var transform = Transform.MAPPER.get(entity);
        // TODO: move or get rid of...
        move.setSpeed(0.25f);
        move.accumulate(0, -5f);
        if (transform.getPosition().y < 0) {
            transform.setPosition(transform.getPosition().x, 0);
//            move.setDirection(move.getDirection().set(move.getDirection().x, 0));
        }
        final var moveDirection = move.getDirection().scl(move.getSpeed());
        final var addDampFactor = Vector2.One.cpy().setLength(move.getAdditiveDamping());
        final var mulDampFactor = Vector2.One.cpy().scl(move.getMultiplicativeDamping());
        final var dampingThreshold = move.getDampingThreshold();
        Optional.of(moveDirection)
            .map(dir -> mulDamp(dir, mulDampFactor, dampingThreshold))
            .map(dir -> addDamp(dir, addDampFactor, dampingThreshold))
            .map(Optionals.peek(move::setDirection))
            .map(transform.getPosition()::add)
            .ifPresent(transform::setPosition);
    }

    private Vector2 addDamp(Vector2 direction, Vector2 dampFactor, float zeroThreshold) {
        final var xDampFactor = Math.signum(dampFactor.x) == Math.signum(direction.x)
            ? -dampFactor.x
            : dampFactor.x;
        final var yDampFactor = Math.signum(dampFactor.y) == Math.signum(direction.y)
            ? -dampFactor.y
            : dampFactor.y;
        return dampClamp(direction.add(xDampFactor, yDampFactor), zeroThreshold);
    }

    private Vector2 mulDamp(Vector2 direction, Vector2 dampFactor, float zeroThreshold) {
        return dampClamp(direction.scl(dampFactor), zeroThreshold);
    }

    private Vector2 dampClamp(Vector2 values, float tolerance) {
        return values.set(dampClamp(values.x, tolerance), dampClamp(values.y, tolerance));
    }

    private float dampClamp(float value, float tolerance) {
        return MathUtils.isZero(value, Math.abs(tolerance)) ? 0 : value;
    }
}
