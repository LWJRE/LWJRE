package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.QuadMesh2D;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.rendering.Texture;
import io.github.scalamath.colorlib.Col4f;
import io.github.scalamath.vecmatlib.Vec2f;

/**
 * Node that renders a 2D sprite.
 * <p>
 *     Uses a shader of type {@code sprite_shader}.
 * </p>
 */
public class Sprite2D extends Node2D {

    /**
     * The texture to use for this sprite.
     * <p>
     *     Accessible in the fragment shader as {@code sprite_texture}.
     *     The size of the texture is accessible in the vertex shader as {@code texture_size}.
     * </p>
     */
    public Texture spriteTexture;
    // TODO: Add normal map

    /**
     * The sprite's drawing offset.
     * <p>
     *     Accessible in the vertex shader as {@code offset}.
     * </p>
     */
    public Vec2f offset = Vec2f.Zero();
    /**
     * True if the sprite is flipped horizontally.
     * <p>
     *     If true, {@code flip_h} in the vertex shader will be -1, if false, it will be 1.
     * </p>
     */
    public boolean flipH = false;
    /**
     * True if the sprite is flipped vertically.
     * <p>
     *     If true, {@code flip_v} in the vertex shader will be -1, if false, it will be 1.
     * </p>
     */
    public boolean flipV = false;

    /**
     * The number of columns in the sprite sheet.
     * Must be greater than zero.
     * <p>
     *     Accessible in the vertex shader as {@code h_frames}.
     * </p>
     */
    public int hFrames = 1;
    /**
     * The number of rows in the sprite sheet.
     * Must be greater than zero.
     * <p>
     *     Accessible in the vertex shader as {@code v_frames}.
     * </p>
     */
    public int vFrames = 1;
    /**
     * Index of the frame to display.
     * Must be in the [0, {@link Sprite2D#hFrames} * {@link Sprite2D#vFrames}) range.
     * <p>
     *     Accessible in the vertex shader as {@code frame}.
     * </p>
     */
    public int frame = 0;

    // TODO: Region enabled and region rect

    /**
     * The color applied to the sprite.
     * <p>
     *     Accessible in the fragment shader as {@code modulate}.
     * </p>
     */
    public Col4f modulate = new Col4f(1.0f, 1.0f, 1.0f, 1.0f);

    public Shader shader = Shader.getOrLoad("io/github/ardentengine/core/shaders/sprite_2d.yaml");

    @Override
    protected void onUpdate(float delta) {
        if(this.shader == null) {
            this.shader = Shader.getOrLoad("io/github/ardentengine/core/shaders/sprite_2d.yaml");
        }
        // TODO: Do not render the sprite if it is outside of the camera's view
        if(this.shader != null) {
            this.shader.set("transformation_matrix", this.globalTransform());
            this.shader.set("projection_matrix", Camera2D.currentProjection());
            this.shader.set("view_matrix", Camera2D.currentView());
            this.shader.set("z_index", this.effectiveZIndex());
            if(this.spriteTexture != null) {
                this.shader.set("texture_size", this.spriteTexture.getWidth(), this.spriteTexture.getHeight());
            } else {
                this.shader.set("texture_size", 0, 0);
            }
            this.shader.set("sprite_texture", this.spriteTexture);
            this.shader.set("offset", this.offset);
            this.shader.set("flip_h", this.flipH ? -1 : 1);
            this.shader.set("flip_v", this.flipV ? -1 : 1);
            this.shader.set("h_frames", this.hFrames);
            this.shader.set("v_frames", this.vFrames);
            this.shader.set("frame", this.frame);
            this.shader.set("modulate", this.modulate);
            this.shader.draw(QuadMesh2D.getInstance());
        }
    }
}