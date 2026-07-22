package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import io.github.andrewmatzureff.arg.util.Restorable;
import lombok.Getter;
import lombok.Setter;

public class Jump implements Restorable, Component {
    public static final ComponentMapper<Jump> MAPPER = ComponentMapper.getFor(Jump.class);

    private static final long invalid_trigger_time = Long.MAX_VALUE;

    @Getter @Setter
    private int delayMillis = 200;

    @Getter @Setter
    private long triggerTimeMillis = invalid_trigger_time;

    public boolean isJumping() {
        return getTriggerTimeMillis() != invalid_trigger_time;
    }

    public void jump() {
        final int delayMillis = getDelayMillis();
        final long now = System.currentTimeMillis();
        setTriggerTimeMillis(now + delayMillis);
    }

    @Override
    public void restore() {
        setTriggerTimeMillis(invalid_trigger_time);
    }
}
