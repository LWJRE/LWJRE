package gamma.engine.core.node;

import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

/**
 * Node that represents a transformation in a 3D space.
 *
 * @author Nico
 */
public class Transform3D extends Node {

	/** Position of this node in a 3D space */
	public Vec3f position = Vec3f.Zero();
	/** Rotation of this node in radians around the x, y, z axes */
	public Vec3f rotation = Vec3f.Zero();
	/** Scale of this node in a 3D space */
	public Vec3f scale = Vec3f.One();

	/**
	 * Returns the local position of this node without considering the parent's position.
	 * This is the same as {@link Transform3D#position}.
	 *
	 * @return This node's local position
	 */
	public final Vec3f localPosition() {
		return this.position;
	}

	/**
	 * Returns the global position of this node, considering the parent's transformation.
	 *
	 * @return This node's position in the 3D world
	 */
	public final Vec3f globalPosition() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalTransformation().multiply(new Vec4f(this.position, 1.0f)).xyz();
		return this.position;
	}

	/**
	 * Returns a 4x4 matrix representing the local translation of this node.
	 *
	 * @return A 4x4 matrix representing the local translation of this node
	 */
	public final Mat4f localTranslation() {
		return Mat4f.translation(this.position);
	}

	/**
	 * Gets the rotation of this node in degrees around the X, Y, and Z axes.
	 *
	 * @return The rotation of this node in degrees around the X, Y, and Z axes
	 */
	public final Vec3f rotationDegrees() {
		return new Vec3f((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	/**
	 * Sets the rotation of this node around the X, Y, and Z axes.
	 *
	 * @param x Rotation degrees around the X axis
	 * @param y Rotation degrees around the Y axis
	 * @param z Rotation degrees around the Z axis
	 */
	public final void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vec3f((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	/**
	 * Sets the rotation of this node around the X, Y, and Z axes.
	 *
	 * @param degrees Rotation of this node in degrees around the X, Y, and Z axes
	 */
	public final void setRotationDegrees(Vec3f degrees) {
		this.setRotationDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	/**
	 * Rotates this node on the X, Y, and Z axes by the given degrees.
	 *
	 * @param x Rotation degrees around the X axis
	 * @param y Rotation degrees around the Y axis
	 * @param z Rotation degrees around the Z axis
	 */
	public final void rotateDegrees(float x, float y, float z) {
		this.rotation = this.rotation.plus((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	/**
	 * Returns a 4x4 matrix representing the local rotation of this node.
	 *
	 * @return A 4x4 matrix representing the local rotation of this node
	 */
	public final Mat4f localRotation() {
		return Mat4f.rotation(this.rotation);
	}

	/**
	 * Returns a 4x4 matrix representing the global rotation of this node.
	 *
	 * @return A 4x4 matrix representing the global rotation of this node
	 */
	public final Mat4f globalRotation() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalRotation().multiply(this.localRotation());
		return this.localRotation();
	}

	/**
	 * Returns a 4x4 matrix representing the global scaling of this node.
	 *
	 * @return A 4x4 matrix representing the global scaling of this node
	 */
	public final Vec3f globalScale() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalScale().multiply(this.scale);
		return this.scale;
	}

	/**
	 * Returns a 4x4 matrix representing the local scaling of this node.
	 *
	 * @return A 4x4 matrix representing the local scaling of this node
	 */
	public final Mat4f scalingMatrix() {
		return Mat4f.scaling(this.scale);
	}

	/**
	 * Returns a 4x4 matrix representing the local translation, rotation, and scale of this node.
	 *
	 * @return A 4x4 matrix representing the local translation, rotation, and scale of this node
	 */
	public final Mat4f localTransformation() {
		return this.localTranslation().multiply(this.localRotation()).multiply(this.scalingMatrix());
	}

	/**
	 * Returns a 4x4 matrix representing the global transformation of this node.
	 *
	 * @return A 4x4 matrix representing the global transformation of this node
	 */
	public final Mat4f globalTransformation() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalTransformation().multiply(this.localTransformation());
		return this.localTransformation();
	}
}
