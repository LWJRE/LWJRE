package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 4D vector using integer coordinates.
 *
 * @param x The vector's x component.
 * @param y The vector's y component.
 * @param z The vector's z component.
 * @param w The vector's w component.
 */
public record Vector4i(int x, int y, int z, int w) implements Serializable {

    /** Shorthand for {@code new Vector4i(0, 0, 0, 0)} */
    public static final Vector4i ZERO = new Vector4i(0, 0, 0, 0);
    /** Shorthand for {@code new Vector4i(1, 1, 1, 1)} */
    public static final Vector4i ONE = new Vector4i(1, 1, 1, 1);

    /**
     * Constructs a 4D vector from the given components.
     *
     * @param xyz The vector's x, y, and z components.
     * @param w The vector's w component.
     */
    public Vector4i(Vector3i xyz, int w) {
        this(xyz.x(), xyz.y(), xyz.z(), w);
    }

    /**
     * Constructs a 4D vector from the given components.
     *
     * @param xy The vector's x and y components.
     * @param z The vector's z component.
     * @param w The vector's w component.
     */
    public Vector4i(Vector2i xy, int z, int w) {
        this(xy.x(), xy.y(), z, w);
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
     * Returns a 3D vector composed of the x, the y, and the z components of this vector.
     *
     * @return A 3D vector composed of the x, the y, and the z components of this vector.
     */
    public Vector3i xyz() {
        return new Vector3i(this.x(), this.y(), this.z());
    }

    /**
     * Adds the given values to the components of this vector and returns the result.
     *
     * @param x The x component to add.
     * @param y The y component to add.
     * @param z The z component to add.
     * @param w The w component to add.
     * @return The sum between this vector and the given values.
     */
    public Vector4i plus(int x, int y, int z, int w) {
        return new Vector4i(this.x() + x, this.y() + y, this.z() + z, this.w() + w);
    }

    /**
     * Adds the given vector to this one and returns the result.
     *
     * @param v The vector to add.
     * @return The sum between this vector and the given one.
     */
    public Vector4i plus(Vector4i v) {
        return this.plus(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Subtracts the given values from each component of this vector and returns the result.
     *
     * @param x The x component to subtract.
     * @param y The y component to subtract.
     * @param z The z component to subtract.
     * @param w The w component to subtract.
     * @return The subtraction between this vector and the given values.
     */
    public Vector4i minus(int x, int y, int z, int w) {
        return new Vector4i(this.x() - x, this.y() - y, this.z() - z, this.w() - w);
    }

    /**
     * Subtracts the given vector from this one and returns the result.
     *
     * @param v The vector to subtract.
     * @return The subtraction between this vector and the given one.
     */
    public Vector4i minus(Vector4i v) {
        return this.minus(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the additive inverse of this vector.
     *
     * @return The additive inverse of this vector.
     */
    public Vector4i negated() {
        return new Vector4i(-this.x(), -this.y(), -this.z(), -this.w());
    }

    /**
     * Multiplies each component of this vector by the given values and returns the result.
     *
     * @param x Value to multiply the x component by.
     * @param y Value to multiply the y component by.
     * @param z Value to multiply the z component by.
     * @param w Value to multiply the w component by.
     * @return The component-wise multiplication between this vector and the given values.
     */
    public Vector4i multiply(int x, int y, int z, int w) {
        return new Vector4i(this.x() * x, this.y() * y, this.z() * z, this.w() * w);
    }

    /**
     * Multiplies each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to multiply this one by.
     * @return The component-wise multiplication between this vector and the one.
     */
    public Vector4i multiply(Vector4i v) {
        return this.multiply(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this vector by.
     * @return The product between this vector and the given scalar.
     */
    public Vector4i multiply(int k) {
        return this.multiply(k, k, k, k);
    }

    /**
     * Divides each component of this vector by the given values and returns the result.
     * This method performs an integer division.
     *
     * @param x The value to divide the x component by.
     * @param y The value to divide the y component by.
     * @param z The value to divide the z component by.
     * @param w The value to divide the w component by.
     * @return The component-wise division between this vector and the given values.
     */
    public Vector4i divide(int x, int y, int z, int w) {
        return new Vector4i(this.x() / x, this.y() / y, this.z() / z, this.w() / w);
    }

    /**
     * Divides each component of this vector by the components of the given ones and returns the result.
     * This method performs an integer division.
     *
     * @param v The vector to divide this one by.
     * @return The component-wise division between this vector and the one.
     */
    public Vector4i divide(Vector4i v) {
        return this.divide(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Divides this vector by the given scalar and returns the result.
     * This method performs an integer division.
     *
     * @param k The scalar to divide this vector by.
     * @return The division between this vector and the given scalar.
     */
    public Vector4i divide(int k) {
        return this.divide(k, k, k, k);
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given values.
     *
     * @param x The x component of the vector to multiply this one by.
     * @param y The y component of the vector to multiply this one by.
     * @param z The z component of the vector to multiply this one by.
     * @param w The w component of the vector to multiply this one by.
     * @return The dot product between this vector and the given values.
     */
    public int dot(int x, int y, int z, int w) {
        return this.x() * x + this.y() * y + this.z() * z + this.w() * w;
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The dot product between this vector and the given one.
     */
    public int dot(Vector4i v) {
        return this.dot(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the squared length, or magnitude, or modulus, of this vector.
     * <p>
     *     When comparing vectors by their length, it is more efficient to compare them by their squared length, since computing it does not require computing a square root.
     * </p>
     *
     * @return The squared length of this vector.
     * @see Vector4i#length()
     */
    public int lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or magnitude, or modulus, of this vector.
     *
     * @return The length of this vector.
     * @see Vector4i#lengthSquared()
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
     *     Not to be confused with the modulus, or {@link Vector4i#length()}, of a vector.
     * </p>
     *
     * @return A vector with all components in absolute value.
     */
    public Vector4i abs() {
        return new Vector4i(Math.abs(this.x()), Math.abs(this.y()), Math.abs(this.z()), Math.abs(this.w()));
    }

    /**
     * Returns a vector with the signs of the components of this vector.
     *
     * @return A vector with all components representing the sign of this vector.
     */
    public Vector4i sign() {
        return new Vector4i(Integer.compare(this.x(), 0), Integer.compare(this.y(), 0), Integer.compare(this.z(), 0), Integer.compare(this.w(), 0));
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The squared distance between this vector and the one with the given components.
     * @see Vector4i#distanceSquaredTo(Vector4i)
     */
    public int distanceSquaredTo(int x, int y, int z, int w) {
        x = x - this.x();
        y = y - this.y();
        z = z - this.z();
        w = w - this.w();
        return x * x + y * y + z * z + w * w;
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     * <p>
     *     This method is equivalent to {@code b.minus(a).lengthSquared()}.
     * </p>
     *
     * @param v The second vector.
     * @return The squared distance between this vector and the given one.
     * @see Vector4i#lengthSquared()
     */
    public int distanceSquaredTo(Vector4i v) {
        return this.distanceSquaredTo(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The squared between this vector and the one with the given components.
     * @see Vector4i#distanceTo(Vector4i)
     */
    public float distanceTo(int x, int y, int z, int w) {
        return (float) Math.sqrt(this.distanceSquaredTo(x, y, z, w));
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     * <p>
     *     This method is equivalent to {@code b.minus(a).length()}.
     * </p>
     *
     * @param v The second vector.
     * @return The squared between this vector and the given one.
     * @see Vector4i#length()
     */
    public float distanceTo(Vector4i v) {
        return this.distanceTo(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given values.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @param w The w component.
     * @return A vector composed by the reminder of the division between this vector's components and the given values.
     */
    public Vector4i mod(int x, int y, int z, int w) {
        return new Vector4i(this.x() % x, this.y() % y, this.z() % z, this.w() % w);
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the components of the given vector.
     *
     * @param v The second vector.
     * @return A vector composed by the reminder of the division between this vector's components and the components of the given vector.
     */
    public Vector4i mod(Vector4i v) {
        return this.mod(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given scalar.
     *
     * @param k The scalar.
     * @return A vector composed by the reminder of the division between this vector's components and the given scalar.
     */
    public Vector4i mod(int k) {
        return this.mod(k, k, k, k);
    }

    /**
     * Checks if the components of this vector are equal to the given ones.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @param w The w component.
     * @return True if the components of this vector are equal to the given ones, otherwise false.
     */
    public boolean equals(int x, int y, int z, int w) {
        return this.x() == x && this.y() == y && this.z() == z && this.w() == w;
    }
}