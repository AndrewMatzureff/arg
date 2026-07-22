package io.github.andrewmatzureff.arg.util;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import io.github.andrewmatzureff.arg.component.*;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.state.*;
import io.github.andrewmatzureff.arg.system.PhysicsManager;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.andrewmatzureff.arg.util.Optionals.remap;
import static io.github.andrewmatzureff.arg.util.Optionals.some;
import static io.github.andrewmatzureff.arg.util.Units.PIXELS_TO_METERS;

public class MapManager {

    public record MapLayerContext<T extends MapLayer>(int index, T layer, TiledMapTileSets mapTileSets, MapProperties mapProperties) {

        private <U extends T> MapLayerContext<U> reclassified(U instance) {
            if (instance != layer) throw new IllegalArgumentException("Specified re-class reference must refer to the exact same layer instance as the existing context!");
            return new MapLayerContext<>(index, instance, mapTileSets, mapProperties);
        }
    }

    private final Engine engine;
    private final PhysicsManager physicsManager;
    private final TmxMapLoader tmx;
    private final Box2DFixtureDefiner b2d;

    @Getter
    private TiledMap map;

    @Getter
    private OrthogonalTiledMapRenderer renderer;

    public MapManager(Engine engine, PhysicsManager physicsManager) {
        this.engine = engine;
        this.physicsManager = physicsManager;
        this.tmx = new TmxMapLoader();
        this.b2d = new Box2DFixtureDefiner();
    }

    public void load(String file) {
        final var params = some("main.tiled-project")
            .map(remap(new BaseTiledMapLoader.Parameters(), (lp, fp) -> lp.projectFilePath = fp))
            .orElseThrow(); // TODO: throw a more descriptive error
        map = tmx.load(file, params);
        renderer = new OrthogonalTiledMapRenderer(map, PIXELS_TO_METERS);
        final var index = new int[1];
        map.getLayers()
            .forEach(layer -> Optional.of(layer)
                .map(l -> new MapLayerContext<>(index[0]++, l, map.getTileSets(), map.getProperties()))
                .ifPresent(this::loadLayer));
    }

    private void loadLayer(MapLayerContext<?> context) {
        final var layer = context.layer;
        switch (layer) {
            case TiledMapImageLayer imageLayer -> {}
            case TiledMapTileLayer tileLayer -> {}
            case MapGroupLayer groupLayer -> throw new UnsupportedOperationException("Unsupported layer class: " + MapGroupLayer.class.getSimpleName());
            default -> loadObjectLayer(context);
        }
    }

    private void loadObjectLayer(MapLayerContext<?> context) {
        final var layerEntity = engine.createEntity();
        final var terrain = new ArrayList<FixtureDef>();
        some(context)
            .map(MapLayerContext::layer)
            .map(MapLayer::getObjects)
            .map(Streams::stream)
            .orElseThrow() // TODO: throw more descriptive exception
            .forEach(object -> {
                if (object instanceof TiledMapTileMapObject) {
                    loadMob((TiledMapTileMapObject) object, context);
                } else some(object)
                    .map(b2d::toFixtureDef)
                    .ifPresent(terrain::add);
            });
        layerEntity.add(new RigidBody(physicsManager.getWorld(), BodyDef.BodyType.StaticBody, terrain));
        engine.addEntity(layerEntity);
    }

    private void loadMob(TiledMapTileMapObject mob, MapLayerContext<?> context) {
        final var props = mob.getProperties();
        final var position = new Vector2(mob.getX(), mob.getY())
            .add(props.get("width", Float.class) / 2, props.get("height", Float.class) / 2)
            .scl(PIXELS_TO_METERS);
        final var type = props.get(MobProperties.TYPE, String.class);
        switch (MobProperties.Type.valueOf(type)) {
            case Player -> {
                final var defs = some(mob)
                    .map(TiledMapTileMapObject::getTile)
                    .map(TiledMapTile::getObjects)
                    .map(Streams::stream)
                    .orElse(Stream.empty())
                    .map(b2d::toFixtureDef)
                    .toList();
                final var player = engine.createEntity()
                    .add(new KeyboardBuffer())
                    .add(new GameplayCommandBuffer())
                    .add(new StateManager())
//                    .add(new Jump())
//                    .add(new Move())
                    .add(new Transform())
                    .add(new AnimationController())
                    .add(new SpriteRenderer())
                    .add(new RigidBody(physicsManager.getWorld(), BodyDef.BodyType.DynamicBody, position, defs))//1f, 1.75f)) // 1f, 2f
                    .add(new CameraController())
                    .add(new MobFallState())
                    .add(new MobTraits());
                this.engine.addEntity(player);
            }
            case Prop -> {}
            default -> {}
        }
    }
}
