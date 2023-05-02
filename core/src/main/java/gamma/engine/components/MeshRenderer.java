package gamma.engine.components;

import gamma.engine.rendering.Material;
import gamma.engine.rendering.Mesh;
import gamma.engine.rendering.RenderingSystem;
import vecmatlib.color.Color4f;

/**
 * Component that can render a single mesh.
 *
 * @author Nico
 */
public class MeshRenderer extends RendererComponent {

	public Mesh mesh;
	public Material material = new Material(Color4f.White(), Color4f.White(), Color4f.White(), 0.0f);

	@Override
	protected void onStart() {
		super.onStart();
		RenderingSystem.addToBatch(this.mesh, this);
	}

	@Override
	public void drawMesh(Mesh mesh) {
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
		RenderingSystem.removeFromBatch(this.mesh, this);
	}
}
