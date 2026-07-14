package io.github.andrewmatzureff.arg.animation;

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

    private static Animation<TextureRegion> animation(Clip clip) {
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

    private static void validate(Clip clip) {
        if (clip != null && animations.containsKey(clip)) return;
        invalidate();
        if (clip == null) return;

        final var texture = new Texture(Gdx.files.internal(clip.spriteSheet()));
        final var reel = clip.reel();
        final var clips = TextureRegion.split(texture,
            texture.getWidth() / Arrays.stream(reel)
                .map(Clip::getFrameCount)
                .max(Comparator.naturalOrder())
                .orElse(0),
            texture.getHeight() / reel.length);

        IntStream.range(0, clips.length)
            // TODO: dispose of empty texture frames leftover from region split
            .peek(i -> clips[i] = Arrays.copyOfRange(clips[i], 0, reel[i]
                .getFrameCount()))
            .forEach(i -> animations.put(reel[i], new Array<>(clips[i])));
    }

    public interface Clip {
        default Animation<TextureRegion> animation() {return Reels.animation(this);}
        Animation.PlayMode getPlayMode();
        int getFrameCount();
        String spriteSheet();
        Clip[] reel();
    }
}
