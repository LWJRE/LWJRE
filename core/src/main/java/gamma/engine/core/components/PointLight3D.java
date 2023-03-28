package gamma.engine.core.components;

import gamma.engine.core.annotations.EditorIndex;
import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.rendering.RenderingSystem;
import gamma.engine.core.scene.Component;
import vecmatlib.color.Color3f;
import vecmatlib.vector.Vec3f;

@EditorIndex(1)
public class PointLight3D extends Component {

	@EditorVariable("Offset")
	@EditorRange
	public Vec3f offset = Vec3f.Zero();
	@EditorVariable("Color")
	public Color3f color = Color3f.White();

	@Override
	protected void onStart() {
		super.onStart();
		RenderingSystem.addToBatch(this);
	}

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
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
