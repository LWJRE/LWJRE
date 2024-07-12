package io.github.hexagonnico.core.rendering;

import io.github.hexagonnico.core.resources.ResourceManager;
import io.github.scalamath.vecmatlib.*;

import java.util.Objects;

/**
 * A shader used for rendering.
 * <p>
 *     Objects that can be rendered use a built-in shader.
 *     User-defined shaders can be loaded from {@code .glsl} files.
 * </p>
 */
public final class Shader {

    public static Shader getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof Shader) {
            return (Shader) resource;
        }
        System.err.println("Resource " + resourcePath + " is not a shader");
        return null;
    }

    /** Shader data used internally to abstract shaders across different rendering APIs. */
    private final ShaderData shaderData = RenderingServer.createShader();

    /**
     * Compiles this shader into a runnable shader program.
     * <p>
     *     The provided code must be a valid glsl program.
     *     The shader code is processed at build time.
     * </p>
     *
     * @param vertexCode Code for the vertex shader.
     * @param fragmentCode Code for the fragment shader.
     */
    public void compile(CharSequence vertexCode, CharSequence fragmentCode) {
        this.shaderData.compile(vertexCode, fragmentCode);
    }

    /**
     * Sets a float uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param value Value to assign to the variable.
     */
    public void set(String variable, float value) {
        this.shaderData.set(variable, value);
    }

    /**
     * Sets a {@code vec2} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     */
    public void set(String variable, float x, float y) {
        this.shaderData.set(variable, x, y);
    }

    /**
     * Sets a {@code vec2} uniform variable in the shader.
     * If the given vector is {@code null}, it will default to {@link Vec2f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param vector Value to assign to the variable.
     */
    public void set(String variable, Vec2f vector) {
        vector = Objects.requireNonNullElse(vector, Vec2f.Zero());
        this.set(variable, vector.x(), vector.y());
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
        this.shaderData.set(variable, x, y, z);
    }

    /**
     * Sets a {@code vec3} uniform variable in the shader.
     * If the given vector is {@code null}, it will default to {@link Vec3f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param vector Value to assign to the variable.
     */
    public void set(String variable, Vec3f vector) {
        vector = Objects.requireNonNullElse(vector, Vec3f.Zero());
        this.set(variable, vector.x(), vector.y(), vector.z());
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
        this.shaderData.set(variable, x, y, z, w);
    }

    /**
     * Sets a {@code vec4} uniform variable in the shader.
     * If the given vector is {@code null}, it will default to {@link Vec4f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param vector Value to assign to the variable.
     */
    public void set(String variable, Vec4f vector) {
        vector = Objects.requireNonNullElse(vector, Vec4f.Zero());
        this.set(variable, vector.x(), vector.y(), vector.z(), vector.w());
    }

    /**
     * Sets an int uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param value Value to assign to the variable.
     */
    public void set(String variable, int value) {
        this.shaderData.set(variable, value);
    }

    /**
     * Sets a bool uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param value Value to assign to the variable.
     */
    public void set(String variable, boolean value) {
        this.shaderData.set(variable, value);
    }

    /**
     * Sets an {@code ivec2} uniform variable in the shader.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     */
    public void set(String variable, int x, int y) {
        this.shaderData.set(variable, x, y);
    }

    /**
     * Sets an {@code ivec2} uniform variable in the shader.
     * If the given vector is {@code null}, it will default to {@link Vec2i#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param vector Value to assign to the variable.
     */
    public void set(String variable, Vec2i vector) {
        vector = Objects.requireNonNullElse(vector, Vec2i.Zero());
        this.set(variable, vector.x(), vector.y());
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
        this.shaderData.set(variable, x, y, z);
    }

    /**
     * Sets an {@code ivec3} uniform variable in the shader.
     * If the given vector is {@code null}, it will default to {@link Vec3i#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param vector Value to assign to the variable.
     */
    public void set(String variable, Vec3i vector) {
        vector = Objects.requireNonNullElse(vector, Vec3i.Zero());
        this.set(variable, vector.x(), vector.y(), vector.z());
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
        this.shaderData.set(variable, x, y, z, w);
    }

    /**
     * Sets an {@code ivec4} uniform variable in the shader.
     * If the given vector is {@code null}, it will default to {@link Vec4i#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param vector Value to assign to the variable.
     */
    public void set(String variable, Vec4i vector) {
        vector = Objects.requireNonNullElse(vector, Vec4i.Zero());
        this.set(variable, vector.x(), vector.y(), vector.z(), vector.w());
    }

    /**
     * Sets a {@code mat2} uniform variable in the shader.
     * If the given matrix is {@code null}, it will default to {@link Mat2f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat2f matrix) {
        this.shaderData.set(variable, Objects.requireNonNullElse(matrix, Mat2f.Zero()));
    }

    /**
     * Sets a {@code mat3x2} uniform variable in the shader.
     * If the given matrix is {@code null}, it will default to {@link Mat2x3f#Zero()}.
     * Note that glsl uses column-major order, therefore a 3x2 matrix is a matrix with 2 rows and 3 columns and corresponds to a {@link Mat2x3f} object.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat2x3f matrix) {
        this.shaderData.set(variable, Objects.requireNonNullElse(matrix, Mat2x3f.Zero()));
    }

    /**
     * Sets a {@code mat3} uniform variable in the shader.
     * If the given matrix is {@code null}, it will default to {@link Mat3f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat3f matrix) {
        this.shaderData.set(variable, Objects.requireNonNullElse(matrix, Mat3f.Zero()));
    }

    /**
     * Sets a {@code mat4x3} uniform variable in the shader.
     * If the given matrix is {@code null}, it will default to {@link Mat3x4f#Zero()}.
     * Note that glsl uses column-major order, therefore a 4x3 matrix is a matrix with 3 rows and 4 columns and corresponds to a {@link Mat3x4f} object.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat3x4f matrix) {
        this.shaderData.set(variable, Objects.requireNonNullElse(matrix, Mat3x4f.Zero()));
    }

    /**
     * Sets a {@code mat4} uniform variable in the shader.
     * If the given matrix is {@code null}, it will default to {@link Mat4f#Zero()}.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param matrix Value to assign to the variable.
     */
    public void set(String variable, Mat4f matrix) {
        this.shaderData.set(variable, Objects.requireNonNullElse(matrix, Mat4f.Zero()));
    }

    /**
     * Sets a {@code sampler2D} uniform variable in the shader.
     * The given texture can be {@code null} to tell the shader to use no texture for this variable.
     *
     * @param variable Name of the uniform variable as it is declared in the shader.
     * @param texture Texture to use for the {@code sampler2D}.
     */
    public void set(String variable, Texture texture) {
        this.shaderData.set(variable, texture);
    }

    // TODO: Add colors

    /**
     * Uses this shader to draw the given mesh.
     *
     * @param mesh The mesh to draw.
     */
    public void draw(Mesh mesh) {
        this.shaderData.draw(mesh);
    }
}
