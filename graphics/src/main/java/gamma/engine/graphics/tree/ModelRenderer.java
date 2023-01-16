package gamma.engine.graphics.tree;

import gamma.engine.core.tree.Camera3D;
import gamma.engine.graphics.RenderingSystem3D;
import gamma.engine.graphics.resources.Model;
import gamma.engine.graphics.resources.Shader;
import gamma.engine.graphics.resources.ShaderLoader;
import gamma.engine.core.tree.Transform3D;

public class ModelRenderer extends Transform3D {

	// TODO: Editor variables
	private String model;

	private final Shader shader = ShaderLoader.getOrLoad("/gamma/engine/graphics/shaders/main_shader.yaml");

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
