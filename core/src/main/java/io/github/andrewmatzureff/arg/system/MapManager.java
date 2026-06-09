package io.github.andrewmatzureff.arg.system;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import lombok.Getter;

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

    private final TmxMapLoader tmx = new TmxMapLoader();

    @Getter
    private TiledMap map;

    @Getter
    private OrthogonalTiledMapRenderer renderer;

    public void load(String file) {
        this.map = tmx.load(file);

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
