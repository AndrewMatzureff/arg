package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.andrewmatzureff.arg.component.RigidBodyBox;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.component.Transform;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.GDXGame.WORLD_WIDTH;
import static io.github.andrewmatzureff.arg.util.Optionals.peek;

public class GameplayMovementHandlerSystem extends IteratingSystem {

    public GameplayMovementHandlerSystem() {
        super(Family.all(Move.class, Transform.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var move = Move.MAPPER.get(entity);
        final var transform = Transform.MAPPER.get(entity);
        final var moveDirection = move.getDirection().scl(move.getSpeed());
        final var moveForce = move.getSpeed();
        final var rigidBody = RigidBodyBox.MAPPER.get(entity).getBody();
        if (rigidBody.getPosition().y < 0) {
            rigidBody.setTransform(100, 100, rigidBody.getAngle());
        }
        Optional.of(entity)
            .map(RigidBodyBox.MAPPER::get)
            .map(RigidBodyBox::getBody)
            .map(peek(body -> body.applyForceToCenter(moveDirection.scl(moveForce), true)))
            .map(peek(body -> body.applyTorque(-moveDirection.x * 0.25f, true)))
            .map(Body::getWorldCenter)
            .ifPresent(transform::setPosition);
        transform.setRotation(MathUtils.radDeg * rigidBody.getAngle());
        move.setDirection(Vector2.Zero);
    }
}
