package gamma.engine.graphics.components;

import gamma.engine.core.annotations.DefaultResource;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.graphics.resources.Mesh;
import gamma.engine.graphics.resources.Shader;
import gamma.engine.core.scene.Component;

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
