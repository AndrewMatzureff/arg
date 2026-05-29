package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.mob.MobState;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class StateManager implements Component {
    private final Set<Command> intent = new HashSet<>();
    @Getter
    private MobState state = null;

    public void setState(MobState state) {
        Optional.of(this)
            .map(StateManager::getState)
            .ifPresent(MobState::exit);
        state.enter();
        this.state = state;
    }

    public boolean hasState(MobState mobState) {
        return state.getClass() == mobState.getClass();
    }

    public boolean addIntent(Command command) {
        return intent.add(command);
    }

    public void forEachIntent(Consumer<Command> consumer) {
        final var iter = intent.iterator();
        while (iter.hasNext()) {
            consumer.accept(iter.next());
            iter.remove();
        }
    }

    public static final ComponentMapper<StateManager> MAPPER = ComponentMapper.getFor(StateManager.class);
}
