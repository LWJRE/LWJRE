package gamma.engine.components;

import gamma.engine.annotations.EditorIndex;
import gamma.engine.rendering.RenderingSystem;
import gamma.engine.resources.Material;
import gamma.engine.resources.Mesh;
import gamma.engine.resources.Shader;
import gamma.engine.scene.Component;
import vecmatlib.color.Color4f;

import java.util.Objects;

/**
 * Component that can render a single mesh.
 *
 * @author Nico
 */
@EditorIndex(1)
public class MeshRenderer extends Component {

	// TODO: Find a way to expose this for the editor
	public Mesh mesh;
	public Material material = new Material(Color4f.White(), Color4f.White(), Color4f.White(), 0.0f);

	private Shader shader = Shader.getOrLoad("gamma/engine/shaders/default_shader.glsl");

	@Override
	protected void onStart() {
		super.onStart();
		RenderingSystem.addToBatch(this, this.mesh, () -> {
			this.getComponent(Transform3D.class)
					.map(Transform3D::globalTransformation)
					.ifPresent(matrix -> this.shader.setUniform("transformation_matrix", matrix));
			this.shader.setUniform("material.ambient", material.ambient);
			this.shader.setUniform("material.diffuse", material.diffuse);
			this.shader.setUniform("material.specular", material.specular);
			this.shader.setUniform("material.shininess", material.shininess);
			this.shader.start();
			this.mesh.drawElements();
		});
	}

	@Override
	protected void onExit() {
		super.onExit();
		RenderingSystem.removeFromBatch(this, this.mesh);
	}

	public void setShader(Shader shader) {
		this.shader = Objects.requireNonNull(shader);
	}

	public void setShader(String path) {
		this.shader = Shader.getOrLoad(path);
	}

	public Shader getShader() {
		return this.shader;
	}
}
