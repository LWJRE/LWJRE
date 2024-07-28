package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.rendering.ImageTexture;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Resource loader used to load texture files.
 * Loads instances of {@link ImageTexture}.
 * <p>
 *     Reads image files as a {@link ByteBuffer} and calls {@link ImageTexture#setImage(ByteBuffer, int, int)}.
 * </p>
 */
public class TextureLoader implements ResourceLoader {

    @Override
    public Object load(String resourcePath) {
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if(inputStream == null) {
                Logger.error("Could not find image file " + resourcePath);
            } else {
                var image = ImageIO.read(inputStream);
                var buffer = ByteBuffer.allocateDirect(4 * image.getWidth() * image.getHeight());
                for(var pixel : image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth())) {
                    buffer.put((byte) ((pixel >> 16) & 0xff));
                    buffer.put((byte) ((pixel >> 8) & 0xff));
                    buffer.put((byte) (pixel & 0xff));
                    buffer.put((byte) ((pixel >> 24) & 0xff));
                }
                var texture = new ImageTexture();
                texture.setImage(buffer.flip(), image.getWidth(), image.getHeight());
                // TODO: Load texture properties
                return texture;
            }
        } catch (IOException e) {
            Logger.error("Exception occurred while loading image " + resourcePath, e);
        }
        return null;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".png"}; // TODO: Test other types
    }
}