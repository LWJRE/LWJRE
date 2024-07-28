package io.github.ardentengine.maven;

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
    public void testProcessBaseShader() {
        var shaderProcessor = new ShaderProcessor("");
        var baseShaderCode = loadTextFile("test_base.glsl");
        var expectedVertexCode = loadTextFile("test_base.vert");
        var expectedFragmentCode = loadTextFile("test_base.frag");
        Assertions.assertEquals(expectedVertexCode, shaderProcessor.extractVertexCode(baseShaderCode));
        Assertions.assertEquals(expectedFragmentCode, shaderProcessor.extractFragmentCode(baseShaderCode));
    }
}