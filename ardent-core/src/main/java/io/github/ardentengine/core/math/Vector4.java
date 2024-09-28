package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 4D vector using floating point coordinates.
 *
 * @param x The vector's x component.
 * @param y The vector's y component.
 * @param z The vector's z component.
 * @param w The vector's w component.
 */
public record Vector4(float x, float y, float z, float w) implements Serializable {

    /** Shorthand for {@code new Vector4(0.0f, 0.0f, 0.0f, 0.0f)} */
    public static final Vector4 ZERO = new Vector4(0.0f, 0.0f, 0.0f, 0.0f);
    /** Shorthand for {@code new Vector4(1.0f, 1.0f, 1.0f, 1.0f)} */
    public static final Vector4 ONE = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);

    /**
     * Constructs a 4D vector from the given components.
     * The given values will be cast to float.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @param z The vector's z component.
     * @param w The vector's w component.
     */
    public Vector4(double x, double y, double z, double w) {
        this((float) x, (float) y, (float) z, (float) w);
    }

    /**
     * Constructs a 4D vector from the given components.
     *
     * @param xyz The vector's x, y, and z components.
     * @param w The vector's w component.
     */
    public Vector4(Vector3 xyz, float w) {
        this(xyz.x(), xyz.y(), xyz.z(), w);
    }

    /**
     * Constructs a 4D vector from the given components.
     *
     * @param xy The vector's x and y components.
     * @param z The vector's z component.
     * @param w The vector's w component.
     */
    public Vector4(Vector2 xy, float z, float w) {
        this(xy.x(), xy.y(), z, w);
    }

    /**
     * Returns a 2D vector composed of the x and the y components of this vector.
     *
     * @return A 2D vector composed of the x and the y components of this vector.
     */
    public Vector2 xy() {
        return new Vector2(this.x(), this.y());
    }

    /**
     * Returns a 3D vector composed of the x, the y, and the z components of this vector.
     *
     * @return A 3D vector composed of the x, the y, and the z components of this vector.
     */
    public Vector3 xyz() {
        return new Vector3(this.x(), this.y(), this.z());
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
    public Vector4 plus(float x, float y, float z, float w) {
        return new Vector4(this.x() + x, this.y() + y, this.z() + z, this.w() + w);
    }

    /**
     * Adds the given vector to this one and returns the result.
     *
     * @param v The vector to add.
     * @return The sum between this vector and the given one.
     */
    public Vector4 plus(Vector4 v) {
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
    public Vector4 minus(float x, float y, float z, float w) {
        return new Vector4(this.x() - x, this.y() - y, this.z() - z, this.w() - w);
    }

    /**
     * Subtracts the given vector from this one and returns the result.
     *
     * @param v The vector to subtract.
     * @return The subtraction between this vector and the given one.
     */
    public Vector4 minus(Vector4 v) {
        return this.minus(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the additive inverse of this vector.
     *
     * @return The additive inverse of this vector.
     */
    public Vector4 negated() {
        return new Vector4(-this.x(), -this.y(), -this.z(), -this.w());
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
    public Vector4 multiply(float x, float y, float z, float w) {
        return new Vector4(this.x() * x, this.y() * y, this.z() * z, this.w() * w);
    }

    /**
     * Multiplies each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to multiply this one by.
     * @return The component-wise multiplication between this vector and the one.
     */
    public Vector4 multiply(Vector4 v) {
        return this.multiply(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this vector by.
     * @return The product between this vector and the given scalar.
     */
    public Vector4 multiply(float k) {
        return this.multiply(k, k, k, k);
    }

    /**
     * Divides each component of this vector by the given values and returns the result.
     *
     * @param x The value to divide the x component by.
     * @param y The value to divide the y component by.
     * @param z The value to divide the z component by.
     * @param w The value to divide the w component by.
     * @return The component-wise division between this vector and the given values.
     */
    public Vector4 divide(float x, float y, float z, float w) {
        return new Vector4(this.x() / x, this.y() / y, this.z() / z, this.w() / w);
    }

    /**
     * Divides each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to divide this one by.
     * @return The component-wise division between this vector and the one.
     */
    public Vector4 divide(Vector4 v) {
        return this.divide(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Divides this vector by the given scalar and returns the result.
     *
     * @param k The scalar to divide this vector by.
     * @return The division between this vector and the given scalar.
     */
    public Vector4 divide(float k) {
        return this.divide(k, k, k, k);
    }

    /**
     * Returns the inverse of this vector with respect to component-wise multiplication.
     *
     * @return The inverse of this vector.
     */
    public Vector4 inverse() {
        return new Vector4(1.0f / this.x(), 1.0f / this.y(), 1.0f / this.z(), 1.0f / this.w());
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
    public float dot(float x, float y, float z, float w) {
        return this.x() * x + this.y() * y + this.z() * z + this.w() * w;
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The dot product between this vector and the given one.
     */
    public float dot(Vector4 v) {
        return this.dot(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the squared length, or magnitude, or modulus, of this vector.
     * <p>
     *     When comparing vectors by their length, it is more efficient to compare them by their squared length, since computing it does not require computing a square root.
     * </p>
     *
     * @return The squared length of this vector.
     * @see Vector4#length()
     */
    public float lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or magnitude, or modulus, of this vector.
     *
     * @return The length of this vector.
     * @see Vector4#lengthSquared()
     */
    public float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    /**
     * Returns the result of scaling this vector to unit length.
     * Equivalent to dividing a vector by its length.
     * <p>
     *     The resulting vector has the same direction as this one, but a length of one.
     * </p>
     *
     * @return The result of scaling this vector to unit length.
     * @see Vector4#isNormalized()
     */
    public Vector4 normalized() {
        return this.divide(this.length());
    }

    /**
     * Checks if this vector is a unit vector by checking if its length is approximately equal to one.
     *
     * @return True if this vector is a unit vector, otherwise false.
     * @see Vector4#normalized()
     */
    public boolean isNormalized() {
        return MathUtils.equalsApprox(this.lengthSquared(), 1.0f);
    }

    /**
     * Returns a vector with the same direction as this one and a length not longer than the given limit.
     * Returns this same vector if its length is smaller than the given limit.
     *
     * @param limit The maximum length of the resulting vector.
     * @return A vector with the same direction as this one and a length not longer than the given limit.
     */
    public Vector4 limitLength(float limit) {
        var length = this.length();
        return length > 0.0f && limit < length ? this.multiply(limit / length) : this;
    }

    /**
     * Returns a vector with all components in absolute value.
     * <p>
     *     Not to be confused with the modulus, or {@link Vector4#length()}, of a vector.
     * </p>
     *
     * @return A vector with all components in absolute value.
     */
    public Vector4 abs() {
        return new Vector4(Math.abs(this.x()), Math.abs(this.y()), Math.abs(this.z()), Math.abs(this.w()));
    }

    /**
     * Returns a vector with the signs of the components of this vector.
     *
     * @return A vector with all components representing the sign of this vector.
     */
    public Vector4 sign() {
        return new Vector4(Math.signum(this.x()), Math.signum(this.y()), Math.signum(this.z()), Math.signum(this.w()));
    }

    /**
     * Returns a vector with all components rounded to the nearest integer.
     *
     * @return A vector with all components rounded to the nearest integer.
     */
    public Vector4 round() {
        return new Vector4(Math.round(this.x()), Math.round(this.y()), Math.round(this.z()), Math.round(this.w()));
    }

    /**
     * Returns a vector with all components rounded up.
     *
     * @return A vector with all components rounded up.
     */
    public Vector4 ceil() {
        return new Vector4(Math.ceil(this.x()), Math.ceil(this.y()), Math.ceil(this.z()), Math.ceil(this.w()));
    }

    /**
     * Returns a vector with all components rounded down.
     *
     * @return A vector with all components rounded down.
     */
    public Vector4 floor() {
        return new Vector4(Math.floor(this.x()), Math.floor(this.y()), Math.floor(this.z()), Math.floor(this.w()));
    }

    /**
     * Returns a vector moved toward the given one by the given delta without going past the final value.
     *
     * @param to The final value of the vector.
     * @param delta The fixed delta by which this vector is moved.
     * @return A vector moved toward the given one by the given delta.
     */
    public Vector4 moveToward(Vector4 to, float delta) {
        var x = to.x() - this.x();
        var y = to.y() - this.y();
        var z = to.z() - this.z();
        var w = to.w() - this.w();
        var length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        if(length <= delta) {
            return to;
        }
        var dl = delta / length;
        return this.plus(x * dl, y * dl, z * dl, w * dl);
    }

    /**
     * Computes the linear interpolation between this vector and the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     *
     * @param to The second vector.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of linearly interpolating between this vector and the given one.
     */
    public Vector4 lerp(Vector4 to, float weight) {
        return new Vector4(
            MathUtils.lerp(this.x(), to.x(), weight),
            MathUtils.lerp(this.y(), to.y(), weight),
            MathUtils.lerp(this.z(), to.z(), weight),
            MathUtils.lerp(this.w(), to.w(), weight)
        );
    }

    /**
     * Computes the normalized vector pointing from this one to the one with the given components and returns the result.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The normalized vector pointing from this one to the one with the given components.
     * @see Vector4#directionTo(Vector4)
     */
    public Vector4 directionTo(float x, float y, float z, float w) {
        x = x - this.x();
        y = y - this.y();
        z = z - this.z();
        w = w - this.w();
        var l = (float) Math.sqrt(x * x + y * y + z * z + w * w);
        return new Vector4(x / l, y / l, z / l, w / l);
    }

    /**
     * Computes the normalized vector pointing from this one to the given one and returns the result.
     * <p>
     *     This method is equivalent to {@code b.minus(a).normalized()}.
     * </p>
     *
     * @param v The second vector.
     * @return The normalized vector pointing from this one to the given one.
     */
    public Vector4 directionTo(Vector4 v) {
        return this.directionTo(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The squared distance between this vector and the one with the given components.
     * @see Vector4#distanceSquaredTo(Vector4)
     */
    public float distanceSquaredTo(float x, float y, float z, float w) {
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
     * @see Vector4#lengthSquared()
     */
    public float distanceSquaredTo(Vector4 v) {
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
     * @see Vector4#distanceTo(Vector4)
     */
    public float distanceTo(float x, float y, float z, float w) {
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
     * @see Vector4#length()
     */
    public float distanceTo(Vector4 v) {
        return this.distanceTo(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the angle in radians between this vector and the one with the given components.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The angle in radians between this vector and the one with the given components.
     * @see Vector4#angleTo(Vector4)
     */
    public double angleTo(float x, float y, float z, float w) {
        return Math.acos(this.dot(x, y, z, w) / (this.length() * Math.sqrt(x * x + y * y + z * z + w * w)));
    }

    /**
     * Returns the angle in radians between this vector and the given one.
     *
     * @param v The second vector.
     * @return The angle in radians between this vector and the given one.
     */
    public double angleTo(Vector4 v) {
        return this.angleTo(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Projects this vector on the one with the given components and returns the result.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The projection of this vector on the one with the given components.
     * @see Vector4#project(Vector4)
     */
    public Vector4 project(float x, float y, float z, float w) {
        var d = this.dot(x, y, z, w) / (x * x + y * y + z * z + w * w);
        return new Vector4(x * d, y * d, z * d, w * d);
    }

    /**
     * Projects this vector on the given one and returns the result.
     *
     * @param v The second vector.
     * @return The projection of this vector on the given one.
     */
    public Vector4 project(Vector4 v) {
        return this.project(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Reflects this vector by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the reflection normal.
     * @param y The y component of the reflection normal.
     * @param z The z component of the reflection normal.
     * @param w The w component of the reflection normal.
     * @return The reflection of this vector by the normal defined by the given components.
     * @see Vector4#reflect(Vector4)
     */
    public Vector4 reflect(float x, float y, float z, float w) {
        var d = this.dot(x, y, z, w) * 2.0f;
        return this.minus(x * d, y * d, z * d, w * d);
    }

    /**
     * Reflects this vector by the given normal and returns the result.
     *
     * @param n The reflection normal.
     * @return The reflection of this vector by the given normal.
     */
    public Vector4 reflect(Vector4 n) {
        return this.reflect(n.x(), n.y(), n.z(), n.w());
    }

    /**
     * "Bounces" this vector on a plane defined by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the plane's normal.
     * @param y The y component of the plane's normal.
     * @param z The z component of the plane's normal.
     * @param w The w component of the plane's normal.
     * @return A vector "bounced off" from a plane defined by the normal defined by the given components.
     * @see Vector4#bounce(Vector4)
     */
    public Vector4 bounce(float x, float y, float z, float w) {
        var d = this.dot(x, y, z, w) * 2.0f;
        return new Vector4(x * d - this.x(), y * d - this.y(), z * d - this.z(), w * d - this.w());
    }

    /**
     * "Bounces" this vector on a plane defined by the given normal and returns the result.
     * <p>
     *     This method is equivalent to {@code v.reflect(n).negated()}.
     * </p>
     *
     * @param n The normal of the plane.
     * @return A vector "bounced off" from a plane defined by the given normal.
     * @see Vector4#reflect(Vector4)
     */
    public Vector4 bounce(Vector4 n) {
        return this.bounce(n.x(), n.y(), n.z(), n.w());
    }

    /**
     * Slides this vector along a plane defined by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the plane's normal.
     * @param y The y component of the plane's normal.
     * @param z The z component of the plane's normal.
     * @param w The w component of the plane's normal.
     * @return The result of sliding this vector along a plane defined by the normal defined by the given components.
     * @see Vector4#slide(Vector4)
     */
    public Vector4 slide(float x, float y, float z, float w) {
        var d = this.dot(x, y, z, w);
        return this.minus(x * d, y * d, z * d, w * d);
    }

    /**
     * Slides this vector along a plane defined by the given normal and returns the result.
     *
     * @param n The normal of the plane.
     * @return The result of sliding this vector along a plane defined by the given normal.
     */
    public Vector4 slide(Vector4 n) {
        return this.slide(n.x(), n.y(), n.z(), n.w());
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
    public Vector4 mod(float x, float y, float z, float w) {
        return new Vector4(this.x() % x, this.y() % y, this.z() % z, this.w() % w);
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the components of the given vector.
     *
     * @param v The second vector.
     * @return A vector composed by the reminder of the division between this vector's components and the components of the given vector.
     */
    public Vector4 mod(Vector4 v) {
        return this.mod(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given scalar.
     *
     * @param k The scalar.
     * @return A vector composed by the reminder of the division between this vector's components and the given scalar.
     */
    public Vector4 mod(float k) {
        return this.mod(k, k, k, k);
    }

    /**
     * Returns the outer product between this vector and the given values.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @param w The w component of the second vector.
     * @return The outer product between this vector and the given values.
     */
    public Matrix4 outer(float x, float y, float z, float w) {
        return new Matrix4(
            this.x() * x, this.x() * y, this.x() * z, this.x() * w,
            this.y() * x, this.y() * y, this.y() * z, this.y() * w,
            this.z() * x, this.z() * y, this.z() * z, this.z() * w,
            this.w() * x, this.w() * y, this.w() * z, this.w() * w
        );
    }

    /**
     * Returns the outer product between this vector and the given one.
     *
     * @param v The second vector.
     * @return The outer product between this vector and the given one.
     */
    public Matrix4 outer(Vector4 v) {
        return this.outer(v.x(), v.y(), v.z(), v.w());
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
    public boolean equals(float x, float y, float z, float w) {
        return this.x() == x && this.y() == y && this.z() == z && this.w() == w;
    }

    /**
     * Checks if the components of this vector are approximately equal to the given ones using {@link MathUtils#EPSILON}.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @param w The w component.
     * @return True if the components of this vector are approximately equal to the given ones, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(float x, float y, float z, float w) {
        return MathUtils.equalsApprox(this.x(), x)
            && MathUtils.equalsApprox(this.y(), y)
            && MathUtils.equalsApprox(this.z(), z)
            && MathUtils.equalsApprox(this.w(), w);
    }

    /**
     * Checks if this vector is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param v The second vector.
     * @return True if this vector is approximately equal to the given one, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(Vector4 v) {
        return this.equalsApprox(v.x(), v.y(), v.z(), v.w());
    }
}