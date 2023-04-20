package gamma.engine.components;

import gamma.engine.annotations.EditorResource;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Shader;
import gamma.engine.scene.Component;

public class RendererComponent extends Component {

	@EditorVariable(name = "Shader")
	@EditorResource(defaultValue = "gamma/engine/shaders/default_shader.glsl")
	private Shader shader = Shader.defaultShader();

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
