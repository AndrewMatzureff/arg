package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Input;
import io.github.andrewmatzureff.arg.component.GameplayCommandBuffer;
import io.github.andrewmatzureff.arg.component.KeyboardBuffer;
import io.github.andrewmatzureff.arg.input.Command;
import io.github.andrewmatzureff.arg.input.KeyState;

import java.util.Map;

public class GameplayInputCommandAdapterSystem extends IteratingSystem {

    private final Map<KeyState, Command> keyBindings = Map.of(
        KeyState.Type.HELD.asKeyState(Input.Keys.A), Command.MOVE_LEFT,
        KeyState.Type.HELD.asKeyState(Input.Keys.D), Command.MOVE_RIGHT,
        KeyState.Type.HELD.asKeyState(Input.Keys.W), Command.MOVE_UP,
        KeyState.Type.HELD.asKeyState(Input.Keys.S), Command.MOVE_DOWN,
        KeyState.Type.HELD.asKeyState(Input.Keys.SPACE), Command.JUMP);

    public GameplayInputCommandAdapterSystem() {
        super(Family.all(KeyboardBuffer.class, GameplayCommandBuffer.class).get());
    }

    // In addition to dispatching, this class also maps from device state to commands internally. Consequently, in a hypothetical situation where local multiplayer is possible this system carries with it the implication that all players must share the same key binds. This can be fixed by offloading the mapping responsibility onto a Component.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var keyboard = KeyboardBuffer.MAPPER.get(entity);
        final var controller = GameplayCommandBuffer.MAPPER.get(entity);
        try (var stream = keyboard.stream()) {
            stream.filter(keyBindings::containsKey)
                .map(keyBindings::get)
                .forEach(controller::dispatch);
        }
    }
}
