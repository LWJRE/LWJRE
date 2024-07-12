package io.github.hexagonnico.maven;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestShaderProcessor {

    private static String loadTextFile(String file) {
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
            if(inputStream != null) {
                return new String(inputStream.readAllBytes());
            } else {
                Assertions.fail("Could not find file " + file);
            }
        } catch(IOException e) {
            Assertions.fail("Cannot read file " + file, e);
        }
        return "";
    }

    @Test
    public void testGetBaseSpriteVertexShader() {
        try {
            var expected = loadTextFile("io/github/hexagonnico/core/shaders/sprite_2d.vert");
            var actual = ShaderProcessor.getBaseShaderCode("sprite_2d.vert");
            Assertions.assertEquals(expected, actual);
        } catch (ShaderProcessorException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testGetBaseSpriteFragmentShader() {
        try {
            var expected = loadTextFile("io/github/hexagonnico/core/shaders/sprite_2d.frag");
            var actual = ShaderProcessor.getBaseShaderCode("sprite_2d.frag");
            Assertions.assertEquals(expected, actual);
        } catch (ShaderProcessorException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testProcessVertexShaderNoTrim() {
        try {
            var expected = loadTextFile("expected_vertex_shader.glsl");
            var shaderCode = loadTextFile("test_vertex_shader.glsl");
            shaderCode = ShaderProcessor.processShaderCode(shaderCode);
            Assertions.assertEquals(expected, shaderCode);
        } catch (ShaderProcessorException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testProcessFragmentShaderNoTrim() {
        try {
            var expected = loadTextFile("expected_fragment_shader.glsl");
            var shaderCode = loadTextFile("test_fragment_shader.glsl");
            shaderCode = ShaderProcessor.processShaderCode(shaderCode);
            Assertions.assertEquals(expected, shaderCode);
        } catch (ShaderProcessorException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testTrimCode() {
        var expected = loadTextFile("expected_trim_code.glsl");
        var shaderCode = loadTextFile("test_trim_code.glsl");
        shaderCode = ShaderProcessor.trimCode(shaderCode);
        Assertions.assertEquals(expected, shaderCode);
    }
}
