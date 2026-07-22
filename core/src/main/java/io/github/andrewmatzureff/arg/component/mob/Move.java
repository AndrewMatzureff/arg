package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import io.github.andrewmatzureff.arg.util.CopyPool;
import io.github.andrewmatzureff.arg.util.Restorable;
import lombok.Getter;
import lombok.Setter;

public enum Move implements Component {
    LEFT,
    RIGHT,
    UP,
    DOWN;
    public ComponentMapper<? extends Move> mapper() {
        return ComponentMapper.getFor(this.getClass());
    }
}
