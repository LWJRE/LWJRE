package io.github.view.core;

import io.github.view.graphics.RenderingSystem3D;
import io.github.view.math.Color;
import io.github.view.math.Vector3;
import io.github.view.resources.Mesh;
import io.github.view.resources.Model;
import io.github.view.resources.ModelLoader;
import io.github.view.resources.Shader;
import io.github.view.scene.SceneObject;

import java.util.List;

public class ModelRenderer extends Renderer3D {

	// TODO: Editor variable
	private String model;

	private Shader shader;

	public ModelRenderer(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		Model modelInstance = Model.getOrLoad(model);
		this.shader = Shader.main().createOrLoad();
		modelInstance.meshes.forEach(mesh -> RenderingSystem3D.addToBatch(mesh, () -> {
			this.shader.start();
			this.shader.loadUniform("transformation_matrix", this.transform.matrix());
			this.shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
			this.shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
			this.shader.loadUniform("light.color", Color.WHITE);
			this.shader.loadUniform("light.position", new Vector3(2, -1, 0));
		}));
		super.onStart();
	}

	@Override
	public void render(Mesh.DrawableMesh mesh, List<Light> lights) {
		this.shader.start();
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
		return null;
	}

	@Override
	public void onExit() {
		RenderingSystem3D.removeFromBatch(this);
		super.onExit();
	}
}
