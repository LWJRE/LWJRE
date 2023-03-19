package gamma.engine.core.components;

import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.resources.Model;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.scene.Component;

public class ModelRenderer extends Component {

	@EditorVariable("Model")
	public Model model;
	@EditorVariable("Shader")
	private Shader shader;

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		// TODO: Render stuff in batches
		this.getComponent(Transform3D.class)
				.map(Transform3D::globalTransformation)
				.ifPresent(matrix -> this.shader.setUniform("transformation_matrix", matrix));
		this.shader.setUniform("projection_matrix", Camera3D.getCurrent().projectionMatrix());
		this.shader.setUniform("view_matrix", Camera3D.getCurrent().viewMatrix());
		this.shader.setUniform("John", 3);
		this.shader.start();
		this.model.draw((mesh, material) -> {
			this.shader.setUniform("material.ambient", material.ambient);
			this.shader.setUniform("material.diffuse", material.diffuse);
			this.shader.setUniform("material.specular", material.specular);
			this.shader.setUniform("material.shininess", material.shininess);
			mesh.drawElements();
		});
	}

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
		// TODO: Render stuff in batches
		this.getComponent(Transform3D.class)
				.map(Transform3D::globalTransformation)
				.ifPresent(matrix -> this.shader.setUniform("transformation_matrix", matrix));
		this.shader.setUniform("projection_matrix", Camera3D.getCurrent().projectionMatrix());
		this.shader.setUniform("view_matrix", Camera3D.getCurrent().viewMatrix());
		this.shader.start();
		this.model.draw();
	}
}
