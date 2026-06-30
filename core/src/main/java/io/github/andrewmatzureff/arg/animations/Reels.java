package io.github.andrewmatzureff.arg.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Reels {

    private static final Map<Clip, Array<TextureRegion>> animations = new HashMap<>();

    static Animation<TextureRegion> animation(Clip clip) {
        validate(clip);
        return new Animation<>(1f / 10, animations.get(clip), clip.getPlayMode());
    }

    private static void invalidate() {
        animations.values()
            .stream()
            .map(Array::spliterator)
            .flatMap(split -> StreamSupport.stream(split, false))
            .map(TextureRegion::getTexture)
            .collect(Collectors.toSet())
            .forEach(Texture::dispose);
        animations.clear();
    }

    private static void validate(Clip animation) {
        if (animation != null && animations.containsKey(animation)) return;
        invalidate();
        if (animation == null) return;

        final var texture = new Texture(Gdx.files.internal(animation.spriteSheet()));
        final var frameRows = animation.reel().length;
        final var frames = TextureRegion.split(texture,
            texture.getWidth() / Arrays.stream(animation.reel())
                .map(Clip::getFrameCount)
                .max(Comparator.naturalOrder())
                .orElse(0),
            texture.getHeight() / frameRows);

        IntStream.range(0, frames.length)
            .peek(i -> frames[i] = Arrays.copyOfRange(frames[i], 0,
                animation.reel()[i]
                    .getFrameCount()))
            .forEach(i -> animations.put(animation.reel()[i], new Array<>(frames[i])));
    }

    public interface Clip {
        default Animation<TextureRegion> animation() {return Reels.animation(this);}
        Animation.PlayMode getPlayMode();
        int getFrameCount();
        String spriteSheet();
        Clip[] reel();
    }
}
