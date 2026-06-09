package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.physics.box2d.*;
import lombok.Getter;

import static io.github.andrewmatzureff.arg.GDXGame.WORLD_WIDTH;

public class RigidBodyBox implements Component {

    @Getter
    private final Body body;
    @Getter
    private final float width;
    @Getter
    private final float height;
    @Getter
    private final boolean debugDraw = false;

    public RigidBodyBox(World world, float width, float height) {

        this.width = width;
        this.height = height;

        final var bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(75, 75);
        this.body = world.createBody(bodyDef);
        body.setUserData("player");

        final var box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);

        final var fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        // average human density: 985kg/m^3
        // cube root density: 9.9497478956
        // cube root squared: 98.99748318599662849936
        fixtureDef.density = 100f; // 0.5f
        fixtureDef.friction = 0.25f; // 0.4f
        fixtureDef.restitution = 0.25f; // 0.6f
        final var fixture = body.createFixture(fixtureDef);

        box.dispose();
        body.setLinearVelocity(0, -10);
    }

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<RigidBodyBox> MAPPER = ComponentMapper.getFor(RigidBodyBox.class);
}
