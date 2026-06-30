package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class AnimationController implements Component {

    private Animation<TextureRegion> animation;
    private float stateTime;
    @Setter
    private boolean looping;
    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        this.stateTime = 0;
    }
    public void tick(float deltaTime) {
        this.stateTime += deltaTime;
    }
    public TextureRegion getFrame() {
        return animation.getKeyFrame(stateTime, looping);
    }

    public boolean isFinished() {return animation.isAnimationFinished(stateTime);}

    // static \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public static final ComponentMapper<AnimationController> MAPPER = ComponentMapper.getFor(AnimationController.class);
}
