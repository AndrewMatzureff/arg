package io.github.andrewmatzureff.arg;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.arg.screen.GameScreen;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GDXGame extends Game {

    public static final float WORLD_WIDTH = 16f;
    public static final float WORLD_HEIGHT = 9f;

    @Getter
    private Batch batch;
    @Getter
    private ShapeRenderer shapeRenderer;
    @Getter
    private OrthographicCamera camera;
    @Getter
    private Viewport viewport;
    private GLProfiler glProfiler;
    private FPSLogger fpsLogger;
    private InputMultiplexer inputMultiplexer;

    private final Map<Class<? extends Screen>, Screen> screens = new HashMap<>();

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
        fpsLogger = new FPSLogger();

        addScreen(new GameScreen(this));
        setScreen(GameScreen.class);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);

        super.resize(width, height);
    }
    public void addScreen(Screen screen) {
        screens.put(screen.getClass(), screen);
    }

    public void removeScreen(Screen screen) {
        screens.remove(screen.getClass());
    }

    public void setScreen(Class<? extends Screen> screenClass) {
        Optional.of(screenClass)
            .map(screens::get)
            .ifPresentOrElse(super::setScreen, () -> {
                throw new GdxRuntimeException("No screen with class '%s' has been registered.".formatted(screenClass));
            });
    }

    @Override
    public void render() {
        fpsLogger.log();
        glProfiler.reset();
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        super.render(); // Render current screen.
        Gdx.graphics.setTitle(
            "Arg - Draw Calls: %d, Mouse: (%d,%d)"
                .formatted(glProfiler.getDrawCalls(), Gdx.input.getX(), Gdx.input.getY()));
    }

    @Override
    public void dispose() {
        screens.values().forEach(Screen::dispose);
        screens.clear();
        batch.dispose();
    }

    public void setInputProcessors(InputProcessor... processors) {
        inputMultiplexer.clear();
        if (processors == null) return;
        Arrays.stream(processors).forEach(inputMultiplexer::addProcessor);
    }
}
