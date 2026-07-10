package io.github.andrewmatzureff.arg.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.andrewmatzureff.arg.component.Transform;
import io.github.andrewmatzureff.arg.component.mob.Jump;
import io.github.andrewmatzureff.arg.component.mob.Maneuver;
import io.github.andrewmatzureff.arg.component.mob.MobTraits;
import io.github.andrewmatzureff.arg.component.mob.Move;

public class JumpSystem extends IteratingSystem {

    public JumpSystem() {
        super(Family.all(Jump.class, Move.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final var jump = Jump.MAPPER.get(entity);
        final var move = Move.MAPPER.get(entity);
        final var traits = MobTraits.MAPPER.get(entity);
        if (isPreJump(jump)) traits.add(Maneuver.PRE_JUMP);
        else if (isPostJump(jump)) {
            traits.add(Maneuver.POST_JUMP);
            move.up(75f);
            jump.end();
        }
    }

    private boolean isPreJump(Jump jump) {
        return jump.isJumping() && System.currentTimeMillis() - jump.getTriggerTimeMillis() < 0;
    }

    private boolean isPostJump(Jump jump) {
        return jump.isJumping() && System.currentTimeMillis() - jump.getTriggerTimeMillis() >= 0;
    }
}
