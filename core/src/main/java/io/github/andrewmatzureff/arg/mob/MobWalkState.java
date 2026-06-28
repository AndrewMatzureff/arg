package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.ComponentMapper;

public class MobWalkState extends MobState {
    public static final ComponentMapper<MobWalkState> MAPPER = ComponentMapper.getFor(MobWalkState.class);
}
