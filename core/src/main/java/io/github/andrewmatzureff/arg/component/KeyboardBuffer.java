package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.github.andrewmatzureff.arg.input.KeyState;

import java.util.*;

public class KeyboardBuffer extends AbstractBufferingComponent<KeyState> {

    public static boolean DEMO_MOVE_LEFT = false;
    public static boolean DEMO_MOVE_RIGHT = false;
    public static boolean DEMO_JUMP = false;

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
        DEMO_MOVE_LEFT = false; DEMO_MOVE_RIGHT = false; DEMO_JUMP = false;
        for (int i = 0; i < Gdx.input.getMaxPointers(); i++) {
            if (Gdx.input.isTouched(i)) {
                final var touchX = Gdx.input.getX(i);
                final var touchY = Gdx.input.getY(i);
                DEMO_MOVE_LEFT |= touchX < Gdx.graphics.getWidth() / 2;
                DEMO_MOVE_RIGHT |= touchX >= Gdx.graphics.getWidth() / 2;
                DEMO_JUMP |= touchY < 5 * Gdx.graphics.getHeight() / 6;
            }
        }

        final var demoHeldKeys = new ArrayList<Integer>();

        if (DEMO_MOVE_LEFT) demoHeldKeys.add(Input.Keys.A); //else release(Input.Keys.A);
        if (DEMO_MOVE_RIGHT) demoHeldKeys.add(Input.Keys.D); //else release(Input.Keys.D);
        if (DEMO_JUMP) demoHeldKeys.add(Input.Keys.SPACE); //else release(Input.Keys.SPACE);

        demoHeldKeys.stream()
            .map(KeyState.Type.HELD::asKeyState)
            .forEach(queue::add);

        heldKeys.stream()
            .map(KeyState.Type.HELD::asKeyState)
            .forEach(queue::add);
    }

    // static
    public static final ComponentMapper<KeyboardBuffer> MAPPER = ComponentMapper.getFor(KeyboardBuffer.class);
}
