package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.rendering.Shader;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Resource loader used to load shader files.
 * <p>
 *     Supports {@code .glsl} files. {@code .vert} and {@code .frag} files are used internally.
 * </p>
 */
public class ShaderLoader implements ResourceLoader {

    /**
     * Regex used to match the {@code #define SHADER_TYPE} preprocessor.
     * Used to get the shader type from the shader code.
     */
    private static final Pattern SHADER_TYPE_REGEX = Pattern.compile("#define\\s+SHADER_TYPE\\s+(\\w+)");

    /**
     * Maps shader files to the contained shader code to ensure the same shader file is not loaded more than once.
     * This variable is not static because there is only one instance of {@code ShaderLoader}.
     */
    private final HashMap<String, String> builtinShaders = new HashMap<>();

    /**
     * Loads the shader code from the given shader file.
     * <p>
     *     Used to load the shader code written by the user.
     * </p>
     * <p>
     *     Returns an empty string if the given file does not exist.
     *     In such cases, the shader should use the builtin shader code only.
     * </p>
     *
     * @param file The shader file to read. Must be the full path of a shader file in the classpath.
     * @return The shader code or an empty string if the shader should use the builtin shader code only.
     */
    private String loadShaderCode(String file) {
        try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
            // Load the shader code if the shader file exists
            if(inputStream != null) {
                return new String(inputStream.readAllBytes());
            }
            // Return an empty string if the shader should use the builtin shader for this stage
        } catch (IOException e) {
            Logger.error("Could not load shader file " + file, e);
        }
        return "";
    }

    /**
     * Extracts the shader type from the given code.
     * <p>
     *     The code for all shader stages should be passed here.
     *     This method will returned the first defined shader type.
     * </p>
     * <p>
     *     Returns an empty string if none of the given shaders define a shader type.
     *     In such cases, an error should be logged.
     * </p>
     *
     * @param shaders Shader code for each stage.
     * @return The shader type to use for this shader.
     */
    private String getShaderType(String... shaders) {
        for(var shaderCode : shaders) {
            // Return the shader type found in the first shader that contains it
            var regex = SHADER_TYPE_REGEX.matcher(shaderCode);
            if(regex.find()) {
                return regex.group(1);
            }
        }
        // An empty shader type indicates that the shader code is invalid
        return "";
    }

    /**
     * Loads the code of a builtin shader.
     * <p>
     *     Returns an empty string and logs an error if the shader could not be loaded.
     * </p>
     *
     * @param shaderType The shader type with the extension indicating the shader stage.
     * @return The builtin shader code or an empty string if the shader could not be loaded.
     */
    private String getBuiltinShader(String shaderType) {
        return this.builtinShaders.computeIfAbsent(shaderType, shader -> {
            // Load one of the builtin shaders in the core module
            var shaderFile = "io/github/ardentengine/core/shaders/" + shader;
            try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(shaderFile)) {
                // Return the shader code if it was loaded correctly
                if(inputStream != null) {
                    return new String(inputStream.readAllBytes());
                }
                // If the shader file does not exist it means the given shader type is not valid
                Logger.error("Could not find shader of type " + shader);
            } catch (IOException e) {
                Logger.error("Could not load builtin shader file " + shaderFile, e);
            }
            // Return an empty string to indicate that an error has occurred
            return "";
        });
    }

    /**
     * Returns the final shader code that will be used in the shader.
     * <p>
     *     The resulting shader code may correspond to the builtin shader code or have the additional code written by the user.
     * </p>
     *
     * @param shaderCode The shader code written by the user. Can be an empty string if the final shader code to use is the builtin shader.
     * @param shaderType The shader type with the extension indicating the shader stage.
     * @return The final shader code to be used in the shader.
     */
    private String getFinalShaderCode(String shaderCode, String shaderType) {
        // Load the requested builtin shader
        var builtinShader = this.getBuiltinShader(shaderType);
        // Use the builtin shader only if the shader code is empty
        if(shaderCode.isEmpty()) {
            return builtinShader;
        }
        // Place the code before the main function or before the first #ifdef preprocessor
        return builtinShader.replaceFirst("(void\\s+main\\s*\\(\\)\\s*\\{|#ifdef\\s+\\w+)", shaderCode + "\n$1");
    }

    // TODO: Get the shader version from ApplicationProperties and add it here

    @Override
    public Object load(String resourcePath) {
        var basePath = resourcePath.substring(0, resourcePath.lastIndexOf('.'));
        // Load the shader stages one by one
        var vertexCode = this.loadShaderCode(basePath + ".vert");
        var fragmentCode = this.loadShaderCode(basePath + ".frag");
        // Get the shader type from the first non-empty shader code
        var shaderType = this.getShaderType(vertexCode, fragmentCode);
        if(!shaderType.isEmpty()) {
            // Paste the shader code into the builtin one or use the builtin one only
            vertexCode = this.getFinalShaderCode(vertexCode, shaderType + ".vert");
            fragmentCode = this.getFinalShaderCode(fragmentCode, shaderType + ".frag");
            // Return the created shader if there was no error while loading it
            if(!vertexCode.isEmpty() && !fragmentCode.isEmpty()) {
                return new Shader(vertexCode, fragmentCode);
            }
        } else {
            // Either the requested shader does not exist or it does not define a shader type
            Logger.error("Cannot get shader type from shader " + resourcePath);
        }
        return null;
    }

    @Override
    public String[] supportedExtensions() {
        return new String[] {".glsl"};
    }

    // TODO: Create tests
}