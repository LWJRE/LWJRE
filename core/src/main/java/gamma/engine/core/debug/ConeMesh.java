package gamma.engine.core.debug;

import gamma.engine.core.resources.Mesh;

public class ConeMesh {

	public static final Mesh INSTANCE = new Mesh();

	static {
		INSTANCE.setVertices3D(new float[] {
				0.5f, -0.5f, 0.5f,
				0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f, 0.5f,
				-0.5f, -0.5f, -0.5f,
				0.0f, 0.5f, 0.0f
		});
		INSTANCE.setIndices(new int[] {
				0, 1, 2,
				1, 3, 0,
				0, 1, 4,
				1, 2, 4,
				2, 3, 4,
				3, 0, 4
		});
	}
}
