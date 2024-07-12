package io.github.hexagonnico.maven;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Static class containing methods used for processing the shader code.
 */
public class ShaderProcessor {

    /** Regex pattern that matches the shader type preprocessor. */
    private static final Pattern SHADER_TYPE_REGEX = Pattern.compile("#define\\s+SHADER_TYPE\\s+(\\w+)");
    /** Regex pattern that matches the vertex shader function. */
    private static final Pattern VERTEX_FUNCTION_REGEX = Pattern.compile("^void\\s*vertex_shader\\(\\s*\\)\\s*\\{[\\s\\S]*?}\\s*$", Pattern.MULTILINE);
    /** Regex pattern that matches the fragment shader function. */
    private static final Pattern FRAGMENT_FUNCTION_REGEX = Pattern.compile("^void\\s*fragment_shader\\(\\s*\\)\\s*\\{[\\s\\S]*?}\\s*$", Pattern.MULTILINE);

    /** Stores the code for base shaders. */
    private static final HashMap<String, String> BASE_SHADER_CODE = new HashMap<>();

    /**
     * Returns the code for the requested shader.
     *
     * @param baseShaderFile Name of the shader file.
     * @return The code of the requested shader.
     * @throws ShaderProcessorException If the given shader file could not be found or an IO error occurs.
     */
    public static String getBaseShaderCode(String baseShaderFile) throws ShaderProcessorException {
        baseShaderFile = "io/github/hexagonnico/core/shaders/" + baseShaderFile;
        var shaderCode = BASE_SHADER_CODE.get(baseShaderFile);
        if(shaderCode == null) {
            try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(baseShaderFile)) {
                if(inputStream != null) {
                    shaderCode = new String(inputStream.readAllBytes());
                    BASE_SHADER_CODE.put(baseShaderFile, shaderCode);
                } else {
                    throw new ShaderProcessorException("Could not find base shader file " + baseShaderFile);
                }
            } catch(IOException e) {
                throw new ShaderProcessorException("Cannot read file " + baseShaderFile, e);
            }
        }
        return shaderCode;
    }

    /**
     * Processes the given shader code.
     *
     * @param shaderCode The code to process.
     * @return The processed runnable glsl shader code.
     * @throws ShaderProcessorException If the shader specified in the SHADER_TYPE preprocessor could not be found.
     */
    public static String processShaderCode(String shaderCode) throws ShaderProcessorException {
        var shaderTypeMatcher = SHADER_TYPE_REGEX.matcher(shaderCode);
        if(shaderTypeMatcher.find()) {
            if(VERTEX_FUNCTION_REGEX.matcher(shaderCode).find()) {
                var matcher = VERTEX_FUNCTION_REGEX.matcher(getBaseShaderCode(shaderTypeMatcher.group(1) + ".vert"));
                shaderCode = matcher.replaceFirst(shaderCode);
            } else if(FRAGMENT_FUNCTION_REGEX.matcher(shaderCode).find()) {
                var matcher = FRAGMENT_FUNCTION_REGEX.matcher(getBaseShaderCode(shaderTypeMatcher.group(1) + ".frag"));
                shaderCode = matcher.replaceFirst(shaderCode);
            }
        }
        return shaderCode;
    }

    /**
     * Removes all blank lines, trailing spaces, and comments from the given shader code.
     *
     * @param shaderCode The shader code.
     * @return The given code without blank lines, trailing spaces, and comments.
     */
    public static String trimCode(String shaderCode) {
        return shaderCode.replaceAll("(?m)[ \\t]*((//.*)|(/\\*[\\s\\S]*?\\*/))", "").replaceAll("(?m)^\\s+\\r?", "").trim();
    }
}
