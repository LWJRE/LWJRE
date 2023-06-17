package io.github.lwjre.engine.nodes;

import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.lwjre.engine.resources.Mesh;
import io.github.lwjre.engine.servers.RenderingServer;

public class CubeRenderer extends Renderer3D {

	public static final Mesh MESH = new Mesh.Builder()
			.vertices3D(new float[] {
					-0.5f, -0.5f, 0.5f,
					0.5f, -0.5f, 0.5f,
					0.5f, 0.5f, 0.5f,
					-0.5f, 0.5f, 0.5f,
					-0.5f, -0.5f, -0.5f,
					0.5f, -0.5f, -0.5f,
					0.5f, 0.5f, -0.5f,
					-0.5f, 0.5f, -0.5f,
					-0.5f, -0.5f, -0.5f,
					-0.5f, -0.5f, 0.5f,
					-0.5f, 0.5f, 0.5f,
					-0.5f, 0.5f, -0.5f,
					0.5f, -0.5f, -0.5f,
					0.5f, -0.5f, 0.5f,
					0.5f, 0.5f, 0.5f,
					0.5f, 0.5f, -0.5f,
					-0.5f, 0.5f, 0.5f,
					0.5f, 0.5f, 0.5f,
					0.5f, 0.5f, -0.5f,
					-0.5f, 0.5f, -0.5f,
					-0.5f, -0.5f, 0.5f,
					0.5f, -0.5f, 0.5f,
					0.5f, -0.5f, -0.5f,
					-0.5f, -0.5f, -0.5f
			}).indices(new int[] {
					0, 1, 2,
					2, 3, 0,
					4, 5, 6,
					6, 7, 4,
					8, 9, 10,
					10, 11, 8,
					12, 13, 14,
					14, 15, 12,
					16, 17, 18,
					18, 19, 16,
					20, 21, 22,
					22, 23, 20
			}).uvs(new float[] {
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
			}).normals(new float[] {
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, 1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					0.0f, 0.0f, -1.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					-1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					1.0f, 0.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, 1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f,
					0.0f, -1.0f, 0.0f
			}).create();

	@Override
	protected void onUpdate(float delta) {
		RenderingServer.addToBatch(MESH, this::render);
		super.onUpdate(delta);
	}

	@Override
	protected void onEditorProcess() {
		RenderingServer.addToBatch(MESH, this::render);
		super.onEditorProcess();
	}

	private void render() {
		this.shader().start();
		this.shader().setUniform("transformation_matrix", this.globalTransformation());
		this.shader().setUniform("material.ambient", Color4f.White());
		this.shader().setUniform("material.diffuse", Color4f.White());
		this.shader().setUniform("material.specular", Color4f.White());
		this.shader().setUniform("material.shininess", 16.0f);
		MESH.draw();
	}
}
