package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 2D vector using integer coordinates.
 *
 * @param x The vector's x component.
 * @param y The vector's y component.
 */
public record Vector2i(int x, int y) implements Serializable {

    /** Shorthand for {@code new Vector2(0, 0)} */
    public static final Vector2i ZERO = new Vector2i(0, 0);
    /** Shorthand for {@code new Vector2(1, 1)} */
    public static final Vector2i ONE = new Vector2i(1, 1);
    /** Shorthand for {@code new Vector2(0, 1)} */
    public static final Vector2i UP = new Vector2i(0, 1);
    /** Shorthand for {@code new Vector2(0, -1)} */
    public static final Vector2i DOWN = new Vector2i(0, -1);
    /** Shorthand for {@code new Vector2(-1, 0)} */
    public static final Vector2i LEFT = new Vector2i(-1, 0);
    /** Shorthand for {@code new Vector2(1, 0)} */
    public static final Vector2i RIGHT = new Vector2i(1, 0);

    /**
     * Adds the given values to the components of this vector and returns the result.
     *
     * @param x The x component to add.
     * @param y The y component to add.
     * @return The sum between this vector and the given values.
     */
    public Vector2i plus(int x, int y) {
        return new Vector2i(this.x() + x, this.y() + y);
    }

    /**
     * Adds the given vector to this one and returns the result.
     *
     * @param v The vector to add.
     * @return The sum between this vector and the given one.
     */
    public Vector2i plus(Vector2i v) {
        return this.plus(v.x(), v.y());
    }

    /**
     * Subtracts the given values from each component of this vector and returns the result.
     *
     * @param x The x component to subtract.
     * @param y The y component to subtract.
     * @return The subtraction between this vector and the given values.
     */
    public Vector2i minus(int x, int y) {
        return new Vector2i(this.x() - x, this.y() - y);
    }

    /**
     * Subtracts the given vector from this one and returns the result.
     *
     * @param v The vector to subtract.
     * @return The subtraction between this vector and the given one.
     */
    public Vector2i minus(Vector2i v) {
        return this.minus(v.x(), v.y());
    }

    /**
     * Returns the additive inverse of this vector.
     *
     * @return The additive inverse of this vector.
     */
    public Vector2i negated() {
        return new Vector2i(-this.x(), -this.y());
    }

    /**
     * Multiplies each component of this vector by the given values and returns the result.
     *
     * @param x Value to multiply the x component by.
     * @param y Value to multiply the y component by.
     * @return The component-wise multiplication between this vector and the given values.
     */
    public Vector2i multiply(int x, int y) {
        return new Vector2i(this.x() * x, this.y() * y);
    }

    /**
     * Multiplies each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to multiply this one by.
     * @return The component-wise multiplication between this vector and the one.
     */
    public Vector2i multiply(Vector2i v) {
        return this.multiply(v.x(), v.y());
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this vector by.
     * @return The product between this vector and the given scalar.
     */
    public Vector2i multiply(int k) {
        return this.multiply(k, k);
    }

    /**
     * Divides each component of this vector by the given values and returns the result.
     * This method performs an integer division.
     *
     * @param x The value to divide the x component by.
     * @param y The value to divide the y component by.
     * @return The component-wise division between this vector and the given values.
     */
    public Vector2i divide(int x, int y) {
        return new Vector2i(this.x() / x, this.y() / y);
    }

    /**
     * Divides each component of this vector by the components of the given ones and returns the result.
     * This method performs an integer division.
     *
     * @param v The vector to divide this one by.
     * @return The component-wise division between this vector and the one.
     */
    public Vector2i divide(Vector2i v) {
        return this.divide(v.x(), v.y());
    }

    /**
     * Divides this vector by the given scalar and returns the result.
     * This method performs an integer division.
     *
     * @param k The scalar to divide this vector by.
     * @return The division between this vector and the given scalar.
     */
    public Vector2i divide(int k) {
        return this.divide(k, k);
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given values.
     *
     * @param x The x component of the vector to multiply this one by.
     * @param y The y component of the vector to multiply this one by.
     * @return The dot product between this vector and the given values.
     */
    public int dot(int x, int y) {
        return this.x() * x + this.y() * y;
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The dot product between this vector and the given one.
     */
    public int dot(Vector2i v) {
        return this.dot(v.x(), v.y());
    }

    /**
     * Returns the squared length, or magnitude, or modulus, of this vector.
     * <p>
     *     When comparing vectors by their length, it is more efficient to compare them by their squared length, since computing it does not require computing a square root.
     * </p>
     *
     * @return The squared length of this vector.
     * @see Vector2i#length()
     */
    public int lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or magnitude, or modulus, of this vector.
     *
     * @return The length of this vector.
     * @see Vector2i#lengthSquared()
     */
    public float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    /**
     * Checks if this vector is a unit vector by checking if its length is equal to one.
     *
     * @return True if this vector is a unit vector, otherwise false.
     */
    public boolean isNormalized() {
        return this.lengthSquared() == 1;
    }

    /**
     * Returns a vector with all components in absolute value.
     * <p>
     *     Not to be confused with the modulus, or {@link Vector2i#length()}, of a vector.
     * </p>
     *
     * @return A vector with all components in absolute value.
     */
    public Vector2i abs() {
        return new Vector2i(Math.abs(this.x()), Math.abs(this.y()));
    }

    /**
     * Returns a vector with the signs of the components of this vector.
     *
     * @return A vector with all components representing the sign of this vector.
     */
    public Vector2i sign() {
        return new Vector2i(Integer.compare(this.x(), 0), Integer.compare(this.y(), 0));
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The squared distance between this vector and the one with the given components.
     * @see Vector2i#distanceSquaredTo(Vector2i)
     */
    public int distanceSquaredTo(int x, int y) {
        x = x - this.x();
        y = y - this.y();
        return x * x + y * y;
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     * <p>
     *     This method is equivalent to {@code b.minus(a).lengthSquared()}.
     * </p>
     *
     * @param v The second vector.
     * @return The squared distance between this vector and the given one.
     * @see Vector2i#lengthSquared()
     */
    public int distanceSquaredTo(Vector2i v) {
        return this.distanceSquaredTo(v.x(), v.y());
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The squared between this vector and the one with the given components.
     * @see Vector2i#distanceTo(Vector2i)
     */
    public float distanceTo(int x, int y) {
        return (float) Math.sqrt(this.distanceSquaredTo(x, y));
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     * <p>
     *     This method is equivalent to {@code b.minus(a).length()}.
     * </p>
     *
     * @param v The second vector.
     * @return The squared between this vector and the given one.
     * @see Vector2i#length()
     */
    public float distanceTo(Vector2i v) {
        return this.distanceTo(v.x(), v.y());
    }

    /**
     * Returns the angle in radians between this vector and the x axis.
     *
     * @return The angle in radians between this vector and the x axis.
     */
    public double angle() {
        return Math.atan2(this.y(), this.x());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given values.
     *
     * @param x The x component.
     * @param y The y component.
     * @return A vector composed by the reminder of the division between this vector's components and the given values.
     */
    public Vector2i mod(int x, int y) {
        return new Vector2i(this.x() % x, this.y() % y);
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the components of the given vector.
     *
     * @param v The second vector.
     * @return A vector composed by the reminder of the division between this vector's components and the components of the given vector.
     */
    public Vector2i mod(Vector2i v) {
        return this.mod(v.x(), v.y());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given scalar.
     *
     * @param k The scalar.
     * @return A vector composed by the reminder of the division between this vector's components and the given scalar.
     */
    public Vector2i mod(int k) {
        return this.mod(k, k);
    }

    /**
     * Returns the aspect ratio of this vector.
     * <p>
     *     This method is the equivalent of {@code v.x() / v.y()}.
     * </p>
     *
     * @return The aspect ratio of this vector.
     */
    public float aspect() {
        return (float) this.x() / this.y();
    }

    /**
     * Returns a perpendicular vector rotated 90 degrees counter-clockwise compared to the original, with the same length.
     *
     * @return A vector perpendicular to this one with the same length.
     */
    public Vector2i orthogonal() {
        return new Vector2i(this.y(), -this.x());
    }

    /**
     * Returns the cross product, or vector product, between this vector and the vector defined by the given components.
     * <p>
     *     Since the cross product is only defined in three dimensions, this vector is considered to be laying on the xy plane, i.e. its z component is considered to be zero.
     * </p>
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The cross product between this vector and the vector defined by the given components.
     */
    public Vector3i cross(int x, int y, int z) {
        return new Vector3i(this.y() * z, z * this.x(), this.x() * y - this.y() * x);
    }

    /**
     * Returns the cross product, or vector product, between this vector and the given one.
     * <p>
     *     Since the cross product is only defined in three dimensions, this vector is considered to be laying on the xy plane, i.e. its z component is considered to be zero.
     * </p>
     *
     * @param v The second vector.
     * @return The cross product between this vector and the given one.
     */
    public Vector3i cross(Vector3i v) {
        return this.cross(v.x(), v.y(), v.z());
    }

    /**
     * Returns the cross product, or vector product, between this vector and the given one.
     * <p>
     *     Since the cross product is only defined in three dimensions, the two vectors are considered to be laying on the xy plane, i.e. their z component is considered to be zero.
     * </p>
     *
     * @param v The second vector.
     * @return The cross product between this vector and the given one.
     */
    public Vector3i cross(Vector2i v) {
        return this.cross(v.x(), v.y(), 0);
    }

    /**
     * Checks if the components of this vector are equal to the given ones.
     *
     * @param x The x component.
     * @param y The y component.
     * @return True if the components of this vector are equal to the given ones, otherwise false.
     */
    public boolean equals(int x, int y) {
        return this.x() == x && this.y() == y;
    }
}