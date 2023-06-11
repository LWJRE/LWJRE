package io.github.lwjre.nodes;

import io.github.lwjre.annotations.EditorRange;
import io.github.lwjre.annotations.EditorVariable;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.hexagonnico.vecmatlib.vector.Vec4f;

/**
 * Node class for all 3D objects.
 * Represents a 3D transform.
 *
 * @author Nico
 */
public class Node3D extends Node {

	/**
	 * Local position of the node.
	 */
	@EditorVariable(name = "Position")
	@EditorRange
	public Vec3f position = Vec3f.Zero();

	/**
	 * Local rotation in radians of the node on the three axes.
	 */
	@EditorVariable(name = "Rotation")
	@EditorRange
	public Vec3f rotation = Vec3f.Zero();

	/**
	 * Local scale of the node.
	 */
	@EditorVariable(name = "Scale")
	@EditorRange
	public Vec3f scale = Vec3f.One();

	/**
	 * Computes this node's global position.
	 * If this node's parent is also a {@code Node3D} the global position will be the parent's {@link Node3D#globalTransformation()} multiplied by this node's local position, otherwise it will be equal to the local position.
	 *
	 * @return A {@link Vec3f} representing this node's global position
	 */
	public Vec3f globalPosition() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalTransformation().multiply(new Vec4f(this.position, 1.0f)).xyz();
		}
		return this.position;
	}

	/**
	 * Computes a translation matrix representing this node's local translation.
	 *
	 * @return A {@link Mat4f} representing this node's local translation
	 */
	public Mat4f localTranslation() {
		return Mat4f.translation(this.position);
	}

	/**
	 * Computes this node's local rotation in degrees.
	 *
	 * @return A {@link Vec3f} representing this node's local rotation in degrees around the three axes
	 */
	public Vec3f rotationDegrees() {
		return new Vec3f((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	/**
	 * Sets this node's rotation to the given values in degrees.
	 *
	 * @param x Rotation degrees around the X axis
	 * @param y Rotation degrees around the Y axis
	 * @param z Rotation degrees around the Z axis
	 */
	public void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vec3f((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	/**
	 * Sets this node's rotation to the given rotation in degrees.
	 *
	 * @param degrees Rotation degrees around the three axes
	 */
	public void setRotationDegrees(Vec3f degrees) {
		this.setRotationDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	/**
	 * Rotates this node by the given rotation degrees.
	 *
	 * @param x Rotation degrees around the X axis
	 * @param y Rotation degrees around the Y axis
	 * @param z Rotation degrees around the Z axis
	 */
	public void rotateDegrees(float x, float y, float z) {
		this.rotation = this.rotation.plus((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	/**
	 * Rotates this node by the given rotation degrees.
	 *
	 * @param degrees Rotation degrees around the three axes
	 */
	public void rotateDegrees(Vec3f degrees) {
		this.rotateDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	/**
	 * Computes a rotation matrix representing this node's local rotation around the three axes.
	 *
	 * @return A {@link Mat4f} representing this node's local rotation
	 */
	public Mat4f localRotation() {
		return Mat4f.rotation(this.rotation);
	}

	/**
	 * Computes a rotation matrix representing this node's global rotation around the three axes.
	 * If this node's parent is also a {@code Node3D} the global rotation will be the parent's global rotation multiplied by this node's {@link Node3D#localRotation()}, otherwise it will be equal to the local rotation.
	 *
	 * @return A {@link Mat4f} representing this node's global rotation
	 */
	public Mat4f globalRotation() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalRotation().multiply(this.localRotation());
		}
		return this.localRotation();
	}

	/**
	 * Computes this node's global scale.
	 * If this node's parent is also a {@code Node3D} the global scale will be the parent's global scale multiplied by this node's {@link Node3D#scale}, otherwise it will be equal to the local scale.
	 *
	 * @return A {@link Vec3f} representing this node's global scale
	 */
	public Vec3f globalScale() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalScale().multiply(this.scale);
		}
		return this.scale;
	}

	/**
	 * Computes a scaling matrix representing this node's local scale.
	 *
	 * @return A {@link Mat4f} representing this node's local scale
	 */
	public Mat4f scalingMatrix() {
		return Mat4f.scaling(this.scale);
	}

	/**
	 * Computes a transformation matrix representing this node's local transformation.
	 *
	 * @return A {@link Mat4f} representing this node's local transformation
	 */
	public Mat4f localTransformation() {
		return this.localTranslation().multiply(this.localRotation()).multiply(this.scalingMatrix());
	}

	/**
	 * Computes a transformation matrix representing this node's global transformation.
	 * If this node's parent is also a {@code Node3D} the global transformation will be the parent's global transformation multiplied by this node's {@link Node3D#localTransformation()}, otherwise it will be equal to the local transformation.
	 *
	 * @return A {@link Mat4f} representing this node's local transformation
	 */
	public Mat4f globalTransformation() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalTransformation().multiply(this.localTransformation());
		}
		return this.localTransformation();
	}
}
