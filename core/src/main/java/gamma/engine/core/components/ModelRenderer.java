package gamma.engine.core.components;

import gamma.engine.core.annotations.DefaultValueString;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.rendering.RenderingSystem;
import gamma.engine.core.resources.Material;
import gamma.engine.core.resources.Mesh;
import gamma.engine.core.resources.Model;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.scene.Component;

/**
 * A {@code ModelRenderer} component renders a {@link Model}.
 * The entity optionally needs a {@link Transform3D} component to render the model at the correct position.
 * If the entity has no transform, the model will be rendered at the origin.
 *
 * @author Nico
 */
public class ModelRenderer extends Component {

	/** The model to render */
	@EditorVariable(value = "Model", setter = "setModel")
	private Model model;
	/** The shader used by this renderer */
	@EditorVariable("Shader")
	@DefaultValueString("/gamma/engine/core/shaders/default_shader.glsl")
	private Shader shader;

	@Override
	protected void onStart() {
		super.onStart();
		if(this.model != null)
			this.model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
	}

	/**
	 * Renders one of the meshes that make up this model.
	 * Used as a runnable in {@link RenderingSystem#addToBatch(Component, Mesh, Runnable)}.
	 *
	 * @param mesh The mesh to render
	 * @param material The mesh's material
	 */
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

	/**
	 * Sets the model used by this renderer.
	 * If the given model is {@code null}, the renderer will be "empty" and won't render any model.
	 *
	 * @param model The new model to use
	 */
	public void setModel(Model model) {
		if(this.model != null)
			this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
		if(model != null)
			model.modelData().forEach((mesh, material) -> RenderingSystem.addToBatch(this, mesh, () -> this.drawMesh(mesh, material)));
		this.model = model;
	}

	/**
	 * Sets the model used by this renderer to the one at the given path.
	 *
	 * @param path Path to a model file in the classpath
	 */
	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	/**
	 * Gets the model used by this renderer.
	 *
	 * @return The model used by this renderer or {@code null} if this renderer is "empty".
	 */
	public Model getModel() {
		return this.model;
	}

	/**
	 * Sets the shader used by this renderer.
	 * If the given shader is {@code null}, the default shader will be used instead.
	 *
	 * @param shader The new shader to use
	 */
	public void setShader(Shader shader) {
		this.shader = shader != null ? shader : Shader.getOrLoad("/gamma/engine/core/shaders/default_shader.glsl");
	}

	/**
	 * Sets the shader used by this renderer to the one at the given path.
	 *
	 * @param path Path to a shader file in the classpath
	 */
	public void setShader(String path) {
		this.shader = Shader.getOrLoad(path);
	}

	/**
	 * Gets the shader used by this renderer.
	 *
	 * @return The shader used by this renderer
	 */
	public Shader getShader() {
		return this.shader;
	}
}
