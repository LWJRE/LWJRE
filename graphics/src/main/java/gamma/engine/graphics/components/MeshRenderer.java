package gamma.engine.graphics.components;

import gamma.engine.core.components.Camera3D;
import gamma.engine.core.components.Transform3D;
import gamma.engine.core.scene.Component;
import gamma.engine.graphics.resources.Mesh;
import gamma.engine.graphics.resources.Shader;

/**
 * Component that can render a single mesh.
 *
 * @author Nico
 */
public class MeshRenderer extends Component {

	// TODO: Extend the mesh class to SphereMesh, CubeMesh and so on...
	public Mesh mesh;
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
		this.shader.start();
		this.mesh.drawElements();
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
		this.mesh.drawElements();
	}
}
