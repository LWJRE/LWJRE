package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.resources.ResourceManager;

import java.util.Objects;

/**
 * Represents a shader resource.
 * <p>
 *     Contains the processed glsl code that will be used to create a shader.
 *     The rendering API is responsible for creating the shader program used for rendering.
 * </p>
 * <p>
 *     Shaders are loaded from {@code .glsl} files.
 *     {@code .vert} and {@code .frag} files are used internally and contain the processed shader code.
 *     The shader code is processed when the project is built.
 * </p>
 */
public class Shader {

    /**
     * Utility method to get a shader resource using {@link ResourceManager#getOrLoad(String)}.
     * <p>
     *     Loads the shader at the given path or returns the same instance if it was already loaded.
     * </p>
     * <p>
     *     Returns null and logs an error if the resource at the given path is not of class {@code Shader}.
     * </p>
     * @param resourcePath Path at which to load the shader resource. Must point to a {@code .glsl} shader file.
     * @return The requested shader.
     */
    public static Shader getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof Shader) {
            return (Shader) resource;
        } else if(resource != null) {
            Logger.error("Resource " + resourcePath + " is not a shader");
        }
        return null;
    }

    /**
     * Code for the vertex shader.
     * Must be a valid runnable glsl program.
     */
    private CharSequence vertexCode = "";
    /**
     * Code for the fragment shader.
     * Must be a valid runnable glsl program.
     */
    private CharSequence fragmentCode = "";

    // TODO: The shader should be recompiled if the code is modified

    /**
     * Getter method for {@link Shader#vertexCode}.
     *
     * @return The vertex shader code.
     */
    public CharSequence getVertexCode() {
        return this.vertexCode;
    }

    /**
     * Setter method for {@link Shader#vertexCode}.
     * <p>
     *     The given code must be a runnable glsl program.
     *     This is not equivalent to the code in the {@code .glsl} file, since the shader code is processed when the project is built.
     * </p>
     *
     * @param vertexCode The vertex shader code. Must be a valid runnable glsl program.
     */
    public void setVertexCode(CharSequence vertexCode) {
        this.vertexCode = Objects.requireNonNullElse(vertexCode, "");
    }

    /**
     * Getter method for {@link Shader#fragmentCode}.
     *
     * @return The fragment shader code.
     */
    public CharSequence getFragmentCode() {
        return this.fragmentCode;
    }

    /**
     * Setter method for {@link Shader#fragmentCode}.
     * <p>
     *     The given code must be a runnable glsl program.
     *     This is not equivalent to the code in the {@code .glsl} file, since the shader code is processed when the project is built.
     * </p>
     *
     * @param fragmentCode The fragment shader code. Must be a valid runnable glsl program.
     */
    public void setFragmentCode(CharSequence fragmentCode) {
        this.fragmentCode = Objects.requireNonNullElse(fragmentCode, "");
    }
}