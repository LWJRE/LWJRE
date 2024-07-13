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

    public void set(String variable, float value) {

    }

    public void set(String variable, float x, float y) {

    }

    public void set(String variable, float x, float y, float z) {

    }

    public void set(String variable, float x, float y, float z, float w) {

    }

    public void set(String variable, int value) {

    }

    public void set(String variable, boolean value) {

    }

    public void set(String variable, int x, int y) {

    }

    public void set(String variable, int x, int y, int z) {

    }

    public void set(String variable, int x, int y, int z, int w) {

    }

    public void set(String variable, Mat2f matrix) {

    }

    public void set(String variable, Mat2x3f matrix) {

    }

    public void set(String variable, Mat3f matrix) {

    }

    public void set(String variable, Mat3x4f matrix) {

    }

    public void set(String variable, Mat4f matrix) {

    }

    public void set(String variable, Texture texture) {

    }

    // TODO: Add colors

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
