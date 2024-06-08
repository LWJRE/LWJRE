package io.github.hexagonnico.core.resources;

import io.github.hexagonnico.core.rendering.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ShaderLoader implements ResourceLoader {

    @Override
    public Object load(String path) {
        var shader = new Shader();
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if(inputStream == null) {
                System.err.println("Could not find shader file " + path);
            } else {
                var code = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
                shader.compile(code);
            }
        } catch(IOException e) {
            System.err.println("Error loading shader file " + path);
            e.printStackTrace();
        }
        return shader;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".glsl"};
    }
}
