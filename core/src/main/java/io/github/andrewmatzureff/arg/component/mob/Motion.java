package io.github.andrewmatzureff.arg.component.mob;

import java.util.Set;

public enum Motion implements MobTraits.Trait {
    ANY
        , X
            , X_POSITIVE
            , X_NEGATIVE
        , Y
            , Y_POSITIVE
            , Y_NEGATIVE
        , Z
            , Z_POSITIVE
            , Z_NEGATIVE
        , HORIZONTAL
            , LEFT
            , RIGHT
        , VERTICAL
            , UP
            , DOWN
        , DEPTH
            , IN
            , OUT;

    public Set<MobTraits.Trait> ancestors() {
        return switch (this) {
            case ANY -> Set.of(ANY);

            case X -> Set.of(X, ANY);
            case Y -> Set.of(Y, ANY);
            case Z -> Set.of(Z, ANY);
            case HORIZONTAL -> Set.of(HORIZONTAL, ANY);
            case VERTICAL -> Set.of(VERTICAL, ANY);
            case DEPTH -> Set.of(DEPTH, ANY);

            case X_POSITIVE -> Set.of(X_POSITIVE, X, ANY);
            case X_NEGATIVE -> Set.of(X_NEGATIVE, X, ANY);
            case Y_POSITIVE -> Set.of(Y_POSITIVE, Y, ANY);
            case Y_NEGATIVE -> Set.of(Y_NEGATIVE, Y, ANY);
            case Z_POSITIVE -> Set.of(Z_POSITIVE, Z, ANY);
            case Z_NEGATIVE -> Set.of(Z_NEGATIVE, Z, ANY);
            case LEFT -> Set.of(LEFT, HORIZONTAL, ANY);
            case RIGHT -> Set.of(RIGHT, HORIZONTAL, ANY);
            case UP -> Set.of(UP, VERTICAL, ANY);
            case DOWN -> Set.of(DOWN, VERTICAL, ANY);
            case IN -> Set.of(IN, DEPTH, ANY);
            case OUT -> Set.of(OUT, DEPTH, ANY);
        };
    }

    public Set<MobTraits.Trait> descendants() {
        return switch (this) {
            case ANY -> Set.of(values());

            case X -> Set.of(X, X_POSITIVE, X_NEGATIVE);
            case Y -> Set.of(Y, Y_POSITIVE, Y_NEGATIVE);
            case Z -> Set.of(Z, Z_POSITIVE, Z_NEGATIVE);
            case HORIZONTAL -> Set.of(HORIZONTAL, LEFT, RIGHT);
            case VERTICAL -> Set.of(VERTICAL, UP, DOWN);
            case DEPTH -> Set.of(DEPTH, IN, OUT);

            case LEFT -> Set.of(LEFT);
            case RIGHT -> Set.of(RIGHT);
            case UP -> Set.of(UP);
            case DOWN -> Set.of(DOWN);
            case IN -> Set.of(IN);
            case OUT -> Set.of(OUT);
            case X_POSITIVE -> Set.of(X_POSITIVE);
            case X_NEGATIVE -> Set.of(X_NEGATIVE);
            case Y_POSITIVE -> Set.of(Y_POSITIVE);
            case Y_NEGATIVE -> Set.of(Y_NEGATIVE);
            case Z_POSITIVE -> Set.of(Z_POSITIVE);
            case Z_NEGATIVE -> Set.of(Z_NEGATIVE);
        };
    }
}
