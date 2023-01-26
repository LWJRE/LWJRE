package gamma.engine.graphics.node;

import gamma.engine.core.annotation.EditorVariable;
import gamma.engine.core.node.Camera3D;
import gamma.engine.graphics.RenderingSystem3D;
import gamma.engine.graphics.resources.Model;
import gamma.engine.graphics.resources.Shader;
import gamma.engine.graphics.resources.ShaderLoader;
import gamma.engine.core.node.Transform3D;

public class ModelRenderer extends Transform3D {

	@EditorVariable
	private String model;

	private final Shader shader = ShaderLoader.getOrLoad("/gamma/engine/graphics/shaders/main_shader.yaml");

	private Model modelInstance;

	@Override
	protected void onEnterTree() {
		this.modelInstance = Model.getOrLoad(this.model);
		super.onEnterTree();
	}

	@Override
	protected void onUpdate(float delta) {
		this.render();
		super.onUpdate(delta);
	}

	@Override
	protected void onUpdateInEditor() {
		this.render();
		super.onUpdateInEditor();
	}

	private void render() {
		this.modelInstance.forEach((material, mesh) -> RenderingSystem3D.addToBatch(mesh, () -> {
			this.shader.loadUniform("transformation_matrix", this.globalTransformation());
			this.shader.loadUniform("projection_matrix", Camera3D.current().projectionMatrix());
			this.shader.loadUniform("view_matrix", Camera3D.current().viewMatrix());
			if(material != null) {
				// TODO: Finish materials
				this.shader.loadUniform("material.ambient", material.ambient);
				this.shader.loadUniform("material.diffuse", material.diffuse);
				this.shader.loadUniform("material.specular", material.specular);
				this.shader.loadUniform("material.shininess", 8.0f);
			}
			this.shader.loadUniform("camera_position", Camera3D.current().globalPosition());
			this.shader.start();
		}));
	}
}
