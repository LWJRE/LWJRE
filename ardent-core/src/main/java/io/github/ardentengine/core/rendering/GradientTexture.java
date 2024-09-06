package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.RenderingServer;
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
     * Setter method for {@link GradientTexture#gradient}.
     *
     * @param gradient Gradient used to fill the texture.
     */
    public void setGradient(Gradient gradient) {
        gradient = Objects.requireNonNullElse(gradient, new Gradient());
        if(!this.gradient.equals(gradient)) {
            this.gradient = gradient;
            RenderingServer.getInstance().update(this);
        }
    }

    /**
     * Getter method for {@link GradientTexture#gradient}.
     *
     * @return Gradient used to fill the texture.
     */
    public Gradient gradient() {
        return this.gradient;
    }

    @Override
    public ByteBuffer pixels() {
        var pixels = ByteBuffer.allocateDirect(4 * this.width() * this.height());
        for(var x = 0; x < this.width(); x++) {
            var color = this.gradient().sample(x / (float) this.width());
            for(var y = 0; y < this.height(); y++) {
                pixels.putInt(color.rgba());
            }
        }
        return pixels.flip();
    }

    /**
     * Setter method for {@link GradientTexture#width}.
     *
     * @param width Width of the texture. Also represents the number of horizontal color samples that will be obtained from the gradient.
     */
    public void setWidth(int width) {
        if(this.width != width) {
            this.width = width;
            RenderingServer.getInstance().update(this);
        }
    }

    @Override
    public int width() {
        return this.width;
    }

    /**
     * Setter method for {@link GradientTexture#height}.
     *
     * @param height Height of the texture. Also represents the number of vertical color samples that will be obtained from the gradient.
     */
    public void setHeight(int height) {
        if(this.height != height) {
            this.height = height;
            RenderingServer.getInstance().update(this);
        }
    }

    @Override
    public int height() {
        return this.height;
    }

    /**
     * Setter method for {@link GradientTexture#width} and {@link GradientTexture#height}.
     *
     * @param width Width of the texture.
     * @param height Height of the texture.
     */
    public void setSize(int width, int height) {
        if(this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            RenderingServer.getInstance().update(this);
        }
    }

    /**
     * Setter method for {@link GradientTexture#width} and {@link GradientTexture#height}.
     *
     * @param size Size of the texture.
     */
    public void setSize(Vec2i size) {
        this.setSize(size.x(), size.y());
    }
}