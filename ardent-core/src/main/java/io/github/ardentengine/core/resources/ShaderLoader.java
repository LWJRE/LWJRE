package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.rendering.Shader;

import java.io.IOException;

public class ShaderLoader implements ResourceLoader {

    @Override
    public Object load(String resourcePath) {
        var classLoader = Thread.currentThread().getContextClassLoader();
        var basePath = resourcePath.substring(0, resourcePath.lastIndexOf('.'));
        try(var vertexFile = classLoader.getResourceAsStream(basePath + ".vert"); var fragmentFile = classLoader.getResourceAsStream(basePath + ".frag")) {
            var shader = new Shader();
            if(vertexFile == null) {
                Logger.error("Could not find file " + basePath + ".vert");
            } else if(fragmentFile == null) {
                Logger.error("Could not find file " + basePath + ".frag");
            } else {
                shader.setVertexCode(new String(vertexFile.readAllBytes()));
                shader.setFragmentCode(new String(fragmentFile.readAllBytes()));
            }
            return shader;
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