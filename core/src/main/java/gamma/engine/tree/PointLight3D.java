package gamma.engine.tree;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.RenderingSystem;
import vecmatlib.color.Color3f;

public class PointLight3D extends Node3D {

	// TODO: Finish light node
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

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		RenderingSystem.addToBatch(this);
	}
}
