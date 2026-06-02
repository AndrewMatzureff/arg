package io.github.andrewmatzureff.arg.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.Setter;

import static io.github.andrewmatzureff.arg.util.Units.PIXELS_TO_METERS;

public class SpriteRenderer implements Component {

    @Getter
    private final Sprite sprite = new Sprite();
    @Getter @Setter
    private boolean flipX;
    public Sprite setTextureRegion(TextureRegion textureRegion) {
        final float width = textureRegion.getRegionWidth(), height = textureRegion.getRegionHeight();
        sprite.setBounds(0, 0, width * PIXELS_TO_METERS, height * PIXELS_TO_METERS);
        sprite.setOriginCenter();
        sprite.setRegion(textureRegion);
        return sprite;
    }

    // static
    public static final ComponentMapper<SpriteRenderer> MAPPER = ComponentMapper.getFor(SpriteRenderer.class);
}
