package io.github.hexagonnico.core.resources;

import io.github.hexagonnico.core.rendering.ImageTexture;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TextureLoader implements ResourceLoader {

    @Override
    public Object load(String path) {
        try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if(inputStream == null) {
                System.err.println("Could not find texture file " + path);
                return null;
            }
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
        } catch(IOException e) {
            System.err.println("Could not load texture file " + path);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".png"}; // TODO: Test other types
    }
}
