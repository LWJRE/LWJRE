package gamma.engine.core.components;

import gamma.engine.core.annotations.EditorDegrees;
import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.scene.Component;
import gamma.engine.core.window.Window;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec2i;
import vecmatlib.vector.Vec3f;

import java.util.NoSuchElementException;

/**
 * Component representing a 3D camera.
 *
 * @author Nico
 */
public class Camera3D extends Component {

	/** Reference to the current camera */
	private static Camera3D currentCamera;

	/**
	 * Gets the current camera.
	 * If no camera is current, return a default camera.
	 *
	 * @return The current camera or a default camera if no camera is current
	 */
	public static Camera3D getCurrent() {
		return currentCamera != null ? currentCamera : new Camera3D();
	}

	/** Weather this is the current camera or not */
	@EditorVariable("Current")
	private boolean current = false;

	@EditorVariable("Fov")
	@EditorDegrees
	@EditorRange(min = 0.0f, max = 360.0f, step = 0.001f)
	public float fov = 1.22173f;
	/** Near distance plane */
	@EditorVariable("Near plane")
	@EditorRange(min = 0.001f, step = 0.001f)
	public float nearPlane = 0.1f;
	/** Far distance plane */
	@EditorVariable("Far plane")
	@EditorRange(min = 0.001f, step = 0.001f)
	public float farPlane = 1000.0f;

	@Override
	protected void onStart() {
		super.onStart();
		if(this.current)
			this.makeCurrent();
	}

	/**
	 * Sets this as the current camera.
	 */
	public final void makeCurrent() {
		getCurrent().current = false;
		currentCamera = this;
		this.current = true;
	}

	/**
	 * Gets the global position of the camera.
	 * Same as {@link Transform3D#globalPosition()}.
	 * @return The global position of the camera
	 * @throws NoSuchElementException if this entity does not have a transform component
	 * @throws RuntimeException if this component does not belong to any entity
	 */
	public Vec3f globalPosition() {
		return this.getComponent(Transform3D.class).map(Transform3D::globalPosition).orElse(Vec3f.Zero());
	}

	public Mat4f projectionMatrix() {
		float focalLength = (float) (1.0f / Math.tan(this.fov / 2.0f));
		Vec2i windowSize = Window.getCurrent().getSize();
		float aspect = (float) windowSize.x() / windowSize.y();
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
