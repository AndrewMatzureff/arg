package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import io.github.andrewmatzureff.arg.component.GameplayCommandBuffer;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.component.mob.Motion;
import io.github.andrewmatzureff.arg.component.mob.Move;

import static com.badlogic.gdx.math.MathUtils.isZero;
import static io.github.andrewmatzureff.arg.util.Optionals.some;
import static java.util.function.Predicate.not;

public class RigidBodyTraitSystem extends IteratingSystem {

    public RigidBodyTraitSystem() {
        super(Family.all(RigidBody.class, MobTraits.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var rigidBody = RigidBody.MAPPER.get(entity);
        final var mobTraits = MobTraits.MAPPER.get(entity);
        final var body = rigidBody.getBody();
        final var linearVelocity = body.getLinearVelocity();
        if (linearVelocity.y < 0) mobTraits.add(Motion.MOVING_Y);
    }
}
