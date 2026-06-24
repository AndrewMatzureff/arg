package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.andrewmatzureff.arg.component.RigidBody;
import io.github.andrewmatzureff.arg.component.SpriteRenderer;
import io.github.andrewmatzureff.arg.component.Transform;

import static com.badlogic.gdx.math.MathUtils.isZero;

public class SpriteRendererSystem extends IteratingSystem {

    private final Batch batch;

    public SpriteRendererSystem(Batch batch) {
        super(Family.all(SpriteRenderer.class, Transform.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var spriteRenderer = SpriteRenderer.MAPPER.get(entity);
        final var frame = spriteRenderer.getSprite();
        final var transform = Transform.MAPPER.get(entity);
        final var position = transform.getPosition();
        final var rigidBodyBox = RigidBody.MAPPER.get(entity);
        final var vx = rigidBodyBox.getBody().getLinearVelocity().x;

        frame.setPosition(position.x - frame.getWidth()/2, position.y - frame.getHeight()/2);
        frame.setRotation(transform.getRotation());
        spriteRenderer.setFlipX(isZero(vx, 0.025f) ? spriteRenderer.isFlipX() : vx < -0.025);
        frame.setFlip(spriteRenderer.isFlipX(), false);
        // TODO: move begin/end batch calls outside of entity scope
        batch.begin();
        frame.draw(batch);
        batch.end();
    }
}
