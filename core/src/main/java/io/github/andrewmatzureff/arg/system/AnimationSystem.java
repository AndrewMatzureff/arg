package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.AnimationController;
import io.github.andrewmatzureff.arg.component.SpriteRenderer;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.component.mob.Timing;

import static io.github.andrewmatzureff.arg.util.Optionals.remap;
import static io.github.andrewmatzureff.arg.util.Optionals.some;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem() {
        super(Family.all(AnimationController.class, MobTraits.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var animationController = AnimationController.MAPPER.get(entity);
        if (!animationController.isActive()) return;
        final var frame = animationController.getFrame();
        final var spriteRenderer = SpriteRenderer.MAPPER.get(entity);
        spriteRenderer.setTextureRegion(frame);
        some(deltaTime)
            .map(remap(animationController, AnimationController::tick))
            .filter(AnimationController::isFinished)
            .map(remap(Timing.ANIMATION_FINISHED))
            .ifPresent(MobTraits.MAPPER.get(entity)::add);
    }
}
