package io.github.andrewmatzureff.arg.input;

public enum Command {
    // misc
    NO_OP,

    // movement
    MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN,

    // maneuvering
    JUMP, CROUCH,

    // interaction
    USE, ATTACK
}
