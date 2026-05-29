package io.github.andrewmatzureff.arg.component.mob;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import lombok.Getter;
import lombok.Setter;

public class Jump implements Component {
    public static final ComponentMapper<Jump> MAPPER = ComponentMapper.getFor(Jump.class);

    @Getter @Setter
    private float gravity = 0;

    @Getter @Setter
    private boolean triggered;

    public boolean isNotTriggered(){return !triggered;}

//    private final Vector2 direction = new Vector2();
//    private final CopyPool<Vector2> copy = new CopyPool<>(true, Vector2::cpy, Vector2::set);
//    @Getter @Setter
//    private float additiveDamping = 0;
//    @Getter @Setter
//    private float multiplicativeDamping = 1;
//    @Getter @Setter
//    private float dampingThreshold = 0;
//    private float speed = 1;
//    public Vector2 getDirection() {return copy.obtain(direction);}
//    public void setDirection(Vector2 direction) {
//        this.direction.set(direction);
//    }
//    public void accumulate(float x, float y) {direction.add(x, y);}
}
