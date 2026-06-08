package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.andrewmatzureff.arg.util.ConditionalBox2DDebugRenderer;
import lombok.Getter;

import java.util.Set;

public class PhysicsManager {

    @Getter
    private final World world = new World(new Vector2(0, -10), true);
    @Getter
    private final Engine engine;
    private final Box2DDebugRenderer debugRenderer = new ConditionalBox2DDebugRenderer(Set.of(
        "player"));
    private final float timeStep = 1 / 60f;
    private final int velocityIterations = 6;
    private final int positionIterations = 2;
    private final Camera camera;
    private float accumulator;

    public PhysicsManager(Engine engine, Viewport viewport) {
        this.camera = viewport.getCamera();
        this.engine = engine;
        Box2D.init();
    }

    public void tick(float deltaTime) {
        debugRenderer.render(world, camera.combined);
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep) {
            world.step(timeStep, velocityIterations, positionIterations);
            accumulator -= timeStep;
        }
    }
}
