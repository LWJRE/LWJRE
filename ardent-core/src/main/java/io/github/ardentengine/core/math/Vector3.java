package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 3D vector using floating point coordinates.
 * <p>
 *     Can be used to represent 3D coordinates or any other triplet of numeric values.
 * </p>
 *
 * @param x The vector's x component.
 * @param y The vector's y component.
 * @param z The vector's z component.
 */
public record Vector3(float x, float y, float z) implements Serializable {

    /** Shorthand for {@code new Vector3(0.0f, 0.0f, 0.0f)} */
    public static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
    /** Shorthand for {@code new Vector3(1.0f, 1.0f, 1.0f)} */
    public static final Vector3 ONE = new Vector3(1.0f, 1.0f, 1.0f);
    /** Shorthand for {@code new Vector3(0.0f, 1.0f, 0.0f)} */
    public static final Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);
    /** Shorthand for {@code new Vector3(0.0f, -1.0f, 0.0f)} */
    public static final Vector3 DOWN = new Vector3(0.0f, -1.0f, 0.0f);
    /** Shorthand for {@code new Vector3(-1.0f, 0.0f, 0.0f)} */
    public static final Vector3 LEFT = new Vector3(-1.0f, 0.0f, 0.0f);
    /** Shorthand for {@code new Vector3(1.0f, 0.0f, 0.0f)} */
    public static final Vector3 RIGHT = new Vector3(1.0f, 0.0f, 0.0f);
    /** Shorthand for {@code new Vector3(0.0f, 0.0f, 1.0f)} */
    public static final Vector3 FORWARD = new Vector3(0.0f, 0.0f, 1.0f);
    /** Shorthand for {@code new Vector3(0.0f, 0.0f, -1.0f)} */
    public static final Vector3 BACKWARDS = new Vector3(0.0f, 0.0f, -1.0f);

    /**
     * Constructs a 3D vector from the given components.
     * The given values will be cast to float.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @param z The vector's z component.
     */
    public Vector3(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Constructs a 3D vector from the given components.
     *
     * @param xy The vector's x and y components.
     * @param z The vector's z component.
     */
    public Vector3(Vector2 xy, float z) {
        this(xy.x(), xy.y(), z);
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
     * Adds the given values to the components of this vector and returns the result.
     *
     * @param x The x component to add.
     * @param y The y component to add.
     * @param z The z component to add.
     * @return The sum between this vector and the given values.
     */
    public Vector3 plus(float x, float y, float z) {
        return new Vector3(this.x() + x, this.y() + y, this.z() + z);
    }

    /**
     * Adds the given vector to this one and returns the result.
     *
     * @param v The vector to add.
     * @return The sum between this vector and the given one.
     */
    public Vector3 plus(Vector3 v) {
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
    public Vector3 minus(float x, float y, float z) {
        return new Vector3(this.x() - x, this.y() - y, this.z() - z);
    }

    /**
     * Subtracts the given vector from this one and returns the result.
     *
     * @param v The vector to subtract.
     * @return The subtraction between this vector and the given one.
     */
    public Vector3 minus(Vector3 v) {
        return this.minus(v.x(), v.y(), v.z());
    }

    /**
     * Returns the additive inverse of this vector.
     *
     * @return The additive inverse of this vector.
     */
    public Vector3 negated() {
        return new Vector3(-this.x(), -this.y(), -this.z());
    }

    /**
     * Multiplies each component of this vector by the given values and returns the result.
     *
     * @param x Value to multiply the x component by.
     * @param y Value to multiply the y component by.
     * @param z Value to multiply the z component by.
     * @return The component-wise multiplication between this vector and the given values.
     */
    public Vector3 multiply(float x, float y, float z) {
        return new Vector3(this.x() * x, this.y() * y, this.z() * z);
    }

    /**
     * Multiplies each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to multiply this one by.
     * @return The component-wise multiplication between this vector and the one.
     */
    public Vector3 multiply(Vector3 v) {
        return this.multiply(v.x(), v.y(), v.z());
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this vector by.
     * @return The product between this vector and the given scalar.
     */
    public Vector3 multiply(float k) {
        return this.multiply(k, k, k);
    }

    /**
     * Divides each component of this vector by the given values and returns the result.
     *
     * @param x The value to divide the x component by.
     * @param y The value to divide the y component by.
     * @param z The value to divide the z component by.
     * @return The component-wise division between this vector and the given values.
     */
    public Vector3 divide(float x, float y, float z) {
        return new Vector3(this.x() / x, this.y() / y, this.z() / z);
    }

    /**
     * Divides each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to divide this one by.
     * @return The component-wise division between this vector and the one.
     */
    public Vector3 divide(Vector3 v) {
        return this.divide(v.x(), v.y(), v.z());
    }

    /**
     * Divides this vector by the given scalar and returns the result.
     *
     * @param k The scalar to divide this vector by.
     * @return The division between this vector and the given scalar.
     */
    public Vector3 divide(float k) {
        return this.divide(k, k, k);
    }

    /**
     * Returns the inverse of this vector with respect to component-wise multiplication.
     *
     * @return The inverse of this vector.
     */
    public Vector3 inverse() {
        return new Vector3(1.0f / this.x(), 1.0f / this.y(), 1.0f / this.z());
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given values.
     *
     * @param x The x component of the vector to multiply this one by.
     * @param y The y component of the vector to multiply this one by.
     * @param z The z component of the vector to multiply this one by.
     * @return The dot product between this vector and the given values.
     */
    public float dot(float x, float y, float z) {
        return this.x() * x + this.y() * y + this.z() * z;
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The dot product between this vector and the given one.
     */
    public float dot(Vector3 v) {
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
    public Vector3 cross(float x, float y, float z) {
        return new Vector3(this.y() * z - this.z() * y, x * this.z() - z * this.x(), this.x() * y - this.y() * x);
    }

    /**
     * Returns the cross product, or vector product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The cross product between this vector and the given one.
     */
    public Vector3 cross(Vector3 v) {
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
    public Vector3 cross(Vector2 v) {
        return this.cross(v.x(), v.y(), 0.0f);
    }

    /**
     * Returns the squared length, or magnitude, or modulus, of this vector.
     * <p>
     *     When comparing vectors by their length, it is more efficient to compare them by their squared length, since computing it does not require computing a square root.
     * </p>
     *
     * @return The squared length of this vector.
     * @see Vector3#length()
     */
    public float lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or magnitude, or modulus, of this vector.
     *
     * @return The length of this vector.
     * @see Vector3#lengthSquared()
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
     * @see Vector3#isNormalized()
     */
    public Vector3 normalized() {
        return this.divide(this.length());
    }

    /**
     * Checks if this vector is a unit vector by checking if its length is approximately equal to one.
     *
     * @return True if this vector is a unit vector, otherwise false.
     * @see Vector3#normalized()
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
    public Vector3 limitLength(float limit) {
        var length = this.length();
        return length > 0.0f && limit < length ? this.multiply(limit / length) : this;
    }

    /**
     * Returns a vector with all components in absolute value.
     * <p>
     *     Not to be confused with the modulus, or {@link Vector3#length()}, of a vector.
     * </p>
     *
     * @return A vector with all components in absolute value.
     */
    public Vector3 abs() {
        return new Vector3(Math.abs(this.x()), Math.abs(this.y()), Math.abs(this.z()));
    }

    /**
     * Returns a vector with the signs of the components of this vector.
     *
     * @return A vector with all components representing the sign of this vector.
     */
    public Vector3 sign() {
        return new Vector3(Math.signum(this.x()), Math.signum(this.y()), Math.signum(this.z()));
    }

    /**
     * Returns a vector with all components rounded to the nearest integer.
     *
     * @return A vector with all components rounded to the nearest integer.
     */
    public Vector3 round() {
        return new Vector3(Math.round(this.x()), Math.round(this.y()), Math.round(this.z()));
    }

    /**
     * Returns a vector with all components rounded up.
     *
     * @return A vector with all components rounded up.
     */
    public Vector3 ceil() {
        return new Vector3(Math.ceil(this.x()), Math.ceil(this.y()), Math.ceil(this.z()));
    }

    /**
     * Returns a vector with all components rounded down.
     *
     * @return A vector with all components rounded down.
     */
    public Vector3 floor() {
        return new Vector3(Math.floor(this.x()), Math.floor(this.y()), Math.floor(this.z()));
    }

    /**
     * Returns a vector moved toward the given one by the given delta without going past the final value.
     *
     * @param to The final value of the vector.
     * @param delta The fixed delta by which this vector is moved.
     * @return A vector moved toward the given one by the given delta.
     */
    public Vector3 moveToward(Vector3 to, float delta) {
        var x = to.x() - this.x();
        var y = to.y() - this.y();
        var z = to.z() - this.z();
        var length = (float) Math.sqrt(x * x + y * y + z * z);
        if(length <= delta) {
            return to;
        }
        var dl = delta / length;
        return this.plus(x * dl, y * dl, z * dl);
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
    public Vector3 lerp(Vector3 to, float weight) {
        return new Vector3(
            MathUtils.lerp(this.x(), to.x(), weight),
            MathUtils.lerp(this.y(), to.y(), weight),
            MathUtils.lerp(this.z(), to.z(), weight)
        );
    }

    /**
     * Computes the spherical linear interpolation between this vector and the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     * <p>
     *     If this vector or the given one are approximately zero, this method behaves like {@link Vector3#lerp(Vector3, float)}.
     * </p>
     *
     * @param to The second vector.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of the spherical linear interpolation between this vector and the given one by the given weight.
     */
    public Vector3 slerp(Vector3 to, float weight) {
        if(MathUtils.equalsApprox(this.lengthSquared(), 0.0f) || MathUtils.equalsApprox(to.lengthSquared(), 0.0f)) {
            return this.lerp(to, weight);
        }
        return this.rotated(this.cross(to).normalized(), this.angleTo(to) * weight).multiply(to.length() / this.length());
    }

    /**
     * Computes the normalized vector pointing from this one to the one with the given components and returns the result.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The normalized vector pointing from this one to the one with the given components.
     * @see Vector3#directionTo(Vector3)
     */
    public Vector3 directionTo(float x, float y, float z) {
        x = x - this.x();
        y = y - this.y();
        z = z - this.z();
        var l = (float) Math.sqrt(x * x + y * y + z * z);
        return new Vector3(x / l, y / l, z / l);
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
    public Vector3 directionTo(Vector3 v) {
        return this.directionTo(v.x(), v.y(), v.z());
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The squared distance between this vector and the one with the given components.
     * @see Vector3#distanceSquaredTo(Vector3)
     */
    public float distanceSquaredTo(float x, float y, float z) {
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
     * @see Vector3#lengthSquared()
     */
    public float distanceSquaredTo(Vector3 v) {
        return this.distanceSquaredTo(v.x(), v.y(), v.z());
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The squared between this vector and the one with the given components.
     * @see Vector3#distanceTo(Vector3)
     */
    public float distanceTo(float x, float y, float z) {
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
     * @see Vector3#length()
     */
    public float distanceTo(Vector3 v) {
        return this.distanceTo(v.x(), v.y(), v.z());
    }

    /**
     * Returns the angle in radians between this vector and the one with the given components.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The angle in radians between this vector and the one with the given components.
     * @see Vector3#angleTo(Vector3)
     */
    public double angleTo(float x, float y, float z) {
        return Math.acos(this.dot(x, y, z) / (this.length() * Math.sqrt(x * x + y * y + z * z)));
    }

    /**
     * Returns the angle in radians between this vector and the given one.
     *
     * @param v The second vector.
     * @return The angle in radians between this vector and the given one.
     */
    public double angleTo(Vector3 v) {
        return this.angleTo(v.x(), v.y(), v.z());
    }

    /**
     * Returns the signed angle to the given vector in radians.
     *
     * @param to The second vector.
     * @param axis Specifies the sign of the angle.
     * @return The signed angle to the given vector in radians.
     */
    public double signedAngleTo(Vector3 to, Vector3 axis) {
        var cross = this.cross(to);
        return Math.atan2(cross.length(), this.dot(to)) * Math.signum(cross.dot(axis));
    }

    /**
     * Projects this vector on the one with the given components and returns the result.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The projection of this vector on the one with the given components.
     * @see Vector3#project(Vector3)
     */
    public Vector3 project(float x, float y, float z) {
        var d = this.dot(x, y, z) / (x * x + y * y + z * z);
        return new Vector3(x * d, y * d, z * d);
    }

    /**
     * Projects this vector on the given one and returns the result.
     *
     * @param v The second vector.
     * @return The projection of this vector on the given one.
     */
    public Vector3 project(Vector3 v) {
        return this.project(v.x(), v.y(), v.z());
    }

    /**
     * Reflects this vector by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the reflection normal.
     * @param y The y component of the reflection normal.
     * @param z The z component of the reflection normal.
     * @return The reflection of this vector by the normal defined by the given components.
     * @see Vector3#reflect(Vector3)
     */
    public Vector3 reflect(float x, float y, float z) {
        var d = this.dot(x, y, z) * 2.0f;
        return this.minus(x * d, y * d, z * d);
    }

    /**
     * Reflects this vector by the given normal and returns the result.
     *
     * @param n The reflection normal.
     * @return The reflection of this vector by the given normal.
     */
    public Vector3 reflect(Vector3 n) {
        return this.reflect(n.x(), n.y(), n.z());
    }

    /**
     * "Bounces" this vector on a plane defined by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the plane's normal.
     * @param y The y component of the plane's normal.
     * @param z The z component of the plane's normal.
     * @return A vector "bounced off" from a plane defined by the normal defined by the given components.
     * @see Vector3#bounce(Vector3)
     */
    public Vector3 bounce(float x, float y, float z) {
        var d = this.dot(x, y, z) * 2.0f;
        return new Vector3(x * d - this.x(), y * d - this.y(), z * d - this.z());
    }

    /**
     * "Bounces" this vector on a plane defined by the given normal and returns the result.
     * <p>
     *     This method is equivalent to {@code v.reflect(n).negated()}.
     * </p>
     *
     * @param n The normal of the plane.
     * @return A vector "bounced off" from a plane defined by the given normal.
     * @see Vector3#reflect(Vector3)
     */
    public Vector3 bounce(Vector3 n) {
        return this.bounce(n.x(), n.y(), n.z());
    }

    /**
     * Slides this vector along a plane defined by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the plane's normal.
     * @param y The y component of the plane's normal.
     * @param z The z component of the plane's normal.
     * @return The result of sliding this vector along a plane defined by the normal defined by the given components.
     * @see Vector3#slide(Vector3)
     */
    public Vector3 slide(float x, float y, float z) {
        var d = this.dot(x, y, z);
        return this.minus(x * d, y * d, z * d);
    }

    /**
     * Slides this vector along a plane defined by the given normal and returns the result.
     *
     * @param n The normal of the plane.
     * @return The result of sliding this vector along a plane defined by the given normal.
     */
    public Vector3 slide(Vector3 n) {
        return this.slide(n.x(), n.y(), n.z());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given values.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @return A vector composed by the reminder of the division between this vector's components and the given values.
     */
    public Vector3 mod(float x, float y, float z) {
        return new Vector3(this.x() % x, this.y() % y, this.z() % z);
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the components of the given vector.
     *
     * @param v The second vector.
     * @return A vector composed by the reminder of the division between this vector's components and the components of the given vector.
     */
    public Vector3 mod(Vector3 v) {
        return this.mod(v.x(), v.y(), v.z());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given scalar.
     *
     * @param k The scalar.
     * @return A vector composed by the reminder of the division between this vector's components and the given scalar.
     */
    public Vector3 mod(float k) {
        return this.mod(k, k, k);
    }

    /**
     * Rotates this vector around the given axis by the given angle and returns the result.
     * The given axis must be {@link Vector3#normalized()}.
     *
     * @param axis The rotation axis. Must be a unit vector.
     * @param angle The rotation angle in radians.
     * @return The result of rotating this vector around the given axis by the given angle.
     */
    public Vector3 rotated(Vector3 axis, double angle) {
        return Matrix3.rotation(axis, angle).multiply(this);
    }

    /**
     * Returns the outer product between this vector and the given values.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The outer product between this vector and the given values.
     */
    public Matrix3 outer(float x, float y, float z) {
        return new Matrix3(
            this.x() * x, this.x() * y, this.x() * z,
            this.y() * x, this.y() * y, this.y() * z,
            this.z() * x, this.z() * y, this.z() * z
        );
    }

    /**
     * Returns the outer product between this vector and the given one.
     *
     * @param v The second vector.
     * @return The outer product between this vector and the given one.
     */
    public Matrix3 outer(Vector3 v) {
        return this.outer(v.x(), v.y(), v.z());
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
    public Matrix3x4 outer(float x, float y, float z, float w) {
        return new Matrix3x4(
            this.x() * x, this.x() * y, this.x() * z, this.x() * w,
            this.y() * x, this.y() * y, this.y() * z, this.y() * w,
            this.z() * x, this.z() * y, this.z() * z, this.z() * w
        );
    }

    /**
     * Returns the outer product between this vector and the given one.
     *
     * @param v The second vector.
     * @return The outer product between this vector and the given one.
     */
    public Matrix3x4 outer(Vector4 v) {
        return this.outer(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Returns the point at the given {@code t} on the quadratic Bézier curve defined by this vector and the given points.
     *
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the quadratic Bézier curve defined by this vector and the given points.
     */
    public Vector3 bezierInterpolate(Vector3 to, Vector3 control, float t) {
        return new Vector3(
            MathUtils.bezierInterpolate(this.x(), to.x(), control.x(), t),
            MathUtils.bezierInterpolate(this.y(), to.y(), control.y(), t),
            MathUtils.bezierInterpolate(this.z(), to.z(), control.z(), t)
        );
    }

    /**
     * Returns the derivative at the given {@code t} on the quadratic Bézier curve defined by this vector and the given points.
     *
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The derivative at the given {@code t} on the quadratic Bézier curve defined by this vector and the given points.
     */
    public Vector3 bezierDerivative(Vector3 to, Vector3 control, float t) {
        return new Vector3(
            MathUtils.bezierDerivative(this.x(), to.x(), control.x(), t),
            MathUtils.bezierDerivative(this.y(), to.y(), control.y(), t),
            MathUtils.bezierDerivative(this.z(), to.z(), control.z(), t)
        );
    }

    /**
     * Returns the point at the given {@code t} on the cubic Bézier curve defined by this vector and the given points.
     *
     * @param to The end point of the Bézier curve.
     * @param control1 The first control point of the Bézier curve.
     * @param control2 The second control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the cubic Bézier curve defined by this vector and the given points.
     */
    public Vector3 bezierInterpolate(Vector3 to, Vector3 control1, Vector3 control2, float t) {
        return new Vector3(
            MathUtils.bezierInterpolate(this.x(), to.x(), control1.x(), control2.x(), t),
            MathUtils.bezierInterpolate(this.y(), to.y(), control1.y(), control2.y(), t),
            MathUtils.bezierInterpolate(this.z(), to.z(), control1.z(), control2.z(), t)
        );
    }

    /**
     * Returns the derivative at the given {@code t} on the cubic Bézier curve defined by this vector and the given points.
     *
     * @param to The end point of the Bézier curve.
     * @param control1 The first control point of the Bézier curve.
     * @param control2 The second control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The derivative at the given {@code t} on the cubic Bézier curve defined by this vector and the given points.
     */
    public Vector3 bezierDerivative(Vector3 to, Vector3 control1, Vector3 control2, float t) {
        return new Vector3(
            MathUtils.bezierDerivative(this.x(), to.x(), control1.x(), control2.x(), t),
            MathUtils.bezierDerivative(this.y(), to.y(), control1.y(), control2.y(), t),
            MathUtils.bezierDerivative(this.z(), to.z(), control1.z(), control2.z(), t)
        );
    }

    /**
     * Checks if the components of this vector are equal to the given ones.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @return True if the components of this vector are equal to the given ones, otherwise false.
     */
    public boolean equals(float x, float y, float z) {
        return this.x() == x && this.y() == y && this.z() == z;
    }

    /**
     * Checks if the components of this vector are approximately equal to the given ones using {@link MathUtils#EPSILON}.
     *
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     * @return True if the components of this vector are approximately equal to the given ones, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(float x, float y, float z) {
        return MathUtils.equalsApprox(this.x(), x)
            && MathUtils.equalsApprox(this.y(), y)
            && MathUtils.equalsApprox(this.z(), z);
    }

    /**
     * Checks if this vector is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param v The second vector.
     * @return True if this vector is approximately equal to the given one, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(Vector3 v) {
        return this.equalsApprox(v.x(), v.y(), v.z());
    }
}