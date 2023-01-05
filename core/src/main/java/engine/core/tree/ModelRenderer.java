package engine.core.tree;

import engine.core.RenderingSystem3D;
import engine.core.resources.Model;
import engine.core.resources.Shader;

public class ModelRenderer extends Transform3D {

	// TODO: Editor variable
	private String model;

	private Shader shader;

	@Override
	protected void onStart() {
		this.shader = Shader.main().createOrLoad();
		Model.getOrLoad(this.model).forEach((material, mesh) -> RenderingSystem3D.addToBatch(mesh, this, () -> this.shader.runProgram(shader -> {
			shader.loadUniform("transformation_matrix", this.globalTransformation());
			shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
			shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
			if(material != null) {
				shader.loadUniform("material.ambient", material.getAmbient());
				shader.loadUniform("material.diffuse", material.getDiffuse());
				shader.loadUniform("material.specular", material.getSpecular());;
				shader.loadUniform("material.shininess", 8.0f);
			}
			shader.loadUniform("camera_position", Camera3D.current().globalPosition());
		})));
		super.onStart();
	}

	@Override
	public void onExit() {
		RenderingSystem3D.removeFromBatch(this);
		super.onExit();
	}
}
