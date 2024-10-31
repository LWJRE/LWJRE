package io.github.ardentengine.core.rendering;

/**
 * Resource that represents a user-defined shader.
 * Contains the code that the rendering api will use for rendering.
 * <p>
 *     Can be used in a {@link ShaderMaterial} to customize how visual instances are rendered.
 * </p>
 * <p>
 *     The shader code is processed both at build time and when the shader file is loaded,
 *     therefore the code may not correspond to the code written by the user in the {@code .glsl} file.
 * </p>
 */
@SuppressWarnings("ClassCanBeRecord")
public final class Shader {

    /**
     * Code for the vertex shader.
     * Must be a valid glsl program.
     */
    private final String vertexCode;
    /**
     * Code for the fragment shader.
     * Must be a valid glsl program.
     */
    private final String fragmentCode;

    /**
     * Creates a shader resource.
     *
     * @param vertexCode Code for the vertex shader. Must be a valid glsl program.
     * @param fragmentCode Code for the fragment shader. Must be a valid glsl program.
     */
    public Shader(String vertexCode, String fragmentCode) {
        this.vertexCode = vertexCode;
        this.fragmentCode = fragmentCode;
    }

    /**
     * Returns the code for the vertex shader.
     *
     * @return The code for the vertex shader.
     */
    public String vertexCode() {
        return this.vertexCode;
    }

    /**
     * Returns the code for the fragment shader.
     *
     * @return The code for the fragment shader.
     */
    public String fragmentCode() {
        return this.fragmentCode;
    }
}