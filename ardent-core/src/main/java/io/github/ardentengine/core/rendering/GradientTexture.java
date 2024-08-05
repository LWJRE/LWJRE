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

    /**
     * Setter method for gradient.
     *
     * @param gradient Gradient used to fill the texture.
     */
    public void setGradient(Gradient gradient) {
        gradient = Objects.requireNonNullElse(gradient, new Gradient());
        if(!this.gradient.equals(gradient)) {
            this.gradient = gradient;
            RenderingServer.update(this);
        }
    }

    /**
     * Getter method for gradient.
     *
     * @return Gradient used to fill the texture.
     */
    public Gradient getGradient() {
        return this.gradient;
    }

    @Override
    public ByteBuffer getPixels() {
        var pixels = ByteBuffer.allocateDirect(4 * this.getWidth() * this.getHeight());
        for(var x = 0; x < this.getWidth(); x++) {
            var color = this.getGradient().sample(x / (float) this.getWidth());
            for(var y = 0; y < this.getHeight(); y++) {
                pixels.putInt(color.rgba());
            }
        }
        return pixels.flip();
    }

    /**
     * Setter method for width.
     *
     * @param width Width of the texture. Also represents the number of horizontal color samples that will be obtained from the gradient.
     */
    public void setWidth(int width) {
        if(this.width != width) {
            this.width = width;
            RenderingServer.update(this);
        }
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
        if(this.height != height) {
            this.height = height;
            RenderingServer.update(this);
        }
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
        if(this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            RenderingServer.update(this);
        }
    }

    /**
     * Setter method for width and height.
     *
     * @param size Size of the texture.
     */
    public void setSize(Vec2i size) {
        this.setSize(size.x(), size.y());
    }
}