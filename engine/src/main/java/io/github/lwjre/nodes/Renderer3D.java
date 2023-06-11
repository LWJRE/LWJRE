package io.github.lwjre.nodes;

import io.github.lwjre.annotations.DefaultResource;
import io.github.lwjre.annotations.EditorVariable;
import io.github.lwjre.nodes.Node3D;
import io.github.lwjre.resources.Mesh;
import io.github.lwjre.resources.Shader;

/**
 * Node used as a base for all 3D objects that can be rendered.
 *
 * @author Nico
 */
public abstract class Renderer3D extends Node3D {

	/**
	 * The shader used by this object.
	 */
	@EditorVariable(name = "Shader")
	@DefaultResource(path = "shaders/default_shader.glsl")
	private Shader shader = Shader.defaultShader();

	public abstract void render(Mesh mesh);

	/**
	 * Gets the shader used by this object.
	 *
	 * @return The shader that this object uses.
	 */
	public final Shader shader() {
		return this.shader;
	}

	/**
	 * Sets this object's shader to the given one.
	 * If the given shader is null, the {@link Shader#defaultShader()} will be used instead.
	 *
	 * @param shader The shader to set to this object
	 */
	public final void setShader(Shader shader) {
		this.shader = shader != null ? shader : Shader.defaultShader();
	}

	/**
	 * Sets this object's shader to the one at the given path.
	 *
	 * @see Shader#getOrLoad(String)
	 *
	 * @param path Path to the shader to use in the classpath
	 */
	public final void setShader(String path) {
		this.shader = Shader.getOrLoad(path);
	}
}
