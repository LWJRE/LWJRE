package io.github.ardentengine.maven;

import java.util.regex.Pattern;

/**
 * Shader processor class used in {@link ProcessShadersMojo} for processing shader code.
 */
public class ShaderProcessor {

    /** Regex pattern that matches the header of the vertex shader function. */
    private static final Pattern VERTEX_FUNCTION_REGEX = Pattern.compile("(?m)\\bvoid\\s+vertex_shader\\s*\\(\\s*\\)\\s*\\{");
    /** Regex pattern that matches the header of the fragment shader function. */
    private static final Pattern FRAGMENT_FUNCTION_REGEX = Pattern.compile("(?m)\\bvoid\\s+fragment_shader\\s*\\(\\s*\\)\\s*\\{");

    // TODO: Differentiate between shader processor for OpenGL and for Vulkan

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
     * Removes all white spaces from the given code.
     *
     * @param code The code from which to remove white spaces.
     * @return The resulting code with white spaces removed.
     */
    public static String removeWhiteSpaces(String code) {
        return code.replaceAll("(?m)^\\s+\\r?", "").replaceAll("((?!\\b) +| +(?!\\b))", "").trim();
    }

    public static String trimCode(String code) {
        // TODO: Remove '0.' and '.0'
        return removeWhiteSpaces(removeComments(code));
    }

    private static String processVariables(String shaderCode) {
        var regex = Pattern.compile("(uniform|varying)\\s+(\\w+)\\s+(\\w+)").matcher(shaderCode);
        while(regex.find()) {
            shaderCode = shaderCode.replaceAll("(?<!\\.)\\b" + regex.group(3) + "\\b", regex.group(1).charAt(0) + "_" + regex.group(3));
        }
        return shaderCode;
    }

    private static String processFunctions(String shaderCode) {
        var regex = Pattern.compile("(\\w+)\\s*\\b(?!(fragment_shader|vertex_shader)\\b)(\\w+)\\b\\s*\\(([\\s\\S]*?)\\)\\s*\\{").matcher(shaderCode);
        while(regex.find()) {
            shaderCode = shaderCode.replace(regex.group(3), "f_" + regex.group(3));
        }
        return shaderCode;
    }

    // TODO: Do the same, but for structs

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

    public static String addConstants(String shaderCode) {
        var index = shaderCode.indexOf('\n');
        return shaderCode.substring(0, index) + "\n#define PI " + Math.PI + "\n#define E " + Math.E + "\n" + shaderCode.substring(index + 1);
    }

    // TODO: Unify these two methods

    public static String extractVertexCode(String shaderCode) {
        // Check if this shader contains the vertex function
        if(VERTEX_FUNCTION_REGEX.matcher(shaderCode).find()) {
            // Remove comments to prevent the regex from matching commented code
            shaderCode = removeComments(shaderCode);
            // Remove the fragment function
            shaderCode = removeFunction(shaderCode, FRAGMENT_FUNCTION_REGEX);
            // Prepend a prefix to functions and variables to prevent them from clashing with the builtin shader
            shaderCode = processVariables(shaderCode);
            shaderCode = processFunctions(shaderCode);
            // Replace varying variables
            shaderCode = shaderCode.replaceAll("\\bvarying\\b", "out");
            // TODO: Add an option to keep white spaces for easier debugging
            return removeWhiteSpaces(shaderCode);
        }
        return "";
    }

    public static String extractFragmentCode(String shaderCode) {
        // Check if this shader contains the fragment function
        if(FRAGMENT_FUNCTION_REGEX.matcher(shaderCode).find()) {
            // Remove comments to prevent the regex from matching commented code
            shaderCode = removeComments(shaderCode);
            // Remove the vertex function
            shaderCode = removeFunction(shaderCode, VERTEX_FUNCTION_REGEX);
            // Prepend a prefix to functions and variables to prevent them from clashing with the builtin shader
            shaderCode = processVariables(shaderCode);
            shaderCode = processFunctions(shaderCode);
            // Replace varying variables
            shaderCode = shaderCode.replaceAll("\\bvarying\\b", "in");
            // TODO: Add an option to keep white spaces for easier debugging
            return removeWhiteSpaces(shaderCode);
        }
        return "";
    }
}