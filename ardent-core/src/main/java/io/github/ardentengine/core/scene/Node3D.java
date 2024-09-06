package io.github.ardentengine.core.scene;

import io.github.scalamath.vecmatlib.*;

import java.util.Objects;

/**
 * Base class for all 3D objects.
 * Has a position, rotation, and scale.
 */
public class Node3D extends Node {

    /** Position of this node relative to its parent. */
    private Vec3f position = Vec3f.Zero();
    /**Rotation in radians of this node relative to its parent. */
    private Vec3f rotation = Vec3f.Zero();
    /** Scale of this node. */
    private Vec3f scale = Vec3f.One();

    /** Cached local transform. */
    private Mat3x4f localTransform = null;
    /** Cached global transform. */
    private Mat3x4f globalTransform = null;

    /**
     * Private method used to invalidate this node's transform when its position, rotation, or scale are changed.
     * <p>
     *     Calling this method causes the local and global transform to be recomputed when the {@link Node3D#localTransform()} and {@link Node3D#globalTransform()} methods are called.
     * </p>
     * <p>
     *     Iterates recursively through the children of this node and invalidates their transform if it isn't already.
     * </p>
     * <p>
     *     Calling this method has no effect if this node's transform is already invalid.
     * </p>
     */
    private void invalidateTransform() {
        if(this.globalTransform != null) {
            this.localTransform = null;
            this.globalTransform = null;
            for(var child : this.getChildren()) {
                if(child instanceof Node3D) {
                    ((Node3D) child).invalidateTransform();
                }
            }
        }
    }

    @Override
    void exitTree() {
        this.invalidateTransform();
        super.exitTree();
    }

    /**
     * Getter method for {@link Node3D#position)}.
     * Returns the position of this node relative to its parent.
     *
     * @return The position of this node relative to its parent.
     */
    public final Vec3f position() {
        return this.position;
    }

    /**
     * Setter method for {@link Node3D#position}.
     * Sets the position of this node relative to its parent.
     *
     * @param position Position of this node relative to its parent.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node3D#setPosition(float, float, float)
     */
    public final void setPosition(Vec3f position) {
        Objects.requireNonNull(position, "Position cannot be null");
        if(!this.position.equals(position)) {
            this.position = position;
            this.invalidateTransform();
        }
    }

    /**
     * Setter method for {@link Node3D#position}.
     * Sets the position of this node relative to its parent.
     *
     * @param x Position of this node on the x axis.
     * @param y Position of this node on the y axis.
     * @param z Position of this node on the z axis.
     *
     * @see Node3D#setPosition(Vec3f)
     */
    public final void setPosition(float x, float y, float z) {
        if(!this.position.equals(x, y, z)) {
            this.position = new Vec3f(x, y, z);
            this.invalidateTransform();
        }
    }

    /**
     * Translates this node by the given offset.
     *
     * @param offset The translation to apply.
     * @throws NullPointerException If the given offset is null.
     *
     * @see Node3D#translate(float, float, float)
     */
    public final void translate(Vec3f offset) {
        Objects.requireNonNull(offset, "Offset cannot be null");
        if(!offset.equals(0.0f, 0.0f, 0.0f)) {
            this.position = this.position.plus(offset);
            this.invalidateTransform();
        }
    }

    /**
     * Translates this node by the given offset.
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     * @param z Translation on the z axis.
     *
     * @see Node3D#translate(Vec3f)
     */
    public final void translate(float x, float y, float z) {
        if(x != 0.0f || y != 0.0f || z != 0.0f) {
            this.position = this.position.plus(x, y, z);
            this.invalidateTransform();
        }
    }

    public final Vec3f rotation() {
        return this.rotation;
    }

    public final Vec3f rotationDegrees() {
        return new Vec3f(
            (float) Math.toDegrees(this.rotation().x()),
            (float) Math.toDegrees(this.rotation().y()),
            (float) Math.toDegrees(this.rotation().z())
        );
    }

    // TODO: Rotation quaternion

    public final void setRotation(float x, float y, float z) {
        if(!this.rotation.equals(x, y, z)) {
            this.rotation = new Vec3f(x, y, z);
            this.invalidateTransform();
        }
    }

    public final void setRotation(Vec3f rotation) {
        Objects.requireNonNull(rotation, "Rotation cannot be null");
        if(!this.rotation.equals(rotation)) {
            this.rotation = rotation;
            this.invalidateTransform();
        }
    }

    public final void setRotationDegrees(float x, float y, float z) {
        this.setRotation(
            (float) Math.toDegrees(x),
            (float) Math.toDegrees(y),
            (float) Math.toDegrees(z)
        );
    }

    public final void setRotationDegrees(Vec3f rotation) {
        Objects.requireNonNull(rotation, "Rotation cannot be null");
        this.setRotationDegrees(rotation.x(), rotation.y(), rotation.z());
    }

    // TODO: Set quaternion

    public final void rotate(float x, float y, float z) {
        if(x != 0.0f || y != 0.0f || z != 0.0f) {
            this.rotation = this.rotation.plus(x, y, z);
            this.invalidateTransform();
        }
    }

    public final void rotate(Vec3f rotation) {
        Objects.requireNonNull(rotation, "Rotation cannot be null");
        if(!rotation.equals(0.0f, 0.0f, 0.0f)) {
            this.rotation = this.rotation.plus(rotation);
            this.invalidateTransform();
        }
    }

    public final void rotateDegrees(float x, float y, float z) {
        this.rotate(
            (float) Math.toDegrees(x),
            (float) Math.toDegrees(y),
            (float) Math.toDegrees(z)
        );
    }

    public final void rotateDegrees(Vec3f rotation) {
        Objects.requireNonNull(rotation, "Rotation cannot be null");
        this.rotateDegrees(rotation.x(), rotation.y(), rotation.z());
    }

    // TODO: Rotate quaternion

    /**
     * Getter method for {@link Node3D#scale}.
     * Returns the scale of this node.
     *
     * @return The scale of this node.
     */
    public final Vec3f scale() {
        return this.scale;
    }

    /**
     * Setter method for {@link Node3D#scale}.
     * Sets the scale of this node.
     *
     * @param scale Scale of this node.
     * @throws NullPointerException If the given scale is null.
     *
     * @see Node3D#setScale(float, float, float)
     */
    public final void setScale(Vec3f scale) {
        Objects.requireNonNull(scale, "Scale cannot be null");
        if(!this.scale.equals(scale)) {
            this.scale = scale;
            this.invalidateTransform();
        }
    }

    /**
     * Setter method for {@link Node3D#scale}.
     * Sets the scale of this node.
     *
     * @param x Scale on the x axis.
     * @param y Scale on the y axis.
     * @param z Scale on the z axis.
     *
     * @see Node3D#setScale(Vec3f)
     */
    public final void setScale(float x, float y, float z) {
        if(!this.scale.equals(x, y, z)) {
            this.scale = new Vec3f(x, y, z);
            this.invalidateTransform();
        }
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param scale Scale ratio.
     * @throws NullPointerException If the given scale is null.
     *
     * @see Node3D#applyScale(float, float, float)
     */
    public final void applyScale(Vec3f scale) {
        Objects.requireNonNull(scale, "Scale cannot be null");
        if(!scale.equals(1.0f, 1.0f, 1.0f)) {
            this.scale = this.scale.multiply(scale);
            this.invalidateTransform();
        }
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param x Scale ratio on the x axis.
     * @param y Scale ratio on the y axis.
     * @param z Scale ratio on the z axis.
     *
     * @see Node3D#applyScale(Vec3f)
     */
    public final void applyScale(float x, float y, float z) {
        if(x != 1.0f || y != 1.0f || z != 1.0f) {
            this.scale = this.scale.multiply(x, y, z);
            this.invalidateTransform();
        }
    }

    /**
     * Returns this node's local transform as a 3x4 transformation matrix.
     *
     * @return This node's local transform as a 3x4 transformation matrix.
     */
    public final Mat3x4f localTransform() {
        if(this.localTransform != null) {
            return this.localTransform;
        }
        return this.localTransform = Mat3x4f.translation(this.position())
            .multiply(Mat4f.rotation(this.rotation()))
            .multiply(Mat4f.scaling(this.scale()));
    }

    /**
     * Returns this node's global transform as a 3x4 transformation matrix.
     * <p>
     *     If this node's parent is not a {@code Node3D}, this method is equivalent to {@link Node3D#localTransform()}.
     * </p>
     *
     * @return This node's global transform as a 3x4 transformation matrix.
     */
    public final Mat3x4f globalTransform() {
        if(this.globalTransform != null) {
            return this.globalTransform;
        } else if(this.parent() instanceof Node3D parent) {
            return this.globalTransform = parent.globalTransform().multiply(this.localTransform(), 0.0f, 0.0f, 0.0f, 1.0f);
        }
        return this.globalTransform = this.localTransform();
    }

    /**
     * Returns the global position of this node.
     *
     * @return The global position of this node.
     */
    public final Vec3f globalPosition() {
        return this.globalTransform().col3();
    }

    /**
     * Sets the global position of this node.
     * <p>
     *     Sets this node's {@link Node3D#position} to a value such that its {@link Node3D#globalPosition()} will be equal to the given value.
     * </p>
     *
     * @param position Global position of this node.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node3D#setGlobalPosition(float, float, float)
     */
    public final void setGlobalPosition(Vec3f position) {
        Objects.requireNonNull(position, "Position cannot be null");
        if(this.parent() instanceof Node3D parent) {
            this.setPosition(parent.globalTransform().affineInverse().multiply(position, 1.0f));
        } else {
            this.setPosition(position);
        }
    }

    /**
     * Sets the global position of this node.
     * <p>
     *     Sets this node's {@link Node3D#position} to a value such that its {@link Node3D#globalPosition()} will be equal to the given value.
     * </p>
     *
     * @param x Global position on the x axis.
     * @param y Global position on the y axis.
     * @param z Global position on the z axis.
     *
     * @see Node3D#setGlobalPosition(Vec3f)
     */
    public final void setGlobalPosition(float x, float y, float z) {
        if(this.parent() instanceof Node3D parent) {
            this.setPosition(parent.globalTransform().affineInverse().multiply(x, y, z, 1.0f));
        } else {
            this.setPosition(x, y, z);
        }
    }

    // TODO: Get/Set global rotation (euler, degrees)

    // TODO: What about global scale?

    /**
     * Sets the local transform of this node.
     * <p>
     *     Sets the {@link Node3D#position}, {@link Node3D#rotation}, and {@link Node3D#scale} of this node so that its {@link Node3D#localTransform()} will be equal to the given transform.
     * </p>
     *
     * @param transform Local transform as a 3x4 transformation matrix.
     * @throws NullPointerException If the given transform is null.
     */
    public final void setLocalTransform(Mat3x4f transform) {
        Objects.requireNonNull(transform, "Transform cannot be null");
        this.setPosition(transform.m03(), transform.m13(), transform.m23());
        var basis = transform.submatrix(3);
        // TODO: Add a better implementation for this
        this.setRotation(EulerOrder.ZYX.toEulerAngles(basis.toDouble()).toFloat());
        var sign = Math.signum(basis.determinant());
        this.setScale(basis.col0().length() * sign, basis.col1().length() * sign, basis.col2().length() * sign);
    }

    /**
     * Sets the global transform of this node.
     * <p>
     *     Sets the {@link Node3D#position}, {@link Node3D#rotation}, and {@link Node3D#scale} of this node so that its {@link Node3D#globalTransform()} will be equal to the given transform.
     * </p>
     * <p>
     *     If this node's parent is not a {@code Node3D}, this method is equivalent to {@link Node3D#setLocalTransform(Mat3x4f)}.
     * </p>
     *
     * @param transform Global transform as a 3x4 transformation matrix.
     * @throws NullPointerException If the given transform is null.
     */
    public final void setGlobalTransform(Mat3x4f transform) {
        Objects.requireNonNull(transform, "Transform cannot be null");
        if(this.parent() instanceof Node3D parent) {
            this.setLocalTransform(parent.globalTransform().affineInverse().multiply(transform, 0.0f, 0.0f, 0.0f, 1.0f));
        } else {
            this.setLocalTransform(transform);
        }
    }

    /**
     * Sets this node's parent and keeps its global transform.
     * <p>
     *     Calls {@link Node#setParent(Node)} and then sets this node's global transform to its previous value.
     * </p>
     *
     * @param parent This node's new parent or null to remove this node from its parent.
     */
    public final void setParentKeepTransform(Node parent) {
        var transform = this.globalTransform();
        this.setParent(parent);
        this.setGlobalTransform(transform);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param globalPosition Position in global coordinates.
     * @return The given global position in local coordinates relative to this node.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node3D#toLocal(float, float, float)
     */
    public final Vec3f toLocal(Vec3f globalPosition) {
        Objects.requireNonNull(globalPosition, "Position cannot be null");
        return this.globalTransform().affineInverse().multiply(globalPosition, 1.0f);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param x Position in local coordinates on the x axis relative to this node.
     * @param y Position in local coordinates on the y axis relative to this node.
     * @param z Position in local coordinates on the z axis relative to this node.
     * @return The given global position in local coordinates relative to this node.
     *
     * @see Node3D#toLocal(Vec3f)
     */
    public final Vec3f toLocal(float x, float y, float z) {
        return this.globalTransform().affineInverse().multiply(x, y, z, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param localPosition Position in local coordinates relative to this node.
     * @return The given local position relative to this node in global coordinates.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node3D#toGlobal(float, float, float)
     */
    public final Vec3f toGlobal(Vec3f localPosition) {
        Objects.requireNonNull(localPosition, "Position cannot be null");
        return this.globalTransform().multiply(localPosition, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param x Position in local coordinates on the x axis relative to this node.
     * @param y Position in local coordinates on the y axis relative to this node.
     * @param z Position in local coordinates on the z axis relative to this node.
     * @return The given local position relative to this node in global coordinates.
     *
     * @see Node3D#toGlobal(Vec3f)
     */
    public final Vec3f toGlobal(float x, float y, float z) {
        return this.globalTransform().multiply(x, y, z, 1.0f);
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
        this.rotation = EulerOrder.ZYX.toEulerAngles(basis.toDouble()).toFloat();
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

    // TODO: Vec3f forward direction
}