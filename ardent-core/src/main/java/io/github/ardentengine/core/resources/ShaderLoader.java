package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.rendering.Shader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Resource loader used to load shader files.
 * <p>
 *     Loads the shader code as a {@link String}.
 *     The loaded code is used to create a {@link Shader} program.
 * </p>
 * <p>
 *     Supports {@code .glsl}, {@code .vert}, and {@code .frag} files.
 * </p>
 */
public class ShaderLoader implements ResourceLoader {

    @Override
    public Object load(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".glsl", ".vert", ".frag"};
    }
}
