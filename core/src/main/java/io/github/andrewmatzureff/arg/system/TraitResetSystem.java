package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.util.Restorable;

import static io.github.andrewmatzureff.arg.util.Optionals.some;

public class TraitResetSystem extends IteratingSystem {

    public TraitResetSystem() {
        super(Family.all(Component.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MobTraits.MAPPER.get(entity)
            .clear();
        entity.getComponents()
            .forEach(c -> some(c)
                .filter(Restorable.class::isInstance)
                .map(Restorable.class::cast)
                .ifPresent(Restorable::restore));
    }
}
