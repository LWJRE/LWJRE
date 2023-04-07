package gamma.engine.components;

import gamma.engine.annotations.EditorDegrees;
import gamma.engine.annotations.EditorIndex;
import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.scene.Component;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

/**
 * Component that holds transformation in a 3D space.
 *
 * @author Nico
 */
@EditorIndex(0)
public class Transform3D extends Component {

	/** Position of the entity in a 3D space */
	@EditorVariable(name = "Position")
	@EditorRange
	public Vec3f position = Vec3f.Zero();

	/** Rotation of the entity in radians around the x, y, z axes */
	@EditorVariable(name = "Rotation")
	@EditorDegrees
	@EditorRange
	public Vec3f rotation = Vec3f.Zero();

	/** Scale of the entity in a 3D space */
	@EditorVariable(name = "Scale")
	@EditorRange
	public Vec3f scale = Vec3f.One();

	/**
	 * Returns the local position of the entity without considering the parent's position.
	 * This is the same as {@link Transform3D#position}.
	 *
	 * @return The entity's local position
	 */
	public Vec3f localPosition() {
		return this.position;
	}

	/**
	 * Returns the global position of the entity, considering the parent's transformation.
	 *
	 * @return The entity's position in the 3D world
	 */
	public Vec3f globalPosition() {
		return this.getComponentInParent(Transform3D.class)
				.map(parent -> parent.globalTransformation().multiply(new Vec4f(this.position, 1.0f)).xyz())
				.orElse(this.position);
	}

	/**
	 * Returns a 4x4 matrix representing the local translation of the entity.
	 *
	 * @return A 4x4 matrix representing the local translation of the entity
	 */
	public Mat4f localTranslation() {
		return Mat4f.translation(this.position);
	}

	/**
	 * Gets the rotation of the entity in degrees around the x, y, and z axes.
	 *
	 * @return The rotation of the entity in degrees around the x, y, and z axes
	 */
	public Vec3f rotationDegrees() {
		return new Vec3f((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	/**
	 * Sets the rotation of the entity around the x, y, and z axes.
	 *
	 * @param x Rotation degrees around the x axis
	 * @param y Rotation degrees around the y axis
	 * @param z Rotation degrees around the z axis
	 */
	public void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vec3f((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	/**
	 * Sets the rotation of the entity around the x, y, and z axes.
	 *
	 * @param degrees Rotation of the entity in degrees around the x, y, and z axes
	 */
	public void setRotationDegrees(Vec3f degrees) {
		this.setRotationDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	/**
	 * Rotates the entity on the x, y, and z axes by the given degrees.
	 *
	 * @param x Rotation degrees around the x axis
	 * @param y Rotation degrees around the y axis
	 * @param z Rotation degrees around the z axis
	 */
	public void rotateDegrees(float x, float y, float z) {
		this.rotation = this.rotation.plus((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	/**
	 * Rotates the entity on the x, y, and z axes by the given degrees.
	 *
	 * @param degrees Rotation degrees around the x, y, and z axes
	 */
	public void rotateDegrees(Vec3f degrees) {
		this.rotateDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	/**
	 * Returns a 4x4 matrix representing the local rotation of the entity.
	 *
	 * @return A 4x4 matrix representing the local rotation of the entity
	 */
	public Mat4f localRotation() {
		return Mat4f.rotation(this.rotation);
	}

	/**
	 * Returns a 4x4 matrix representing the global rotation of the entity.
	 *
	 * @return A 4x4 matrix representing the global rotation of the entity
	 */
	public Mat4f globalRotation() {
		return this.getComponentInParent(Transform3D.class)
				.map(parent -> parent.globalRotation().multiply(this.localRotation()))
				.orElse(this.localRotation());
	}

	/**
	 * Returns a 4x4 matrix representing the global scaling of the entity.
	 *
	 * @return A 4x4 matrix representing the global scaling of the entity
	 */
	public Vec3f globalScale() {
		return this.getComponentInParent(Transform3D.class)
				.map(parent -> parent.globalScale().multiply(this.scale))
				.orElse(this.scale);
	}

	/**
	 * Returns a 4x4 matrix representing the local scaling of the entity.
	 *
	 * @return A 4x4 matrix representing the local scaling of the entity
	 */
	public Mat4f scalingMatrix() {
		return Mat4f.scaling(this.scale);
	}

	/**
	 * Returns a 4x4 matrix representing the local translation, rotation, and scale of the entity.
	 *
	 * @return A 4x4 matrix representing the local translation, rotation, and scale of the entity
	 */
	public Mat4f localTransformation() {
		return this.localTranslation().multiply(this.localRotation()).multiply(this.scalingMatrix());
	}

	/**
	 * Returns a 4x4 matrix representing the global transformation of the entity.
	 *
	 * @return A 4x4 matrix representing the global transformation of the entity
	 */
	public Mat4f globalTransformation() {
		return this.getComponentInParent(Transform3D.class)
				.map(parent -> parent.globalTransformation().multiply(this.localTransformation()))
				.orElse(this.localTransformation());
	}
}
