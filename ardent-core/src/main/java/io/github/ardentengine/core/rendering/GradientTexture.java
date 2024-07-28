package io.github.ardentengine.core.rendering;

import io.github.scalamath.colorlib.Gradient;
import io.github.scalamath.vecmatlib.Vec2i;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * A texture created with colors sampled from a {@link Gradient}.
 */
public class GradientTexture extends Texture {

    /** Gradient used to fill the texture. */
    private Gradient gradient = new Gradient();

    // TODO: Direction and fill mode (linear, radial, square)

    /** Width of the texture. */
    private int width = 0;
    /** Height of the texture. */
    private int height = 0;

    /** Set to true when the texture needs to be updated. */
    private boolean dirty = true;

    /**
     * Setter method for gradient.
     *
     * @param gradient Gradient used to fill the texture.
     */
    public void setGradient(Gradient gradient) {
        this.dirty = !this.gradient.equals(gradient);
        this.gradient = Objects.requireNonNullElse(gradient, new Gradient());
    }

    /**
     * Getter method for gradient.
     *
     * @return Gradient used to fill the texture.
     */
    public Gradient getGradient() {
        return this.gradient;
    }

    /**
     * Setter method for width.
     *
     * @param width Width of the texture. Also represents the number of horizontal color samples that will be obtained from the gradient.
     */
    public void setWidth(int width) {
        this.dirty = this.width != width;
        this.width = width;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    /**
     * Setter method for height.
     *
     * @param height Height of the texture. Also represents the number of vertical color samples that will be obtained from the gradient.
     */
    public void setHeight(int height) {
        this.dirty = this.height != height;
        this.height = height;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Setter method for width and height.
     *
     * @param width Width of the texture.
     * @param height Height of the texture.
     */
    public void setSize(int width, int height) {
        this.dirty = this.width != width || this.height != height;
        this.width = width;
        this.height = height;
    }

    /**
     * Setter method for width and height.
     *
     * @param size Size of the texture.
     */
    public void setSize(Vec2i size) {
        this.setSize(size.x(), size.y());
    }

    @Override
    public void updateTexture() {
        if(this.dirty) {
            var pixels = ByteBuffer.allocateDirect(4 * this.getWidth() * this.getHeight());
            for(var x = 0; x < this.getWidth(); x++) {
                var color = this.getGradient().sample(x / (float) this.getWidth());
                for(var y = 0; y < this.getHeight(); y++) {
                    pixels.put((byte) color.r8());
                    pixels.put((byte) color.g8());
                    pixels.put((byte) color.b8());
                    pixels.put((byte) color.a8());
                }
            }
            this.textureData.setImage(pixels.flip(), this.getWidth(), this.getHeight());
            this.dirty = false;
        }
    }
}