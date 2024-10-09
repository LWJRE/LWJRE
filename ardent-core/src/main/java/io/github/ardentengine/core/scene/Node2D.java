package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.math.Matrix2x3;
import io.github.ardentengine.core.math.Vector2;

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
    private Vector2 position = Vector2.ZERO;
    /**Rotation in radians of this node relative to its parent. */
    private double rotation = 0.0;
    /** Scale of this node. */
    private Vector2 scale = Vector2.ONE;

    // TODO: Skew

    /**
     * Controls the depth of this 2D node for rendering.
     * Nodes with a higher z index will be drawn in front of others.
     */
    private int zIndex = 0;

    // TODO: Y-Sort

    /** Cached local transform. */
    private Matrix2x3 localTransform = null;
    /** Cached global transform. */
    private Matrix2x3 globalTransform = null;

    @Override
    void exitTree() {
        this.localTransform = null;
        this.globalTransform = null;
        super.exitTree();
    }

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
            for(var child : this.children()) {
                if(child instanceof Node2D) {
                    ((Node2D) child).invalidateTransform();
                }
            }
        }
    }

    /**
     * Getter method for {@link Node2D#position)}.
     * Returns the position of this node relative to its parent.
     *
     * @return The position of this node relative to its parent.
     */
    public final Vector2 position() {
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
    public final void setPosition(Vector2 position) {
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
     * @see Node2D#setPosition(Vector2)
     */
    public final void setPosition(float x, float y) {
        if(!this.position.equals(x, y)) {
            this.position = new Vector2(x, y);
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
    public final void translate(Vector2 offset) {
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
     * @see Node2D#translate(Vector2)
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
    public final Vector2 scale() {
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
    public final void setScale(Vector2 scale) {
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
     * @see Node2D#setScale(Vector2)
     */
    public final void setScale(float x, float y) {
        if(!this.scale.equals(x, y)) {
            this.scale = new Vector2(x, y);
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
    public final void applyScale(Vector2 scale) {
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
     * @see Node2D#applyScale(Vector2)
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
    public final Matrix2x3 localTransform() {
        if(this.localTransform != null) {
            return this.localTransform;
        }
        return this.localTransform = Matrix2x3.transformation(this.position(), this.rotation(), this.scale());
    }

    /**
     * Returns this node's global transform as a 2x3 transformation matrix.
     * <p>
     *     If this node's parent is not a {@code Node2D}, this method is equivalent to {@link Node2D#localTransform()}.
     * </p>
     *
     * @return This node's global transform as a 2x3 transformation matrix.
     */
    public final Matrix2x3 globalTransform() {
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
    public final Vector2 globalPosition() {
        return this.globalTransform().column2();
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
     * @see Node2D#setGlobalPosition(Vector2)
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
    public final void setGlobalPosition(Vector2 position) {
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
            var sx = transform.column0().length();
            var sy = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01()) * transform.column1().length();
            transform = new Matrix2x3(cos * sx, -sin * sy, transform.m02(), sin * sx, cos * sy, transform.m12());
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
    public final Vector2 globalScale() {
        var transform = this.globalTransform();
        var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
        return new Vector2(transform.column0().length(), sign * transform.column1().length());
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
     * @see Node2D#setGlobalScale(Vector2)
     */
    public final void setGlobalScale(float x, float y) {
        if(this.parent() instanceof Node2D parent) {
            // Get global transform
            var parentTransform = parent.globalTransform();
            var transform = parentTransform.multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
            // Set scale
            transform = Matrix2x3.fromColumns(
                transform.column0().normalized().multiply(x),
                transform.column1().normalized().multiply(y),
                transform.column2()
            );
            // Get local transform from global transform
            transform = parentTransform.affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f);
            // Set scale
            var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
            this.setScale(transform.column0().length(), sign * transform.column1().length());
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
    public final void setGlobalScale(Vector2 scale) {
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
    public final void setLocalTransform(Matrix2x3 transform) {
        Objects.requireNonNull(transform, "Transform cannot be null");
        this.setPosition(transform.m02(), transform.m12());
        this.setRotation(Math.atan2(transform.m10(), transform.m00()));
        var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
        this.setScale(transform.column0().length(), sign * transform.column1().length());
    }

    /**
     * Sets the global transform of this node.
     * <p>
     *     Sets the {@link Node2D#position}, {@link Node2D#rotation}, and {@link Node2D#scale} of this node so that its {@link Node2D#globalTransform()} will be equal to the given transform.
     * </p>
     * <p>
     *     If this node's parent is not a {@code Node2D}, this method is equivalent to {@link Node2D#setLocalTransform(Matrix2x3)}.
     * </p>
     *
     * @param transform Global transform as a 2x3 transformation matrix.
     * @throws NullPointerException If the given transform is null.
     */
    public final void setGlobalTransform(Matrix2x3 transform) {
        Objects.requireNonNull(transform, "Transform cannot be null");
        if(this.parent() instanceof Node2D parent) {
            this.setLocalTransform(parent.globalTransform().affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f));
        } else {
            this.setLocalTransform(transform);
        }
    }

    /**
     * Sets the parent of this node.
     * <p>
     *     This method is equivalent to removing the node from the scene and adding it again to the given parent.
     *     Calls {@link Node#removeFromTree()} and {@link Node#addChild(Node)} in this order.
     * </p>
     * <p>
     *     If {@code keepTransform} is true, this node will preserve its global transform.
     * </p>
     *
     * @param parent The new parent of this node.
     * @param keepTransform True to preserve the global transform.
     * @throws NullPointerException If the given parent is null.
     * @throws IllegalArgumentException If this node cannot be added as a child of the given node.
     */
    public final void setParent(Node parent, boolean keepTransform) {
        if(keepTransform) {
            var transform = this.globalTransform();
            this.setParent(parent);
            this.setGlobalTransform(transform);
        } else {
            this.setParent(parent);
        }
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
    public final Vector2 toLocal(Vector2 globalPosition) {
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
     * @see Node2D#toLocal(Vector2)
     */
    public final Vector2 toLocal(float x, float y) {
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
    public final Vector2 toGlobal(Vector2 localPosition) {
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
     * @see Node2D#toGlobal(Vector2)
     */
    public final Vector2 toGlobal(float x, float y) {
        return this.globalTransform().multiply(x, y, 1.0f);
    }

    /**
     * Returns the angle between this node and the given point in radians.
     *
     * @param position Second position.
     * @return The angle between this node and the given point in radians.
     * @throws NullPointerException If the given position is null.
     */
    public final double angleTo(Vector2 position) {
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
    public final void lookAt(Vector2 position) {
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