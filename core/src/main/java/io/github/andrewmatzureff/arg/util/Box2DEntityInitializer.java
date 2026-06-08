package io.github.andrewmatzureff.arg.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import io.github.andrewmatzureff.arg.component.RigidBodyChain;
import io.github.andrewmatzureff.arg.system.PhysicsManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.github.andrewmatzureff.arg.util.Optionals.peek;
import static io.github.andrewmatzureff.arg.util.Units.PIXELS_TO_METERS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Box2DEntityInitializer {
    public static void init(PhysicsManager physicsManager, PolylineMapObject polyline, MapProperties mapProperties) {
        final int mapHeightPixels = Optional.of(mapProperties.get("height", Integer.class))
            .map(n -> n * mapProperties.get("tileheight", Integer.class))
            .orElseThrow();
        final var entity = physicsManager.getEngine().createEntity();
//        final Map<> positions = new IdentityHashMap<>();
        Optional.of(polyline)
            .map(PolylineMapObject::getPolyline)
            .map(Polyline::getTransformedVertices)
            .map(Box2DEntityInitializer::toVectorArray)
            .map(Arrays::stream)
            .map(stream -> stream.peek(v -> v.scl(PIXELS_TO_METERS)))
            .map(stream -> stream.toArray(Vector2[]::new))
            .map(verts -> new RigidBodyChain(physicsManager.getWorld(), verts))
            .ifPresent(entity::add);
    }

    private static Vector2[] toVectorArray(float[] vertices) {
        return IntStream.range(0, vertices.length / 2)
            .mapToObj(i -> new Vector2(vertices[2 * i], vertices[2 * i + 1]))
            .toArray(Vector2[]::new);
    }
}
