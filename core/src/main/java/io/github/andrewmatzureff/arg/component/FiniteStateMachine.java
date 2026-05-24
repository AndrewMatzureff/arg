package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;

public class FiniteStateMachine implements Component {

    // static
    public static final ComponentMapper<FiniteStateMachine> MAPPER = ComponentMapper.getFor(FiniteStateMachine.class);
}
