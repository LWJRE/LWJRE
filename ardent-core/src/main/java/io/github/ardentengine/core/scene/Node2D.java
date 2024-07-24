package io.github.ardentengine.core.scene;

import io.github.scalamath.vecmatlib.Mat2x3f;
import io.github.scalamath.vecmatlib.Vec2f;

/**
 * Base class for all 2D objects.
 * Has a position, rotation, and scale.
 * Controls the rendering order of its children using z index and y sort.
 */
public class Node2D extends Node {

    /**
     * Position relative to this node's parent.
     *
     * @see Node2D#translate(Vec2f)
     * @see Node2D#globalPosition()
     */
    public Vec2f position = Vec2f.Zero();
    /**
     * Rotation in radians relative to this node's parent.
     *
     * @see Node2D#rotationDegrees()
     * @see Node2D#rotate(double)
     */
    public double rotation = 0.0;
    /**
     * Scale of this node.
     */
    public Vec2f scale = Vec2f.One();

    /**
     * Controls the depth of this 2D node for rendering.
     * Nodes with a higher z index will be drawn in front of others.
     */
    public int zIndex = 0;

    // TODO: Y-Sort

    // TODO: Visibility, light mask

    /**
     * Translates this node by the given offset.
     *
     * @param offset The translation to apply.
     */
    public final void translate(Vec2f offset) {
        this.position = this.position.plus(offset);
    }

    /**
     * Translates this node by the given offset.
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     */
    public final void translate(float x, float y) {
        this.position = this.position.plus(x, y);
    }

    /**
     * Applies a rotation to the node starting from its current rotation.
     *
     * @param angle The rotation angle in radians.
     *
     * @see Node2D#rotateDegrees(double)
     */
    public final void rotate(double angle) {
        this.rotation += angle;
    }

    /**
     * Applies a rotation to the node starting from its current rotation.
     *
     * @param degrees The rotation angle in degrees.
     *
     * @see Node2D#rotate(double)
     */
    public final void rotateDegrees(double degrees) {
        this.rotation += Math.toRadians(degrees);
    }

    /**
     * Getter method for {@link Node2D#rotation} that uses degrees instead of radians.
     *
     * @return The rotation of this node in degrees.
     *
     * @see Node2D#setRotationDegrees(double)
     */
    public final double rotationDegrees() {
        return Math.toDegrees(this.rotation);
    }

    /**
     * Setter method for {@link Node2D#rotation} that uses degrees instead of radians.
     *
     * @param angle The rotation angle in degrees.
     *
     * @see Node2D#rotationDegrees()
     */
    public final void setRotationDegrees(double angle) {
        this.rotation = Math.toRadians(angle);
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param x Scale ratio on the x axis.
     * @param y Scale ratio on the y axis.
     */
    public final void applyScale(float x, float y) {
        this.scale = this.scale.multiply(x, y);
    }

    /**
     * Multiplies this node's scale by the given value.
     *
     * @param scale Scale ratio.
     */
    public final void applyScale(Vec2f scale) {
        this.scale = this.scale.multiply(scale);
    }

    /**
     * Returns this node's local transform as a 2x3 transformation matrix.
     *
     * @return This node's local transform as a 2x3 transformation matrix.
     */
    public final Mat2x3f localTransform() {
        var sin = (float) Math.sin(this.rotation);
        var cos = (float) Math.cos(this.rotation);
        return new Mat2x3f(
            cos * this.scale.x(), -sin * this.scale.y(), this.position.x(),
            sin * this.scale.x(), cos * this.scale.y(), this.position.y()
        );
    }

    /**
     * Returns this node's global transform as a 2x3 transformation matrix.
     *
     * @return This node's global transform as a 2x3 transformation matrix.
     */
    public final Mat2x3f globalTransform() {
        if(this.getParent() instanceof Node2D parent) {
            return parent.globalTransform().multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
        }
        return this.localTransform();
    }

    /**
     * Returns the global position of this node.
     *
     * @return The global position of this node.
     *
     * @see Node2D#setGlobalPosition(Vec2f)
     */
    public final Vec2f globalPosition() {
        return this.globalTransform().col2();
    }

    /**
     * Sets this node's {@link Node2D#position} to a value such that its {@link Node2D#globalPosition()} will be equal to the given value.
     *
     * @param position The new global position.
     */
    public final void setGlobalPosition(Vec2f position) {
        if(this.getParent() instanceof Node2D parent) {
            this.position = parent.globalTransform().affineInverse().multiply(position, 1.0f);
        } else {
            this.position = position;
        }
    }

    /**
     * Sets this node's {@link Node2D#position} to a value such that its {@link Node2D#globalPosition()} will be equal to the given value.
     *
     * @param x X position.
     * @param y Y position.
     */
    public final void setGlobalPosition(float x, float y) {
        if(this.getParent() instanceof Node2D parent) {
            this.position = parent.globalTransform().affineInverse().multiply(x, y, 1.0f);
        } else {
            this.position = new Vec2f(x, y);
        }
    }

    /**
     * Returns the global rotation of this node in radians.
     *
     * @return The global rotation of this node in radians.
     *
     * @see Node2D#setGlobalRotation(double)
     */
    public final double globalRotation() {
        var transform = this.globalTransform();
        return Math.atan2(transform.m10(), transform.m00());
    }

    /**
     * Returns the global rotation of this node in degrees.
     *
     * @return The global rotation of this node in degrees.
     *
     * @see Node2D#setGlobalRotationDegrees(double)
     */
    public final double globalRotationDegrees() {
        return Math.toDegrees(this.globalRotation());
    }

    /**
     * Sets this node's {@link Node2D#rotation} to a value such that its {@link Node2D#globalRotation()} will be equal to the given value.
     *
     * @param rotation The new global rotation in radians.
     *
     * @see Node2D#setGlobalRotationDegrees(double)
     */
    public final void setGlobalRotation(double rotation) {
        if(this.getParent() instanceof Node2D parent) {
            // Get global transform
            var parentTransform = parent.globalTransform();
            var transform = parentTransform.multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
            // Set rotation
            // TODO: Check if this is equivalent to a transform with global position, given global rotation, and global scale
            var sin = (float) Math.sin(rotation);
            var cos = (float) Math.cos(rotation);
            var sx = transform.col0().length();
            var sy = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01()) * transform.col1().length();
            transform = new Mat2x3f(cos * sx, -sin * sy, transform.m02(), sin * sx, cos * sy, transform.m12());
            // Get local transform from global transform
            transform = parentTransform.affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f);
            // Set rotation
            this.rotation = Math.atan2(transform.m10(), transform.m00());
        } else {
            this.rotation = rotation;
        }
    }

    /**
     * Sets this node's {@link Node2D#rotation} to a value such that its {@link Node2D#globalRotationDegrees()} will be equal to the given value.
     *
     * @param rotation The new global rotation in degrees.
     *
     * @see Node2D#setGlobalRotation(double)
     */
    public final void setGlobalRotationDegrees(double rotation) {
        this.setGlobalRotation(Math.toRadians(rotation));
    }

    /**
     * Returns the global scale of this node.
     *
     * @return The global scale of this node.
     *
     * @see Node2D#setGlobalScale(Vec2f)
     */
    public final Vec2f globalScale() {
        var transform = this.globalTransform();
        var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
        return new Vec2f(transform.col0().length(), sign * transform.col1().length());
    }

    /**
     * Sets this node's {@link Node2D#scale} to a value such that its {@link Node2D#globalScale()} will be equal to the given value.
     *
     * @param scale The new global scale.
     */
    public final void setGlobalScale(Vec2f scale) {
        if(this.getParent() instanceof Node2D parent) {
            // Get global transform
            var parentTransform = parent.globalTransform();
            var transform = parentTransform.multiply(this.localTransform(), 0.0f, 0.0f, 1.0f);
            // Set scale
            transform = Mat2x3f.fromColumns(
                transform.col0().normalized().multipliedBy(scale.x()),
                transform.col1().normalized().multipliedBy(scale.y()),
                transform.col2()
            );
            // Get local transform from global transform
            transform = parentTransform.affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f);
            // Set scale
            var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
            this.scale = new Vec2f(transform.col0().length(), sign * transform.col1().length());
        } else {
            this.scale = scale;
        }
    }

    /**
     * Sets this node's {@link Node2D#scale} to a value such that its {@link Node2D#globalScale()} will be equal to the given value.
     *
     * @param x The new global scale on the x axis.
     * @param y The new global scale on the y axis.
     */
    public final void setGlobalScale(float x, float y) {
        this.setGlobalScale(new Vec2f(x, y));
    }

    /**
     * Sets this node's {@link Node2D#position}, {@link Node2D#rotation}, and {@link Node2D#scale} so that its {@link Node2D#localTransform()} will be equal to the given transform.
     *
     * @param transform The new local transform as a 2x3 transformation matrix.
     *
     * @see Node2D#setGlobalTransform(Mat2x3f)
     */
    public final void setLocalTransform(Mat2x3f transform) {
        this.position = transform.col2();
        this.rotation = Math.atan2(transform.m10(), transform.m00());
        var sign = Math.signum(transform.m00() * transform.m11() - transform.m10() * transform.m01());
        this.scale = new Vec2f(transform.col0().length(), sign * transform.col1().length());
    }

    /**
     * Sets this node's {@link Node2D#position}, {@link Node2D#rotation}, and {@link Node2D#scale} so that its {@link Node2D#globalTransform()} will be equal to the given transform.
     *
     * @param transform The new global transform as a 2x3 transformation matrix.
     *
     * @see Node2D#setLocalTransform(Mat2x3f)
     */
    public final void setGlobalTransform(Mat2x3f transform) {
        if(this.getParent() instanceof Node2D parent) {
            this.setLocalTransform(parent.globalTransform().affineInverse().multiply(transform, 0.0f, 0.0f, 1.0f));
        } else {
            this.setLocalTransform(transform);
        }
    }

    /**
     * Sets this node's parent and keeps its global transform.
     *
     * @param parent This node's new parent or null to remove this node from its parent.
     *
     * @see Node#setParent(Node)
     */
    public final void setParentKeepTransform(Node parent) {
        var transform = this.globalTransform();
        this.setParent(parent);
        this.setGlobalTransform(transform);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param globalPosition The global position.
     * @return The given global position in local coordinates relative to this node.
     *
     * @see Node2D#toGlobal(Vec2f)
     */
    public final Vec2f toLocal(Vec2f globalPosition) {
        return this.globalTransform().affineInverse().multiply(globalPosition, 1.0f);
    }

    /**
     * Transforms the given global position into a position in local coordinates relative to this node.
     *
     * @param x The global position on the x axis.
     * @param y The global position on the y axis,
     * @return The given global position in local coordinates relative to this node.
     *
     * @see Node2D#toGlobal(float, float)
     */
    public final Vec2f toLocal(float x, float y) {
        return this.globalTransform().affineInverse().multiply(x, y, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param localPosition The local position relative to this node.
     * @return The given local position relative to this node in global coordinates.
     *
     * @see Node2D#toLocal(Vec2f)
     */
    public final Vec2f toGlobal(Vec2f localPosition) {
        return this.globalTransform().multiply(localPosition, 1.0f);
    }

    /**
     * Transforms the given local position relative to this node into a position in global coordinates.
     *
     * @param x Local position on the x axis relative to this node.
     * @param y Local position on the y axis relative to this node.
     * @return The given local position relative to this node in global coordinates.
     *
     * @see Node2D#toLocal(float, float)
     */
    public final Vec2f toGlobal(float x, float y) {
        return this.globalTransform().multiply(x, y, 1.0f);
    }

    /**
     * Returns the angle between this node and the given point in radians.
     *
     * @param position The second position.
     * @return The angle between this node and the given point in radians.
     */
    public final double angleTo(Vec2f position) {
        return this.toLocal(position).multiply(this.scale).angle();
    }

    /**
     * Returns the angle between this node and the given point in radians.
     *
     * @param x Position on the x axis.
     * @param y Position on the y axis.
     * @return The angle between this node and the given point in radians.
     */
    public final double angleTo(float x, float y) {
        return this.toLocal(x, y).multiply(this.scale).angle();
    }

    /**
     * Rotates this node so that it points towards the given position in global coordinates.
     *
     * @param position The point this node should look at in global coordinates.
     *
     * @see Node2D#rotate(double)
     * @see Node2D#angleTo(Vec2f)
     */
    public final void lookAt(Vec2f position) {
        this.rotate(this.angleTo(position));
    }

    /**
     * Rotates this node so that it points towards the given position in global coordinates.
     *
     * @param x Position on the x axis.
     * @param y Position on the y axis.
     *
     * @see Node2D#rotate(double)
     * @see Node2D#angleTo(float, float)
     */
    public final void lookAt(float x, float y) {
        this.rotate(this.angleTo(x, y));
    }

    // TODO: Mouse position
}