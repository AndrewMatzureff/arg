package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.physics.box2d.*;
import lombok.Getter;

import static io.github.andrewmatzureff.arg.GDXGame.WORLD_HEIGHT;

public class DebugRigidBodyBox implements Component {

    @Getter
    private final Body body;
    @Getter
    private final float width;
    @Getter
    private final float height;

    public DebugRigidBodyBox(World world, float width, float height) {

        this.width = width;
        this.height = height;

        final var bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, -WORLD_HEIGHT / 2);
        this.body = world.createBody(bodyDef);

        final var box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);

        final var fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0f;// 0.6f;
        final var fixture = body.createFixture(fixtureDef);

        box.dispose();
    }

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<DebugRigidBodyBox> MAPPER = ComponentMapper.getFor(DebugRigidBodyBox.class);
}
