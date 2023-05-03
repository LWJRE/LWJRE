package gamma.engine.components;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Material;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.Model;
import gamma.engine.rendering.RenderingSystem;
import gamma.engine.scene.Component;

/**
 * Component used to render entities that use a {@link Model}.
 *
 * @author Nico
 */
public class ModelRenderer extends RendererComponent {

	/** The model to use */
	@EditorVariable(name = "Model")
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

	/**
	 * Draws the given mesh and material.
	 * This method is added to the render batch as a {@link Runnable} with {@link RenderingSystem#addToBatch(Component, Mesh, Runnable)}.
	 *
	 * @param mesh The mesh to render, part of {@link ModelRenderer#model}
	 * @param material The material to use
	 */
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

	/**
	 * Sets this renderer's model to the given one.
	 * If the given model is null, an empty model will be used instead.
	 *
	 * @param model The model to use
	 */
	public void setModel(Model model) {
		this.removeModelFromBatch();
		this.model = model != null ? model : new Model();
		this.addModelToBatch();
	}

	/**
	 * Sets this renderer's model to the one at the given path.
	 *
	 * @see Model#getOrLoad(String)
	 *
	 * @param path Path to the model to use
	 */
	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	/**
	 * Gets the model used by this renderer.
	 *
	 * @return The model used by this renderer
	 */
	public Model getModel() {
		return this.model;
	}

	/**
	 * Adds the model to the {@link RenderingSystem}.
	 */
	private void addModelToBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
	}

	/**
	 * Removes the model from the {@link RenderingSystem}.
	 */
	private void removeModelFromBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
	}
}
