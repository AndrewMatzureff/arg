package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.Transform;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.component.mob.Motion;
import io.github.andrewmatzureff.arg.component.mob.Move;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.Optionals.peek;

public class PhysicsSystem extends IteratingSystem {

    public PhysicsSystem() {
        super(Family.all(RigidBody.class, MobTraits.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var rigidBody = RigidBody.MAPPER.get(entity);
        final var mobTraits = MobTraits.MAPPER.get(entity);
        final var body = rigidBody.getBody();
        final var linearVelocity = body.getLinearVelocity();
        final var move = Move.MAPPER.get(entity);
        final var transform = Transform.MAPPER.get(entity);
        final var moveDirection = move.getDirection().scl(move.getSpeed());
        final var moveForce = move.getSpeed();
        Optional.of(entity)
            .map(RigidBody.MAPPER::get)
            .map(RigidBody::getBody)
            .map(peek(b -> b.applyForceToCenter(moveDirection.scl(moveForce), true)))
            .map(peek(b -> b.applyTorque(-moveDirection.x * 0.25f, true)))
            .map(Body::getWorldCenter)
            .ifPresent(transform::setPosition);
        transform.setRotation(MathUtils.radDeg * body.getAngle());
        move.setDirection(Vector2.Zero);

        switch ((int) Math.signum(linearVelocity.y)) {
            case  1 -> mobTraits.add(Motion.MOVING_Y_POSITIVE);
            case -1 -> mobTraits.add(Motion.MOVING_Y_NEGATIVE);
        }

        switch ((int) Math.signum(linearVelocity.x)) {
            case  1 -> mobTraits.add(Motion.MOVING_X_POSITIVE);
            case -1 -> mobTraits.add(Motion.MOVING_X_NEGATIVE);
        }


    }
}
