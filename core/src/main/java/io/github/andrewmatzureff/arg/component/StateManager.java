package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import io.github.andrewmatzureff.arg.state.MobState;
import lombok.Getter;
import lombok.Setter;

public class StateManager implements Component {

    @Getter @Setter
    private MobState state = null;
//    private final Set<MobState.Trait> traits = new HashSet<>();

    public boolean hasState(MobState mobState) {
        return state.getClass() == mobState.getClass();
    }

    public static final ComponentMapper<StateManager> MAPPER = ComponentMapper.getFor(StateManager.class);
}
