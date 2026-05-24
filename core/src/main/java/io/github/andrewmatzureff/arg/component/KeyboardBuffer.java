package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.ComponentMapper;
import io.github.andrewmatzureff.arg.input.KeyState;

import java.util.*;

public class KeyboardBuffer extends AbstractBufferingComponent<KeyState> {

    private final Set<Integer> heldKeys = new HashSet<>();

    public void press(int keycode) {
        heldKeys.add(keycode);
        queue.add(KeyState.Type.PRESSED.asKeyState(keycode));
    }

    public void release(int keycode) {
        heldKeys.remove(keycode);
        queue.add(KeyState.Type.RELEASED.asKeyState(keycode));
    }

    public boolean key(int keycode) {return heldKeys.contains(keycode);}

    @Override
    protected void preflush() {
        heldKeys.stream()
            .map(KeyState.Type.HELD::asKeyState)
            .forEach(queue::add);
    }

    // static
    public static final ComponentMapper<KeyboardBuffer> MAPPER = ComponentMapper.getFor(KeyboardBuffer.class);
}
