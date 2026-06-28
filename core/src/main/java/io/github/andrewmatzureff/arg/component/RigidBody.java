package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import io.github.andrewmatzureff.arg.mob.MobState;
import lombok.Getter;

import java.util.List;

public class RigidBody implements Component {

    @Getter
    private final Body body;
    @Getter
    private final boolean debugDraw = false;

    public RigidBody(World world, BodyDef.BodyType type, List<FixtureDef> fixtureDefs) {
        this(world, type, Vector2.Zero, fixtureDefs);
    }

    public RigidBody(World world, BodyDef.BodyType type, Vector2 position, List<FixtureDef> fixtureDefs) {

        final var bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(position);
        this.body = world.createBody(bodyDef);
        fixtureDefs.forEach(body::createFixture);
        fixtureDefs.forEach(def -> def.shape.dispose()); // TODO: implement pooling strategy
        // average human density: 985kg/m^3
        // cube root density: 9.9497478956
        // cube root squared: 98.99748318599662849936
    }

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<RigidBody> MAPPER = ComponentMapper.getFor(RigidBody.class);

//    public enum Trait implements MobState.Trait {
//        GROUNDED,
//        MOVING_X,
//        MOVING_Y,
//    }
}
