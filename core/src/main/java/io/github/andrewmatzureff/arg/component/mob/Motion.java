package io.github.andrewmatzureff.arg.component.mob;

import java.util.Set;

public enum Motion implements MobTraits.Trait {
    MOVING
        , MOVING_X
            , MOVING_X_POSITIVE
            , MOVING_X_NEGATIVE
        , MOVING_Y
            , MOVING_Y_POSITIVE
            , MOVING_Y_NEGATIVE
        , MOVING_Z
            , MOVING_Z_POSITIVE
            , MOVING_Z_NEGATIVE
        , MOVING_HORIZONTALLY
            , MOVING_LEFTWARD
            , MOVING_RIGHTWARD
        , MOVING_VERTICALLY
            , MOVING_UPWARD
            , MOVING_DOWNWARD
        , MOVING_ALONG_AXIS
            , MOVING_FORWARD
            , MOVING_BACKWARD;

    public Set<MobTraits.Trait> ancestors() {
        return switch (this) {
            case MOVING -> Set.of(MOVING);

            case MOVING_X -> Set.of(MOVING_X, MOVING);
            case MOVING_Y -> Set.of(MOVING_Y, MOVING);
            case MOVING_Z -> Set.of(MOVING_Z, MOVING);
            case MOVING_HORIZONTALLY -> Set.of(MOVING_HORIZONTALLY, MOVING);
            case MOVING_VERTICALLY -> Set.of(MOVING_VERTICALLY, MOVING);
            case MOVING_ALONG_AXIS -> Set.of(MOVING_ALONG_AXIS, MOVING);

            case MOVING_X_POSITIVE -> Set.of(MOVING_X_POSITIVE, MOVING_X, MOVING);
            case MOVING_X_NEGATIVE -> Set.of(MOVING_X_NEGATIVE, MOVING_X, MOVING);
            case MOVING_Y_POSITIVE -> Set.of(MOVING_Y_POSITIVE, MOVING_Y, MOVING);
            case MOVING_Y_NEGATIVE -> Set.of(MOVING_Y_NEGATIVE, MOVING_Y, MOVING);
            case MOVING_Z_POSITIVE -> Set.of(MOVING_Z_POSITIVE, MOVING_Z, MOVING);
            case MOVING_Z_NEGATIVE -> Set.of(MOVING_Z_NEGATIVE, MOVING_Z, MOVING);
            case MOVING_LEFTWARD -> Set.of(MOVING_LEFTWARD, MOVING_HORIZONTALLY, MOVING);
            case MOVING_RIGHTWARD -> Set.of(MOVING_RIGHTWARD, MOVING_HORIZONTALLY, MOVING);
            case MOVING_UPWARD -> Set.of(MOVING_UPWARD, MOVING_VERTICALLY, MOVING);
            case MOVING_DOWNWARD -> Set.of(MOVING_DOWNWARD, MOVING_VERTICALLY, MOVING);
            case MOVING_FORWARD -> Set.of(MOVING_FORWARD, MOVING_ALONG_AXIS, MOVING);
            case MOVING_BACKWARD -> Set.of(MOVING_BACKWARD, MOVING_ALONG_AXIS, MOVING);
        };
    }

    public Set<MobTraits.Trait> descendants() {
        return switch (this) {
            case MOVING -> Set.of(values());

            case MOVING_X -> Set.of(MOVING_X, MOVING_X_POSITIVE, MOVING_X_NEGATIVE);
            case MOVING_Y -> Set.of(MOVING_Y, MOVING_Y_POSITIVE, MOVING_Y_NEGATIVE);
            case MOVING_Z -> Set.of(MOVING_Z, MOVING_Z_POSITIVE, MOVING_Z_NEGATIVE);
            case MOVING_HORIZONTALLY -> Set.of(MOVING_HORIZONTALLY, MOVING_LEFTWARD, MOVING_RIGHTWARD);
            case MOVING_VERTICALLY -> Set.of(MOVING_VERTICALLY, MOVING_UPWARD, MOVING_DOWNWARD);
            case MOVING_ALONG_AXIS -> Set.of(MOVING_ALONG_AXIS, MOVING_FORWARD, MOVING_BACKWARD);

            case MOVING_LEFTWARD -> Set.of(MOVING_LEFTWARD);
            case MOVING_RIGHTWARD -> Set.of(MOVING_RIGHTWARD);
            case MOVING_UPWARD -> Set.of(MOVING_UPWARD);
            case MOVING_DOWNWARD -> Set.of(MOVING_DOWNWARD);
            case MOVING_FORWARD -> Set.of(MOVING_FORWARD);
            case MOVING_BACKWARD -> Set.of(MOVING_BACKWARD);
            case MOVING_X_POSITIVE -> Set.of(MOVING_X_POSITIVE);
            case MOVING_X_NEGATIVE -> Set.of(MOVING_X_NEGATIVE);
            case MOVING_Y_POSITIVE -> Set.of(MOVING_Y_POSITIVE);
            case MOVING_Y_NEGATIVE -> Set.of(MOVING_Y_NEGATIVE);
            case MOVING_Z_POSITIVE -> Set.of(MOVING_Z_POSITIVE);
            case MOVING_Z_NEGATIVE -> Set.of(MOVING_Z_NEGATIVE);
        };
    }
}
