package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.math.Vector2;
import io.github.ardentengine.core.rendering.RenderingServer;
import io.github.ardentengine.core.rendering.Texture;

/**
 * Node that renders a 2D sprite.
 * <p>
 *     Uses a shader of type {@code sprite_shader}.
 * </p>
 */
public class Sprite2D extends VisualInstance2D {

    // TODO: Make these variables private and add getters and setters

    /**
     * The texture to use for this sprite.
     */
    public Texture spriteTexture;

    /**
     * The sprite's drawing offset.
     */
    public Vector2 offset = Vector2.ZERO;
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
    void update(float deltaTime) {
        if(this.spriteTexture != null) {
            // TODO: Check if this thing is inside the camera's view rect
            var vertexScale = new Vector2(1.0f / this.hFrames, 1.0f / this.vFrames);
            var uvScale = new Vector2((this.flipH ? -1.0f : 1.0f) / this.hFrames, (this.flipV ? -1.0f : 1.0f) / this.vFrames);
            var uvOffset = new Vector2((float) (this.frame % this.hFrames) / this.hFrames, (float) this.frame / this.hFrames / this.vFrames);
            RenderingServer.getInstance().draw(this.spriteTexture, this.material(), this.offset, vertexScale, uvOffset, uvScale, this.globalTransform());
        }
        super.update(deltaTime);
    }
}