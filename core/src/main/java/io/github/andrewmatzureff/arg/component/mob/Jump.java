package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import lombok.Getter;
import lombok.Setter;

public class Jump implements Component {
    public static final ComponentMapper<Jump> MAPPER = ComponentMapper.getFor(Jump.class);

    private int delayMillis = 200;
    private long triggerTimeMillis = Long.MAX_VALUE;

    public void begin() {
        triggerTimeMillis = System.currentTimeMillis() + delayMillis;
    }

    public boolean isJumping() {
        return triggerTimeMillis != Long.MAX_VALUE && System.currentTimeMillis() - triggerTimeMillis < 0;
    }

    public boolean isComplete() {
        final var jumped = triggerTimeMillis != Long.MAX_VALUE && System.currentTimeMillis() - triggerTimeMillis >= 0;
        if (jumped) triggerTimeMillis = Long.MAX_VALUE;
        return jumped;
    }

    public enum Phase {TRIGGERED,}
}
