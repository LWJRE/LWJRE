package io.github.hexagonnico.core.resources;

import io.github.hexagonnico.core.rendering.Shader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ShaderLoader implements ResourceLoader {

    @Override
    public Object load(String path) {
        var shader = new Shader();
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if(inputStream == null) {
                throw new FileNotFoundException(); // TODO: Better error handling
            }
            var code = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            shader.compile(code);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return shader;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".glsl"};
    }
}
