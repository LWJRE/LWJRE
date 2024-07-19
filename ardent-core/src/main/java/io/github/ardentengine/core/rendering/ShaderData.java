package io.github.ardentengine.core.rendering;

import io.github.scalamath.vecmatlib.*;

/**
 * Class used internally by shaders to abstract the representation of a shader across different rendering APIs.
 * <p>
 *     All the methods in this class are dummy methods.
 *     The rendering API module should extend this class to provide its implementation.
 * </p>
 *
 * @see Shader
 */
public class ShaderData {

    /**
     * Compiles this shader.
     *
     * @param vertexCode Code for the vertex shader.
     * @param fragmentCode Code for the fragment shader.
     */
    public void compile(CharSequence vertexCode, CharSequence fragmentCode) {

    }

    /**
     * Sets a float uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param value Value to assign to the variable.
     */
    public void set(String variable, float value) {

    }

    /**
     * Sets a {@code vec2} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     */
    public void set(String variable, float x, float y) {

    }

    /**
     * Sets a {@code vec3} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     */
    public void set(String variable, float x, float y, float z) {

    }

    /**
     * Sets a {@code vec4} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     * @param w W component of the variable.
     */
    public void set(String variable, float x, float y, float z, float w) {

    }

    /**
     * Sets an int uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param value Value to assign to the variable.
     */
    public void set(String variable, int value) {

    }

    /**
     * Sets a bool uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param value Value to assign to the variable.
     */
    public void set(String variable, boolean value) {

    }

    /**
     * Sets an {@code ivec2} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     */
    public void set(String variable, int x, int y) {

    }

    /**
     * Sets an {@code ivec3} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     */
    public void set(String variable, int x, int y, int z) {

    }

    /**
     * Sets an {@code ivec4} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     * @param w W component of the variable.
     */
    public void set(String variable, int x, int y, int z, int w) {

    }

    /**
     * Sets a {@code mat2} uniform variable in the shader.
     * If the given matrix is null, it will default to {@link Mat2f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat2f matrix) {

    }

    /**
     * Sets a {@code mat3x2} uniform variable in the shader.
     * If the given matrix is null, it will default to {@link Mat2x3f#Zero()}.
     * Note that glsl uses column-major order, therefore a 3x2 matrix is a matrix with 2 rows and 3 columns and corresponds to a {@link Mat2x3f} object.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat2x3f matrix) {

    }

    /**
     * Sets a {@code mat3} uniform variable in the shader.
     * If the given matrix is null, it will default to {@link Mat3f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat3f matrix) {

    }

    /**
     * Sets a {@code mat4x3} uniform variable in the shader.
     * If the given matrix is null, it will default to {@link Mat3x4f#Zero()}.
     * Note that glsl uses column-major order, therefore a 4x3 matrix is a matrix with 3 rows and 4 columns and corresponds to a {@link Mat3x4f} object.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat3x4f matrix) {

    }

    /**
     * Sets a {@code mat4} uniform variable in the shader.
     * If the given matrix is null, it will default to {@link Mat4f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat4f matrix) {

    }

    /**
     * Sets a {@code sampler2D} uniform variable in the shader.
     * The given texture can be null to tell the shader to use no texture for this variable.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param texture Texture to use for the {@code sampler2D}.
     */
    public void set(String variable, Texture texture) {

    }

    /**
     * Uses this shader to draw the given mesh.
     * <p>
     *     The default implementation of this method only calls {@link Mesh#draw()}.
     *     The rendering API must provide its way to use the shader.
     * </p>
     *
     * @param mesh The mesh to draw.
     */
    public void draw(Mesh mesh) {
        mesh.draw();
    }
}