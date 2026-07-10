package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import lombok.Getter;
import lombok.Setter;

public class Jump implements Component {
    public static final ComponentMapper<Jump> MAPPER = ComponentMapper.getFor(Jump.class);

    private static final long invalid_trigger_time = Long.MAX_VALUE;

    @Getter @Setter
    private int delayMillis = 200;

    @Getter @Setter
    private long triggerTimeMillis = invalid_trigger_time;

    public boolean isJumping() {
        return getTriggerTimeMillis() != invalid_trigger_time;
    }

    public void begin() {
        final int delayMillis = getDelayMillis();
        final long now = System.currentTimeMillis();
        setTriggerTimeMillis(now + delayMillis);
    }

    public void end() {
        setTriggerTimeMillis(invalid_trigger_time);
    }
}
