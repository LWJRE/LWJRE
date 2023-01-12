package gamma.engine.core.tree;

import gamma.engine.core.graphics.RenderingSystem3D;
import gamma.engine.core.resources.Model;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.resources.ShaderLoader;

public class ModelRenderer extends Transform3D {

	// TODO: Editor variables
	private String model;

	private final Shader shader = ShaderLoader.getOrLoad("/gamma/engine/core/shaders/main_shader.yaml");

	@Override
	protected void onUpdate(float delta) {
		Model.getOrLoad(this.model).forEach((material, mesh) -> RenderingSystem3D.addToBatch(mesh, () -> {
			this.shader.loadUniform("transformation_matrix", this.globalTransformation());
			this.shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
			this.shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
			if(material != null) {
				this.shader.loadUniform("material.ambient", material.ambient);
				this.shader.loadUniform("material.diffuse", material.diffuse);
				this.shader.loadUniform("material.specular", material.specular);;
				this.shader.loadUniform("material.shininess", 8.0f);
			}
			this.shader.loadUniform("camera_position", Camera3D.current().globalPosition());
			this.shader.start();
		}));
		super.onUpdate(delta);
	}
}
