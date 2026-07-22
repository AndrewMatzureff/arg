package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.GameplayCommandBuffer;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.state.MobState;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.andrewmatzureff.arg.util.ComponentMappers.maybe;
import static io.github.andrewmatzureff.arg.util.Optionals.*;

public abstract class AbstractMobStateIteratingSystem <T extends MobState> extends IteratingSystem {
    private final Class<T> type;

    @SafeVarargs
    public AbstractMobStateIteratingSystem(Class<T> state, Class<? extends Component>...dependencies) {
        super(Family.all(concat(state, dependencies)).get());
        this.type = state;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        try (final var stream = some(entity)
            .map(GameplayCommandBuffer.MAPPER::get)
            .map(GameplayCommandBuffer::stream)
            .orElse(Stream.empty())) {
            stream.forEach(command -> handle(command, entity, deltaTime));
        }

        final var phase = maybe(entity, type)
            .map(MobState::getPhase)
            .orElseThrow(); // TODO: throw a more descriptive error
        switch (phase) {
            case ENTER -> onEnter(entity, deltaTime);
            case UPDATE -> onUpdate(entity, deltaTime);
            case EXIT -> onExit(entity, deltaTime);
        }
    }

    private void onEnter(Entity entity, float deltaTime) {
        enter(entity, deltaTime);
        maybe(entity, type)
            .ifPresentOrElse(MobState::commit
                , () -> {throw new RuntimeException("Something went wrong..."); /* TODO: throw a more descriptive error */});
    }

    private void onUpdate(Entity entity, float deltaTime) {
        final var state = maybe(entity, type)
            .orElseThrow(); // TODO: throw a more descriptive error
        update(entity, deltaTime)
            .ifPresent(state::transition);
    }

    private void onExit(Entity entity, float deltaTime) {
        exit(entity, deltaTime);
        some(type)
            .map(entity::remove)
            .map(MobState::getNext)
            .ifPresentOrElse(entity::add
                , () -> {throw new RuntimeException("Something went wrong..."); /* TODO: throw a more descriptive error */});
    }

    protected void handle(Command command, Entity entity, float deltaTime) {}

    protected abstract ComponentMapper<T> mapper();
    protected abstract void enter(Entity entity, float deltaTime);
    protected abstract Optional<MobState> update(Entity entity, float deltaTime);
    protected abstract void exit(Entity entity, float deltaTime);

    @SafeVarargs
    private static Class<? extends Component>[] concat(Class<? extends MobState> state, Class<? extends Component>...others) {
        return some(others)
            .map(arr -> Arrays.copyOf(arr, arr.length + 1))
            .map(peek(arr -> arr[arr.length - 1] = state))
            .orElseThrow(); // TODO: throw a more descriptive error
    }
}
