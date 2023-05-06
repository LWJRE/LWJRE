package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.RenderingSystem;
import vecmatlib.color.Color3f;
import vecmatlib.vector.Vec3f;

public class PointLight3D extends Node3D {

	@EditorVariable(name = "Offset")
	@EditorRange
	public Vec3f offset = Vec3f.Zero();
	@EditorVariable(name = "Color")
	public Color3f color = Color3f.White();

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
}
