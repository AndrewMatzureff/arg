package io.github.andrewmatzureff.arg.util;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.badlogic.gdx.math.MathUtils.isZero;
import static io.github.andrewmatzureff.arg.util.Optionals.*;
import static io.github.andrewmatzureff.arg.util.Units.PIXELS_TO_METERS;
import static io.github.andrewmatzureff.arg.util.Units.TWO_PI;

@AllArgsConstructor
public class Box2DFixtureDefiner {

    public FixtureDef toFixtureDef(MapObject collisionBoundary) {
        final var props = collisionBoundary.getProperties();

        final var result = switch (collisionBoundary) {
            case RectangleMapObject rectangle -> toBoxFixtureDef(rectangle);
            case PolylineMapObject polyline -> toChainFixtureDef(polyline);
            case PolygonMapObject polygon -> toPolygonFixtureDef(polygon);
            case EllipseMapObject ellipse -> toCircleFixtureDef(ellipse);
            case null, default -> throw new RuntimeException("Something went wrong..."); // TODO: throw a more descriptive error
        };

        result.friction     = props.get("friction",     result.friction,    Float.class);
        result.restitution  = props.get("restitution",  result.restitution, Float.class);
        result.density      = props.get("density",      result.density,     Float.class);
        result.isSensor     = props.get("isSensor",     result.isSensor,    Boolean.class);
        // TODO: implement Filter props

        return result;
    }

    private FixtureDef toCircleFixtureDef(EllipseMapObject ellipse) {
        final var result = new FixtureDef();

        result.shape = some(ellipse)
            .map(EllipseMapObject::getEllipse)
            .filter(e -> isZero(e.width - e.height))
            .map(remap(new Circle(), (c, e) -> c.set(e.x, e.y, e.circumference() / TWO_PI)))
            .map(remap(new CircleShape(),
                (cs, c) -> cs.setPosition(new Vector2(c.x, c.y)),
                (cs, c) -> cs.setRadius(c.radius)))
            .orElseThrow(); // TODO: throw a more descriptive error

        return result;
    }

    private FixtureDef toBoxFixtureDef(RectangleMapObject rectangle) {
//        final var angle = rectangle.getProperties()
//            .get("rotation", Float.class);
        final var result = new FixtureDef();

        result.shape = some(rectangle)
            .map(RectangleMapObject::getRectangle)
            .map(this::toPolygon)
//            .map(peek(polygon -> polygon.rotate(angle)))
            .map(this::toTransformedVertexArray)
            .map(remap(new PolygonShape(), PolygonShape::set))
            .orElseThrow(); // TODO: throw a more descriptive error

        return result;
    }

    private FixtureDef toChainFixtureDef(PolylineMapObject polyline) {
        final var result = new FixtureDef();

        result.shape = some(polyline)
            .map(PolylineMapObject::getPolyline)
            .map(this::toTransformedVertexArray)
            .map(remap(new ChainShape(), ChainShape::createChain))
            .orElseThrow(); // TODO: throw a more descriptive error

        return result;
    }

    private FixtureDef toPolygonFixtureDef(PolygonMapObject polygon) {
        final var result = new FixtureDef();

        result.shape = some(polygon)
            .map(PolygonMapObject::getPolygon)
            .map(this::toTransformedVertexArray)
            .map(remap(new PolygonShape(), PolygonShape::set))
            .orElseThrow(); // TODO: throw a more descriptive error

        return result;
    }

    private Vector2[] toTransformedVertexArray(Shape2D polyshape) {
        return (switch (polyshape) {
            case Polyline polyline -> some(polyline)
                .map(Polyline::getTransformedVertices);
            case Polygon polygon -> some(polygon)
                .map(Polygon::getTransformedVertices);
            default -> throw new RuntimeException("Something went wrong..."); // TODO: throw a more descriptive error
        })
            .map(this::toVectorArray)
            .map(Arrays::stream)
            .map(stream -> stream.peek(v -> v.scl(PIXELS_TO_METERS)))
            .map(stream -> stream.toArray(Vector2[]::new))
            .orElseThrow(); // TODO: throw a more descriptive error
    }

    private Polygon toPolygon(Rectangle rectangle) {
        final var center = some(rectangle)
            .filter(r -> !(isZero(r.getWidth()) || isZero(r.getHeight())))
            .map(r -> r.getCenter(new Vector2()))
            .orElseThrow(); // TODO: throw a more descriptive error
        final var polygon = new Polygon(new float[] {
            0,                      0,
            0,                      rectangle.getHeight(),
            rectangle.getWidth(),   rectangle.getHeight(),
            rectangle.getWidth(),   0});
        polygon.translate(rectangle.getX() - center.x, rectangle.getY() - center.y);
        return polygon;
    }

    private Vector2[] toVectorArray(float[] vertices) {
        return IntStream.range(0, vertices.length / 2)
            .mapToObj(i -> new Vector2(vertices[2 * i], vertices[2 * i + 1]))
            .toArray(Vector2[]::new);
    }
}
