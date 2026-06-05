package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import io.github.andrewmatzureff.arg.component.CameraController;
import io.github.andrewmatzureff.arg.component.Transform;

public class CameraControlSystem extends IteratingSystem {

    private final OrthographicCamera camera;

    public CameraControlSystem(OrthographicCamera camera) {
        super(Family.all(CameraController.class, Transform.class).get());
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var transform = Transform.MAPPER.get(entity);
        final var deltaXYZ = new Vector3(camera.position)
            .sub(transform.getPosition().x, transform.getPosition().y, 0)
            .scl(-1);
        camera.translate(deltaXYZ);
//        final var cameraAngle = (float) ((Math.atan2(camera.up.x, camera.up.y) * radDeg + 360) % 360);
//        final var transformAngle = -transform.getRotation();
//        camera.rotate(transformAngle - cameraAngle);
    }
}
