package io.github.andrewmatzureff.arg.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Player {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public enum Clip implements Reels.Clip {
        IDLE(1, Animation.PlayMode.LOOP),
        WALK(10, Animation.PlayMode.LOOP),
        AIM(3, Animation.PlayMode.NORMAL),
        FIRE(4, Animation.PlayMode.NORMAL),
        RELOAD(36, Animation.PlayMode.NORMAL),
        RUN(6, Animation.PlayMode.LOOP),
        JUMP(3, Animation.PlayMode.NORMAL),
        FALL(3, Animation.PlayMode.LOOP),
        LAND(2, Animation.PlayMode.NORMAL);
        private final int frameCount;
        private final Animation.PlayMode playMode;
        public String spriteSheet() {return "pirate.png";}
        public Clip[] reel() {return values();}
    }
}
