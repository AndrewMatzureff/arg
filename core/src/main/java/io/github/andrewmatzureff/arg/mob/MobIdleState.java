package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.ComponentMapper;

public class MobIdleState extends MobState {
    public static final ComponentMapper<MobIdleState> MAPPER = ComponentMapper.getFor(MobIdleState.class);
}
