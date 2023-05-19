package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.CubeMesh;
import gamma.engine.rendering.DebugRenderer;
import gamma.engine.rendering.RenderingSystem;
import vecmatlib.color.Color3f;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

public class PointLight3D extends Node3D {

	@EditorVariable(name = "Ambient")
	public Color3f ambient = Color3f.White();
	@EditorVariable(name = "Diffuse")
	public Color3f diffuse = Color3f.White();
	@EditorVariable(name = "Specular")
	public Color3f specular = Color3f.White();

	@EditorVariable(name = "Energy")
	@EditorRange(min = 0.0f)
	public float energy = 1.0f;

	@EditorVariable(name = "Attenuation")
	@EditorRange(min = 0.0f)
	public Vec3f attenuation = new Vec3f(1.0f, 0.0f, 0.0f);

	@Override
	protected void onEnter() {
		super.onEnter();
		RenderingSystem.addToBatch(this);
	}

	@Override
	protected void onExit() {
		super.onExit();
		RenderingSystem.removeFromBatch(this);
	}

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		DebugRenderer.addToBatch(CubeMesh.INSTANCE, mesh -> {
			DebugRenderer.SHADER.setUniform("transformation_matrix", Mat4f.translation(this.globalPosition()).multiply(Mat4f.scaling(this.energy, this.energy, this.energy)));
			DebugRenderer.SHADER.setUniform("color", 1.0f, 0.5f, 0.0f, 1.0f);
			mesh.drawElements();
		});
		RenderingSystem.addToBatch(this);
	}
}
