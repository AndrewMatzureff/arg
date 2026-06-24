package io.github.andrewmatzureff.arg.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import io.github.andrewmatzureff.arg.GDXGame;
import io.github.andrewmatzureff.arg.input.GameplayDeviceInputAdapter;
import io.github.andrewmatzureff.arg.system.*;
import io.github.andrewmatzureff.arg.util.MapManager;

import java.util.Arrays;

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

        mapManager = new MapManager(engine, physicsManager);
        mapManager.load("test.tmx");
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
        game.getCamera().zoom = 1;//(float) Math.sin(System.nanoTime() / 1000000000d)+1f;
        mapManager.getRenderer().setView(game.getCamera());
        mapManager.getRenderer().render();
        engine.update(delta);
        physicsManager.tick(delta);
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
