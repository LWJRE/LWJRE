package io.github.ardentengine.maven;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Shader processor class used in {@link ProcessShadersMojo} for processing shader code.
 */
public class ShaderProcessor {

    /** Regex pattern that matches the shader type preprocessor. */
    private static final Pattern SHADER_TYPE_REGEX = Pattern.compile("#define\\s+SHADER_TYPE\\s+(\\w+)");

    /** Store the code for base shaders for future use. */
    private final HashMap<String, String> baseShaders = new HashMap<>();

    /**
     * Path to the {@code ardent-core} artifact in the maven local repository.
     * Needed to get the code for base shaders.
     * <p>
     *     Note that since this plugin is also used on the core artifact itself,
     *     the core artifact will not be present while it is being built.
     * </p>
     */
    private final Path coreArtifactJarFile;

    /**
     * Constructs a new shader processor.
     *
     * @param coreArtifactJarFile Path to the {@code ardent-core} artifact in the maven local repository.
     */
    public ShaderProcessor(Path coreArtifactJarFile) {
        this.coreArtifactJarFile = coreArtifactJarFile;
    }

    /**
     * Constructs a new shader processor.
     * Shorthand for {@code new ShaderProcessor(Path.of(path...))}.
     * The given path must point to the {@code ardent-core} artifact in the maven local repository.
     *
     * @param pathBase The path string or initial part of the path string.
     * @param path Additional strings to be joined to form the path string.
     */
    public ShaderProcessor(String pathBase, String... path) {
        this(Path.of(pathBase, path));
    }

    /**
     * Returns the shader code for the given file in the {@code ardent-core} jar file.
     *
     * @param shaderFileName Name of the shader file (not the full path).
     * @return Code of the requested shader file.
     * @throws UncheckedIOException If an IO error occurs while loading the base shader file.
     */
    private String getBaseShaderFile(String shaderFileName) {
        return this.baseShaders.computeIfAbsent(shaderFileName, key -> {
            try(var fileSystem = FileSystems.newFileSystem(this.coreArtifactJarFile)) {
                return Files.readString(fileSystem.getPath("io/github/ardentengine/core/shaders", key));
            } catch (IOException e) {
                throw new UncheckedIOException("Could not load shader " + key + " from " + this.coreArtifactJarFile, e);
            }
        });
    }

    /**
     * Extracts the vertex shader code from the given unprocessed shader code.
     *
     * @param shaderCode Unprocessed shader code.
     * @return Vertex shader code.
     * @throws UncheckedIOException If the given shader code contains a {@code #define SHADER_TYPE} preprocessor and an IO error occurs while loading the base shader code.
     */
    public String extractVertexCode(String shaderCode) {
        // Remove the fragment function
        shaderCode = shaderCode.replaceFirst("(?m)^(\\s*)void\\s+fragment_shader\\s*\\(\\s*\\)\\s*\\{[\\s\\S]*?^\\1}\\s*$", "");
        // Check if this shader file uses one of the built-in shaders
        var shaderTypeMatcher = SHADER_TYPE_REGEX.matcher(shaderCode);
        if(shaderTypeMatcher.find() && Files.exists(this.coreArtifactJarFile)) {
            // Look for the base shader file in the core artifact if it exists
            var baseShaderCode = this.getBaseShaderFile(shaderTypeMatcher.group(1) + ".vert");
            // Add the vertex shader function before the main function
            shaderCode = baseShaderCode.replaceFirst("void main\\(\\)\\{", shaderCode + "\nvoid main(){");
            // Call the vertex shader function at the beginning of the main function
            shaderCode = shaderCode.replaceFirst("(void main\\(\\)\\{(\\n(\\w+)=in_\\3;)*)([\\s\\S]*?})", "$1\nvertex_shader();$4");
        } else {
            // Remove out variables since they are only relevant for the fragment shader
            shaderCode = shaderCode.replaceAll("(?m)^out\\s+\\w+\\s+\\w+;", "");
            // Convert in variables in out variables
            var matcher = Pattern.compile("(?m)^((layout\\s*\\(\\s*location\\s*=\\s*\\d+\\s*\\))*\\s*in)\\s+(\\w+)\\s+(\\w+);").matcher(shaderCode);
            shaderCode = matcher.replaceAll(matchResult -> matchResult.group(1) + " " + matchResult.group(3) + " in_" + matchResult.group(4) + ";\nout " + matchResult.group(3) + " " + matchResult.group(4) + ";");
            matcher.reset();
            // Initialize out variables at the beginning of the main function
            var mainFunction = new StringBuilder("void main(){\n");
            while(matcher.find()) {
                mainFunction.append(matcher.group(4)).append("=in_").append(matcher.group(4)).append(";\n");
            }
            // Replace the vertex function with the main function
            shaderCode = shaderCode.replaceFirst("(?m)^void\\s*vertex_shader\\s*\\(\\s*\\)\\s*\\{", mainFunction.toString());
            // Replace varying variables
            shaderCode = shaderCode.replaceAll("\\bvarying\\b", "out");
        }
        // Remove comments and white spaces
        shaderCode = trimCode(shaderCode);
        return shaderCode;
    }

    /**
     * Extracts the fragment shader code from the given unprocessed shader code.
     *
     * @param shaderCode Unprocessed shader code.
     * @return Fragment shader code.
     * @throws UncheckedIOException If the given shader code contains a {@code #define SHADER_TYPE} preprocessor and an IO error occurs while loading the base shader code.
     */
    public String extractFragmentCode(String shaderCode) {
        // Remove the vertex function
        shaderCode = shaderCode.replaceFirst("(?m)^(\\s*)void\\s+vertex_shader\\s*\\(\\s*\\)\\s*\\{[\\s\\S]*?^\\1}\\s*$", "");
        // Check if this shader file uses one of the built-in shaders
        var shaderTypeMatcher = SHADER_TYPE_REGEX.matcher(shaderCode);
        if(shaderTypeMatcher.find() && Files.exists(this.coreArtifactJarFile)) {
            // Look for the base shader file in the core artifact if it exists
            var baseShaderCode = this.getBaseShaderFile(shaderTypeMatcher.group(1) + ".frag");
            // Add the fragment shader function before the main function
            shaderCode = baseShaderCode.replaceFirst("void main\\(\\)\\{", shaderCode + "\nvoid main(){");
            // Call the fragment shader function at the end of the main function
            shaderCode = shaderCode.replaceFirst("(void main\\(\\)\\{[\\s\\S]*?)}", "$1\nfragment_shader();\n}");
        } else {
            // Remove layout qualifier from in variables
            shaderCode = shaderCode.replaceAll("(?m)^layout\\s*\\(\\s*location\\s*=\\s*\\d+\\s*\\)\\s*", "");
            // Replace the fragment function with the main function
            shaderCode = shaderCode.replaceFirst("(?m)^void\\s*fragment_shader\\s*\\(\\s*\\)\\s*\\{", "void main(){");
            // Replace varying variables
            shaderCode = shaderCode.replaceAll("\\bvarying\\b", "in");
        }
        // Remove comments and white spaces
        shaderCode = trimCode(shaderCode);
        return shaderCode;
    }

    // TODO: Add an option to remove unused functions and uniforms

    /**
     * Removes all blank lines, trailing spaces, and comments from the given shader code.
     *
     * @param shaderCode The shader code.
     * @return The given code without blank lines, trailing spaces, and comments.
     */
    private static String trimCode(String shaderCode) {
        return shaderCode.replaceAll("(?m)[ \\t]*((//.*)|(/\\*[\\s\\S]*?\\*/))", "").replaceAll("(?m)^\\s+\\r?", "").trim();
    }
}