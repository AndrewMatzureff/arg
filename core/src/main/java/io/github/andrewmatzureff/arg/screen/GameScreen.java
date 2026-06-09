package io.github.andrewmatzureff.arg.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.andrewmatzureff.arg.GDXGame;
import io.github.andrewmatzureff.arg.component.*;
import io.github.andrewmatzureff.arg.component.DebugRigidBodyBox;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.GameplayDeviceInputAdapter;
import io.github.andrewmatzureff.arg.mob.MobFallState;
import io.github.andrewmatzureff.arg.system.*;
import io.github.andrewmatzureff.arg.mob.MobIdleState;
import io.github.andrewmatzureff.arg.util.Box2DEntityInitializer;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static io.github.andrewmatzureff.arg.GDXGame.WORLD_HEIGHT;
import static io.github.andrewmatzureff.arg.GDXGame.WORLD_WIDTH;
import static io.github.andrewmatzureff.arg.component.AnimationController.WALK;
import static java.util.stream.StreamSupport.stream;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {

    private final Engine engine;
    private final GDXGame game;
    private final GameplayDeviceInputAdapter gameplayDeviceInputAdapter;
    private final GameplayInputCommandAdapterSystem gameplayInputCommandAdapterSystem;
    private final CommandHandlerSystem commandHandlerSystem;
    private final GameplayMovementHandlerSystem gameplayMovementHandlerSystem;
    private final AnimationSystem animationSystem;
    private final SpriteRendererSystem spriteRendererSystem;
    private final PhysicsManager physicsManager;
    private final MapManager mapManager;

    public GameScreen(GDXGame game) {
        this.game = game;
        this.engine = new Engine();
        this.gameplayDeviceInputAdapter = new GameplayDeviceInputAdapter(engine);
        this.gameplayInputCommandAdapterSystem = new GameplayInputCommandAdapterSystem();
        this.commandHandlerSystem = new CommandHandlerSystem();
        this.gameplayMovementHandlerSystem = new GameplayMovementHandlerSystem();
        this.animationSystem = new AnimationSystem();
        this.spriteRendererSystem = new SpriteRendererSystem(game.getBatch());
        this.physicsManager = new PhysicsManager(engine, game.getViewport());

        this.engine.addSystem(gameplayInputCommandAdapterSystem);
        this.engine.addSystem(commandHandlerSystem);
        this.engine.addSystem(gameplayMovementHandlerSystem);
        this.engine.addSystem(animationSystem);
        this.engine.addSystem(new CameraControlSystem(game.getCamera()));
        this.engine.addSystem(spriteRendererSystem);

        final Entity player = engine.createEntity()
            .add(new KeyboardBuffer())
            .add(new GameplayCommandBuffer())
            .add(new StateManager())
            .add(new Jump())
            .add(new Move())
            .add(new Transform())
            .add(new AnimationController())
            .add(new SpriteRenderer())
            .add(new RigidBodyBox(physicsManager.getWorld(), 1f, 1.75f)) // 1f, 2f
//            .add(new DebugRigidBodyBox(physicsManager.getWorld(), 1000, 0.125f))
            .add(new CameraController());

        StateManager.MAPPER.get(player).setState(new MobIdleState(player));
        this.engine.addEntity(player);

        mapManager = new MapManager();
        mapManager.load("test.tmx");
        mapManager.getMap().getLayers().forEach(layer -> {
            switch (layer) {
                case TiledMapImageLayer imageLayer -> {}
                case TiledMapTileLayer tileLayer -> {}
                case MapGroupLayer groupLayer -> throw new UnsupportedOperationException("Unsupported layer type: " + MapGroupLayer.class.getSimpleName());
                default -> {
                    if (layer.getName().equals("box2d")) {
                        Optional.of(layer)
                            .map(MapLayer::getObjects)
                            .map(Iterable::spliterator)
                            .map(split -> stream(split, false))
                            .orElse(Stream.empty())
                            .forEach(object -> {
                                switch (object) {
                                    case PolylineMapObject polyline -> Box2DEntityInitializer.init(physicsManager, polyline, mapManager.getMap().getProperties());
                                    default -> throw new UnsupportedOperationException("Unsupported box2d shape object type: " + object.getClass().getSimpleName());
                                }
                            });
                    }
                }
            }
        });
    }

    @Override
    public void show() {
        game.setInputProcessors(gameplayDeviceInputAdapter);
    }

    @Override
    public void hide() {
        engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30);
        final var v = RigidBodyBox.MAPPER.get(engine.getEntitiesFor(Family.all(RigidBodyBox.class).get()).get(0)).getBody().getLinearVelocity().len2() / 100f;
        game.getCamera().zoom = Math.clamp(game.getCamera().zoom + (v - game.getCamera().zoom)*0.0125f, 0.5f, 2);//(float) Math.sin(System.nanoTime() / 1000000000d)+1f;
        game.getViewport().apply();
        mapManager.getRenderer().setView(game.getCamera());
        mapManager.getRenderer().render();
        engine.update(delta);
        physicsManager.tick(delta);

        Optional.of(game)
            .map(GDXGame::getBatch)
            .ifPresent(b -> {
                final var c = new OrthographicCamera();//game.getCamera();
                c.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                final var vp = new ScreenViewport(c);
                vp.update((int) c.viewportWidth, (int) c.viewportHeight, false);
                vp.apply();
                b.setProjectionMatrix(c.combined);
                b.begin();
                final var w = c.viewportWidth * 0.1f;
                final var h = c.viewportHeight * 0.1f;
                b.setColor(Color.GRAY);
                b.draw(AnimationController.PIRATE_SHEET, 0, c.viewportHeight * 0.5f - h / 2, w, h, 64, 32, 32, 32, true, false);
                b.draw(AnimationController.PIRATE_SHEET, c.viewportWidth - w, c.viewportHeight * 0.5f - h / 2, w, h, 64, 32, 32, 32, false, false);
                b.end();
            });
    }

    @Override
    public void dispose() {
        Arrays.stream(engine.getSystems().toArray(EntitySystem.class))
            .filter(Disposable.class::isInstance)
            .map(Disposable.class::cast)
            .forEach(Disposable::dispose);
    }

    // Unimplemented...

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
