package io.github.andrewmatzureff.arg.system;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.arg.util.ConditionalBox2DDebugRenderer;
import lombok.Data;
import lombok.Getter;
import lombok.Value;

import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import static io.github.andrewmatzureff.arg.util.Units.PIXELS_TO_METERS;

public class MapManager {

    @Getter
    public static class MapLayerContext {

        private int index;
        private MapLayer layer;
        private TiledMapTileSets mapTileSets;
        private MapProperties mapProperties;

        private MapLayerContext reset(int index, MapLayer layer, TiledMapTileSets mapTileSets, MapProperties mapProperties) {

            this.index = index;
            this.layer = layer;
            this.mapTileSets = mapTileSets;
            this.mapProperties = mapProperties;
            return this;
        }
    }

    private final AssetManager assetManager;

    @Getter
    private TiledMap map;

    @Getter
    private OrthogonalTiledMapRenderer renderer;

    public MapManager() {
        this.assetManager = new AssetManager(new InternalFileHandleResolver());
        this.assetManager.setLoader(TiledMap .class, new TmxMapLoader());
    }

    public void load(String file) {
        assetManager.load(file, TiledMap.class);
        map = assetManager.finishLoadingAsset(file);

//        final int mapWidthTiles = map.getProperties().get("width", Integer.class);
//        final int mapHeightTiles = map.getProperties().get("height", Integer.class);
//
//        final int tileWidthPixels = map.getProperties().get("tilewidth", Integer.class);
//        final int tileHeightPixels = map.getProperties().get("tileheight", Integer.class);
//
//        final float mapWidthPixels = mapWidthTiles * tileWidthPixels;
//        final float mapHeightPixels = mapHeightTiles * tileHeightPixels;

        renderer = new OrthogonalTiledMapRenderer(map, PIXELS_TO_METERS);
    }
}
