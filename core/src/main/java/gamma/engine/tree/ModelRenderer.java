package gamma.engine.tree;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Material;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.Model;
import gamma.engine.rendering.RenderingSystem;

public class ModelRenderer extends Renderer3D {

	@EditorVariable(name = "Model")
	private Model model = new Model();

	@Override
	protected void onEnter() {
		super.onEnter();
		this.addModelToBatch();
	}

	@Override
	public void render(Mesh mesh) {
		this.shader().start();
		Material material = this.model.getMaterial(mesh);
		this.shader().setUniform("transformation_matrix", this.globalTransformation());
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

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		this.addModelToBatch();
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
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(mesh, this::render));
	}

	private void removeModelFromBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(mesh, this::render));
	}
}
