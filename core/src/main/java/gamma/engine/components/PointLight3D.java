package gamma.engine.components;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.RenderingSystem;
import gamma.engine.scene.Component;
import vecmatlib.color.Color3f;
import vecmatlib.vector.Vec3f;

public class PointLight3D extends Component {

	@EditorVariable(name = "Offset")
	@EditorRange
	public Vec3f offset = Vec3f.Zero();
	@EditorVariable(name = "Color")
	public Color3f color = Color3f.White();

	@Override
	protected void onStart() {
		super.onStart();
		RenderingSystem.addToBatch(this);
	}

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
		RenderingSystem.addToBatch(this);
	}

	@Override
	protected void onExit() {
		super.onExit();
		RenderingSystem.removeFromBatch(this);
	}

	public Vec3f position() {
		return this.getComponent(Transform3D.class).map(Transform3D::localPosition).orElse(Vec3f.Zero()).plus(this.offset);
	}

	public Vec3f globalPosition() {
		return this.getComponent(Transform3D.class).map(Transform3D::globalPosition).orElse(Vec3f.Zero()).plus(this.offset);
	}
}
