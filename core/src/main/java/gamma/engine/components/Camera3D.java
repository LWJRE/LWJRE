package gamma.engine.components;

import gamma.engine.ApplicationProperties;
import gamma.engine.annotations.EditorDegrees;
import gamma.engine.annotations.EditorIndex;
import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.resources.Shader;
import gamma.engine.scene.Component;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

/**
 * Component representing a 3D camera.
 *
 * @author Nico
 */
@EditorIndex(1)
public class Camera3D extends Component {

	/** Reference to the current camera */
	private static Camera3D currentCamera;

	public static Camera3D getCurrent() {
		return currentCamera;
	}

	/** Weather this is the current camera or not */
	@EditorVariable("Current")
	private boolean current = false;

	@EditorVariable("Fov")
	@EditorDegrees
	@EditorRange(min = 0.0f, max = 360.0f)
	public float fov = 1.22173f;
	/** Near distance plane */
	@EditorVariable("Near plane")
	@EditorRange(min = 0.001f)
	public float nearPlane = 0.1f;
	/** Far distance plane */
	@EditorVariable("Far plane")
	@EditorRange(min = 0.001f)
	public float farPlane = 1000.0f;

	@Override
	protected void onStart() {
		super.onStart();
		if(this.current)
			this.makeCurrent();
	}

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		if(this.current) {
			Shader.setUniformStatic("projection_matrix", this.projectionMatrix());
			Shader.setUniformStatic("view_matrix", this.viewMatrix());
		}
	}

	/**
	 * Sets this as the current camera.
	 */
	public final void makeCurrent() {
		if(currentCamera != null)
			currentCamera.current = false;
		currentCamera = this;
		this.current = true;
	}

	public final Vec3f globalPosition() {
		return this.getComponent(Transform3D.class).map(Transform3D::globalPosition).orElse(Vec3f.Zero());
	}

	// TODO: Add setters

	public final Vec3f rotation() {
		return this.getComponent(Transform3D.class).map(transform -> transform.rotation).orElse(Vec3f.Zero());
	}

	public final Vec3f rotationDegrees() {
		return this.getComponent(Transform3D.class).map(Transform3D::rotationDegrees).orElse(Vec3f.Zero());
	}

	public final float pitch() {
		return this.getComponent(Transform3D.class).map(transform -> transform.rotation.x()).orElse(0.0f);
	}

	public final float pitchDegrees() {
		return (float) Math.toDegrees(this.pitch());
	}

	public final float yaw() {
		return this.getComponent(Transform3D.class).map(transform -> transform.rotation.y()).orElse(0.0f);
	}

	public final float yawDegrees() {
		return (float) Math.toDegrees(this.yaw());
	}

	public final float roll() {
		return this.getComponent(Transform3D.class).map(transform -> transform.rotation.z()).orElse(0.0f);
	}

	public final float rollDegrees() {
		return (float) Math.toDegrees(this.roll());
	}

	public Mat4f projectionMatrix() {
		float focalLength = (float) (1.0f / Math.tan(this.fov / 2.0f));
		float aspect = ApplicationProperties.get("window/viewport/x", 16.0f) / ApplicationProperties.get("window/viewport/y", 9.0f);
		return new Mat4f(
				focalLength, 0.0f, 0.0f, 0.0f,
				0.0f, focalLength * aspect, 0.0f, 0.0f,
				0.0f, 0.0f, -(this.farPlane + this.nearPlane) / (this.farPlane - this.nearPlane), -(2 * this.farPlane * this.nearPlane) / (this.farPlane - this.nearPlane),
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	public Mat4f viewMatrix() {
		return Mat4f.translation(this.globalPosition().negated()).multiply(this.getComponent(Transform3D.class).map(Transform3D::localRotation).orElse(Mat4f.Identity()));
	}
}
