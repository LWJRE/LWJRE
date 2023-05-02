package gamma.engine.components;

import gamma.engine.annotations.DefaultResource;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.Shader;
import gamma.engine.scene.Component;

public abstract class RendererComponent extends Component {

	/** The shader used by this renderer */
	@EditorVariable(name = "Shader")
	@DefaultResource(path = "gamma/engine/shaders/default_shader.glsl")
	private Shader shader = Shader.defaultShader();

	/**
	 * Gets the shader used by this renderer.
	 *
	 * @return The shader used by this renderer
	 */
	public final Shader shader() {
		return this.shader;
	}

	public abstract void drawMesh(Mesh mesh);

	/**
	 * Sets this renderer's shader.
	 * If the given shader is null, the {@link Shader#defaultShader()} will be used instead.
	 *
	 * @param shader The shader to set
	 */
	public final void setShader(Shader shader) {
		this.shader = shader != null ? shader : Shader.defaultShader();
	}

	/**
	 * Sets this renderer's shader to the one at the given path.
	 *
	 * @see Shader#getOrLoad(String)
	 *
	 * @param path Path of the shader file
	 */
	public final void setShader(String path) {
		this.shader = Shader.getOrLoad(path);
	}
}
