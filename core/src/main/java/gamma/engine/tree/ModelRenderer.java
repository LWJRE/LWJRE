package gamma.engine.tree;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Material;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.Model;
import gamma.engine.rendering.RenderingSystem;

/**
 * Node that represents an object rendered as a 3D {@link Model}.
 *
 * @author Nico
 */
public class ModelRenderer extends Renderer3D {

	/**
	 * The model used by this renderer.
	 */
	@EditorVariable(name = "Model")
	private Model model = new Model();

	@Override
	protected void onEnter() {
		super.onEnter();
		this.addModelToBatch();
	}

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		this.addModelToBatch();
	}

	@Override
	protected void onExit() {
		super.onExit();
		this.removeModelFromBatch();
	}

	/**
	 * Adds this object's model to the {@link RenderingSystem}.
	 */
	private void addModelToBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(mesh, this));
	}

	/**
	 * Removes this object's model from the {@link RenderingSystem}.
	 */
	private void removeModelFromBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(mesh, this));
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

	/**
	 * Sets this object's model.
	 * If the given model is null, then an empty {@link Model#Model()} will be used.
	 *
	 * @param model The model to set to this object
	 */
	public void setModel(Model model) {
		this.removeModelFromBatch();
		this.model = model != null ? model : new Model();
		this.addModelToBatch();
	}

	/**
	 * Sets this object's model to the one at the given path.
	 *
	 * @see Model#getOrLoad(String)
	 *
	 * @param path Path to the model to use in the classpath
	 */
	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	/**
	 * Gets the model used by this object.
	 *
	 * @return The model used by this object
	 */
	public Model model() {
		return this.model;
	}
}
