package gamma.engine.graphics.components;

import gamma.engine.core.scene.Component;
import gamma.engine.graphics.resources.Mesh;

/**
 * Component that can render a single mesh.
 *
 * @author Nico
 */
public class MeshRenderer extends Component {

	// TODO: Extend the mesh class to CircleMesh, CubeMesh and so on...
	public Mesh mesh;

	@Override
	protected void onStart() {
		this.mesh = new Mesh();
		this.mesh.setVertices3D(new float[] {
				-0.5f, 0.5f, 0f,
				-0.5f, -0.5f, 0f,
				0.5f, -0.5f, 0f,
				0.5f, 0.5f, 0f
		});
		this.mesh.setIndices(new int[] {
				0, 1, 3,
				3, 1, 2
		});
	}

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		// TODO: Render stuff in batches
		this.mesh.drawElements(6);
	}
}
