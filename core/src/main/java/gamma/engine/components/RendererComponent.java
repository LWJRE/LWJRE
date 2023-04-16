package gamma.engine.components;

import gamma.engine.annotations.EditorResource;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Shader;
import gamma.engine.scene.Component;

public class RendererComponent extends Component {

	@EditorVariable(name = "Shader")
	@EditorResource(type = Shader.class, defaultValue = "gamma/engine/shaders/default_shader.glsl")
	private String shaderPath = "gamma/engine/shaders/default_shader.glsl"; // TODO: Does not work if final

	private transient Shader shader = Shader.defaultShader();

	@Override
	protected void onStart() {
		super.onStart();
		this.shader = Shader.getOrLoad(this.shaderPath);
	}

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
		this.shader = Shader.getOrLoad(this.shaderPath);
	}

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
