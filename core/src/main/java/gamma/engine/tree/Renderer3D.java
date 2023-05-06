package gamma.engine.tree;

import gamma.engine.annotations.DefaultResource;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.Shader;

public abstract class Renderer3D extends Node3D {

	@EditorVariable(name = "Shader")
	@DefaultResource(path = "gamma/engine/shaders/default_shader.glsl")
	private Shader shader = Shader.defaultShader();

	public abstract void render(Mesh mesh);

	public final Shader shader() {
		return this.shader;
	}

	public final void setShader(Shader shader) {
		this.shader = shader != null ? shader : Shader.defaultShader();
	}

	public final void setShader(String path) {
		this.shader = Shader.getOrLoad(path);
	}
}
