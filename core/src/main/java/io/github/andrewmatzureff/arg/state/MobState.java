package io.github.andrewmatzureff.arg.state;

import com.badlogic.ashley.core.Component;
import lombok.Getter;

public abstract class MobState implements Component {

    @Getter
    private Phase phase = Phase.ENTER;

    @Getter
    private MobState next = null;

    public void commit() {
        this.phase = Phase.UPDATE;
        this.next = null;
    }

    public void transition(MobState next) {
        if (next == null || next == this) {
            commit();
            return;
        }
        this.next  = next;
        this.phase = Phase.EXIT;
        next.phase = Phase.ENTER;
        next.next  = null;
    }

    @Override
    public String toString() {
        return "%s<%s>".formatted(super.toString(), phase);
    }

    public enum Phase {
        ENTER, UPDATE, EXIT
    }
}
