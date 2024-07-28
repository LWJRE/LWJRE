package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.rendering.Shader;

import java.io.IOException;

/**
 * Resource loader used to load shader files.
 * <p>
 *     Returns instances of {@link Shader}.
 *     This loader expects the shader files in the classpath to contain the processed shader code.
 * </p>
 * <p>
 *     Supports {@code .glsl} files.
 * </p>
 */
public class ShaderLoader implements ResourceLoader {

    @Override
    public Object load(String resourcePath) {
        var classLoader = Thread.currentThread().getContextClassLoader();
        var basePath = resourcePath.substring(0, resourcePath.lastIndexOf('.'));
        try(var vertexFile = classLoader.getResourceAsStream(basePath + ".vert"); var fragmentFile = classLoader.getResourceAsStream(basePath + ".frag")) {
            if(vertexFile == null) {
                Logger.error("Could not find file " + basePath + ".vert");
            } else if(fragmentFile == null) {
                Logger.error("Could not find file " + basePath + ".frag");
            } else {
                var shader = new Shader();
                var vertexCode = new String(vertexFile.readAllBytes());
                var fragmentCode = new String(fragmentFile.readAllBytes());
                shader.compile(vertexCode, fragmentCode);
                return shader;
            }
        } catch (IOException e) {
            Logger.error("Exception occurred while loading shader " + resourcePath, e);
        }
        return null;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".glsl"};
    }
}