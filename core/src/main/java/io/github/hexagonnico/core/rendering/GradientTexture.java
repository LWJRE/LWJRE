package io.github.hexagonnico.core.rendering;

import io.github.scalamath.colorlib.Gradient;

import java.nio.ByteBuffer;

/**
 * A texture created with colors sampled from a {@link Gradient}.
 */
public class GradientTexture extends Texture {

    private Gradient gradient = null;
    // TODO: Direction and fill mode (linear, radial, square)
    private int width = 0;
    private int height = 0;
    private boolean dirty = true;

    public void setGradient(Gradient gradient) {
        this.gradient = gradient;
        this.dirty = true;
    }

    public Gradient getGradient() {
        return this.gradient == null ? this.gradient = new Gradient() : this.gradient;
    }

    public void setWidth(int width) {
        this.width = width;
        this.dirty = true;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setHeight(int height) {
        this.height = height;
        this.dirty = true;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void bind() {
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
        super.bind();
    }
}
