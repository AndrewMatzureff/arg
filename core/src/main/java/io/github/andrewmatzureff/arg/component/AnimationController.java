package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class AnimationController implements Component {

    private final Texture pirateSheet = new Texture(Gdx.files.internal("pirate.png"));
    private final TextureRegion[][] frames = trim(TextureRegion.split(
        pirateSheet,
        pirateSheet.getWidth() / Arrays.stream(FRAME_COLS).max().orElse(0),
        pirateSheet.getHeight() / FRAME_ROWS));
    @Getter
    private final List<Animation<TextureRegion>> animations = Arrays.stream(frames)
        .map(row -> new Animation<>(1f / 10, row))
        .toList();
    private float stateTime;
    @Getter
    private int animationState;
    public void setAnimationState(int animationState) {
        this.animationState = animationState;
        this.stateTime = 0;
    }
    public void tick(float deltaTime) {
        this.stateTime += deltaTime;
    }
    public TextureRegion getFrame(boolean loop) {
        return animations.get(animationState).getKeyFrame(stateTime, loop);
    }

    // static
    public static final ComponentMapper<AnimationController> MAPPER = ComponentMapper.getFor(AnimationController.class);
    public static final int FRAME_ROWS = 7;
    public static final int IDLE_WALK = 0, WALK = 1, AIM = 2, FIRE = 3, RELOAD = 4, RUN = 5, JUMP = 6;
    public static final int[] FRAME_COLS = {1,10,3,4,36,6,8};//{2,10,3,4,36,6,8};

    private static TextureRegion[][] trim(TextureRegion[][] frames) {
        IntStream.range(0, frames.length)
            .forEach(i -> frames[i] = Arrays.copyOfRange(frames[i], 0, FRAME_COLS[i]));
        return frames;
    }
}
