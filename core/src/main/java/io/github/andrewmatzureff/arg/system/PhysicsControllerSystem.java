package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.Transform;
import io.github.andrewmatzureff.arg.component.mob.*;
import io.github.andrewmatzureff.arg.util.CopyPool;

import java.util.Optional;

import static com.badlogic.gdx.math.MathUtils.radDeg;
import static io.github.andrewmatzureff.arg.util.ComponentMappers.get;
import static io.github.andrewmatzureff.arg.util.Optionals.peek;
import static io.github.andrewmatzureff.arg.util.Optionals.some;

public class PhysicsControllerSystem extends IteratingSystem implements ContactListener {
    private static final float slopeThreshold = (float) Math.cos(Math.PI / 4);
//    private final CopyPool<Vector2> vec2 = new CopyPool<>(true, Vector2::cpy, Vector2::set);

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
        body.getWorld()
            .setContactListener(this);
        stabilize(rigidBody, deltaTime);
        Optional.of(entity)
            .map(RigidBody.MAPPER::get)
            .map(RigidBody::getBody)
            .map(peek(b -> b.applyForceToCenter(lmotion.scl(1000), true)))
            .map(peek(b -> b.applyLinearImpulse(lthrust.scl(750f), body.getPosition(), true)))
//            .map(peek(b -> b.applyForceToCenter(lthrust.scl(500), true)))
//            .map(peek(b -> b.applyTorque(-lmotion.x * 0.0125f, true)))
            .map(Body::getWorldCenter)
            .ifPresent(transform::setPosition);
        transform.setRotation(radDeg * body.getAngle());
        rigidBody.restore();
    }

    private void stabilize(RigidBody rigidBody, float deltaTime) {
        final var body = rigidBody.getBody();
        final var angle = body.getAngle();
        final var phase = 180f;
        final var degrees = (radDeg * angle + phase) % 360f - phase; // Convert radians to degrees; map to target range (-180, +180).
        final var strength = 5f;
        body.applyAngularImpulse(-strength * degrees / phase, true);
        rigidBody.setUpright(Math.abs(degrees) <= 45);
    }

    private void setGrounded(Contact contact) {
        final var rigidBodyA = some(contact)
            .map(Contact::getFixtureA)
            .map(Fixture::getBody)
            .map(Body::getUserData)
            .map(RigidBody.class::cast)
            .orElseThrow(); // TODO: throw a more descriptive error
        final var rigidBodyB = some(contact)
            .map(Contact::getFixtureB)
            .map(Fixture::getBody)
            .map(Body::getUserData)
            .map(RigidBody.class::cast)
            .orElseThrow(); // TODO: throw a more descriptive error
        final var normalAB = contact.getWorldManifold()
            .getNormal(); // Collision normal; direction of force applied in response to contact event.
        final var normalBA = normalAB.cpy()
            .scl(-1); // Reverse collision normal; opposite direction of force applied in response to contact event.
        final var up = rigidBodyA.getBody() // Direction of "up" with respect to gravity; the opposite direction to gravity.
            .getWorld()
            .getGravity()
            .cpy()
            .scl(-1)
            .nor();
        if (contact.isTouching() && normalAB.dot(up) >= slopeThreshold) rigidBodyB.addGrounded(); // If fixtures have collided and collision normal from A -> B is close enough to "up" direction then B is grounded.
        if (contact.isTouching() && normalBA.dot(up) >= slopeThreshold) rigidBodyA.addGrounded(); // If fixtures have collided and collision normal from B -> A is close enough to "up" direction then A is grounded.
    }

    @Override
    public void beginContact(Contact contact) {}

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {setGrounded(contact);}
}
