package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;

public class TraitSystem extends IteratingSystem {

    public TraitSystem() {
        super(Family.all(MobTraits.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MobTraits.MAPPER.get(entity)
            .clear();
    }
}
