package io.github.andrewmatzureff.arg.input;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.andrewmatzureff.arg.component.KeyboardBuffer;
import io.github.andrewmatzureff.arg.util.Streams;
import lombok.Getter;

import java.util.*;

public class GameplayInputAdapter extends InputAdapter {
    @Getter
    private final ImmutableArray<Entity> entities;

    public GameplayInputAdapter(Engine engine) {
        this.entities = engine.getEntitiesFor(Family.all(KeyboardBuffer.class).get());
    }

    /** Called when a key was pressed
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown (int keycode) {
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
}
