package io.github.andrewmatzureff.arg.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import io.github.andrewmatzureff.arg.GDXGame;
import io.github.andrewmatzureff.arg.component.*;
import io.github.andrewmatzureff.arg.input.GameplayInputAdapter;
import io.github.andrewmatzureff.arg.system.*;

import java.util.Arrays;

/** First screen of the application. Displayed after the application is created. */
public class GameScreen implements Screen {
    private static final int[][] testMap = {
        {}
    };

    private final Engine engine;
    private final GDXGame game;
    private final GameplayInputAdapter gameplayInputAdapter;
    private final GameplayInputCommandDispatchSystem gameplayInputCommandDispatchSystem;
    private final StateManagerSystem stateManagerSystem;
    private final GameplayMovementHandlerSystem gameplayMovementHandlerSystem;
    private final AnimationSystem animationSystem;
    private final ThingRendererSystem thingRendererSystem;

    public GameScreen(GDXGame game) {
        this.game = game;
        this.engine = new Engine();
        this.gameplayInputAdapter = new GameplayInputAdapter(engine);
        this.gameplayInputCommandDispatchSystem = new GameplayInputCommandDispatchSystem();
        this.stateManagerSystem = new StateManagerSystem();
        this.gameplayMovementHandlerSystem = new GameplayMovementHandlerSystem();
        this.animationSystem = new AnimationSystem();
        this.thingRendererSystem = new ThingRendererSystem(game.getBatch());

        this.engine.addSystem(gameplayInputCommandDispatchSystem);
        this.engine.addSystem(stateManagerSystem);
        this.engine.addSystem(gameplayMovementHandlerSystem);
        this.engine.addSystem(animationSystem);
        this.engine.addSystem(thingRendererSystem);

        final Entity player = engine.createEntity()
            .add(new KeyboardBuffer())
            .add(new GameplayCommandBuffer())
            .add(new Move())
            .add(new Transform())
            .add(new AnimationController())
            .add(new SpriteRenderer());

        this.engine.addEntity(player);

    }

    @Override
    public void show() {
        game.setInputProcessors(gameplayInputAdapter);
    }

    @Override
    public void hide() {
        engine.removeAllEntities();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30);
        game.getCamera().zoom = 1;//(float) Math.sin(System.nanoTime() / 1000000000d)+1f;
        engine.update(delta);
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
