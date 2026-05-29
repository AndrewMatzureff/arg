package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.input.Command;

import java.util.function.Function;

public interface MobState {
    void enter();
    void handle(Command command);
    void update();
    void exit();
    Entity entity();
    default <T extends MobState> T create(Function<Entity, T> constructor) {
        return constructor.apply(entity());
    }
}
