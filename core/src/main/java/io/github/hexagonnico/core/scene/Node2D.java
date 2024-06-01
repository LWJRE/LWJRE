package io.github.hexagonnico.core.scene;

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
     * @see Node2D#rotate(float)
     */
    public float rotation = 0.0f;
    /**
     * Scale of this node.
     */
    public Vec2f scale = Vec2f.One();

    public int zIndex = 0;

    // TODO: Y-Sort

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
     */
    public final void rotate(float angle) {
        this.rotation += angle;
    }

    /**
     * Getter method for {@link Node2D#rotation} that uses degrees instead of radians.
     *
     * @return The rotation of this node in degrees.
     */
    public final float rotationDegrees() {
        return (float) Math.toDegrees(this.rotation);
    }

    /**
     * Setter method for {@link Node2D#rotation} that uses degrees instead of radians.
     *
     * @param angle The rotation angle in degrees.
     */
    public final void setRotationDegrees(float angle) {
        this.rotation = (float) Math.toRadians(angle);
    }

    public final void applyScale(Vec2f scale) {
        this.scale = this.scale.multiply(scale);
    }

    /**
     * Returns this node's local transform as a 2x3 transformation matrix.
     *
     * @return This node's local transform as a 2x3 transformation matrix.
     */
    public final Mat2x3f localTransform() {
        return Mat2x3f.translation(this.position)
            .multiply(Mat2x3f.rotation(this.rotation), 0.0f, 0.0f, 1.0f)
            .multiply(Mat2x3f.scaling(this.scale), 0.0f, 0.0f, 1.0f);
    }

    // TODO: Cache global transform until parent is updated

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
     * Returns the global positions of this node.
     *
     * @return The global positions of this node.
     */
    public final Vec2f globalPosition() {
        return this.globalTransform().col2();
    }

    /**
     * Sets this node's position to a value such that its {@link Node2D#globalPosition()} will be equal to the given value.
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

    public final float globalRotation() {
        Mat2x3f transform = this.globalTransform();
        return (float) Math.atan2(transform.m10(), transform.m00());
    }

    public final float globalRotationDegrees() {
        return (float) Math.toDegrees(this.globalRotation());
    }

    // TODO: Set global rotation

    // TODO: Global scale

    // TODO: Reparent keeping global transform

    // TODO: Look at
}
