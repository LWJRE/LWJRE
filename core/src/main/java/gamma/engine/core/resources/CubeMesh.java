package gamma.engine.core.resources;

import vecmatlib.vector.Vec3f;

public class CubeMesh extends Mesh {

	public CubeMesh(float size) {
		this(size, size, size);
	}

	public CubeMesh(Vec3f size) {
		this(size.x(), size.y(), size.z());
	}

	public CubeMesh(float x, float y, float z) {
		this.setVertices3D(new float[] {
				x * -0.5f, y * 0.5f, z * -0.5f,
				x * -0.5f, y * -0.5f, z * -0.5f,
				x * 0.5f, y * -0.5f, z * -0.5f,
				x * 0.5f, y * 0.5f, z * -0.5f,

				x * -0.5f, y * 0.5f, z * 0.5f,
				x * -0.5f, y * -0.5f, z * 0.5f,
				x * 0.5f, y * -0.5f, z * 0.5f,
				x * 0.5f, y * 0.5f, z * 0.5f,

				x * 0.5f, y * 0.5f, z * -0.5f,
				x * 0.5f, y * -0.5f, z * -0.5f,
				x * 0.5f, y * -0.5f, z * 0.5f,
				x * 0.5f, y * 0.5f, z * 0.5f,

				x * -0.5f, y * 0.5f, z * -0.5f,
				x * -0.5f, y * -0.5f, z * -0.5f,
				x * -0.5f, y * -0.5f, z * 0.5f,
				x * -0.5f, y * 0.5f, z * 0.5f,

				x * -0.5f, y * 0.5f, z * 0.5f,
				x * -0.5f, y * 0.5f, z * -0.5f,
				x * 0.5f, y * 0.5f, z * -0.5f,
				x * 0.5f, y * 0.5f, z * 0.5f,

				x * -0.5f, y * -0.5f, z * 0.5f,
				x * -0.5f, y * -0.5f, z * -0.5f,
				x * 0.5f, y * -0.5f, z * -0.5f,
				x * 0.5f, y * -0.5f, z * 0.5f
		});
		this.setIndices(new int[] {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22
		});
		this.setTextures(new float[] {
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0
		});
		this.setNormals(new float[] {
				0.0f,0.0f,-1.0f,
				0.0f,0.0f,-1.0f,
				0.0f,0.0f,-1.0f,
				0.0f,0.0f,-1.0f,

				0.0f,0.0f,1.0f,
				0.0f,0.0f,1.0f,
				0.0f,0.0f,1.0f,
				0.0f,0.0f,1.0f,

				1.0f,0.0f,0.0f,
				1.0f,0.0f,0.0f,
				1.0f,0.0f,0.0f,
				1.0f,0.0f,0.0f,

				-1.0f,0.0f,0.0f,
				-1.0f,0.0f,0.0f,
				-1.0f,0.0f,0.0f,
				-1.0f,0.0f,0.0f,

				0.0f,1.0f,0.0f,
				0.0f,1.0f,0.0f,
				0.0f,1.0f,0.0f,
				0.0f,1.0f,0.0f,

				0.0f,-1.0f,0.0f,
				0.0f,-1.0f,0.0f,
				0.0f,-1.0f,0.0f,
				0.0f,-1.0f,0.0f
		});
	}
}
