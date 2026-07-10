package io.github.andrewmatzureff.arg.component.mob;

import java.util.Set;

public enum Maneuver implements MobTraits.Trait {
    JUMPING
        , PRE_JUMP
        , POST_JUMP;

    public Set<MobTraits.Trait> ancestors() {
        return switch (this) {
            case JUMPING -> Set.of(JUMPING);

            case PRE_JUMP -> Set.of(PRE_JUMP, JUMPING);
            case POST_JUMP -> Set.of(POST_JUMP, JUMPING);
        };
    }

    public Set<MobTraits.Trait> descendants() {
        return switch (this) {
            case JUMPING -> Set.of(values());

            case PRE_JUMP -> Set.of(PRE_JUMP);
            case POST_JUMP -> Set.of(POST_JUMP);
        };
    }
}
