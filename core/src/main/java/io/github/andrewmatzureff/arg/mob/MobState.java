package io.github.andrewmatzureff.arg.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.andrewmatzureff.arg.component.StateManager;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.util.CopyPool;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Function;

import static io.github.andrewmatzureff.arg.util.Optionals.peek;
import static io.github.andrewmatzureff.arg.util.Optionals.remap;
import static java.util.function.Predicate.not;

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
