package io.github.ardentengine.maven;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Shader processor class used in {@link ProcessShadersMojo} for processing shader code.
 */
public class ShaderProcessor {

    /** Regex pattern that matches the shader type preprocessor. */
    private static final Pattern SHADER_TYPE_REGEX = Pattern.compile("#define\\s+SHADER_TYPE\\s+(\\w+)");
    /** Regex pattern that matches the header of the vertex shader function. */
    private static final Pattern VERTEX_FUNCTION_REGEX = Pattern.compile("(?m)\\bvoid\\s+vertex_shader\\s*\\(\\s*\\)\\s*\\{");
    /** Regex pattern that matches the header of the fragment shader function. */
    private static final Pattern FRAGMENT_FUNCTION_REGEX = Pattern.compile("(?m)\\bvoid\\s+fragment_shader\\s*\\(\\s*\\)\\s*\\{");

    /** Store the code for base shaders for future use. */
    private static final HashMap<String, String> BASE_SHADERS = new HashMap<>();

    /**
     * Returns the shader code for the given file in the {@code io/github/ardentengine/core/shaders/} directory in the classpath.
     *
     * @param shaderFileName Name of the shader file relative to the {@code io/github/ardentengine/core/shaders/} directory.
     * @return Code of the requested shader file.
     * @throws ShaderProcessingException If a shader file with the given name does not exist.
     * @throws UncheckedIOException If an IO exception occurs while loading the base shader file.
     */
    private static String getBaseShaderFile(String shaderFileName) throws ShaderProcessingException {
        var baseShaderCode = BASE_SHADERS.get(shaderFileName);
        if(baseShaderCode == null) {
            try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("io/github/ardentengine/core/shaders/" + shaderFileName)) {
                if(inputStream != null) {
                    baseShaderCode = new String(inputStream.readAllBytes());
                    BASE_SHADERS.put(shaderFileName, baseShaderCode);
                } else {
                    throw new ShaderProcessingException("There is no shader of type " + shaderFileName.substring(shaderFileName.lastIndexOf('.')));
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Could not load shader " + shaderFileName + " from classpath", e);
            }
        }
        return baseShaderCode;
    }

    /**
     * Returns the shader type defined in the given shader code.
     * <p>
     *     The shader type is defined in the shader code with a {@code #define SHADER_TYPE} macro.
     * </p>
     *
     * @param shaderCode Shader code.
     * @return The shader type defined in the given code.
     * @throws ShaderProcessingException If the given code does not contain a {@code #define SHADER_TYPE} macro.
     */
    public static String getShaderType(String shaderCode) throws ShaderProcessingException {
        var shaderTypeMatcher = SHADER_TYPE_REGEX.matcher(shaderCode);
        if(shaderTypeMatcher.find()) {
            return shaderTypeMatcher.group(1);
        }
        throw new ShaderProcessingException("No shader type defined in shader");
    }

    /**
     * Removes comments from the given code.
     * <p>
     *     Replaces all single-line and multi-line comments with an empty string.
     * </p>
     *
     * @param code The code from which to remove comments.
     * @return The resulting code with comments removed.
     */
    public static String removeComments(String code) {
        return code.replaceAll("(?m)[ \\t]*((//.*)|(/\\*[\\s\\S]*?\\*/))", "");
    }

    /**
     * Finds the end position of a shader function by matching opening and closing brackets.
     *
     * @param shaderCode Shader code.
     * @param start Start index to search from. Must match the first character after the function's first open bracket.
     * @return The index of the last character of the function.
     */
    private static int findIndex(String shaderCode, int start) {
        var brackets = 1;
        var index = start;
        while(brackets > 0 && index < shaderCode.length()) {
            if(shaderCode.charAt(index) == '{') {
                brackets++;
            } else if(shaderCode.charAt(index) == '}') {
                brackets--;
            }
            index++;
        }
        return index;
    }

    /**
     * Removes a function whose header is matched by the given regex from the given code.
     * The entire function, from its header to its last closing bracket is removed from the code.
     *
     * @param shaderCode Shader code.
     * @param regex A regex matching the function's header.
     * @return The resulting shader code without the removed function.
     */
    private static String removeFunction(String shaderCode, Pattern regex) {
        var matcher = regex.matcher(shaderCode);
        if(matcher.find()) {
            var index = findIndex(shaderCode, matcher.end());
            return shaderCode.substring(0, matcher.start()) + shaderCode.substring(index);
        }
        return shaderCode;
    }

    /**
     * Removes a void function with the given name from the given code.
     * The entire function, from its header to its last closing bracket is removed from the code.
     *
     * @param shaderCode Shader code.
     * @param functionName Name of the function to remove.
     * @return The resulting shader code without the removed function.
     */
    public static String removeFunction(String shaderCode, String functionName) {
        return removeFunction(shaderCode, Pattern.compile("\\bvoid\\s+" + functionName + "\\s*\\(\\s*\\)\\s*\\{"));
    }

    /**
     * Removes all white spaces from the given code.
     *
     * @param code The code from which to remove white spaces.
     * @return The resulting code with white spaces removed.
     */
    public static String removeWhiteSpaces(String code) {
        return code.replaceAll("(?m)^\\s+\\r?", "").replaceAll("((?!\\b) +| +(?!\\b))", "").trim();
    }

    // TODO: Add a method that looks for duplicate identifiers and renames them

    /**
     * Extracts the vertex shader code from the given unprocessed shader code.
     *
     * @param shaderCode Unprocessed shader code read from a {@code .glsl} shader file.
     * @return Processed vertex shader code.
     * @throws ShaderProcessingException If the given shader code does not contain a {@code #define SHADER_TYPE} macro or the defined shader type does not exist.
     * @throws UncheckedIOException If an IO exception occurs while loading the base shader file.
     */
    public static String extractVertexCode(String shaderCode) throws ShaderProcessingException {
        // Get the base shader file used by the given shader code
        var baseShaderCode = getBaseShaderFile(getShaderType(shaderCode) + ".vert");
        // Check if this shader contains the vertex function
        if(VERTEX_FUNCTION_REGEX.matcher(shaderCode).find()) {
            // Remove comments to prevent the regex from matching commented code
            shaderCode = removeComments(shaderCode);
            // Remove the fragment function
            shaderCode = removeFunction(shaderCode, FRAGMENT_FUNCTION_REGEX);
            // TODO: Throw an error if the fragment shader function is being invoked where not allowed
            // Change 'main' to '_main' to prevent this function from clashing with the main function
            shaderCode = shaderCode.replace("main", "_main");
            // Add the shader code before the main function
            // TODO: Add the #line directive for debugging purposes
            shaderCode = baseShaderCode.replaceFirst("\\bvoid\\s+main\\s*\\(\\s*\\)\\s*\\{", shaderCode + "\nvoid main() {");
            // Replace varying variables
            shaderCode = shaderCode.replaceAll("\\bvarying\\b", "out");
        } else {
            // Use the same shader code as the base if this shader does not contain the vertex function
            return baseShaderCode;
        }
        // TODO: Add an option to keep white spaces for easier debugging
        return removeWhiteSpaces(shaderCode);
    }

    /**
     * Extracts the fragment shader code from the given unprocessed shader code.
     *
     * @param shaderCode Unprocessed shader code read from a {@code .glsl} shader file.
     * @return Processed fragment shader code.
     * @throws ShaderProcessingException If the given shader code does not contain a {@code #define SHADER_TYPE} macro or the defined shader type does not exist.
     * @throws UncheckedIOException If an IO exception occurs while loading the base shader file.
     */
    public static String extractFragmentCode(String shaderCode) throws ShaderProcessingException {
        // Get the base shader file used by the given shader code
        var baseShaderCode = getBaseShaderFile(getShaderType(shaderCode) + ".frag");
        // Check if this shader contains the fragment function
        if(FRAGMENT_FUNCTION_REGEX.matcher(shaderCode).find()) {
            // Remove comments to prevent the regex from matching commented code
            shaderCode = removeComments(shaderCode);
            // Remove the vertex function
            shaderCode = removeFunction(shaderCode, VERTEX_FUNCTION_REGEX);
            // TODO: Throw an error if the vertex shader function is being invoked where not allowed
            // Change 'main' to '_main' to prevent this function from clashing with the main function
            shaderCode = shaderCode.replace("main", "_main");
            // Add the shader code before the main function
            // TODO: Add the #line directive for debugging purposes
            shaderCode = baseShaderCode.replaceFirst("\\bvoid\\s+main\\s*\\(\\s*\\)\\s*\\{", shaderCode + "\nvoid main() {");
            // Replace varying variables
            shaderCode = shaderCode.replaceAll("\\bvarying\\b", "in");
        } else {
            // Use the same shader code as the base if this shader does not contain the fragment function
            return baseShaderCode;
        }
        // TODO: Add an option to keep white spaces for easier debugging
        return removeWhiteSpaces(shaderCode);
    }
}