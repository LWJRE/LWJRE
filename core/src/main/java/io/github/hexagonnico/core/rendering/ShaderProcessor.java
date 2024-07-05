package io.github.hexagonnico.core.rendering;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Shader processor class used to process shader code.
 * Responsible for transforming {@code glsl} code into runnable shader programs.
 */
public class ShaderProcessor {

    // TODO: Find a way to process shader code only when the project is built

    /** Map containing the code for built-in shaders. */
    private static final HashMap<String, String> BUILTIN_SHADER_CODE = new HashMap<>();

    /** Regex pattern that matches the vertex shader function. */
    private static final Pattern VERTEX_FUNCTION_REGEX = Pattern.compile("^void\\s*vertex_shader\\(\\s*\\)\\s*\\{[\\s\\S]*?}\\s*$", Pattern.MULTILINE);
    /** Regex pattern that matches the fragment shader function. */
    private static final Pattern FRAGMENT_FUNCTION_REGEX = Pattern.compile("^void\\s*fragment_shader\\(\\s*\\)\\s*\\{[\\s\\S]*?}\\s*$", Pattern.MULTILINE);

    /**
     * Loads a built-in shader file and returns the shader code as a string.
     * Shader files are loaded the first time this method is called, then the result is cached for future accesses.
     *
     * @param shader Name of the built-in shader to use with the correct extension.
     * @return The code for the requested built-in shader.
     */
    private static String getBuiltinShaderCode(String shader) {
        // TODO: Create a FileUtils class
        return BUILTIN_SHADER_CODE.computeIfAbsent(shader, shaderType -> {
            try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("io/github/hexagonnico/core/shaders/" + shaderType)) {
                if(inputStream != null) {
                    return new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
                }
                return "";
            } catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }

    /** Non-processed shader code. */
    private final String code;
    /** Shader type. Needed to decide which built-in shader to use. */
    private String type;

    /** Final vertex shader code. */
    private String vertexCode;
    /** Final fragment shader code. */
    private String fragmentCode;

    /**
     * Constructs a shader processor.
     *
     * @param code Non-processed shader code.
     */
    public ShaderProcessor(String code) {
        this.code = code;
    }

    /**
     * Returns the type of this shader.
     * <p>
     *     Shader types are used to determine which built-in shader to use as a base.
     *     They are specified by adding a {@code #define SHADER_TYPE} preprocessor in the shader file.
     * </p>
     * <p>
     *     Logs an error and returns an empty string if the shader code does not contain a {@code #define SHADER_TYPE} preprocessor.
     * </p>
     *
     * @return The type of this shader.
     */
    public String getShaderType() {
        if(this.type == null || this.type.isEmpty()) {
            var regex = Pattern.compile("#define\\s+SHADER_TYPE\\s+(\\w+)").matcher(this.code);
            if(regex.find()) {
                this.type = regex.group(1);
            } else {
                System.err.println("No shader type defined in shader. Add a '#define SHADER_TYPE' preprocessor to define a shader type.");
                this.type = "";
            }
        }
        return this.type;
    }

    /**
     * Returns the processed vertex shader code.
     * The code returned can be used to create a vertex shader that can run.
     *
     * @return The processed vertex shader code.
     */
    public String getVertexCode() {
        if(this.vertexCode == null || this.vertexCode.isEmpty()) {
            this.vertexCode = getBuiltinShaderCode(this.getShaderType() + ".vert");
            var fragmentMatcher = FRAGMENT_FUNCTION_REGEX.matcher(this.code);
            if(fragmentMatcher.find()) {
                var vertexMatcher = VERTEX_FUNCTION_REGEX.matcher(this.vertexCode);
                if(vertexMatcher.find()) {
                    this.vertexCode = vertexMatcher.replaceFirst(fragmentMatcher.replaceFirst(""));
                }
            }
        }
        return this.vertexCode;
    }

    /**
     * Returns the processed fragment shader code.
     * The code returned can be used to create a fragment shader that can run.
     *
     * @return The processed fragment shader code.
     */
    public String getFragmentCode() {
        if(this.fragmentCode == null || this.fragmentCode.isEmpty()) {
            this.fragmentCode = getBuiltinShaderCode(this.getShaderType() + ".frag");
            var vertexMatcher = VERTEX_FUNCTION_REGEX.matcher(this.code);
            if(vertexMatcher.find()) {
                var fragmentMatcher = FRAGMENT_FUNCTION_REGEX.matcher(this.fragmentCode);
                if(fragmentMatcher.find()) {
                    this.fragmentCode = fragmentMatcher.replaceFirst(vertexMatcher.replaceFirst(""));
                }
            }
        }
        return this.fragmentCode;
    }
}
