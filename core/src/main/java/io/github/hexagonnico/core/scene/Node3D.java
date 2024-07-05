package io.github.hexagonnico.core.scene;

import io.github.scalamath.vecmatlib.*;

/**
 * Base class for all 3D objects.
 * Has a position, rotation, and scale.
 */
public class Node3D extends Node {

    /**
     * Position relative to this node's parent.
     *
     * @see Node3D#translate(Vec3f)
     * @see Node3D#globalPosition()
     */
    public Vec3f position = Vec3f.Zero();
    /**
     * Quaternion representing the rotation of this node relative to its parent.
     * <p>
     *     Modifying this value is useful to create complex rotation.
     *     Use {@link Quatf#euler()} to get the rotation in euler angles.
     * </p>
     *
     * @see Node3D#rotationDegrees()
     * @see Node3D#globalRotation()
     */
    public Quatf rotation = Quatf.Identity();
    /**
     * Scale of this node.
     *
     * @see Node3D#applyScale(Vec3f)
     */
    public Vec3f scale = Vec3f.One();

    /**
     * Translates this node by the given offset.
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     * @param z Translation on the z axis.
     */
    public final void translate(float x, float y, float z) {
        this.position = this.position.plus(x, y, z);
    }

    /**
     * Translates this node by the given offset.
     *
     * @param offset The translation to apply.
     */
    public final void translate(Vec3f offset) {
        this.position = this.position.plus(offset);
    }

    /**
     * Returns the rotation of this node relative to its parent on the three axes in degrees.
     *
     * @return The rotation of this node in degrees.
     *
     * @see Node3D#setRotationDegrees(Vec3f)
     */
    public final Vec3f rotationDegrees() {
        var rotation = this.rotation.euler();
        return new Vec3f(
            (float) Math.toDegrees(rotation.x()),
            (float) Math.toDegrees(rotation.y()),
            (float) Math.toDegrees(rotation.z())
        );
    }

    /**
     * Sets this node's rotation in the form of euler angles.
     *
     * @param x Rotation on the x axis in radians.
     * @param y Rotation on the y axis in radians.
     * @param z Rotation on the z axis in radians.
     *
     * @see Node3D#rotation
     * @see Quatf#fromEuler(Vec3f)
     */
    public final void setRotation(double x, double y, double z) {
        this.rotation = Quatf.fromEuler(x, y, z);
    }

    /**
     * Sets this node's rotation in the form of euler angles.
     *
     * @param rotation The rotation of this node in the form of euler angles.
     *
     * @see Node3D#rotation
     * @see Quatf#fromEuler(Vec3f)
     */
    public final void setRotation(Vec3f rotation) {
        this.rotation = Quatf.fromEuler(rotation);
    }

    /**
     * Setter method equivalent to {@link Node3D#setRotation(double, double, double)} that uses degrees instead of radians.
     *
     * @param x Rotation on the x axis in degrees.
     * @param y Rotation on the y axis in degrees.
     * @param z Rotation on the z axis in degrees.
     */
    public final void setRotationDegrees(double x, double y, double z) {
        this.setRotation(Math.toRadians(x), Math.toRadians(y), Math.toRadians(z));
    }

    /**
     * Setter method equivalent to {@link Node3D#setRotation(Vec3f)} that uses degrees instead of radians.
     *
     * @param rotation Rotation in degrees.
     */
    public final void setRotationDegrees(Vec3f rotation) {
        this.setRotationDegrees(rotation.x(), rotation.y(), rotation.z());
    }

    // TODO: Rotate

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param x Scale ratio on the x axis.
     * @param y Scale ratio on the y axis.
     * @param z Scale ratio on the z axis.
     */
    public final void applyScale(float x, float y, float z) {
        this.scale = this.scale.multiply(x, y, z);
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param scale Scale ratio.
     */
    public final void applyScale(Vec3f scale) {
        this.scale = this.scale.multiply(scale);
    }

    /**
     * Returns this node's local transform as a 3x4 transformation matrix.
     *
     * @return This node's local transform as a 3x4 transformation matrix.
     */
    public final Mat3x4f localTransform() {
        return Mat3x4f.translation(this.position)
            .multiply(Mat4f.rotation(this.rotation))
            .multiply(Mat4f.scaling(this.scale));
    }

    /**
     * Returns this node's global transform as a 3x4 transformation matrix.
     *
     * @return This node's global transform as a 3x4 transformation matrix.
     */
    public final Mat3x4f globalTransform() {
        if(this.getParent() instanceof Node3D parent) {
            return parent.globalTransform().multiply(this.localTransform(), 0.0f, 0.0f, 0.0f, 1.0f);
        }
        return this.localTransform();
    }

    /**
     * Returns the global position of this node.
     *
     * @return The global position of this node.
     *
     * @see Node3D#setGlobalPosition(Vec3f)
     */
    public final Vec3f globalPosition() {
        return this.globalTransform().col3();
    }

    /**
     * Sets this node's {@link Node3D#position} to a value such that its {@link Node3D#globalPosition()} will be equal to the given value.
     *
     * @param x New global position on the x axis.
     * @param y New global position on the y axis.
     * @param z New global position on the z axis.
     */
    public final void setGlobalPosition(float x, float y, float z) {
        if(this.getParent() instanceof Node3D parent) {
            this.position = parent.globalTransform().affineInverse().multiply(x, y, z, 1.0f);
        } else {
            this.position = new Vec3f(x, y, z);
        }
    }

    /**
     * Sets this node's {@link Node3D#position} to a value such that its {@link Node3D#globalPosition()} will be equal to the given value.
     *
     * @param position New global position.
     */
    public final void setGlobalPosition(Vec3f position) {
        if(this.getParent() instanceof Node3D parent) {
            this.position = parent.globalTransform().affineInverse().multiply(position, 1.0f);
        } else {
            this.position = position;
        }
    }

    /**
     * Returns the global rotation of this node in the form of euler angles.
     *
     * @return The global rotation of this node in the form of euler angles.
     *
     * @see Node3D#globalRotationDegrees()
     */
    public final Vec3f globalRotation() {
        // TODO: Add a EulerOrder#toEulerAngles(Mat3f) method
        return EulerOrder.ZYX.toEulerAngles(this.globalTransform().submatrix(3).toDouble()).toFloat();
    }

    /**
     * Equivalent of {@link Node3D#globalRotation()} that uses degrees instead of radians.
     *
     * @return The global rotation of this node in degrees.
     */
    public final Vec3f globalRotationDegrees() {
        var globalRotation = this.globalRotation();
        return new Vec3f(
            (float) Math.toDegrees(globalRotation.x()),
            (float) Math.toDegrees(globalRotation.y()),
            (float) Math.toDegrees(globalRotation.z())
        );
    }

    // TODO: Set global rotation

    /**
     * Sets this node's {@link Node3D#position}, {@link Node3D#rotation}, and {@link Node3D#scale} so that its {@link Node3D#localTransform()} will be equal to the given transform.
     *
     * @param transform The new local transform as a 3x4 transformation matrix.
     *
     * @see Node3D#setGlobalTransform(Mat3x4f)
     */
    public final void setLocalTransform(Mat3x4f transform) {
        this.position = transform.col3();
        var basis = transform.submatrix(3);
        // TODO: Add a Quatf#fromEuler(Mat3f) method
        this.rotation = Quatf.fromEuler(EulerOrder.ZYX.toEulerAngles(basis.toDouble()));
        var sign = Math.signum(basis.determinant());
        this.scale = new Vec3f(basis.col0().length() * sign, basis.col1().length() * sign, basis.col2().length() * sign);
    }

    /**
     * Sets this node's {@link Node3D#position}, {@link Node3D#rotation}, and {@link Node3D#scale} so that its {@link Node3D#globalTransform()} will be equal to the given transform.
     *
     * @param transform The new global transform as a 3x4 transformation matrix.
     *
     * @see Node3D#setLocalTransform(Mat3x4f)
     */
    public final void setGlobalTransform(Mat3x4f transform) {
        if(this.getParent() instanceof Node3D parent) {
            this.setLocalTransform(parent.globalTransform().affineInverse().multiply(transform, 0.0f, 0.0f, 0.0f, 1.0f));
        } else {
            this.setLocalTransform(transform);
        }
    }

    /**
     * Removes this node from its parent and adds it as a child of the given node keeping its {@link Node3D#globalTransform()}.
     *
     * @param parent The new parent.
     *
     * @see Node3D#setGlobalTransform(Mat3x4f)
     */
    public final void reparent(Node parent) {
        var transform = this.globalTransform();
        this.removeFromParent();
        parent.addChild(this);
        this.setGlobalTransform(transform);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param x Global position on the x axis.
     * @param y Global position on the y axis.
     * @param z Global position on the z axis.
     * @return The given global position in local coordinates relative to this node.
     *
     * @see Node3D#toGlobal(float, float, float)
     */
    public final Vec3f toLocal(float x, float y, float z) {
        return this.globalTransform().affineInverse().multiply(x, y, z, 1.0f);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param globalPosition The global position.
     * @return The given global position in local coordinates relative to this node.
     *
     * @see Node3D#toGlobal(Vec3f)
     */
    public final Vec3f toLocal(Vec3f globalPosition) {
        return this.globalTransform().affineInverse().multiply(globalPosition, 1.0f);
    }
    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param x Local position on the x axis relative to this node.
     * @param y Local position on the y axis relative to this node.
     * @param z Local position on the z axis relative to this node.
     * @return The given local position relative to this node in global coordinates.
     *
     * @see Node3D#toLocal(float, float, float)
     */
    public final Vec3f toGlobal(float x, float y, float z) {
        return this.globalTransform().multiply(x, y, z, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param localPosition The local position relative to this node.
     * @return The given local position relative to this node in global coordinates.
     *
     * @see Node3D#toLocal(Vec3f)
     */
    public final Vec3f toGlobal(Vec3f localPosition) {
        return this.globalTransform().multiply(localPosition, 1.0f);
    }

    public final void lookAtFromPosition(Vec3f position, Vec3f target, Vec3f up) {
        // TODO: Pos must not be approximately equal to target
        // TODO: Up must bot be approximately zero
        // TODO: Up X (target - position) must not be approximately zero
        var forward = target.minus(position);
        var vz = forward.normalized().negated();
        var vx = up.cross(vz).normalized();
        // TODO: The target vector and up vector can't be parallel to each other
        var vy = vz.cross(vx);
        var basis = Mat3f.fromColumns(vx, vy, vz);
        // TODO: Add a Quatf#fromEuler(Mat3f) method
        this.rotation = Quatf.fromEuler(EulerOrder.ZYX.toEulerAngles(basis.toDouble()));
    }

    public final void lookAtFromPosition(Vec3f position, Vec3f target) {
        this.lookAtFromPosition(position, target, Vec3f.Up());
    }

    public final void lookAt(Vec3f target, Vec3f up) {
        this.lookAtFromPosition(this.globalPosition(), target, up);
    }

    public final void lookAt(Vec3f target) {
        this.lookAt(target, Vec3f.Up());
    }
}
