package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.math.*;

/**
 * A material that uses a user-defined {@link Shader} program.
 * <p>
 *     The material's parameters reflect the uniform variables defined in the shader code.
 * </p>
 * <p>
 *     Multiple {@code ShaderMaterial}s can use the same shader and different values for the uniforms.
 * </p>
 */
public final class ShaderMaterial extends Material {

    /**
     * The shader used by this material.
     * Represents a user-defined shader.
     */
    private Shader shader;

    /**
     * Setter method for {@link ShaderMaterial#shader}.
     *
     * @param shader The shader used by this material.
     */
    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Getter method for {@link ShaderMaterial#shader}.
     *
     * @return The shader used by this material.
     */
    public Shader shader() {
        return this.shader;
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, float value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Vector2 value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Vector3 value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Vector4 value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, int value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Vector2i value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Vector3i value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Vector4i value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, boolean value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Color value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Sets a parameter with the given name.
     *
     * @param name Name of the parameter to set.
     * @param value Value of the parameter to set.
     */
    public void setParameter(String name, Texture value) {
        this.parameters.put("u_" + name, value);
    }

    /**
     * Returns a material parameter with the given name.
     *
     * @param name Name of the requested parameter.
     * @return The value of the requested parameter or {@code null} if that parameter does not exist, or it has not been set.
     */
    public Object getParameter(String name) {
        return this.parameters.get("u_" + name);
    }
}