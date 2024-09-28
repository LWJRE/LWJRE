package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A single-precision quaternion.
 * Can be used to represent a 3D rotation.
 *
 * @param w The real/scalar part of the quaternion.
 * @param x The first component of the vector part (imaginary {@code i} axis).
 * @param y The second component of the vector part (imaginary {@code j} axis).
 * @param z The third component of the vector part (imaginary {@code k} axis).
 */
public record Quaternion(float w, float x, float y, float z) implements Serializable {

    /** Shorthand for `new Quaternion(1.0f, 0.0f, 0.0f, 0.0f)` */
    public static final Quaternion IDENTITY = new Quaternion(1.0f, 0.0f, 0.0f, 0.0f);
    /** Shorthand for `new Quaternion(0.0f, 0.0f, 0.0f, 0.0f)` */
    public static final Quaternion ZERO = new Quaternion(0.0f, 0.0f, 0.0f, 0.0f);

    /**
     * Constructs a quaternion from the given components.
     * The given values will be cast to float.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @param z The vector's z component.
     * @param w The vector's w component.
     */
    public Quaternion(double x, double y, double z, double w) {
        this((float) x, (float) y, (float) z, (float) w);
    }

    /**
     * Constructs a quaternion from the given real part and vector part.
     *
     * @param w The real/scalar part of the quaternion.
     * @param v The vector part of the quaternion.
     */
    public Quaternion(float w, Vector3 v) {
        this(w, v.x(), v.y(), v.z());
    }

    /**
     * Constructs a quaternion that represents a rotation around the given axis by the specified angle.
     * The given axis must be normalized.
     *
     * @param axis The rotation axis. Must be normalized.
     * @param angle The rotation angle in radians.
     * @see Vector3#normalized()
     */
    public Quaternion(Vector3 axis, double angle) {
        this((float) Math.cos(angle * 0.5), axis.multiply((float) (Math.sin(angle * 0.5) / axis.length())));
    }

    /**
     * Computes the sum between this quaternion and the given values and returns the result.
     *
     * @param w The w component to add.
     * @param x The x component to add.
     * @param y The y component to add.
     * @param z The z component to add.
     * @return The sum of this quaternion and the given values.
     */
    public Quaternion plus(float w, float x, float y, float z) {
        return new Quaternion(this.w() + w, this.x() + x, this.y() + y, this.z() + z);
    }

    /**
     * Computes the sum between this quaternion and the given one and returns the result.
     *
     * @param q The quaternion to add.
     * @return The sum of this quaternion and the given one.
     */
    public Quaternion plus(Quaternion q) {
        return this.plus(q.w(), q.x(), q.y(), q.z());
    }

    /**
     * Computes the subtraction of the given values from this quaternion and returns the result.
     *
     * @param w The w component to subtract.
     * @param x The x component to subtract.
     * @param y The y component to subtract.
     * @param z The z component to subtract.
     * @return The subtraction of the given values from this quaternion
     */
    public Quaternion minus(float w, float x, float y, float z) {
        return new Quaternion(this.w() - w, this.x() - x, this.y() - y, this.z() - z);
    }

    /**
     * Computes the subtraction of the given quaternion from this one and returns the result.
     *
     * @param q The quaternion to subtract.
     * @return The subtraction of the given quaternion from this one.
     */
    public Quaternion minus(Quaternion q) {
        return this.minus(q.w(), q.x(), q.y(), q.z());
    }

    /**
     * Returns the additive inverse of this quaternion.
     *
     * @return The additive inverse of this quaternion.
     */
    public Quaternion negated() {
        return new Quaternion(-this.w(), -this.x(), -this.y(), -this.z());
    }

    /**
     * Multiplies this quaternion by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this quaternion by.
     * @return The product of this quaternion by the given scalar.
     */
    public Quaternion multiply(float k) {
        return new Quaternion(this.w() * k, this.x() * k, this.y() * k, this.z() * k);
    }

    /**
     * Divides this quaternion by the given scalar and returns the result.
     *
     * @param k The scalar to divide this quaternion by.
     * @return The division of this quaternion by the given scalar.
     */
    public Quaternion divide(float k) {
        return new Quaternion(this.w() / k, this.x() / k, this.y() / k, this.z() / k);
    }

    /**
     * Multiplies this quaternion by the given values as defined by the Hamilton product and returns the result.
     * <p>
     *     Multiplying two rotation quaternions result in a quaternion that represents the two rotations applied in the same order.
     * </p>
     *
     * @param w The real/scalar part of the quaternion to multiply this one by.
     * @param x The x component of the vector part of the quaternion to multiply this one by.
     * @param y The y component of the vector part of the quaternion to multiply this one by.
     * @param z The z component of the vector part of the quaternion to multiply this one by.
     * @return The product of this quaternion by the given values.
     */
    public Quaternion multiply(float w, float x, float y, float z) {
        return new Quaternion(
            this.w() * w - this.x() * x - this.y() * y - this.z() * z,
            this.w() * x + this.x() * w + this.y() * z - this.z() * y,
            this.w() * y - this.x() * z + this.y() * w + this.z() * x,
            this.w() * z + this.x() * y - this.y() * x + this.z() * w
        );
    }

    /**
     * Multiplies this quaternion by the given one as defined by the Hamilton product and returns the result.
     * <p>
     *     Multiplying two rotation quaternions result in a quaternion that represents the two rotations applied in the same order.
     * </p>
     *
     * @param q The quaternion to multiply this one by.
     * @return The product between this quaternion and the given one.
     */
    public Quaternion multiply(Quaternion q) {
        return this.multiply(q.w(), q.x(), q.y(), q.z());
    }

    /**
     * Returns the conjugate of this quaternion.
     * The conjugate of a quaternion {@code w + xi + yj + zk} is the quaternion {@code w - xi - yj - zk}.
     *
     * @return The conjugate of this quaternion.
     */
    public Quaternion conjugate() {
        return new Quaternion(this.w(), -this.x(), -this.y(), -this.z());
    }

    /**
     * Computes the dot product between this quaternion and the given values and returns the result.
     *
     * @param w The w component of the second quaternion.
     * @param x The x component of the second quaternion.
     * @param y The y component of the second quaternion.
     * @param z The z component of the second quaternion.
     * @return The dot product between this quaternion and the given values.
     */
    public float dot(float w, float x, float y, float z) {
        return this.w() * w + this.x() * x + this.y() * y + this.z() * z;
    }

    /**
     * Computes the dot product between this quaternion and the given one and returns the result.
     *
     * @param q The second quaternion.
     * @return The dot product between this quaternion and the given one.
     */
    public float dot(Quaternion q) {
        return this.dot(q.w(), q.x(), q.y(), q.z());
    }

    /**
     * Returns the squared length, or squared norm, of this quaternion.
     * <p>
     *     When comparing quaternions by their length, it is more efficient to compare them by their squared length, since computing it does not require a square root.
     * </p>
     *
     * @return The squared length of this quaternion.
     * @see Quaternion#length()
     */
    public float lengthSquared() {
        return this.dot(this);
    }

    /**
     * Returns the length, or norm, of this quaternion.
     *
     * @return The length of this quaternion.
     * @see Quaternion#lengthSquared()
     */
    public float length() {
        return (float) Math.sqrt(this.lengthSquared());
    }

    /**
     * Returns the result of scaling this quaternion to unit length.
     * Equivalent to dividing a quaternion by its length.
     * <p>
     *     Only unit quaternions represent a rotation.
     * </p>
     * <p>
     *     The result will be NaN if this quaternion is the zero quaternion.
     * </p>
     *
     * @return The result of scaling this quaternion to unit length.
     * @see Quaternion#isNormalized()
     */
    public Quaternion normalized() {
        return this.divide(this.length());
    }

    /**
     * Checks if this quaternion is a unit quaternion, i.e. its length is approximately equal to one.
     *
     * @return True if this quaternion is a unit quaternion, otherwise false.
     * @see Quaternion#normalized()
     */
    public boolean isNormalized() {
        return MathUtils.equalsApprox(this.lengthSquared(), 1.0f);
    }

    /**
     * Returns the multiplicative inverse of this quaternion.
     *
     * @return The multiplicative inverse of this quaternion.
     */
    public Quaternion inverse() {
        var norm = this.lengthSquared();
        return new Quaternion(this.w() / norm, -this.x() / norm, -this.y() / norm, -this.z() / norm);
    }

    /**
     * Multiplies this quaternion by the {@link Quaternion#inverse()} of the one defined by the given values and returns the result.
     *
     * @param w The real/scalar part of the quaternion to divide this one by.
     * @param x The x component of the vector part of the quaternion to divide this one by.
     * @param y The y component of the vector part of the quaternion to divide this one by.
     * @param z The z component of the vector part of the quaternion to divide this one by.
     * @return The division of this quaternion by the given values.
     */
    public Quaternion divide(float w, float x, float y, float z) {
        var norm = w * w + x * x + y * y + z * z;
        w = w / norm;
        x = -x / norm;
        y = -y / norm;
        z = -z / norm;
        return new Quaternion(
            this.w() * w - this.x() * x - this.y() * y - this.z() * z,
            this.w() * x + this.x() * w + this.y() * z - this.z() * y,
            this.w() * y - this.x() * z + this.y() * w + this.z() * x,
            this.w() * z + this.x() * y - this.y() * x + this.z() * w
        );
    }

    /**
     * Multiplies this quaternion by the {@link Quaternion#inverse()} of the given one as defined by the Hamilton product and returns the result.
     *
     * @param q The quaternion to divide this one by.
     * @return The product between this quaternion and the inverse of the given one.
     */
    public Quaternion divide(Quaternion q) {
        return this.divide(q.w(), q.x(), q.y(), q.z());
    }

    /**
     * Returns the vector part of this quaternion.
     *
     * @return The vector part of this quaternion.
     */
    public Vector3 vector() {
        return new Vector3(this.x(), this.y(), this.z());
    }

    /**
     * Returns the angle in radians of the rotation represented by this quaternion.
     *
     * @return The angle in radians of the rotation represented by this quaternion.
     * @see Quaternion#axis()
     */
    public double angle() {
        return 2.0 * Math.acos(this.w());
    }

    /**
     * Returns the axis of the rotation specified by this quaternion.
     *
     * @return The axis of the rotation specified by this quaternion.
     * @see Quaternion#angle()
     */
    public Vector3 axis() {
        if(Math.abs(this.w()) > 1.0 - MathUtils.EPSILON) {
            return this.vector();
        }
        var d = Math.sqrt(1.0 - this.w() * this.w());
        return new Vector3(this.x() / d, this.y() / d, this.z() / d);
    }

    /**
     * Returns the exponential of this quaternion.
     * <p>
     *     Returns the identity quaternion if this quaternion has no vector part.
     * </p>
     *
     * @return The exponential of this quaternion
     */
    public Quaternion exp() {
        var l = Math.sqrt(this.x() * this.x() + this.y() * this.y() + this.z() * this.z());
        if(l > 0.0f) {
            var e = Math.exp(this.w());
            var sl = Math.sin(l) / l * e;
            return new Quaternion(Math.cos(l) * e, this.x() * sl, this.y() * sl, this.z() * sl);
        }
        return Quaternion.IDENTITY;
    }

    /**
     * Returns the natural logarithm of this quaternion.
     *
     * @return The natural logarithm of this quaternion.
     */
    public Quaternion log() {
        var n = this.length();
        var l = Math.acos(this.w() / n) / Math.sqrt(this.x() * this.x() + this.y() * this.y() + this.z() * this.z());
        return new Quaternion(Math.log(n), this.x() * l, this.y() * l, this.z() * l);
    }

    /**
     * Raises this quaternion to the power of the given exponent and returns the result.
     *
     * @param exp The exponent.
     * @return This quaternion to the power of the given exponent.
     */
    public Quaternion pow(float exp) {
        return this.log().multiply(exp).exp();
    }

    /**
     * Returns the angle in radians between this quaternion and the given one.
     * This is the magnitude of the angle you would need to rotate by to get from one to the other.
     *
     * @param q The second quaternion.
     * @return The angle in radians between this quaternion and the given one.
     */
    public double angleTo(Quaternion q) {
        var d = this.dot(q);
        return Math.acos(d * d * 2.0f - 1.0f);
    }

    /**
     * Computes the spherical linear interpolation between this quaternion and the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     *
     * @param to The second quaternion.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of the spherical linear interpolation between this quaternion and the given one by the given weight.
     */
    public Quaternion slerp(Quaternion to, float weight) {
        return to.divide(this).pow(weight).multiply(this);
    }

    /**
     * Returns this quaternion's rotation in the form of euler angles.
     *
     * @param order The decomposing oder.
     * @return A vector representing this quaternion's rotation in the form of euler angles.
     */
    public Vector3 euler(EulerOrder order) {
        return order.toEulerAngles(this);
    }

    /**
     * Returns this quaternion's rotation in the form of euler angles using the {@code ZYX} convention.
     *
     * @return A vector representing this quaternion's rotation in the form of euler angles.
     * @see Quaternion#euler(EulerOrder)
     */
    public Vector3 euler() {
        return this.euler(EulerOrder.ZYX);
    }

    /**
     * Checks if the components of this quaternion are equal to the given ones.
     *
     * @param w The real/scalar part of the quaternion.
     * @param x The first component of the vector part.
     * @param y The second component of the vector part.
     * @param z The third component of the vector part.
     * @return True if the components of this quaternion are equal to the given ones, otherwise false.
     */
    public boolean equals(float w, float x, float y, float z) {
        return this.w() == w && this.x() == x && this.y() == y && this.z() == z;
    }

    /**
     * Checks if the components of this quaternion are approximately equal to the given ones using {@link MathUtils#EPSILON}.
     *
     * @param w The real/scalar part of the quaternion.
     * @param x The first component of the vector part.
     * @param y The second component of the vector part.
     * @param z The third component of the vector part.
     * @return True if the components of this quaternion are approximately equal to the given ones, otherwise false.
     */
    public boolean equalsApprox(float w, float x, float y, float z) {
        return MathUtils.equalsApprox(this.w(), w)
            && MathUtils.equalsApprox(this.y(), y)
            && MathUtils.equalsApprox(this.z(), z)
            && MathUtils.equalsApprox(this.x(), x);
    }

    /**
     * Checks if this quaternion is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param q The second quaternion.
     * @return True if this quaternion is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(Quaternion q) {
        return this.equalsApprox(q.w(), q.x(), q.y(), q.z());
    }

    /**
     * Constructs a quaternion from the given euler angles and rotation order.
     *
     * @param euler A vector representing the quaternion's rotation in form of euler angles.
     * @param order The rotation order.
     * @return The quaternion constructed from the given euler angles and rotation order.
     */
    public static Quaternion fromEuler(Vector3 euler, EulerOrder order) {
        return order.toQuaternion(euler);
    }

    /**
     * Constructs a quaternion from the given euler angles and rotation order.
     *
     * @param x Rotation angle in radians on the x axis.
     * @param y Rotation angle in radians on the y axis.
     * @param z Rotation angle in radians on the z axis.
     * @param order The rotation order.
     * @return The quaternion constructed from the given euler angles and rotation order.
     */
    public static Quaternion fromEuler(double x, double y, double z, EulerOrder order) {
        return order.toQuaternion(x, y, z);
    }

    /**
     * Constructs a quaternion from the given euler angles in the {@code ZYX} order.
     *
     * @param euler A vector representing the quaternion's rotation in form of euler angles.
     * @return The quaternion constructed from the given euler angles in the {@code ZYX} order.
     * @see Quaternion#fromEuler(Vector3, EulerOrder)
     */
    public static Quaternion fromEuler(Vector3 euler) {
        return fromEuler(euler, EulerOrder.ZYX);
    }

    /**
     * Constructs a quaternion from the given euler angles in the {@code ZYX} order.
     *
     * @param x Rotation angle in radians on the x axis.
     * @param y Rotation angle in radians on the y axis.
     * @param z Rotation angle in radians on the z axis.
     * @return The quaternion constructed from the given euler angles in the {@code ZYX} order.
     * @see Quaternion#fromEuler(double, double, double, EulerOrder)
     */
    public static Quaternion fromEuler(double x, double y, double z) {
        return fromEuler(x, y, z, EulerOrder.ZYX);
    }

    /**
     * Constructs a quaternion from the given rotation matrix and rotation order.
     * The given matrix must be a pure rotation matrix.
     *
     * @param m The rotation matrix.
     * @param order The rotation order.
     * @return A quaternion constructed from the given rotation matrix.
     */
    public static Quaternion fromEuler(Matrix3 m, EulerOrder order) {
        return order.toQuaternion(m);
    }

    /**
     * Constructs a quaternion from the given rotation matrix in the {@code ZYX} order.
     * The given matrix must be a pure rotation matrix.
     *
     * @param m The rotation matrix.
     * @return A quaternion constructed from the given rotation matrix.
     * @see Quaternion#fromEuler(Matrix3, EulerOrder)
     */
    public static Quaternion fromEuler(Matrix3 m) {
        return fromEuler(m, EulerOrder.ZYX);
    }

    /**
     * Constructs a quaternion representing the shortest arc between two points on the surface of a sphere with a radius of one.
     *
     * @param from The first point.
     * @param to The second point.
     * @return A quaternion representing the shortest arc between two points on the surface of a sphere with a radius of one.
     */
    public static Quaternion shortestArc(Vector3 from, Vector3 to) {
        var dot = from.dot(to);
        if(dot < -1.0 + MathUtils.EPSILON) {
            return new Quaternion(0.0f, 0.0f, 1.0f, 0.0f);
        } else {
            var s = (float) Math.sqrt((1.0 + dot) * 2.0);
            return new Quaternion(s * 0.5f, from.cross(to).divide(s));
        }
    }
}