package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.github.andrewmatzureff.arg.component.SpriteRenderer;
import io.github.andrewmatzureff.arg.component.Transform;

public class ThingRendererSystem extends IteratingSystem {

    private final Batch batch;

    public ThingRendererSystem(Batch batch) {
        super(Family.all(SpriteRenderer.class, Transform.class).get());
        this.batch = batch;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var spriteRenderer = SpriteRenderer.MAPPER.get(entity);
        final var frame = spriteRenderer.getSprite();
        final var transform = Transform.MAPPER.get(entity);
        final var position = transform.getPosition();

        batch.begin();
        batch.draw(frame, position.x, position.y);
        batch.end();
    }
}
