package io.github.ardentengine.maven;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestShaderProcessor {

    @Test
    public void testGetShaderType() {
        var code = """
            #define SHADER_TYPE test_shader

            void fragment_shader() {
                frag_color = vec4(1.0, 1.0, 1.0, 1.0);
            }
            """;
        try {
            Assertions.assertEquals("test_shader", ShaderProcessor.getShaderType(code));
        } catch (ShaderProcessingException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testGetShaderTypeThrowsException() {
        var code = """
            void fragment_shader() {
                frag_color = vec4(1.0, 1.0, 1.0, 1.0);
            }
            """;
        Assertions.assertThrows(ShaderProcessingException.class, () -> ShaderProcessor.getShaderType(code));
    }

    @Test
    public void testRemoveComments() {
        var code = """
            // This is a test shader code
            void fragment_shader() {
                frag_color = vec4(1.0, 1.0, 1.0, 1.0); // Contains comments
            }
            /*
                Multiline comment
            */
            void vertex_shader() {
                vertex.y *= 0.9;
            }
            """;
        var expected = """

           void fragment_shader() {
               frag_color = vec4(1.0, 1.0, 1.0, 1.0);
           }

           void vertex_shader() {
               vertex.y *= 0.9;
           }
           """;
        Assertions.assertEquals(expected, ShaderProcessor.removeComments(code));
    }

    @Test
    public void testRemoveFunction() {
        var code = """
            void scale_uv() {
                uv *= 1.1;
            }
            void vertex_shader() {

            }
            """;
        var expected = """

            void vertex_shader() {

            }
            """;
        Assertions.assertEquals(expected, ShaderProcessor.removeFunction(code, "scale_uv"));
    }

    @Test
    public void testRemoveFunctionWithNestedBlocks() {
        var code = """
            void function() {
                for(int i = 0; i < 100; i++) {
                    if(i % 2 == 0) {
                        // Something
                    } else {
                        // Something else
                    }
                }
            }
            void vertex_shader() {

            }
            """;
        var expected = """

            void vertex_shader() {

            }
            """;
        Assertions.assertEquals(expected, ShaderProcessor.removeFunction(code, "function"));
    }

    @Test
    public void testRemoveWhiteSpaces() {
        var code = """
            #define SHADER_TYPE test_shader

            void vertex_shader() {
                uv.y = 1.0 - uv.y;
            }

            void fragment_shader() {
                frag_color = vec4(1.0, 1.0, 1.0, 1.0);
            }
            """;
        var expected = """
            #define SHADER_TYPE test_shader
            void vertex_shader(){
            uv.y=1.0-uv.y;
            }
            void fragment_shader(){
            frag_color=vec4(1.0,1.0,1.0,1.0);
            }""";
        Assertions.assertEquals(expected, ShaderProcessor.removeWhiteSpaces(code));
    }

    // TODO: Test full shader processing
}