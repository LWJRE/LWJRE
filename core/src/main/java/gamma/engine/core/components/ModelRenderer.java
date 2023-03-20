package gamma.engine.core.components;

import gamma.engine.core.annotations.DefaultValueString;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.rendering.RenderingSystem;
import gamma.engine.core.resources.Material;
import gamma.engine.core.resources.Mesh;
import gamma.engine.core.resources.Model;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.scene.Component;

import java.util.Objects;

public class ModelRenderer extends Component {

	@EditorVariable(value = "Model", setter = "setModel")
	private Model model;
	@EditorVariable("Shader")
	@DefaultValueString("/gamma/engine/core/shaders/default_shader.glsl")
	private Shader shader;

	@Override
	protected void onStart() {
		super.onStart();
		if(this.model != null)
			this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
	}

	private void drawMesh(Mesh mesh, Material material) {
		this.shader.start();
		this.getComponent(Transform3D.class)
				.map(Transform3D::globalTransformation)
				.ifPresent(matrix -> this.shader.setUniform("transformation_matrix", matrix));
		this.shader.setUniform("projection_matrix", Camera3D.getCurrent().projectionMatrix());
		this.shader.setUniform("view_matrix", Camera3D.getCurrent().viewMatrix());
		this.shader.setUniform("material.ambient", material.ambient);
		this.shader.setUniform("material.diffuse", material.diffuse);
		this.shader.setUniform("material.specular", material.specular);
		this.shader.setUniform("material.shininess", material.shininess);
		mesh.drawElements();
	}

	@Override
	protected void onExit() {
		super.onExit();
		if(this.model != null)
			this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
	}

	public void setModel(Model model) {
		if(this.model != null)
			this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
		if(model != null)
			model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
		this.model = model;
	}

	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	public Model getModel() {
		return this.model;
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
