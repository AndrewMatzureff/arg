package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.StateManager;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.CopyPool;

import java.util.Optional;
import java.util.function.Function;

import static io.github.andrewmatzureff.arg.util.Optionals.peek;
import static io.github.andrewmatzureff.arg.util.Optionals.remap;
import static java.util.function.Predicate.not;

public interface MobState {
    void enter();
    void handle(Command command);
    void update();
    void exit();
    Entity entity();

    default <T extends MobState> T transition(Function<Entity, T> next) {
        final var result = next.apply(entity());
        return Optional.of(this)
            .filter(not(result::is))
            .map(peek(MobState::exit))
            .map(remap(result))
            .map(peek(StateManager.MAPPER.get(entity())::setState))
            .map(peek(MobState::enter))
            .orElseThrow();
    }

    default boolean is(MobState other) {
        return getClass() == other.getClass();
    }
}
