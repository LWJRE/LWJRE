package io.github.view.core;

import io.github.view.graphics.RenderingSystem3D;
import io.github.view.resources.Model;
import io.github.view.resources.Shader;

public class ModelRenderer extends Transform3D {

	// TODO: Editor variable
	private String model;

	private Shader shader;

	@Override
	protected void onStart() {
		this.shader = Shader.main().createOrLoad();
		Model.getOrLoad(this.model).forEach((material, mesh) -> RenderingSystem3D.addToBatch(mesh, this, () -> {
			this.shader.start();
			this.shader.loadUniform("transformation_matrix", this.globalTransformation());
			this.shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
			this.shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
			if(material != null) {
				this.shader.loadUniform("material.ambient", material.getAmbient());
				this.shader.loadUniform("material.diffuse", material.getDiffuse());
				this.shader.loadUniform("material.specular", material.getSpecular());;
				this.shader.loadUniform("material.shininess", 8.0f);
			}
			this.shader.loadUniform("camera_position", Camera3D.current().globalPosition());
			this.shader.loadUniform("light_position", 0.0f, 20.0f, 0.0f);
			this.shader.loadUniform("light_color", 1.0f, 1.0f, 1.0f);
		}));
		super.onStart();
	}

	@Override
	public void onExit() {
		RenderingSystem3D.removeFromBatch(this);
		super.onExit();
	}
}
