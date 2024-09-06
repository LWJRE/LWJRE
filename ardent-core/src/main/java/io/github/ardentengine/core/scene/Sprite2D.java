package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Texture;
import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;

import java.util.HashMap;
import java.util.Map;

/**
 * Node that renders a 2D sprite.
 * <p>
 *     Uses a shader of type {@code sprite_shader}.
 * </p>
 */
public class Sprite2D extends VisualInstance2D {

    /**
     * The texture to use for this sprite.
     */
    public Texture spriteTexture;
    // TODO: Add normal map

    /**
     * The sprite's drawing offset.
     */
    public Vec2f offset = Vec2f.Zero();
    /**
     * True if the sprite is flipped horizontally.
     */
    public boolean flipH = false;
    /**
     * True if the sprite is flipped vertically.
     */
    public boolean flipV = false;

    /**
     * The number of columns in the sprite sheet.
     * Must be greater than zero.
     */
    public int hFrames = 1;
    /**
     * The number of rows in the sprite sheet.
     * Must be greater than zero.
     */
    public int vFrames = 1;
    /**
     * Index of the frame to display.
     * Must be between 0 (inclusive) and {@link Sprite2D#hFrames} * {@link Sprite2D#vFrames} (exclusive).
     */
    public int frame = 0;

    // TODO: Region enabled and region rect

    @Override
    public final String shaderType() {
        return "sprite_shader";
    }

    @Override
    public final Map<String, Object> getShaderParameters() {
        var map = new HashMap<String, Object>();
        map.put("sprite_texture", this.spriteTexture);
        map.put("texture_size", this.spriteTexture == null ? Vec2i.Zero() : this.spriteTexture.size());
        map.put("offset", this.offset == null ? Vec2i.Zero() : this.offset);
        map.put("flip", new Vec2i(this.flipH ? -1 : 1, this.flipV ? -1 : 1));
        map.put("frames", new Vec2i(this.hFrames, this.vFrames));
        map.put("frame", this.frame);
        return map;
    }
}