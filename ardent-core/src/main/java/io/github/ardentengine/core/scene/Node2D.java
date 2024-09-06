package io.github.ardentengine.core.scene;

import io.github.scalamath.vecmatlib.Mat2x3f;
import io.github.scalamath.vecmatlib.Vec2f;

import java.util.Objects;

/**
 * Base class for all 2D objects.
 * <p>
 *     Has a position, rotation, and scale.
 *     Controls the rendering order of its children using z index and y sort.
 * </p>
 */
public class Node2D extends Node {

    /** Position of this node relative to its parent. */
    private Vec2f position = Vec2f.Zero();
    /**Rotation in radians of this node relative to its parent. */
    private double rotation = 0.0;
    /** Scale of this node. */
    private Vec2f scale = Vec2f.One();

    // TODO: Skew

    /**
     * Controls the depth of this 2D node for rendering.
     * Nodes with a higher z index will be drawn in front of others.
     */
    private int zIndex = 0;

    // TODO: Y-Sort

    /** Cached local transform. */
    private Mat2x3f localTransform = null;
    /** Cached global transform. */
    private Mat2x3f globalTransform = null;

    /**
     * Private method used to invalidate this node's transform when its position, rotation, or scale are changed.
     * <p>
     *     Calling this method causes the local and global transform to be recomputed when the {@link Node2D#localTransform()} and {@link Node2D#globalTransform()} methods are called.
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
                if(child instanceof Node2D) {
                    ((Node2D) child).invalidateTransform();
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
     * Getter method for {@link Node2D#position)}.
     * Returns the position of this node relative to its parent.
     *
     * @return The position of this node relative to its parent.
     */
    public final Vec2f position() {
        return this.position;
    }

    /**
     * Setter method for {@link Node2D#position}.
     * Sets the position of this node relative to its parent.
     *
     * @param position Position of this node relative to its parent.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node2D#setPosition(float, float)
     */
    public final void setPosition(Vec2f position) {
        Objects.requireNonNull(position, "Position cannot be null");
        if(!this.position.equals(position)) {
            this.position = position;
            this.invalidateTransform();
        }
    }

    /**
     * Setter method for {@link Node2D#position}.
     * Sets the position of this node relative to its parent.
     *
     * @param x Position of this node on the x axis.
     * @param y Position of this node on the y axis.
     *
     * @see Node2D#setPosition(Vec2f)
     */
    public final void setPosition(float x, float y) {
        if(!this.position.equals(x, y)) {
            this.position = new Vec2f(x, y);
            this.invalidateTransform();
        }
    }

    /**
     * Translates this node by the given offset.
     *
     * @param offset The translation to apply.
     * @throws NullPointerException If the given offset is null.
     *
     * @see Node2D#translate(float, float)
     */
    public final void translate(Vec2f offset) {
        Objects.requireNonNull(offset, "Offset cannot be null");
        if(!offset.equals(0.0f, 0.0f)) {
            this.position = this.position.plus(offset);
            this.invalidateTransform();
        }
    }

    /**
     * Translates this node by the given offset.
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     *
     * @see Node2D#translate(Vec2f)
     */
    public final void translate(float x, float y) {
        if(x != 0.0f || y != 0.0f) {
            this.position = this.position.plus(x, y);
            this.invalidateTransform();
        }
    }

    /**
     * Getter method for {@link Node2D#rotation}.
     * Returns the rotation in radians of this node relative to its parent.
     * <p>
     *     Use {@link Node2D#rotationDegrees()} to use degrees instead of radians.
     * </p>
     *
     * @return The rotation in radians of this node relative to its parent.
     */
    public final double rotation() {
        return this.rotation;
    }

    /**
     * Setter method for {@link Node2D#rotation}.
     * Sets the rotation in radians of this node relative to its parent.
     * <p>
     *     Use {@link Node2D#setRotationDegrees(double)} to use degrees instead of radians.
     * </p>
     *
     * @param rotation Rotation in radians of this node relative to its parent.
     */
    public final void setRotation(double rotation) {
        if(this.rotation != rotation) {
            this.rotation = rotation;
            this.invalidateTransform();
        }
    }

    /**
     * Getter method for {@link Node2D#rotation} that uses degrees instead of radians.
     * Returns the rotation of this node relative to its parent.
     * <p>
     *     Use {@link Node2D#rotation()} to use radians instead of degrees.
     * </p>
     *
     * @return The rotation of this node relative to its parent.
     */
    public final double rotationDegrees() {
        return Math.toDegrees(this.rotation);
    }

    /**
     * Setter method for {@link Node2D#rotation} that uses degrees instead of radians.
     * Sets the rotation of this node relative to its parent.
     * <p>
     *     Use {@link Node2D#setRotation(double)} to use radians instead of degrees.
     * </p>
     *
     * @param angle Rotation of this node relative to its parent.
     */
    public final void setRotationDegrees(double angle) {
        this.setRotation(Math.toRadians(angle));
    }

    /**
     * Applies a rotation to the node starting from its current rotation.
     *
     * @param angle The rotation angle in radians.
     *
     * @see Node2D#rotateDegrees(double)
     */
    public final void rotate(double angle) {
        if(angle != 0.0) {
            this.rotation += angle;
            this.invalidateTransform();
        }
    }

    /**
     * Applies a rotation to the node starting from its current rotation.
     *
     * @param angle The rotation angle in degrees.
     *
     * @see Node2D#rotate(double)
     */
    public final void rotateDegrees(double angle) {
        this.rotate(Math.toRadians(angle));
    }

    /**
     * Getter method for {@link Node2D#scale}.
     * Returns the scale of this node.
     *
     * @return The scale of this node.
     */
    public final Vec2f scale() {
        return this.scale;
    }

    /**
     * Setter method for {@link Node2D#scale)}.
     * Sets the scale of this node.
     *
     * @param scale Scale of this node.
     * @throws NullPointerException If the given scale is null.
     *
     * @see Node2D#setScale(float, float)
     */
    public final void setScale(Vec2f scale) {
        Objects.requireNonNull(scale, "Scale cannot be null");
        if(!this.scale.equals(scale)) {
            this.scale = scale;
            this.invalidateTransform();
        }
    }

    /**
     * Setter method for {@link Node2D#scale}.
     * Sets the scale of this node.
     *
     * @param x Scale on the x axis.
     * @param y Scale on the y axis.
     *
     * @see Node2D#setScale(Vec2f)
     */
    public final void setScale(float x, float y) {
        if(!this.scale.equals(x, y)) {
            this.scale = new Vec2f(x, y);
            this.invalidateTransform();
        }
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param scale Scale ratio.
     * @throws NullPointerException If the given scale is null.
     *
     * @see Node2D#applyScale(float, float)
     */
    public final void applyScale(Vec2f scale) {
        Objects.requireNonNull(scale, "Scale cannot be null");
        if(!scale.equals(1.0f, 1.0f)) {
            this.scale = this.scale.multiply(scale);
            this.invalidateTransform();
        }
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param x Scale ratio on the x axis.
     * @param y Scale ratio on the y axis.
     *
     * @see Node2D#applyScale(Vec2f)
     */
    public final void applyScale(float x, float y) {
        if(x != 1.0f || y != 1.0f) {
            this.scale = this.scale.multiply(x, y);
            this.invalidateTransform();
        }
    }

    /**
     * Getter method for {@link Node2D#zIndex)}.
     * Returns the z index of this node.
     *
     * @return The z index of this node,
     */
    public final int zIndex() {
        return this.zIndex;
    }

    /**
     * Setter method for {@link Node2D#zIndex)}.
     * Sets the z index of this node.
     *
     * @param zIndex Z index of this node.
     */
    public final void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Returns this node's local transform as a 2x3 transformation matrix.
     *
     * @return This node's local transform as a 2x3 transformation matrix.
     */
    public final Mat2x3f localTransform() {
        if(this.localTransform != null) {
            return this.localTransform;
        }
        var sin = (float) Math.sin(this.rotation());
        var cos = (float) Math.cos(this.rotation());
        return this.localTransform = new Mat2x3f(
            cos * this.scale().x(), -sin * this.scale().y(), this.position().x(),
            sin * this.scale().x(), cos * this.scale().y(), this.position().y()
        );
    }

    /**
     * Returns this node's global transform as a 2x3 transformation matrix.
     * <p>
     *     If this node's parent is not a {@code Node2D}, this method is equivalent to {@link Node2D#localTransform()}.
     * </p>
     *
     * @return This node's global transform as a 2x3 transformation matrix.
     */
    public final Mat2x3f globalTransform() {
        if(this.globalTransform != null) {
            return this.globalTransform;
        } else if(this.parent() instanceof Node2D parent) {
            return this.globalTransform = parent.globalTransform().multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
        }
        return this.globalTransform = this.localTransform();
    }

    /**
     * Returns the global position of this node.
     *
     * @return The global position of this node.
     */
    public final Vec2f globalPosition() {
        return this.globalTransform().col2();
    }

    /**
     * Sets the global position of this node.
     * <p>
     *     Sets this node's {@link Node2D#position} to a value such that its {@link Node2D#globalPosition()} will be equal to the given value.
     * </p>
     *
     * @param x Global position on the x axis.
     * @param y Global position on the y axis.
     *
     * @see Node2D#setGlobalPosition(Vec2f)
     */
    public final void setGlobalPosition(float x, float y) {
        if(this.parent() instanceof Node2D parent) {
            this.setPosition(parent.globalTransform().affineInverse().multiply(x, y, 1.0f));
        } else {
            this.setPosition(x, y);
        }
    }

    /**
     * Sets the global position of this node.
     * <p>
     *     Sets the {@link Node2D#position} of this node to a value such that its {@link Node2D#globalPosition()} will be equal to the given value.
     * </p>
     *
     * @param position Global position of this node.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node2D#setGlobalPosition(float, float)
     */
    public final void setGlobalPosition(Vec2f position) {
        Objects.requireNonNull(position, "Position cannot be null");
        if(this.parent() instanceof Node2D parent) {
            this.setPosition(parent.globalTransform().affineInverse().multiply(position, 1.0f));
        } else {
            this.setPosition(position);
        }
    }

    /**
     * Returns the global rotation of this node in radians.
     * <p>
     *     Use {@link Node2D#globalRotationDegrees()} to get degrees instead of radians.
     * </p>
     *
     * @return The global rotation of this node in radians.
     */
    public final float globalRotation() {
        var transform = this.globalTransform();
        return (float) Math.atan2(transform.m10(), transform.m00());
    }

    /**
     * Returns the global rotation of this node in degrees.
     * <p>
     *     Use {@link Node2D#globalRotation()} to get radians instead of degrees.
     * </p>
     *
     * @return The global rotation of this node in degrees.
     */
    public final float globalRotationDegrees() {
        return (float) Math.toDegrees(this.globalRotation());
    }

    /**
     * Sets the global rotation of this node.
     * <p>
     *     Sets the {@link Node2D#rotation} of this node to a value such that its {@link Node2D#globalRotation()} will be equal to the given value.
     * </p>
     * <p>
     *     Use {@link Node2D#setGlobalRotationDegrees(float)} to use degrees instead of radians.
     * </p>
     *
     * @param rotation Global rotation in radians.
     */
    public final void setGlobalRotation(double rotation) {
        if(this.parent() instanceof Node2D parent) {
            // Get global transform
            var parentTransform = parent.globalTransform();
            var transform = parentTransform.multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
            // Set rotation
            var sin = (float) Math.sin(rotation);
            var cos = (float) Math.cos(rotation);
            var sx = transform.col0().length();
            var sy = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01()) * transform.col1().length();
            transform = new Mat2x3f(cos * sx, -sin * sy, transform.m02(), sin * sx, cos * sy, transform.m12());
            // Get local transform from global transform
            transform = parentTransform.affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f);
            // Set rotation
            this.setRotation(Math.atan2(transform.m10(), transform.m00()));
        } else {
            this.setRotation(rotation);
        }
    }

    /**
     * Sets the global rotation of this node.
     * <p>
     *     Sets the {@link Node2D#rotation} of this node to a value such that its {@link Node2D#globalRotation()} will be equal to the given value.
     * </p>
     * <p>
     *     Use {@link Node2D#setGlobalRotation(double)} to use radians instead of degrees.
     * </p>
     *
     * @param rotation Global rotation in degrees.
     */
    public final void setGlobalRotationDegrees(float rotation) {
        this.setGlobalRotation(Math.toRadians(rotation));
    }

    /**
     * Returns the global scale of this node.
     *
     * @return The global scale of this node.
     */
    public final Vec2f globalScale() {
        var transform = this.globalTransform();
        var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
        return new Vec2f(transform.col0().length(), sign * transform.col1().length());
    }

    /**
     * Sets the global scale of this node.
     * <p>
     *     Sets the {@link Node2D#scale} of this node to a value such that its {@link Node2D#globalScale()} will be equal to the given value.
     * </p>
     *
     * @param x Global scale on the x axis.
     * @param y Global scale on the y axis.
     *
     * @see Node2D#setGlobalScale(Vec2f)
     */
    public final void setGlobalScale(float x, float y) {
        if(this.parent() instanceof Node2D parent) {
            // Get global transform
            var parentTransform = parent.globalTransform();
            var transform = parentTransform.multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
            // Set scale
            transform = Mat2x3f.fromColumns(
                transform.col0().normalized().multipliedBy(x),
                transform.col1().normalized().multipliedBy(y),
                transform.col2()
            );
            // Get local transform from global transform
            transform = parentTransform.affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f);
            // Set scale
            var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
            this.setScale(transform.col0().length(), sign * transform.col1().length());
        } else {
            this.setScale(x, y);
        }
    }

    /**
     * Sets the global scale of this node.
     * <p>
     *     Sets the {@link Node2D#scale} of this node to a value such that its {@link Node2D#globalScale()} will be equal to the given value.
     * </p>
     *
     * @param scale Global scale.
     * @throws NullPointerException If the given scale is null.
     *
     * @see Node2D#setGlobalScale(float, float)
     */
    public final void setGlobalScale(Vec2f scale) {
        Objects.requireNonNull(scale, "Scale cannot be null");
        this.setGlobalScale(scale.x(), scale.y());
    }

    /**
     * Sets the local transform of this node.
     * <p>
     *     Sets the {@link Node2D#position}, {@link Node2D#rotation}, and {@link Node2D#scale} of this node so that its {@link Node2D#localTransform()} will be equal to the given transform.
     * </p>
     *
     * @param transform Local transform as a 2x3 transformation matrix.
     * @throws NullPointerException If the given transform is null.
     */
    public final void setLocalTransform(Mat2x3f transform) {
        Objects.requireNonNull(transform, "Transform cannot be null");
        this.setPosition(transform.m02(), transform.m12());
        this.setRotation(Math.atan2(transform.m10(), transform.m00()));
        var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
        this.setScale(transform.col0().length(), sign * transform.col1().length());
    }

    /**
     * Sets the global transform of this node.
     * <p>
     *     Sets the {@link Node2D#position}, {@link Node2D#rotation}, and {@link Node2D#scale} of this node so that its {@link Node2D#globalTransform()} will be equal to the given transform.
     * </p>
     * <p>
     *     If this node's parent is not a {@code Node2D}, this method is equivalent to {@link Node2D#setLocalTransform(Mat2x3f)}.
     * </p>
     *
     * @param transform Global transform as a 2x3 transformation matrix.
     * @throws NullPointerException If the given transform is null.
     */
    public final void setGlobalTransform(Mat2x3f transform) {
        Objects.requireNonNull(transform, "Transform cannot be null");
        if(this.parent() instanceof Node2D parent) {
            this.setLocalTransform(parent.globalTransform().affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f));
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
     * @see Node2D#toLocal(float, float)
     */
    public final Vec2f toLocal(Vec2f globalPosition) {
        Objects.requireNonNull(globalPosition, "Position cannot be null");
        return this.globalTransform().affineInverse().multiply(globalPosition, 1.0f);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param x Position in global coordinates on the x axis.
     * @param y Position in global coordinates on the y axis,
     * @return The given global position in local coordinates relative to this node.
     *
     * @see Node2D#toLocal(Vec2f)
     */
    public final Vec2f toLocal(float x, float y) {
        return this.globalTransform().affineInverse().multiply(x, y, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param localPosition Position in local coordinates relative to this node.
     * @return The given local position relative to this node in global coordinates.
     * @throws NullPointerException If the given position is null.
     *
     * @see Node2D#toGlobal(float, float)
     */
    public final Vec2f toGlobal(Vec2f localPosition) {
        Objects.requireNonNull(localPosition, "Position cannot be null");
        return this.globalTransform().multiply(localPosition, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param x Position in local coordinates on the x axis relative to this node.
     * @param y Position in local coordinates on the y axis relative to this node.
     * @return The given local position relative to this node in global coordinates.
     *
     * @see Node2D#toGlobal(Vec2f)
     */
    public final Vec2f toGlobal(float x, float y) {
        return this.globalTransform().multiply(x, y, 1.0f);
    }

    /**
     * Returns the angle between this node and the given point in radians.
     *
     * @param position Second position.
     * @return The angle between this node and the given point in radians.
     * @throws NullPointerException If the given position is null.
     */
    public final double angleTo(Vec2f position) {
        Objects.requireNonNull(position, "Position cannot be null");
        return this.toLocal(position).multiply(this.scale()).angle();
    }

    /**
     * Returns the angle between this node and the given point in radians.
     *
     * @param x Position on the x axis.
     * @param y Position on the y axis.
     * @return The angle between this node and the given point in radians.
     */
    public final double angleTo(float x, float y) {
        return this.toLocal(x, y).multiply(this.scale()).angle();
    }

    /**
     * Rotates this node so that it points towards the given position in global coordinates.
     *
     * @param position The point this node should look at in global coordinates.
     * @throws NullPointerException If the given position is null.
     */
    public final void lookAt(Vec2f position) {
        Objects.requireNonNull(position, "Position cannot be null");
        this.rotate(this.angleTo(position));
    }

    /**
     * Rotates this node so that it points towards the given position in global coordinates.
     *
     * @param x Position on the x axis.
     * @param y Position on the y axis.
     */
    public final void lookAt(float x, float y) {
        this.rotate(this.angleTo(x, y));
    }
}