package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 2D vector using floating point coordinates.
 * <p>
 *     Can be used to represent 2D coordinates or any other tuple of numeric values.
 * </p>
 *
 * @param x The vector's x component.
 * @param y The vector's y component.
 */
public record Vector2(float x, float y) implements Serializable {

    /** Shorthand for {@code new Vector2(0.0f, 0.0f)} */
    public static final Vector2 ZERO = new Vector2(0.0f, 0.0f);
    /** Shorthand for {@code new Vector2(1.0f, 1.0f)} */
    public static final Vector2 ONE = new Vector2(1.0f, 1.0f);
    /** Shorthand for {@code new Vector2(0.0f, 1.0f)} */
    public static final Vector2 UP = new Vector2(0.0f, 1.0f);
    /** Shorthand for {@code new Vector2(0.0f, -1.0f)} */
    public static final Vector2 DOWN = new Vector2(0.0f, -1.0f);
    /** Shorthand for {@code new Vector2(-1.0f, 0.0f)} */
    public static final Vector2 LEFT = new Vector2(-1.0f, 0.0f);
    /** Shorthand for {@code new Vector2(1.0f, 0.0f)} */
    public static final Vector2 RIGHT = new Vector2(1.0f, 0.0f);

    /**
     * Constructs a 2D vector from the given components.
     * The given values will be cast to float.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     */
    public Vector2(double x, double y) {
        this((float) x, (float) y);
    }

    /**
     * Adds the given values to the components of this vector and returns the result.
     *
     * @param x The x component to add.
     * @param y The y component to add.
     * @return The sum between this vector and the given values.
     */
    public Vector2 plus(float x, float y) {
        return new Vector2(this.x() + x, this.y() + y);
    }

    /**
     * Adds the given vector to this one and returns the result.
     *
     * @param v The vector to add.
     * @return The sum between this vector and the given one.
     */
    public Vector2 plus(Vector2 v) {
        return this.plus(v.x(), v.y());
    }

    /**
     * Subtracts the given values from each component of this vector and returns the result.
     *
     * @param x The x component to subtract.
     * @param y The y component to subtract.
     * @return The subtraction between this vector and the given values.
     */
    public Vector2 minus(float x, float y) {
        return new Vector2(this.x() - x, this.y() - y);
    }

    /**
     * Subtracts the given vector from this one and returns the result.
     *
     * @param v The vector to subtract.
     * @return The subtraction between this vector and the given one.
     */
    public Vector2 minus(Vector2 v) {
        return this.minus(v.x(), v.y());
    }

    /**
     * Returns the additive inverse of this vector.
     *
     * @return The additive inverse of this vector.
     */
    public Vector2 negated() {
        return new Vector2(-this.x(), -this.y());
    }

    /**
     * Multiplies each component of this vector by the given values and returns the result.
     *
     * @param x Value to multiply the x component by.
     * @param y Value to multiply the y component by.
     * @return The component-wise multiplication between this vector and the given values.
     */
    public Vector2 multiply(float x, float y) {
        return new Vector2(this.x() * x, this.y() * y);
    }

    /**
     * Multiplies each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to multiply this one by.
     * @return The component-wise multiplication between this vector and the one.
     */
    public Vector2 multiply(Vector2 v) {
        return this.multiply(v.x(), v.y());
    }

    /**
     * Multiplies this vector by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this vector by.
     * @return The product between this vector and the given scalar.
     */
    public Vector2 multiply(float k) {
        return this.multiply(k, k);
    }

    /**
     * Divides each component of this vector by the given values and returns the result.
     *
     * @param x The value to divide the x component by.
     * @param y The value to divide the y component by.
     * @return The component-wise division between this vector and the given values.
     */
    public Vector2 divide(float x, float y) {
        return new Vector2(this.x() / x, this.y() / y);
    }

    /**
     * Divides each component of this vector by the components of the given ones and returns the result.
     *
     * @param v The vector to divide this one by.
     * @return The component-wise division between this vector and the one.
     */
    public Vector2 divide(Vector2 v) {
        return this.divide(v.x(), v.y());
    }

    /**
     * Divides this vector by the given scalar and returns the result.
     *
     * @param k The scalar to divide this vector by.
     * @return The division between this vector and the given scalar.
     */
    public Vector2 divide(float k) {
        return this.divide(k, k);
    }

    /**
     * Returns the inverse of this vector with respect to component-wise multiplication.
     *
     * @return The inverse of this vector.
     */
    public Vector2 inverse() {
        return new Vector2(1.0f / this.x(), 1.0f / this.y());
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given values.
     *
     * @param x The x component of the vector to multiply this one by.
     * @param y The y component of the vector to multiply this one by.
     * @return The dot product between this vector and the given values.
     */
    public float dot(float x, float y) {
        return this.x() * x + this.y() * y;
    }

    /**
     * Returns the dot product, or scalar product, between this vector and the given one.
     *
     * @param v The second vector.
     * @return The dot product between this vector and the given one.
     */
    public float dot(Vector2 v) {
        return this.dot(v.x(), v.y());
    }

    /**
     * Returns the squared length, or magnitude, or modulus, of this vector.
     * <p>
     *     When comparing vectors by their length, it is more efficient to compare them by their squared length, since computing it does not require computing a square root.
     * </p>
     *
     * @return The squared length of this vector.
     * @see Vector2#length()
     */
    public float lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or magnitude, or modulus, of this vector.
     *
     * @return The length of this vector.
     * @see Vector2#lengthSquared()
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
     * @see Vector2#isNormalized()
     */
    public Vector2 normalized() {
        return this.divide(this.length());
    }

    /**
     * Checks if this vector is a unit vector by checking if its length is approximately equal to one.
     *
     * @return True if this vector is a unit vector, otherwise false.
     * @see Vector2#normalized()
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
    public Vector2 limitLength(float limit) {
        var length = this.length();
        return length > 0.0f && limit < length ? this.multiply(limit / length) : this;
    }

    /**
     * Returns a vector with all components in absolute value.
     * <p>
     *     Not to be confused with the modulus, or {@link Vector2#length()}, of a vector.
     * </p>
     *
     * @return A vector with all components in absolute value.
     */
    public Vector2 abs() {
        return new Vector2(Math.abs(this.x()), Math.abs(this.y()));
    }

    /**
     * Returns a vector with the signs of the components of this vector.
     *
     * @return A vector with all components representing the sign of this vector.
     */
    public Vector2 sign() {
        return new Vector2(Math.signum(this.x()), Math.signum(this.y()));
    }

    /**
     * Returns a vector with all components rounded to the nearest integer.
     *
     * @return A vector with all components rounded to the nearest integer.
     */
    public Vector2 round() {
        return new Vector2(Math.round(this.x()), Math.round(this.y()));
    }

    /**
     * Returns a vector with all components rounded up.
     *
     * @return A vector with all components rounded up.
     */
    public Vector2 ceil() {
        return new Vector2(Math.ceil(this.x()), Math.ceil(this.y()));
    }

    /**
     * Returns a vector with all components rounded down.
     *
     * @return A vector with all components rounded down.
     */
    public Vector2 floor() {
        return new Vector2(Math.floor(this.x()), Math.floor(this.y()));
    }

    /**
     * Returns a vector moved toward the given one by the given delta without going past the final value.
     *
     * @param to The final value of the vector.
     * @param delta The fixed delta by which this vector is moved.
     * @return A vector moved toward the given one by the given delta.
     */
    public Vector2 moveToward(Vector2 to, float delta) {
        var x = to.x() - this.x();
        var y = to.y() - this.y();
        var length = (float) Math.sqrt(x * x + y * y);
        if(length <= delta) {
            return to;
        }
        var dl = delta / length;
        return this.plus(x * dl, y * dl);
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
    public Vector2 lerp(Vector2 to, float weight) {
        return new Vector2(MathUtils.lerp(this.x(), to.x(), weight), MathUtils.lerp(this.y(), to.y(), weight));
    }

    /**
     * Computes the spherical linear interpolation between this vector and the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     * <p>
     *     If this vector or the given one are approximately zero, this method behaves like {@link Vector2#lerp(Vector2, float)}.
     * </p>
     *
     * @param to The second vector.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of the spherical linear interpolation between this vector and the given one by the given weight.
     */
    public Vector2 slerp(Vector2 to, float weight) {
        if(MathUtils.equalsApprox(this.lengthSquared(), 0.0f) || MathUtils.equalsApprox(to.lengthSquared(), 0.0f)) {
            return this.lerp(to, weight);
        }
        return this.rotated(this.angleTo(to) * weight).multiply(to.length() / this.length());
    }

    /**
     * Computes the normalized vector pointing from this one to the one with the given components and returns the result.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The normalized vector pointing from this one to the one with the given components.
     * @see Vector2#directionTo(Vector2)
     */
    public Vector2 directionTo(float x, float y) {
        x = x - this.x();
        y = y - this.y();
        var l = (float) Math.sqrt(x * x + y * y);
        return new Vector2(x / l, y / l);
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
    public Vector2 directionTo(Vector2 v) {
        return this.directionTo(v.x(), v.y());
    }

    /**
     * Returns the squared distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The squared distance between this vector and the one with the given components.
     * @see Vector2#distanceSquaredTo(Vector2)
     */
    public float distanceSquaredTo(float x, float y) {
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
     * @see Vector2#lengthSquared()
     */
    public float distanceSquaredTo(Vector2 v) {
        return this.distanceSquaredTo(v.x(), v.y());
    }

    /**
     * Returns the distance between the point represented by this vector and the point represented by the given one.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The squared between this vector and the one with the given components.
     * @see Vector2#distanceTo(Vector2)
     */
    public float distanceTo(float x, float y) {
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
     * @see Vector2#length()
     */
    public float distanceTo(Vector2 v) {
        return this.distanceTo(v.x(), v.y());
    }

    /**
     * Returns the angle in radians between this vector and the one with the given components.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The angle in radians between this vector and the one with the given components.
     * @see Vector2#angleTo(Vector2)
     */
    public double angleTo(float x, float y) {
        return Math.acos(this.dot(x, y) / (this.length() * Math.sqrt(x * x + y * y)));
    }

    /**
     * Returns the angle in radians between this vector and the given one.
     *
     * @param v The second vector.
     * @return The angle in radians between this vector and the given one.
     */
    public double angleTo(Vector2 v) {
        return this.angleTo(v.x(), v.y());
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
     * Returns the angle in radians between the line connecting the two points represented by this vector and the given coordinates and the X axis.
     *
     * @param x The x coordinate of the second point.
     * @param y The y coordinate of the second point.
     * @return The angle in radians between the line connecting the two points represented by this vector and the given coordinates and the X axis.
     * @see Vector2#angleToPoint(Vector2)
     */
    public double angleToPoint(float x, float y) {
        return Math.atan2(y - this.y(), x - this.x());
    }

    /**
     * Returns the angle in radians between the line connecting the two points represented by this vector and the given one and the X axis.
     * <p>
     *     This method is equivalent to {@code b.minus(a).angle()}.
     * </p>
     *
     * @param v The second point.
     * @return The angle in radians between the line connecting the two points represented by this vector and the given one and the X axis.
     * @see Vector2#angle()
     */
    public double angleToPoint(Vector2 v) {
        return this.angleToPoint(v.x(), v.y());
    }

    /**
     * Projects this vector on the one with the given components and returns the result.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The projection of this vector on the one with the given components.
     * @see Vector2#project(Vector2)
     */
    public Vector2 project(float x, float y) {
        var d = this.dot(x, y) / (x * x + y * y);
        return new Vector2(x * d, y * d);
    }

    /**
     * Projects this vector on the given one and returns the result.
     *
     * @param v The second vector.
     * @return The projection of this vector on the given one.
     */
    public Vector2 project(Vector2 v) {
        return this.project(v.x(), v.y());
    }

    /**
     * Reflects this vector by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the reflection normal.
     * @param y The y component of the reflection normal.
     * @return The reflection of this vector by the normal defined by the given components.
     * @see Vector2#reflect(Vector2)
     */
    public Vector2 reflect(float x, float y) {
        var d = this.dot(x, y) * 2.0f;
        return this.minus(x * d, y * d);
    }

    /**
     * Reflects this vector by the given normal and returns the result.
     *
     * @param n The reflection normal.
     * @return The reflection of this vector by the given normal.
     */
    public Vector2 reflect(Vector2 n) {
        return this.reflect(n.x(), n.y());
    }

    /**
     * "Bounces" this vector on a plane defined by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the plane's normal.
     * @param y The y component of the plane's normal.
     * @return A vector "bounced off" from a plane defined by the normal defined by the given components.
     * @see Vector2#bounce(Vector2)
     */
    public Vector2 bounce(float x, float y) {
        var d = this.dot(x, y) * 2.0f;
        return new Vector2(x * d - this.x(), y * d - this.y());
    }

    /**
     * "Bounces" this vector on a plane defined by the given normal and returns the result.
     * <p>
     *     This method is equivalent to {@code v.reflect(n).negated()}.
     * </p>
     *
     * @param n The normal of the plane.
     * @return A vector "bounced off" from a plane defined by the given normal.
     * @see Vector2#reflect(Vector2)
     */
    public Vector2 bounce(Vector2 n) {
        return this.bounce(n.x(), n.y());
    }

    /**
     * Slides this vector along a plane defined by the normal defined by the given components and returns the result.
     *
     * @param x The x component of the plane's normal.
     * @param y The y component of the plane's normal.
     * @return The result of sliding this vector along a plane defined by the normal defined by the given components.
     * @see Vector2#slide(Vector2)
     */
    public Vector2 slide(float x, float y) {
        var d = this.dot(x, y);
        return this.minus(x * d, y * d);
    }

    /**
     * Slides this vector along a plane defined by the given normal and returns the result.
     *
     * @param n The normal of the plane.
     * @return The result of sliding this vector along a plane defined by the given normal.
     */
    public Vector2 slide(Vector2 n) {
        return this.slide(n.x(), n.y());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given values.
     *
     * @param x The x component.
     * @param y The y component.
     * @return A vector composed by the reminder of the division between this vector's components and the given values.
     */
    public Vector2 mod(float x, float y) {
        return new Vector2(this.x() % x, this.y() % y);
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the components of the given vector.
     *
     * @param v The second vector.
     * @return A vector composed by the reminder of the division between this vector's components and the components of the given vector.
     */
    public Vector2 mod(Vector2 v) {
        return this.mod(v.x(), v.y());
    }

    /**
     * Returns a vector composed by the reminder of the division between this vector's components and the given scalar.
     *
     * @param k The scalar.
     * @return A vector composed by the reminder of the division between this vector's components and the given scalar.
     */
    public Vector2 mod(float k) {
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
        return this.x() / this.y();
    }

    /**
     * Returns a perpendicular vector rotated 90 degrees counter-clockwise compared to the original, with the same length.
     *
     * @return A vector perpendicular to this one with the same length.
     */
    public Vector2 orthogonal() {
        return new Vector2(this.y(), -this.x());
    }

    /**
     * Rotates this vector by the given angle in radians and returns the result.
     *
     * @param angle The rotation angle in radians.
     * @return This vector rotated by the given angle.
     */
    public Vector2 rotated(double angle) {
        var sin = (float) Math.sin(angle);
        var cos = (float) Math.cos(angle);
        return new Vector2(this.x() * cos - this.y() * sin, this.x() * sin + this.y() * cos);
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
    public Vector3 cross(float x, float y, float z) {
        return new Vector3(this.y() * z, z * this.x(), this.x() * y - this.y() * x);
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
    public Vector3 cross(Vector3 v) {
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
    public Vector3 cross(Vector2 v) {
        return this.cross(v.x(), v.y(), 0.0f);
    }

    /**
     * Returns the outer product between this vector and the given values.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @return The outer product between this vector and the given values.
     */
    public Matrix2 outer(float x, float y) {
        return new Matrix2(
            this.x() * x, this.x() * y,
            this.y() * x, this.y() * y
        );
    }

    /**
     * Returns the outer product between this vector and the given one.
     *
     * @param v The second vector.
     * @return The outer product between this vector and the given one.
     */
    public Matrix2 outer(Vector2 v) {
        return this.outer(v.x(), v.y());
    }

    /**
     * Returns the outer product between this vector and the given values.
     *
     * @param x The x component of the second vector.
     * @param y The y component of the second vector.
     * @param z The z component of the second vector.
     * @return The outer product between this vector and the given values.
     */
    public Matrix2x3 outer(float x, float y, float z) {
        return new Matrix2x3(
            this.x() * x, this.x() * y, this.x() * z,
            this.y() * x, this.y() * y, this.y() * z
        );
    }

    /**
     * Returns the outer product between this vector and the given one.
     *
     * @param v The second vector.
     * @return The outer product between this vector and the given one.
     */
    public Matrix2x3 outer(Vector3 v) {
        return this.outer(v.x(), v.y(), v.z());
    }

    /**
     * Returns the point at the given {@code t} on the quadratic Bézier curve defined by this vector and the given points.
     *
     * @param to The end point of the Bézier curve.
     * @param control The control point of the Bézier curve.
     * @param t The interpolation weight. Must be between zero and one.
     * @return The point at the given {@code t} on the quadratic Bézier curve defined by this vector and the given points.
     */
    public Vector2 bezierInterpolate(Vector2 to, Vector2 control, float t) {
        return new Vector2(
            MathUtils.bezierInterpolate(this.x(), to.x(), control.x(), t),
            MathUtils.bezierInterpolate(this.y(), to.y(), control.y(), t)
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
    public Vector2 bezierDerivative(Vector2 to, Vector2 control, float t) {
        return new Vector2(
            MathUtils.bezierDerivative(this.x(), to.x(), control.x(), t),
            MathUtils.bezierDerivative(this.y(), to.y(), control.y(), t)
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
    public Vector2 bezierInterpolate(Vector2 to, Vector2 control1, Vector2 control2, float t) {
        return new Vector2(
            MathUtils.bezierInterpolate(this.x(), to.x(), control1.x(), control2.x(), t),
            MathUtils.bezierInterpolate(this.y(), to.y(), control1.y(), control2.y(), t)
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
    public Vector2 bezierDerivative(Vector2 to, Vector2 control1, Vector2 control2, float t) {
        return new Vector2(
            MathUtils.bezierDerivative(this.x(), to.x(), control1.x(), control2.x(), t),
            MathUtils.bezierDerivative(this.y(), to.y(), control1.y(), control2.y(), t)
        );
    }

    /**
     * Checks if the components of this vector are equal to the given ones.
     *
     * @param x The x component.
     * @param y The y component.
     * @return True if the components of this vector are equal to the given ones, otherwise false.
     */
    public boolean equals(float x, float y) {
        return this.x() == x && this.y() == y;
    }

    /**
     * Checks if the components of this vector are approximately equal to the given ones using {@link MathUtils#EPSILON}.
     *
     * @param x The x component.
     * @param y The y component.
     * @return True if the components of this vector are approximately equal to the given ones, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(float x, float y) {
        return MathUtils.equalsApprox(this.x(), x) && MathUtils.equalsApprox(this.y(), y);
    }

    /**
     * Checks if this vector is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param v The second vector.
     * @return True if this vector is approximately equal to the given one, otherwise false.
     * @see MathUtils#equalsApprox(float, float)
     */
    public boolean equalsApprox(Vector2 v) {
        return this.equalsApprox(v.x(), v.y());
    }

    /**
     * Constructs a unit vector with the given angle from the x axis.
     * <p>
     *     This method is equivalent to {@code Vector2(cos(angle), sin(angle))} or {@code Vector2.RIGHT.rotated(angle)}.
     * </p>
     *
     * @param angle The angle in radians of the resulting vector from the x axis.
     * @return A unit vector with the given angle from the x axis.
     * @see Vector2#rotated(double)
     */
    public static Vector2 fromAngle(double angle) {
        return new Vector2(Math.cos(angle), Math.sin(angle));
    }
}