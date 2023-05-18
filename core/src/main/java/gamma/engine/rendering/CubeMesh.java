package gamma.engine.rendering;

public final class CubeMesh {

	public static final Mesh INSTANCE = new Mesh();

	static {
		INSTANCE.setVertices3D(new float[] {
				-1.0f, -1.0f,  1.0f,
				1.0f, -1.0f,  1.0f,
				1.0f,  1.0f,  1.0f,
				-1.0f,  1.0f,  1.0f,
				-1.0f, -1.0f, -1.0f,
				1.0f, -1.0f, -1.0f,
				1.0f,  1.0f, -1.0f,
				-1.0f,  1.0f, -1.0f
		});
		INSTANCE.setIndices(new int[] {
				0, 1, 2,
				2, 3, 0,
				1, 5, 6,
				6, 2, 1,
				7, 6, 5,
				5, 4, 7,
				4, 0, 3,
				3, 7, 4,
				4, 5, 1,
				1, 0, 4,
				3, 2, 6,
				6, 7, 3
		});
	}
}
