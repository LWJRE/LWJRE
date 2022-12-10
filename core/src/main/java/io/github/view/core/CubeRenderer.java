package io.github.view.core;

import io.github.view.graphics.RenderingSystem3D;
import io.github.view.resources.Mesh;
import io.github.view.resources.Shader;
import io.github.view.scene.SceneObject;

import java.util.List;

public class CubeRenderer extends Renderer3D {

	private Mesh cubeMesh;
	private Shader shader;

	public CubeRenderer(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		this.cubeMesh = Mesh.createMesh3D(new float[] {
				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,0.5f,-0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,

				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,

				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
		}, new int[] {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14
		}); // TODO: Cube normals
		this.shader = Shader.main().createOrLoad();
		RenderingSystem3D.addToBatch(this);
		super.onStart();
	}

	@Override
	public void render(Mesh.DrawableMesh mesh, List<Light> lights) {
		this.shader.start(); // TODO: Common rendering between 3D objects
		this.shader.loadUniform("transformation_matrix", this.transform.matrix());
		this.shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
		this.shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
		lights.forEach(light -> {
			this.shader.loadUniform("light.color", light.color);
			if(light instanceof PointLight3D pointLight) {
				this.shader.loadUniform("light.position", pointLight.getPosition());
			}
		});
		mesh.draw();
	}

	@Override
	public Mesh getMesh() {
		return this.cubeMesh;
	}

	@Override
	public void onExit() {
		RenderingSystem3D.removeFromBatch(this);
		super.onExit();
	}
}
