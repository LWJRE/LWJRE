package gamma.engine.components;

import gamma.engine.annotations.EditorIndex;
import gamma.engine.annotations.EditorResource;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.RenderingSystem;
import gamma.engine.resources.Material;
import gamma.engine.resources.Mesh;
import gamma.engine.resources.Model;

@EditorIndex(1)
public class ModelRenderer extends RendererComponent {

	@EditorVariable(name = "Model")
	@EditorResource(type = Model.class)
	private String modelPath = ""; // TODO: Does not work if final

	private transient Model model = new Model();

	@Override
	protected void onStart() {
		super.onStart();
		this.setModel(this.modelPath);
	}

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
		this.setModel(this.modelPath);
	}

	private void drawMesh(Mesh mesh, Material material) {
		this.shader().start();
		this.getComponent(Transform3D.class)
				.map(Transform3D::globalTransformation)
				.ifPresent(matrix -> this.shader().setUniform("transformation_matrix", matrix));
		this.shader().setUniform("material.ambient", material.ambient);
		this.shader().setUniform("material.diffuse", material.diffuse);
		this.shader().setUniform("material.specular", material.specular);
		this.shader().setUniform("material.shininess", material.shininess);
		mesh.drawElements();
	}

	@Override
	protected void onExit() {
		super.onExit();
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
	}

	public void setModel(Model model) {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
		this.model = model != null ? model : new Model();
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
	}

	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	public Model getModel() {
		return this.model;
	}
}
