package io.github.hexagonnico.core.resources;

import io.github.hexagonnico.core.rendering.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Resource loader used to load shader files.
 * <p>
 *     The {@link ShaderLoader#load(InputStream)} method creates a new {@link Shader}, loads the code and calls the {@link Shader#compile(String)} method with the loaded code.
 *     Shader files contain the glsl shader code that needs to be processed before it is compiled into a {@code Shader} program that can run.
 * </p>
 * <p>
 *     Supports {@code .glsl} files.
 * </p>
 */
public class ShaderLoader implements ResourceLoader {

    @Override
    public Object load(InputStream inputStream) throws IOException {
        var shader = new Shader();
        var code = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        shader.compile(code);
        return shader;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".glsl"};
    }
}
