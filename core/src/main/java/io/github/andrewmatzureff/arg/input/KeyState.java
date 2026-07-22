package io.github.andrewmatzureff.arg.input;

import com.badlogic.gdx.Input;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record KeyState(Type type, int key) {
    public static final KeyState NONE = new KeyState(null, Input.Keys.UNKNOWN);

    public enum Type {

        PRESSED, HELD, RELEASED;

        public synchronized KeyState asKeyState(int key) {
            return Optional.of(this)
                .map(pool::get)
                .orElseThrow(this::getInvalidTypePoolEntryError)
                .computeIfAbsent(key, this::createKeyState);
        }

        private RuntimeException getInvalidTypePoolEntryError() {
            return new IllegalStateException("Supposedly, '%s' is not a valid entry in the '%s' pool: '%s'..."
                .formatted(this, getClass().getSimpleName(), pool));
        }

        private KeyState createKeyState(int key) {return new KeyState(this, key);}
    }

    private static final Map<Type, Map<Integer, KeyState>> pool = Arrays.stream(Type.values())
        .collect(Collectors.toMap(Function.identity(), $ -> new HashMap<>()));
}
