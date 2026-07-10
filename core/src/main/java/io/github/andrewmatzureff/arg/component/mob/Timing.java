package io.github.andrewmatzureff.arg.component.mob;

import java.util.Set;

public enum Timing implements MobTraits.Trait {
    ANIMATION_FINISHED;

    public Set<MobTraits.Trait> ancestors() {
        return switch (this) {
            case ANIMATION_FINISHED -> Set.of(ANIMATION_FINISHED);
        };
    }

    public Set<MobTraits.Trait> descendants() {
        return switch (this) {
            case ANIMATION_FINISHED -> Set.of(values());
        };
    }
}
