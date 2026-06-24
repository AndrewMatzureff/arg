package io.github.andrewmatzureff.arg.util;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

import static io.github.andrewmatzureff.arg.util.Optionals.some;

@RequiredArgsConstructor
public class ConditionalBox2DDebugRenderer extends Box2DDebugRenderer {

    private final Predicate<Object> omit;

    @Override
    protected void renderBody(Body body) {
        some(body)
            .map(Body::getUserData)
            .filter(omit)
            .ifPresentOrElse(__ -> {}, () -> super.renderBody(body));
    }
}
