package io.github.andrewmatzureff.arg.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Disposable;
import io.github.andrewmatzureff.arg.GDXGame;
import io.github.andrewmatzureff.arg.component.*;
import io.github.andrewmatzureff.arg.component.DebugRigidBodyBox;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Move;
import io.github.andrewmatzureff.arg.input.GameplayDeviceInputAdapter;
import io.github.andrewmatzureff.arg.system.*;
import io.github.andrewmatzureff.arg.mob.MobIdleState;

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

    public GameScreen(GDXGame game) {
        this.game = game;
        this.engine = new Engine();
        this.gameplayDeviceInputAdapter = new GameplayDeviceInputAdapter(engine);
        this.gameplayInputCommandAdapterSystem = new GameplayInputCommandAdapterSystem();
        this.commandHandlerSystem = new CommandHandlerSystem();
        this.gameplayMovementHandlerSystem = new GameplayMovementHandlerSystem();
        this.animationSystem = new AnimationSystem();
        this.spriteRendererSystem = new SpriteRendererSystem(game.getBatch());
        this.physicsManager = new PhysicsManager(game.getViewport());

        this.engine.addSystem(gameplayInputCommandAdapterSystem);
        this.engine.addSystem(commandHandlerSystem);
        this.engine.addSystem(gameplayMovementHandlerSystem);
        this.engine.addSystem(animationSystem);
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
            .add(new RigidBodyBox(physicsManager.getWorld(), 1, 2))
            .add(new DebugRigidBodyBox(physicsManager.getWorld(), 1000, 2));

        StateManager.MAPPER.get(player).setState(new MobIdleState(player));
        this.engine.addEntity(player);

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
