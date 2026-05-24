package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.stream.IntStream;

public class SpriteRenderer implements Component {

    @Getter
    private final Sprite sprite = new Sprite();
    public Sprite setTextureRegion(TextureRegion textureRegion) {
        sprite.setRegion(textureRegion);
        return sprite;
    }

    // static
    public static final ComponentMapper<SpriteRenderer> MAPPER = ComponentMapper.getFor(SpriteRenderer.class);
}
