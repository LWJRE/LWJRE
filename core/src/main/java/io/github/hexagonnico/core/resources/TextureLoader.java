package io.github.hexagonnico.core.resources;

import io.github.hexagonnico.core.rendering.ImageTexture;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Resource loader used to load texture files.
 * Loads instances of {@link ImageTexture}.
 * <p>
 *     Reads image files as a {@link ByteBuffer} and calls {@link ImageTexture#setPixels(ByteBuffer, int, int)}.
 * </p>
 */
public class TextureLoader implements ResourceLoader {

    @Override
    public Object load(InputStream inputStream) throws IOException {
        var image = ImageIO.read(inputStream);
        var buffer = ByteBuffer.allocateDirect(4 * image.getWidth() * image.getHeight());
        for(var pixel : image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth())) {
            buffer.put((byte) ((pixel >> 16) & 0xff));
            buffer.put((byte) ((pixel >> 8) & 0xff));
            buffer.put((byte) (pixel & 0xff));
            buffer.put((byte) ((pixel >> 24) & 0xff));
        }
        var texture = new ImageTexture();
        texture.setPixels(buffer.flip(), image.getWidth(), image.getHeight());
        return texture;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".png"}; // TODO: Test other types
    }
}
