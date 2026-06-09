package io.github.andrewmatzureff.arg.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import io.github.andrewmatzureff.arg.component.KeyboardBuffer;
import io.github.andrewmatzureff.arg.util.Streams;
import lombok.Getter;

import java.util.*;

public class GameplayDeviceInputAdapter extends InputAdapter {
    @Getter
    private final ImmutableArray<Entity> entities;

    int lastButton;
    final List<Integer> lastKeys = new LinkedList<>();

    public GameplayDeviceInputAdapter(Engine engine) {
        this.entities = engine.getEntitiesFor(Family.all(KeyboardBuffer.class).get());
    }

    /** Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown (int keycode) {
        lastKeys.addLast(keycode);
        Arrays.stream(entities.toArray(Entity.class))
            .map(KeyboardBuffer.MAPPER::get)
            .forEach(Streams.consumer(keycode, KeyboardBuffer::press));
        return false;
    }

    /** Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyUp (int keycode) {
        lastKeys.removeLast();
        Arrays.stream(entities.toArray(Entity.class))
            .map(KeyboardBuffer.MAPPER::get)
            .forEach(Streams.consumer(keycode, KeyboardBuffer::release));
        return false;
    }

    /** Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed */
    @Override
    public boolean keyTyped (char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO: replace temporary demo touch support
        lastButton = button;
        if (button == Input.Buttons.RIGHT || pointer > 0) keyDown(Input.Keys.SPACE);
        else if (screenX < Gdx.graphics.getWidth() / 2) keyDown(Input.Keys.A);
        else if (screenX >= Gdx.graphics.getWidth() / 2) keyDown(Input.Keys.D);
        else keyDown(Input.Keys.SPACE);
        return false;
    }

    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Input.Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO: replace temporary demo touch support
        keyUp(lastKeys.getLast());
        return false;
    }
}
