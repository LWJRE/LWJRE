package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 3D vector using integer coordinates.
 *
 * @param x The vector's x component.
 * @param y The vector's y component.
 * @param z The vector's z component.
 */
public record Vector3i(int x, int y, int z) implements Serializable {

    /** Shorthand for {@code new Vector3i(0, 0, 0)} */
    public static final Vector3i ZERO = new Vector3i(0, 0, 0);
    /** Shorthand for {@code new Vector3i(1, 1, 1)} */
    public static final Vector3i ONE = new Vector3i(1, 1, 1);
    /** Shorthand for {@code new Vector3i(0, 1, 0)} */
    public static final Vector3i UP = new Vector3i(0, 1, 0);
    /** Shorthand for {@code new Vector3i(0, -1, 0)} */
    public static final Vector3i DOWN = new Vector3i(0, -1, 0);
    /** Shorthand for {@code new Vector3i(-1, 0, 0)} */
    public static final Vector3i LEFT = new Vector3i(-1, 0, 0);
    /** Shorthand for {@code new Vector3i(1, 0, 0)} */
    public static final Vector3i RIGHT = new Vector3i(1, 0, 0);
    /** Shorthand for {@code new Vector3i(0, 0, 1)} */
    public static final Vector3i FORWARD = new Vector3i(0, 0, 1);
    /** Shorthand for {@code new Vector3i(0, 0, -1)} */
    public static final Vector3i BACKWARDS = new Vector3i(0, 0, -1);

    /**
     * Constructs a 3D vector from the given components.
     *
     * @param xy The vector's x and y components.
     * @param z The vector's z component.
     */
    public Vector3i(Vector2i xy, int z) {
        this(xy.x(), xy.y(), z);
    }

    /**
     * Returns a 2D vector composed of the x and the y components of this vector.
     *
     * @return A 2D vector composed of the x and the y components of this vector.
     */
    public Vector2i xy() {
        return new Vector2i(this.x(), this.y());
    }

    /**
     * Adds the given values to the components of this vector and returns the result.
     *
     * @param x The x component to add.
     * @param y The y component to add.
     * @param z The z component to add.
     * @return The sum between this vector and the given values.
     */
    public Vector3i plus(int x, int y, int z) {
        return new Vector3i(this.x() + x, this.y() + y, this.z() + z);
    }

    /**
     * Adds the given vector to this one and returns the result.
     *
     * @param v The vector to add.
     * @return The sum between this vector and the given one.
     */
    public Vector3i plus(Vector3i v) {
        return this.plus(v.x(), v.y(), v.z());
    }

    /**
     * Subtracts the given values from each component of this vector and returns the result.
     *
     * @param x The x component to subtract.
     * @param y The y component to subtract.
     * @param z The z component to subtract.
     * @return The subtraction between this vector and the given values.
     */
    public Vector3i minus(int x, int y, int z) {
        return new Vector3i(this.x() - x, this.y() - y, this.z() - z);
    }

    /**
     * Subtracts the given vector from this one and returns the result.
     *
     * @param v The vector to subtract.
     * @return The subtraction between this vector and the given one.
     */
    public Vector3i minus(Vector3i v) {
        return this.minus(v.x(), v.y(), v.z());
    }

    /**
     * Returns the additive inverse of this vector.
     *
     * @return The additive inverse of this vector.
     */
    public Vector3i negated() {
        return new Vector3i(-this.x(), -this.y(), -this.z());
    }

    /**
     * Multiplies each component of this vector by the given values and returns the result.
     *
     * @param x Value to multiply the x component by.
     * @param y Value to multiply the y component by.
     * @param z Value to multiply the z component by.
     * @return The component-wise multiplication between this vector and the given values.
     */
    public Vector3i multiply(int x, int y, int z) {
        return new Vector3i(this.x() * x, this.y() * y, this.z() * z);
    }

    /**
     * Multiplies each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to multiply this one by.
     * @return The component-wise multiplication between this vector and the one.
     */
    public Vector3i multiply(Vector3i v) {
        return this.multiply(v.x(), v.y(), v.z());
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this vector by.
     * @return The product between this vector and the given scalar.
     */
    public Vector3i multiply(int k) {
        return this.multiply(k, k, k);
    }

    /**
     * Divides each component of this vector by the given values and returns the result.
     * This method performs an integer division.
     *
     * @param x The value to divide the x component by.
     * @param y The value to divide the y component by.
     * @param z The value to divide the z component by.
     * @return The component-wise division between this vector and the given values.
     */
    public Vector3i divide(int x, int y, int z) {
        return new Vector3i(this.x() / x, this.y() / y, this.z() / z);
    }

    /**
     * Divides each component of this vector by the components of the given ones and returns the result.
     * This method performs an integer division.
     *
     * @param v The vector to divide this one by.
     * @return The component-wise division between this vector and the one.
     */
    public Vector3i divide(Vector3i v) {
        return this.divide(v.x(), v.y(), v.z());
    }

    /**
     * Divides this vector by the given scalar and returns the result.
     * This method performs an integer division.
     *
     * @param k The scalar to divide this vector by.
     * @return The division between this vector and the given scalar.
     */
    public Vector3i divide(int k) {
        return this.divide(k, k, k);
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given values.
     *
     * @param x The x component of the vector to multiply this one by.
     * @param y The y component of the vector to multiply this one by.
     * @param z The z component of the vector to multiply this one by.
     * @return The dot product between this vector and the given values.
     */
    public int dot(int x, int y, int z) {
        return this.x() * x + this.y() * y + this.z() * z;
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The dot product between this vector and the given one.
     */
    public int dot(Vector3i v) {
        return this.dot(v.x(), v.y(), v.z());
    }

    /**
     * Returns the cross product, or vector product, between this vector and the vector defined by the given components.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The cross product between this vector and the vector defined by the given components.
     */
    public Vector3i cross(int x, int y, int z) {
        return new Vector3i(this.y() * z - this.z() * y, x * this.z() - z * this.x(), this.x() * y - this.y() * x);
    }

    /**
     * Returns the cross product, or vector product, between this vector and the given one.
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
     *     Since the cross product is only defined in three dimensions, the given vector is considered to be laying on the xy plane, i.e. its z component is considered to be zero.
     * </p>
     *
     * @param v The second vector.
     * @return The cross product between this vector and the given one.
     */
    public Vector3i cross(Vector2i v) {
        return this.cross(v.x(), v.y(), 0);
    }

    /**
     * Returns the squared length, or magnitude, or modulus, of this vector.
     * <p>
     *     When comparing vectors by their length, it is more efficient to compare them by their squared length, since computing it does not require computing a square root.
     * </p>
     *
     * @return The squared length of this vector.
     * @see Vector3i#length()
     */
    public int lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or magnitude, or modulus, of this vector.
     *
     * @return The length of this vector.
     * @see Vector3i#lengthSquared()
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
     *     Not to be confused with the modulus, or {@link Vector3i#length()}, of a vector.
     * </p>
     *
     * @return A vector with all components in absolute value.
     */
    public Vector3i abs() {
        return new Vector3i(Math.abs(this.x()), Math.abs(this.y()), Math.abs(this.z()));
    }

    /**
     * Returns a vector with the signs of the components of this vector.
     *
     * @return A vector with all components representing the sign of this vector.
     */
    public Vector3i sign() {
        return new Vector3i(Integer.compare(this.x(), 0), Integer.compare(this.y(), 0), Integer.compare(this.z(), 0));
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The squared distance between this vector and the one with the given components.
     * @see Vector3i#distanceSquaredTo(Vector3i)
     */
    public int distanceSquaredTo(int x, int y, int z) {
        x = x - this.x();
        y = y - this.y();
        z = z - this.z();
        return x * x + y * y + z * z;
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     * <p>
     *     This method is equivalent to {@code b.minus(a).lengthSquared()}.
     * </p>
     *
     * @param v The second vector.
     * @return The squared distance between this vector and the given one.
     * @see Vector3i#lengthSquared()
     */
    public int distanceSquaredTo(Vector3i v) {
        return this.distanceSquaredTo(v.x(), v.y(), v.z());
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The squared between this vector and the one with the given components.
     * @see Vector3i#distanceTo(Vector3i)
     */
    public float distanceTo(int x, int y, int z) {
        return (float) Math.sqrt(this.distanceSquaredTo(x, y, z));
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     * <p>
     *     This method is equivalent to {@code b.minus(a).length()}.
     * </p>
     *
     * @param v The second vector.
     * @return The squared between this vector and the given one.
     * @see Vector3i#length()
     */
    public float distanceTo(Vector3i v) {
        return this.distanceTo(v.x(), v.y(), v.z());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given values.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @return A vector composed by the reminder of the division between this vector's components and the given values.
     */
    public Vector3i mod(int x, int y, int z) {
        return new Vector3i(this.x() % x, this.y() % y, this.z() % z);
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the components of the given vector.
     *
     * @param v The second vector.
     * @return A vector composed by the reminder of the division between this vector's components and the components of the given vector.
     */
    public Vector3i mod(Vector3i v) {
        return this.mod(v.x(), v.y(), v.z());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given scalar.
     *
     * @param k The scalar.
     * @return A vector composed by the reminder of the division between this vector's components and the given scalar.
     */
    public Vector3i mod(int k) {
        return this.mod(k, k, k);
    }

    /**
     * Checks if the components of this vector are equal to the given ones.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @return True if the components of this vector are equal to the given ones, otherwise false.
     */
    public boolean equals(int x, int y, int z) {
        return this.x() == x && this.y() == y && this.z() == z;
    }
}