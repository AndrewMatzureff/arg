package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import lombok.Getter;

import java.util.stream.IntStream;

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
        bodyDef.position.set(0, 0*-WORLD_HEIGHT / 2);
        this.body = world.createBody(bodyDef);

        final var chainShape = chainShape(width, height);//new ChainShape();
//        box.setAsBox(width / 2, height / 2);

        final var fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0f;// 0.6f;
        final var fixture = body.createFixture(fixtureDef);

        chainShape.dispose();
    }

    private ChainShape chainShape(float width, float height) {
        final var chain = new ChainShape();
        final var vertY = IntStream.range(0, (int) width)
            .asDoubleStream()
            .map(Math::sin)
            .map(y -> y * height)
            .mapToObj(d -> (float) d)
            .toArray(Float[]::new);
        final var verts = IntStream.range(0, (int) width)
            .mapToObj(i -> new Vector2(i - width / 2, vertY[i] + height / 2))
            .toArray(Vector2[]::new);
        chain.createChain(verts);
        return chain;
    }

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<DebugRigidBodyBox> MAPPER = ComponentMapper.getFor(DebugRigidBodyBox.class);
}
