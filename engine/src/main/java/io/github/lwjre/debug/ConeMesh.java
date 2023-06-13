package io.github.lwjre.debug;

import io.github.lwjre.resources.Mesh;

public class ConeMesh {

	public static final Mesh INSTANCE = new Mesh.Builder()
			.vertices3D(new float[] {
					0.5f, -0.5f, 0.5f,
					0.5f, -0.5f, -0.5f,
					-0.5f, -0.5f, 0.5f,
					-0.5f, -0.5f, -0.5f,
					0.0f, 0.5f, 0.0f
			}).indices(new int[] {
					0, 1, 2,
					1, 3, 0,
					0, 1, 4,
					1, 2, 4,
					2, 3, 4,
					3, 0, 4
			}).create();
}
