package io.github.andrewmatzureff.arg.util;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.Set;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class ConditionalBox2DDebugRenderer extends Box2DDebugRenderer {

    private final Set<String> omit;

    @Override
    protected void renderBody(Body body) {
        Optional.of(body)
            .map(Body::getUserData)
            .map(Object::toString)
            .filter(omit::contains)
            .ifPresentOrElse(__ -> {}, () -> super.renderBody(body));
    }

}
