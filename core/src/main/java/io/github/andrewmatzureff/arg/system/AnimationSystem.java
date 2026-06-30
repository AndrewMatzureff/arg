package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.SpriteRenderer;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationController.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var animationController = AnimationController.MAPPER.get(entity);
        final var frame = animationController.getFrame();
        final var spriteRenderer = SpriteRenderer.MAPPER.get(entity);
        spriteRenderer.setTextureRegion(frame);
        animationController.tick(deltaTime);
    }
}
