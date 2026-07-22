package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.Transform;
import io.github.andrewmatzureff.arg.component.mob.*;

import java.util.Optional;

import static io.github.andrewmatzureff.arg.util.ComponentMappers.get;
import static io.github.andrewmatzureff.arg.util.Optionals.peek;

public class PhysicsControllerSystem extends IteratingSystem {

    public PhysicsControllerSystem() {
        super(Family.all(RigidBody.class, MobTraits.class).exclude().get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var rigidBody = get(entity, RigidBody.class);
        final var body = rigidBody.getBody();
        final var lmotion = rigidBody.getLinearMotion();
        final var lthrust = rigidBody.getLinearThrust();
        final var amotion = rigidBody.getAngularMotion();
        final var athrust = rigidBody.getAngularThrust();
        final var transform = Transform.MAPPER.get(entity);
        Optional.of(entity)
            .map(RigidBody.MAPPER::get)
            .map(RigidBody::getBody)
            .map(peek(b -> b.applyForceToCenter(lmotion.scl(1000), true)))
            .map(peek(b -> b.applyLinearImpulse(lthrust.scl(5), body.getPosition(), true)))
//            .map(peek(b -> b.applyTorque(-moveDirection.x * 0.25f, true)))
            .map(Body::getWorldCenter)
            .ifPresent(transform::setPosition);
        transform.setRotation(MathUtils.radDeg * body.getAngle());
        rigidBody.restore();
        final var vy = rigidBody.getBody().getLinearVelocity().y;
        rigidBody.setGrounded(vy > -0.25 && vy < 4); // TODO: implement contact listener
    }

    private boolean isPreJump(Jump jump) {
        return jump.isJumping() && System.currentTimeMillis() - jump.getTriggerTimeMillis() < 0;
    }

    private boolean isPostJump(Jump jump) {
        return jump.isJumping() && System.currentTimeMillis() - jump.getTriggerTimeMillis() >= 0;
    }
}
