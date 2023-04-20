package gamma.engine.components;

import gamma.engine.annotations.EditorResource;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Material;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.Model;
import gamma.engine.rendering.RenderingSystem;

public class ModelRenderer extends RendererComponent {

	@EditorVariable(name = "Model")
	@EditorResource
	private Model model = new Model();

	@Override
	protected void onStart() {
		super.onStart();
		this.addModelToBatch();
	}

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
		this.addModelToBatch();
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
		this.removeModelFromBatch();
	}

	public void setModel(Model model) {
		this.removeModelFromBatch();
		this.model = model != null ? model : new Model();
		this.addModelToBatch();
	}

	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	public Model getModel() {
		return this.model;
	}

	private void addModelToBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
	}

	private void removeModelFromBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
	}
}
