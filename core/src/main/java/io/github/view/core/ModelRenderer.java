package io.github.view.core;

import io.github.view.RenderingSystem3D;
import io.github.view.resources.Mesh;
import io.github.view.resources.ModelLoader;
import io.github.view.resources.Shader;

import java.util.List;

public class ModelRenderer extends Renderer3D {

	private Transform3D transform;
	private Mesh model;
	private Shader shader;

	public ModelRenderer(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		this.transform = this.object.getScript(Transform3D.class);
		super.onStart();
	}

	@Override
	public void prepareRendering() {
		this.model = ModelLoader.getOrLoadObj("/models/bunny.obj");
		this.shader = Shader.main().create();
		RenderingSystem3D.addToBatch(this);
		super.prepareRendering();
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
		return this.model;
	}

	@Override
	public void exitRendering() {
		RenderingSystem3D.removeFromBatch(this);
		super.exitRendering();
	}
}
