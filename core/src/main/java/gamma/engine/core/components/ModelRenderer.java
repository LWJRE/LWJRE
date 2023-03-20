package gamma.engine.core.components;

import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.rendering.RenderingSystem;
import gamma.engine.core.resources.Material;
import gamma.engine.core.resources.Mesh;
import gamma.engine.core.resources.Model;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.scene.Component;

public class ModelRenderer extends Component {

	@EditorVariable("Model")
	public Model model;
	@EditorVariable("Shader")
	private Shader shader;

	@Override
	protected void onStart() {
		super.onStart();
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
		this.model.modelData().forEach((mesh, material) -> RenderingSystem.removeFromBatch(this, mesh));
	}
}
