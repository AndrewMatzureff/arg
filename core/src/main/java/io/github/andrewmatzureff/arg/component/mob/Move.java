package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import io.github.andrewmatzureff.arg.util.CopyPool;
import lombok.Getter;
import lombok.Setter;

public class Move implements Component {
    public static final ComponentMapper<Move> MAPPER = ComponentMapper.getFor(Move.class);

    private final Vector2 direction = new Vector2();
    private final CopyPool<Vector2> copy = new CopyPool<>(true, Vector2::cpy, Vector2::set);
    @Getter @Setter
    private float speed = 30;
    public Vector2 getDirection() {return copy.obtain(direction);}
    public void setDirection(Vector2 direction) {
        this.direction.set(direction);
    }
    public void left(float speed) {
        direction.add(-speed, 0);
    }
    public void right(float speed) {
        direction.add(speed, 0);
    }
    public void up(float speed) {
        direction.add(0, speed);
    }
    public void down(float speed) {
        direction.add(0, -speed);
    }

//    public enum Trait implements MobState.Trait {
//        GROUNDED,
//        MOVING_X,
//        MOVING_Y,
//    }
}
