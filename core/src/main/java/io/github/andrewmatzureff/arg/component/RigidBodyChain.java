package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.IntStream;

import static io.github.andrewmatzureff.arg.GDXGame.WORLD_HEIGHT;
import static io.github.andrewmatzureff.arg.util.Optionals.peek;

public class RigidBodyChain implements Component {

    @Getter
    private final Body body;

    public RigidBodyChain(World world, Vector2[] vertices) {

        final var bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
//        bodyDef.position.set(0, -WORLD_HEIGHT / 2);
        this.body = world.createBody(bodyDef);

        final var chainShape = Optional.of(new ChainShape())
            .map(peek(chain -> chain.createChain(vertices)))
            .orElseThrow();

        final var fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0f;// 0.6f;
        final var fixture = body.createFixture(fixtureDef);

        chainShape.dispose();
    }

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<RigidBodyChain> MAPPER = ComponentMapper.getFor(RigidBodyChain.class);
}
