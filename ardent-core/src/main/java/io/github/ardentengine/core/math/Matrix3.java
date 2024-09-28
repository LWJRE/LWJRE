package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 3x3 float matrix.
 *
 * @param m00 Element 0 0
 * @param m01 Element 0 1
 * @param m02 Element 0 2
 * @param m10 Element 1 0
 * @param m11 Element 1 1
 * @param m12 Element 1 2
 * @param m20 Element 2 0
 * @param m21 Element 2 1
 * @param m22 Element 2 2
 */
public record Matrix3(
    float m00, float m01, float m02,
    float m10, float m11, float m12,
    float m20, float m21, float m22
) implements Serializable {

    /** Shorthand for the zero matrix. */
    public static Matrix3 ZERO = new Matrix3(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    /** Shorthand for the identity matrix. */
    public static Matrix3 IDENTITY = new Matrix3(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f);

    /**
     * Constructs a 3x3 matrix from a 2x3 matrix and an additional row.
     *
     * @param m The 2x3 matrix representing the first two rows.
     * @param m20 First element of the third row.
     * @param m21 Second element of the third row.
     * @param m22 Third element of the third row.
     */
    public Matrix3(Matrix2x3 m, float m20, float m21, float m22) {
        this(m.m00(), m.m01(), m.m02(), m.m10(), m.m11(), m.m12(), m20, m21, m22);
    }

    /**
     * Constructs a 3x3 matrix from a 2x3 matrix and an additional row.
     *
     * @param m The 2x3 matrix representing the first two rows.
     * @param row2 The third row.
     */
    public Matrix3(Matrix2x3 m, Vector3 row2) {
        this(m, row2.x(), row2.y(), row2.z());
    }

    /**
     * Constructs a 3x3 matrix from a 2x3 matrix and an additional row.
     *
     * @param m00 First element of the first row.
     * @param m01 Second element of the first row.
     * @param m02 Third element of the first row.
     * @param m The 2x3 matrix representing the second and third row.
     */
    public Matrix3(float m00, float m01, float m02, Matrix2x3 m) {
        this(m00, m01, m02, m.m00(), m.m01(), m.m02(), m.m10(), m.m11(), m.m12());
    }

    /**
     * Constructs a 3x3 matrix from a 2x3 matrix and an additional row.
     *
     * @param row0 The first row.
     * @param m The 2x3 matrix representing the second and third row.
     */
    public Matrix3(Vector3 row0, Matrix2x3 m) {
        this(row0.x(), row0.y(), row0.z(), m);
    }

    /**
     * Adds the given matrix to this one and returns the result.
     *
     * @param m The matrix to add.
     * @return The sum between this matrix and the given one.
     */
    public Matrix3 plus(Matrix3 m) {
        return new Matrix3(
            this.m00() + m.m00(), this.m01() + m.m01(), this.m02() + m.m02(),
            this.m10() + m.m10(), this.m11() + m.m11(), this.m12() + m.m12(),
            this.m20() + m.m20(), this.m21() + m.m21(), this.m22() + m.m22()
        );
    }

    /**
     * Subtracts the given matrix from this one and returns the result.
     *
     * @param m The matrix to subtract.
     * @return The subtraction between this matrix and the given one.
     */
    public Matrix3 minus(Matrix3 m) {
        return new Matrix3(
            this.m00() - m.m00(), this.m01() - m.m01(), this.m02() - m.m02(),
            this.m10() - m.m10(), this.m11() - m.m11(), this.m12() - m.m12(),
            this.m20() - m.m20(), this.m21() - m.m21(), this.m22() - m.m22()
        );
    }

    /**
     * Returns the additive inverse of this matrix.
     *
     * @return The additive inverse of this matrix.
     */
    public Matrix3 negated() {
        return new Matrix3(
            -this.m00(), -this.m01(), -this.m02(),
            -this.m10(), -this.m11(), -this.m12(),
            -this.m20(), -this.m21(), -this.m22()
        );
    }

    /**
     * Multiplies this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this matrix by.
     * @return The product between this matrix and the given scalar.
     */
    public Matrix3 multiply(float k) {
        return new Matrix3(
            this.m00() * k, this.m01() * k, this.m02() * k,
            this.m10() * k, this.m11() * k, this.m12() * k,
            this.m20() * k, this.m21() * k, this.m22() * k
        );
    }

    /**
     * Divides this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to divide this matrix by.
     * @return The division between this matrix and the given scalar.
     */
    public Matrix3 divide(float k) {
        return new Matrix3(
            this.m00() / k, this.m01() / k, this.m02() / k,
            this.m10() / k, this.m11() / k, this.m12() / k,
            this.m20() / k, this.m21() / k, this.m22() / k
        );
    }

    /**
     * Returns the first row of this matrix as a {@link Vector3}.
     *
     * @return The first row of this matrix.
     * @see Matrix3#row(int)
     */
    public Vector3 row0() {
        return new Vector3(this.m00(), this.m01(), this.m02());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector3}.
     *
     * @return The second row of this matrix.
     * @see Matrix3#row(int)
     */
    public Vector3 row1() {
        return new Vector3(this.m10(), this.m11(), this.m12());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector3}.
     *
     * @return The second row of this matrix.
     * @see Matrix3#row(int)
     */
    public Vector3 row2() {
        return new Vector3(this.m20(), this.m21(), this.m22());
    }

    /**
     * Returns the row at the given index as a {@link Vector3}.
     *
     * @param i The index of the requested row. Must be either 0, 1, or 2.
     * @return The row at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector3 row(int i) {
        return switch (i) {
            case 0 -> this.row0();
            case 1 -> this.row1();
            case 2 -> this.row2();
            default -> throw new IndexOutOfBoundsException("Row " + i + " is out of bounds for a 3x3 matrix");
        };
    }

    /**
     * Returns the first column of this matrix as a {@link Vector3}.
     *
     * @return The first column of this matrix.
     * @see Matrix3#column(int)
     */
    public Vector3 column0() {
        return new Vector3(this.m00(), this.m10(), this.m20());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector3}.
     *
     * @return The second column of this matrix.
     * @see Matrix3#column(int)
     */
    public Vector3 column1() {
        return new Vector3(this.m01(), this.m11(), this.m21());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector3}.
     *
     * @return The second column of this matrix.
     * @see Matrix3#column(int)
     */
    public Vector3 column2() {
        return new Vector3(this.m02(), this.m12(), this.m22());
    }

    /**
     * Returns the column at the given index as a {@link Vector3}.
     *
     * @param i The index of the requested column. Must be either 0, 1, or 2.
     * @return The column at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector3 column(int i) {
        return switch (i) {
            case 0 -> this.column0();
            case 1 -> this.column1();
            case 2 -> this.column2();
            default -> throw new IndexOutOfBoundsException("Column " + i + " is out of bounds for a 3x3 matrix");
        };
    }

    /**
     * Multiplies this matrix by the vector with the given components and returns the result.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @return The product of this matrix by the vector with the given components.
     */
    public Vector3 multiply(float x, float y, float z) {
        return new Vector3(
            this.m00() * x + this.m01() * y + this.m02() * z,
            this.m10() * x + this.m11() * y + this.m12() * z,
            this.m20() * x + this.m21() * y + this.m22() * z
        );
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     *
     * @param v The vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector3 multiply(Vector3 v) {
        return this.multiply(v.x(), v.y(), v.z());
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     * <p>
     *     This method can be used to simplify the transformation of a 2D vector by a 3x3 transformation matrix.
     * </p>
     *
     * @param xy The x and y components of the vector to multiply this matrix by.
     * @param z The z component of the vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector3 multiply(Vector2 xy, float z) {
        return this.multiply(xy.x(), xy.y(), z);
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix3 multiply(Matrix3 m) {
        return new Matrix3(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m.m20(), this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m.m21(), this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m.m22(),
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m.m20(), this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m.m21(), this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m.m22(),
            this.m20() * m.m00() + this.m21() * m.m10() + this.m22() * m.m20(), this.m20() * m.m01() + this.m21() * m.m11() + this.m22() * m.m21(), this.m20() * m.m02() + this.m21() * m.m12() + this.m22() * m.m22()
        );
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix3x4 multiply(Matrix3x4 m) {
        return new Matrix3x4(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m.m20(), this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m.m21(), this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m.m22(), this.m00() * m.m03() + this.m01() * m.m13() + this.m02() * m.m23(),
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m.m20(), this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m.m21(), this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m.m22(), this.m10() * m.m03() + this.m11() * m.m13() + this.m12() * m.m23(),
            this.m20() * m.m00() + this.m21() * m.m10() + this.m22() * m.m20(), this.m20() * m.m01() + this.m21() * m.m11() + this.m22() * m.m21(), this.m20() * m.m02() + this.m21() * m.m12() + this.m22() * m.m22(), this.m20() * m.m03() + this.m21() * m.m13() + this.m22() * m.m23()
        );
    }

    /**
     * Returns the transposed of this matrix.
     * <p>
     *     The transposed of a matrix is obtained by switching its rows and its columns.
     * </p>
     *
     * @return The transposed of this matrix.
     */
    public Matrix3 transposed() {
        return new Matrix3(
            this.m00(), this.m10(), this.m20(),
            this.m01(), this.m11(), this.m21(),
            this.m02(), this.m12(), this.m22()
        );
    }

    /**
     * Checks if this matrix is symmetric by checking if it is equal to its {@link Matrix3#transposed()}.
     *
     * @return True if this matrix is equal to its transposed, otherwise false.
     */
    public boolean isSymmetric() {
        return this.m01() == this.m10() && this.m02() == this.m20() && this.m12() == this.m21();
    }

    /**
     * Checks if this matrix is skew-symmetric by checking if it is equal to its {@link Matrix3#negated()} {@link Matrix3#transposed()}.
     *
     * @return True if this matrix is equal to its negated transposed, otherwise false.
     */
    public boolean isSkewSymmetric() {
        return this.m01() == -this.m10() && this.m02() == -this.m20() && this.m12() == -this.m21();
    }

    /**
     * Returns the determinant of this matrix.
     *
     * @return The determinant of this matrix.
     */
    public float determinant() {
        return this.m00() * (this.m11() * this.m22() - this.m21() * this.m12())
            - this.m01() * (this.m10() * this.m22() - this.m20() * this.m12())
            + this.m02() * (this.m10() * this.m21() - this.m20() * this.m11());
    }

    /**
     * Returns the inverse of this matrix.
     * <p>
     * The result is undefined if this matrix is not invertible.
     * It is possible to check if the matrix is invertible by checking if its {@link Matrix3#determinant()} is not zero.
     *
     * @return The inverse of this matrix.
     */
    public Matrix3 inverse() {
        var det = this.determinant();
        return new Matrix3(
            (this.m11() * this.m22() - this.m21() * this.m12()) / det,
            (this.m02() * this.m21() - this.m01() * this.m22()) / det,
            (this.m01() * this.m12() - this.m11() * this.m02()) / det,
            (this.m12() * this.m20() - this.m10() * this.m22()) / det,
            (this.m00() * this.m22() - this.m20() * this.m02()) / det,
            (this.m02() * this.m10() - this.m00() * this.m12()) / det,
            (this.m10() * this.m21() - this.m20() * this.m11()) / det,
            (this.m20() * this.m01() - this.m00() * this.m21()) / det,
            (this.m00() * this.m11() - this.m10() * this.m01()) / det
        );
    }

    /**
     * Raises this matrix to the given power by multiplying it with itself {@code exp} times and returns the result.
     * <p>
     *     If the given exponent is zero, the result will be the identity matrix.
     * </p>
     * <p>
     *     A matrix raised to a negative power is defined as the {@link Matrix3#inverse()} matrix raised to {@code -exp}.
     *     The result is undefined if the given exponent is negative and this matrix is not invertible.
     * </p>
     *
     * @param exp The exponent to raise this matrix to.
     * @return This matrix raised to the given power.
     */
    public Matrix3 power(int exp) {
        if(exp == -1) {
            return this.inverse();
        } else if(exp < 0) {
            return this.inverse().power(-exp);
        } else if(exp == 1) {
            return this;
        } else if(exp == 0) {
            return Matrix3.IDENTITY;
        } else {
            return this.multiply(this.power(exp - 1));
        }
    }

    /**
     * Computes the linear interpolation between the elements of this matrix and the elements of the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     *
     * @param to The second matrix.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of linearly interpolating between the elements of this matrix and the elements of the given one.
     */
    public Matrix3 lerp(Matrix3 to, float weight) {
        return new Matrix3(
            MathUtils.lerp(this.m00(), to.m00(), weight), MathUtils.lerp(this.m01(), to.m01(), weight), MathUtils.lerp(this.m02(), to.m02(), weight),
            MathUtils.lerp(this.m10(), to.m10(), weight), MathUtils.lerp(this.m11(), to.m11(), weight), MathUtils.lerp(this.m12(), to.m12(), weight),
            MathUtils.lerp(this.m20(), to.m20(), weight), MathUtils.lerp(this.m21(), to.m21(), weight), MathUtils.lerp(this.m22(), to.m22(), weight)
        );
    }

    /**
     * Returns a matrix with all elements in absolute value.
     *
     * @return A matrix with all elements in absolute value.
     */
    public Matrix3 abs() {
        return new Matrix3(
            Math.abs(this.m00()), Math.abs(this.m01()), Math.abs(this.m02()),
            Math.abs(this.m10()), Math.abs(this.m11()), Math.abs(this.m12()),
            Math.abs(this.m20()), Math.abs(this.m21()), Math.abs(this.m22())
        );
    }

    /**
     * Returns a matrix with the signs of the elements of this matrix.
     *
     * @return A matrix with all elements representing the sign of this matrix.
     */
    public Matrix3 sign() {
        return new Matrix3(
            Math.signum(this.m00()), Math.signum(this.m01()), Math.signum(this.m02()),
            Math.signum(this.m10()), Math.signum(this.m11()), Math.signum(this.m12()),
            Math.signum(this.m20()), Math.signum(this.m21()), Math.signum(this.m22())
        );
    }

    /**
     * Returns a matrix with all elements rounded to the nearest integer.
     *
     * @return A matrix with all elements rounded to the nearest integer.
     */
    public Matrix3 round() {
        return new Matrix3(
            Math.round(this.m00()), Math.round(this.m01()), Math.round(this.m02()),
            Math.round(this.m10()), Math.round(this.m11()), Math.round(this.m12()),
            Math.round(this.m20()), Math.round(this.m21()), Math.round(this.m22())
        );
    }

    /**
     * Returns a matrix with all elements rounded up.
     *
     * @return A matrix with all elements rounded up.
     */
    public Matrix3 ceil() {
        return new Matrix3(
            (float) Math.ceil(this.m00()), (float) Math.ceil(this.m01()), (float) Math.ceil(this.m02()),
            (float) Math.ceil(this.m10()), (float) Math.ceil(this.m11()), (float) Math.ceil(this.m12()),
            (float) Math.ceil(this.m20()), (float) Math.ceil(this.m21()), (float) Math.ceil(this.m22())
        );
    }

    /**
     * Returns a matrix with all elements rounded down.
     *
     * @return A matrix with all elements rounded down.
     */
    public Matrix3 floor() {
        return new Matrix3(
            (float) Math.floor(this.m00()), (float) Math.floor(this.m01()), (float) Math.floor(this.m02()),
            (float) Math.floor(this.m10()), (float) Math.floor(this.m11()), (float) Math.floor(this.m12()),
            (float) Math.floor(this.m20()), (float) Math.floor(this.m21()), (float) Math.floor(this.m22())
        );
    }

    /**
     * Constructs an orthonormal matrix from the columns of this matrix using the Gram-Schmidt procedure.
     *
     * @return This matrix with orthogonal columns of unit length.
     */
    public Matrix3 orthonormalized() {
        var v0 = this.column0();
        var v1 = this.column1();
        var v2 = this.column2();
        v1 = v1.minus(v1.project(v0));
        v2 = v2.minus(v2.project(v0)).minus(v2.project(v1));
        return Matrix3.fromColumns(v0.normalized(), v1.normalized(), v2.normalized());
    }

    /**
     * Checks if this matrix is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param m The second matrix.
     * @return True if this matrix is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(Matrix3 m) {
        return MathUtils.equalsApprox(this.m00(), m.m00()) && MathUtils.equalsApprox(this.m01(), m.m01()) && MathUtils.equalsApprox(this.m02(), m.m02())
            && MathUtils.equalsApprox(this.m10(), m.m10()) && MathUtils.equalsApprox(this.m11(), m.m11()) && MathUtils.equalsApprox(this.m12(), m.m12())
            && MathUtils.equalsApprox(this.m20(), m.m20()) && MathUtils.equalsApprox(this.m21(), m.m21()) && MathUtils.equalsApprox(this.m22(), m.m22());
    }

    /**
     * Returns a 2x2 matrix from the given rows.
     *
     * @param row0 The first row.
     * @param row1 The second row.
     * @return A 2x2 matrix from the given rows.
     */
    public static Matrix3 fromRows(Vector3 row0, Vector3 row1, Vector3 row2) {
        return new Matrix3(row0.x(), row0.y(), row0.z(), row1.x(), row1.y(), row1.z(), row2.x(), row2.y(), row2.z());
    }

    /**
     * Returns a 2x2 matrix from the given columns.
     *
     * @param column0 The first column.
     * @param column1 The second column.
     * @return A 2x2 matrix from the given columns.
     */
    public static Matrix3 fromColumns(Vector3 column0, Vector3 column1, Vector3 column2) {
        return new Matrix3(column0.x(), column1.x(), column2.x(), column0.y(), column1.y(), column2.y(), column0.z(), column1.z(), column2.z());
    }

    /**
     * Returns a 3x3 matrix representing a translation in a 2D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     * @return A 3x3 matrix representing a translation in a 2D space.
     */
    public static Matrix3 translation(float x, float y) {
        return new Matrix3(1.0f, 0.0f, x, 0.0f, 1.0f, y, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 3x3 matrix representing a translation in a 2D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param t The translation vector.
     * @return A 3x3 matrix representing a translation in a 2D space.
     */
    public static Matrix3 translation(Vector2 t) {
        return translation(t.x(), t.y());
    }

    /**
     * Returns a 3x3 rotation matrix with the given rotation on the x axis.
     *
     * @param x Rotation angle in radians.
     * @return A 3x3 rotation matrix.
     */
    public static Matrix3 rotationX(double x) {
        var sin = (float) Math.sin(x);
        var cos = (float) Math.cos(x);
        return new Matrix3(1.0f, 0.0f, 0.0f, 0.0f, cos, -sin, 0.0f, sin, cos);
    }

    /**
     * Returns a 3x3 rotation matrix with the given rotation on the y axis.
     *
     * @param y Rotation angle in radians.
     * @return A 3x3 rotation matrix.
     */
    public static Matrix3 rotationY(double y) {
        var sin = (float) Math.sin(y);
        var cos = (float) Math.cos(y);
        return new Matrix3(cos, 0.0f, sin, 0.0f, 1.0f, 0.0f, -sin, 0.0f, cos);
    }

    /**
     * Returns a 3x3 rotation matrix with the given rotation on the z axis.
     *
     * @param z Rotation angle in radians.
     * @return A 3x3 rotation matrix.
     */
    public static Matrix3 rotationZ(double z) {
        var sin = (float) Math.sin(z);
        var cos = (float) Math.cos(z);
        return new Matrix3(cos, -sin, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 3x3 rotation matrix with the given rotation.
     * The rotation is first applied on the z axis, then on the y axis, then on the x axis.
     *
     * @param x Rotation angle in radians on the x axis.
     * @param y Rotation angle in radians on the y axis.
     * @param z Rotation angle in radians on the z axis.
     * @return A 3x3 rotation matrix with the given rotation.
     */
    public static Matrix3 rotation(double x, double y, double z) {
        var sx = (float) Math.sin(x);
        var cx = (float) Math.cos(x);
        var sy = (float) Math.sin(y);
        var cy = (float) Math.cos(y);
        var sz = (float) Math.sin(z);
        var cz = (float) Math.cos(z);
        return new Matrix3(
            cy * cz, cy * -sz, sy,
            -sx * -sy * cz + cx * sz, -sx * -sy * -sz + cx * cz, -sx * cy,
            cx * -sy * cz + sx * sz, cx * -sy * -sz + sx * cz, cx * cy
        );
    }

    /**
     * Returns a 3x3 rotation matrix with the given rotation.
     * The rotation is first applied on the z axis, then on the y axis, then on the x axis.
     *
     * @param r A vector representing the rotation angle on the x, y, and z axes.
     * @return A 3x3 rotation matrix with the given rotation.
     */
    public static Matrix3 rotation(Vector3 r) {
        return rotation(r.x(), r.y(), r.z());
    }

    /**
     * Returns a 3x3 rotation matrix with a rotation of the given angle around the given axis.
     *
     * @param axis A unit vector representing the rotation axis. The result is undefined if this vector is not {@link Vector3#normalized()}.
     * @param angle The rotation angle in radians.
     * @return A 3x3 rotation matrix with the given rotation.
     */
    public static Matrix3 rotation(Vector3 axis, double angle) {
        var sin = (float) Math.sin(angle);
        var cos = (float) Math.cos(angle);
        return new Matrix3(
            cos + axis.x() * axis.x() * (1.0f - cos), axis.x() * axis.y() * (1.0f - cos) - axis.z() * sin, axis.x() * axis.z() * (1.0f - cos) + axis.y() * sin,
            axis.y() * axis.x() * (1.0f - cos) + axis.z() * sin, cos + axis.y() * axis.y() * (1.0f - cos), axis.y() * axis.z() * (1.0f - cos) - axis.x() * sin,
            axis.z() * axis.x() * (1.0f - cos) - axis.y() * sin, axis.z() * axis.y() * (1.0f - cos) + axis.x() * sin, cos + axis.z() * axis.z() * (1.0f - cos)
        );
    }

    /**
     * Returns a 3x3 rotation matrix with the rotation expressed by the given quaternion.
     *
     * @param q The rotation quaternion. Must be a unit quaternion.
     * @return A 3x3 rotation matrix with the rotation expressed by the given quaternion.
     */
    public static Matrix3 rotation(Quaternion q) {
        var s = 2.0f / q.lengthSquared();
        return new Matrix3(
            1.0f - s * (q.y() * q.y() + q.z() * q.z()), s * (q.x() * q.y() - q.z() * q.w()), s * (q.x() * q.z() + q.y() * q.w()),
            s * (q.x() * q.y() + q.z() * q.w()), 1.0f - s * (q.x() * q.x() + q.z() * q.z()), s * (q.y() * q.z() - q.x() * q.w()),
            s * (q.x() * q.z() - q.y() * q.w()), s * (q.y() * q.z() + q.x() * q.w()), 1.0f - s * (q.x() * q.x() + q.y() * q.y())
        );
    }

    /**
     * Returns a 3x3 matrix representing a scaling by the given factor in a 3D space.
     *
     * @param x Scale factor on the x axis.
     * @param y Scale factor on the y axis.
     * @param z Scale factor on the z axis.
     * @return A 3x3 matrix representing a scaling by the given factor in a 3D space.
     */
    public static Matrix3 scaling(float x, float y, float z) {
        return new Matrix3(x, 0.0f, 0.0f, 0.0f, y, 0.0f, 0.0f, 0.0f, z);
    }

    /**
     * Returns a 3x3 matrix representing a scaling by the given factor in a 3D space.
     *
     * @param s A vector representing the scale factor on the x, y, and z axes.
     * @return A 3x3 matrix representing a scaling by the given factor in a 3D space.
     */
    public static Matrix3 scaling(Vector3 s) {
        return scaling(s.x(), s.y(), s.z());
    }

    /**
     * Returns a 3x3 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param s A vector representing the scale factor on the x and y axes.
     * @return A 3x3 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix3 scaling(Vector2 s) {
        return scaling(s.x(), s.y(), 1.0f);
    }
}