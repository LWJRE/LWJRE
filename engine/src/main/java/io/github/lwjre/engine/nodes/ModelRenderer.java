package io.github.lwjre.engine.nodes;

import io.github.lwjre.engine.annotations.EditorVariable;
import io.github.lwjre.engine.resources.Model;
import io.github.lwjre.engine.servers.RenderingServer;

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
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		this.addToBatch();
	}

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		this.addToBatch();
	}

	private void addToBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingServer.addToBatch(mesh, () -> {
			this.shader().start();
			this.shader().setUniform("transformation_matrix", this.globalTransformation());
			this.shader().setUniform("material.ambient", material.ambientColor());
			this.shader().setUniform("material.diffuse", material.diffuseColor());
			this.shader().setUniform("material.specular", material.specularColor());
			this.shader().setUniform("material.shininess", material.shininess());
			mesh.draw();
		}));
	}

	/**
	 * Sets this object's model.
	 * If the given model is null, then an empty {@link Model#Model()} will be used.
	 *
	 * @param model The model to set to this object
	 */
	public void setModel(Model model) {
		this.model = model != null ? model : new Model();
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
