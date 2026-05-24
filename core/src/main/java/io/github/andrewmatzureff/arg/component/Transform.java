package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.github.andrewmatzureff.arg.util.CopyPool;
import lombok.Setter;

import java.util.IdentityHashMap;
import java.util.Map;

public class Transform implements Component {
    public static final ComponentMapper<Transform> MAPPER = ComponentMapper.getFor(Transform.class);

    private final Vector2 position = new Vector2();
    private final CopyPool<Vector2> copy = new CopyPool<>(true, Vector2::cpy, Vector2::set);

    public void setPosition(float x, float y) {position.set(x, y);}
    public void setPosition(Vector2 v) {position.set(v);}
    public Vector2 getPosition() {return copy.obtain(position);}
}
