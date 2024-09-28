package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 3x4 (three rows and four columns) float matrix.
 * <p>
 *     Useful for representing 3D transformations such as translation, rotation, and scaling.
 * </p>
 *
 * @param m00 Element 0 0
 * @param m01 Element 0 1
 * @param m02 Element 0 2
 * @param m03 Element 0 3
 * @param m10 Element 1 0
 * @param m11 Element 1 1
 * @param m12 Element 1 2
 * @param m13 Element 1 3
 * @param m20 Element 2 0
 * @param m21 Element 2 1
 * @param m22 Element 2 2
 * @param m23 Element 2 3
 */
public record Matrix3x4(
    float m00, float m01, float m02, float m03,
    float m10, float m11, float m12, float m13,
    float m20, float m21, float m22, float m23
) implements Serializable {

    /** Shorthand for the zero matrix. */
    public static Matrix3x4 ZERO = new Matrix3x4(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    /**
     * Constructs a 3x4 matrix from a 3x3 matrix and an additional column.
     *
     * @param m The 3x3 matrix representing the first three columns.
     * @param m03 First element of the fourth column.
     * @param m13 Second element of the fourth column.
     * @param m23 Third element of the fourth column.
     */
    public Matrix3x4(Matrix3 m, float m03, float m13, float m23) {
        this(
            m.m00(), m.m01(), m.m02(), m03,
            m.m10(), m.m11(), m.m12(), m13,
            m.m20(), m.m21(), m.m22(), m23
        );
    }

    /**
     * Constructs a 3x4 matrix from a 3x3 matrix and an additional column.
     *
     * @param m The 3x3 matrix representing the first three columns.
     * @param col3 The fourth column.
     */
    public Matrix3x4(Matrix3 m, Vector3 col3) {
        this(m, col3.x(), col3.y(), col3.z());
    }

    /**
     * Constructs a 3x4 matrix from a 3x3 matrix and an additional column.
     *
     * @param m00 First element of the first column.
     * @param m10 Second element of the first column.
     * @param m20 Third element of the first column.
     * @param m The 3x3 matrix representing the second, third, and fourth column.
     */
    public Matrix3x4(float m00, float m10, float m20, Matrix3 m) {
        this(
            m00, m.m00(), m.m01(), m.m02(),
            m10, m.m10(), m.m11(), m.m12(),
            m20, m.m20(), m.m21(), m.m22()
        );
    }

    /**
     * Constructs a 3x4 matrix from a 3x3 matrix and an additional column.
     *
     * @param col0 The first column of this matrix.
     * @param m The second, third, and fourth column of this matrix.
     */
    public Matrix3x4(Vector3 col0, Matrix3 m) {
        this(col0.x(), col0.y(), col0.z(), m);
    }

    /**
     * Adds the given matrix to this one and returns the result.
     *
     * @param m The matrix to add.
     * @return The sum between this matrix and the given one.
     */
    public Matrix3x4 plus(Matrix3x4 m) {
        return new Matrix3x4(
            this.m00() + m.m00(), this.m01() + m.m01(), this.m02() + m.m02(), this.m03() + m.m03(),
            this.m10() + m.m10(), this.m11() + m.m11(), this.m12() + m.m12(), this.m13() + m.m13(),
            this.m20() + m.m20(), this.m21() + m.m21(), this.m22() + m.m22(), this.m23() + m.m23()
        );
    }

    /**
     * Subtracts the given matrix from this one and returns the result.
     *
     * @param m The matrix to subtract.
     * @return The subtraction between this matrix and the given one.
     */
    public Matrix3x4 minus(Matrix3x4 m) {
        return new Matrix3x4(
            this.m00() - m.m00(), this.m01() - m.m01(), this.m02() - m.m02(), this.m03() - m.m03(),
            this.m10() - m.m10(), this.m11() - m.m11(), this.m12() - m.m12(), this.m13() - m.m13(),
            this.m20() - m.m20(), this.m21() - m.m21(), this.m22() - m.m22(), this.m23() - m.m23()
        );
    }

    /**
     * Returns the additive inverse of this matrix.
     *
     * @return The additive inverse of this matrix.
     */
    public Matrix3x4 negated() {
        return new Matrix3x4(
            -this.m00(), -this.m01(), -this.m02(), -this.m03(),
            -this.m10(), -this.m11(), -this.m12(), -this.m13(),
            -this.m20(), -this.m21(), -this.m22(), -this.m23()
        );
    }

    /**
     * Multiplies this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this matrix by.
     * @return The product between this matrix and the given scalar.
     */
    public Matrix3x4 multiply(float k) {
        return new Matrix3x4(
            this.m00() * k, this.m01() * k, this.m02() * k, this.m03() * k,
            this.m10() * k, this.m11() * k, this.m12() * k, this.m13() * k,
            this.m20() * k, this.m21() * k, this.m22() * k, this.m23() * k
        );
    }

    /**
     * Divides this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to divide this matrix by.
     * @return The division between this matrix and the given scalar.
     */
    public Matrix3x4 divide(float k) {
        return new Matrix3x4(
            this.m00() / k, this.m01() / k, this.m02() / k, this.m03() / k,
            this.m10() / k, this.m11() / k, this.m12() / k, this.m13() / k,
            this.m20() / k, this.m21() / k, this.m22() / k, this.m23() / k
        );
    }

    /**
     * Returns the first row of this matrix as a {@link Vector4}.
     *
     * @return The first row of this matrix.
     * @see Matrix3x4#row(int)
     */
    public Vector4 row0() {
        return new Vector4(this.m00(), this.m01(), this.m02(), this.m03());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector4}.
     *
     * @return The second row of this matrix.
     * @see Matrix3x4#row(int)
     */
    public Vector4 row1() {
        return new Vector4(this.m10(), this.m11(), this.m12(), this.m13());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector4}.
     *
     * @return The second row of this matrix.
     * @see Matrix3x4#row(int)
     */
    public Vector4 row2() {
        return new Vector4(this.m20(), this.m21(), this.m22(), this.m23());
    }

    /**
     * Returns the row at the given index as a {@link Vector4}.
     *
     * @param i The index of the requested row. Must be either 0, 1, or 2.
     * @return The row at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector4 row(int i) {
        return switch (i) {
            case 0 -> this.row0();
            case 1 -> this.row1();
            case 2 -> this.row2();
            default -> throw new IndexOutOfBoundsException("Row " + i + " is out of bounds for a 3x4 matrix");
        };
    }

    /**
     * Returns the first column of this matrix as a {@link Vector3}.
     *
     * @return The first column of this matrix.
     * @see Matrix3x4#column(int)
     */
    public Vector3 column0() {
        return new Vector3(this.m00(), this.m10(), this.m20());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector3}.
     *
     * @return The second column of this matrix.
     * @see Matrix3x4#column(int)
     */
    public Vector3 column1() {
        return new Vector3(this.m01(), this.m11(), this.m21());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector3}.
     *
     * @return The second column of this matrix.
     * @see Matrix3x4#column(int)
     */
    public Vector3 column2() {
        return new Vector3(this.m02(), this.m12(), this.m22());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector3}.
     *
     * @return The second column of this matrix.
     * @see Matrix3x4#column(int)
     */
    public Vector3 column3() {
        return new Vector3(this.m03(), this.m13(), this.m23());
    }

    /**
     * Returns the column at the given index as a {@link Vector3}.
     *
     * @param i The index of the requested column. Must be either 0, 1, 2, or 3.
     * @return The column at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector3 column(int i) {
        return switch (i) {
            case 0 -> this.column0();
            case 1 -> this.column1();
            case 2 -> this.column2();
            case 3 -> this.column3();
            default -> throw new IndexOutOfBoundsException("Column " + i + " is out of bounds for a 3x4 matrix");
        };
    }

    /**
     * Multiplies this matrix by the vector with the given components and returns the result.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @return The product of this matrix by the vector with the given components.
     */
    public Vector3 multiply(float x, float y, float z, float w) {
        return new Vector3(
            this.m00() * x + this.m01() * y + this.m02() * z + this.m03() * w,
            this.m10() * x + this.m11() * y + this.m12() * z + this.m13() * w,
            this.m20() * x + this.m21() * y + this.m22() * z + this.m23() * w
        );
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     *
     * @param v The vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector3 multiply(Vector4 v) {
        return this.multiply(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     * <p>
     *     This method can be used to simplify the transformation of a 3D vector by a 3x4 transformation matrix.
     * </p>
     *
     * @param xyz The x, y, and z components of the vector to multiply this matrix by.
     * @param w The w component of the vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector3 multiply(Vector3 xyz, float w) {
        return this.multiply(xyz.x(), xyz.y(), xyz.z(), w);
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     * <p>
     *     This method can be used to simplify the transformation of a 2D vector by a 3x4 transformation matrix.
     * </p>
     *
     * @param xy The x and y components of the vector to multiply this matrix by.
     * @param z The z component of the vector to multiply this matrix by.
     * @param w The w component of the vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector3 multiply(Vector2 xy, float z, float w) {
        return this.multiply(xy.x(), xy.y(), z, w);
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix3x4 multiply(Matrix4 m) {
        return new Matrix3x4(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m.m20() + this.m03() * m.m30(), this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m.m21() + this.m03() * m.m31(), this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m.m22() + this.m03() * m.m32(), this.m00() * m.m03() + this.m01() * m.m13() + this.m02() * m.m23() + this.m03() * m.m33(),
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m.m20() + this.m13() * m.m30(), this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m.m21() + this.m13() * m.m31(), this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m.m22() + this.m13() * m.m32(), this.m10() * m.m03() + this.m11() * m.m13() + this.m12() * m.m23() + this.m13() * m.m33(),
            this.m20() * m.m00() + this.m21() * m.m10() + this.m22() * m.m20() + this.m23() * m.m30(), this.m20() * m.m01() + this.m21() * m.m11() + this.m22() * m.m21() + this.m23() * m.m31(), this.m20() * m.m02() + this.m21() * m.m12() + this.m22() * m.m22() + this.m23() * m.m32(), this.m20() * m.m03() + this.m21() * m.m13() + this.m22() * m.m23() + this.m23() * m.m33()
        );
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     * <p>
     *     This method can be used to simplify the composition of two 3x4 transformation matrices.
     * </p>
     *
     * @param m The matrix to multiply this one by.
     * @param m30 First element of the fourth row.
     * @param m31 Second element of the fourth row.
     * @param m32 Third element of the fourth row.
     * @param m33 Fourth element of the fourth row.
     * @return The product between this matrix and the given one.
     */
    public Matrix3x4 multiply(Matrix3x4 m, float m30, float m31, float m32, float m33) {
        return new Matrix3x4(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m.m20() + this.m03() * m30, this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m.m21() + this.m03() * m31, this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m.m22() + this.m03() * m32, this.m00() * m.m03() + this.m01() * m.m13() + this.m02() * m.m23() + this.m03() * m33,
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m.m20() + this.m13() * m30, this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m.m21() + this.m13() * m31, this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m.m22() + this.m13() * m32, this.m10() * m.m03() + this.m11() * m.m13() + this.m12() * m.m23() + this.m13() * m33,
            this.m20() * m.m00() + this.m21() * m.m10() + this.m22() * m.m20() + this.m23() * m30, this.m20() * m.m01() + this.m21() * m.m11() + this.m22() * m.m21() + this.m23() * m31, this.m20() * m.m02() + this.m21() * m.m12() + this.m22() * m.m22() + this.m23() * m32, this.m20() * m.m03() + this.m21() * m.m13() + this.m22() * m.m23() + this.m23() * m33
        );
    }

    /**
     * Returns the inverse of this matrix, under the assumption that it is an affine transformation matrix.
     * The result is undefined if the determinant of the basis is zero.
     *
     * @return The affine inverse of this matrix.
     */
    public Matrix3x4 affineInverse() {
        var basis = new Matrix3(this.m00(), this.m01(), this.m02(), this.m10(), this.m11(), this.m12(), this.m20(), this.m21(), this.m22());
        var inverseBasis = basis.inverse();
        return new Matrix3x4(inverseBasis, inverseBasis.multiply(-this.m03(), -this.m13(), -this.m23()));
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
    public Matrix3x4 lerp(Matrix3x4 to, float weight) {
        return new Matrix3x4(
            MathUtils.lerp(this.m00(), to.m00(), weight), MathUtils.lerp(this.m01(), to.m01(), weight), MathUtils.lerp(this.m02(), to.m02(), weight), MathUtils.lerp(this.m03(), to.m03(), weight),
            MathUtils.lerp(this.m10(), to.m10(), weight), MathUtils.lerp(this.m11(), to.m11(), weight), MathUtils.lerp(this.m12(), to.m12(), weight), MathUtils.lerp(this.m13(), to.m13(), weight),
            MathUtils.lerp(this.m20(), to.m20(), weight), MathUtils.lerp(this.m21(), to.m21(), weight), MathUtils.lerp(this.m22(), to.m22(), weight), MathUtils.lerp(this.m23(), to.m23(), weight)
        );
    }

    /**
     * Returns a matrix with all elements in absolute value.
     *
     * @return A matrix with all elements in absolute value.
     */
    public Matrix3x4 abs() {
        return new Matrix3x4(
            Math.abs(this.m00()), Math.abs(this.m01()), Math.abs(this.m02()), Math.abs(this.m03()),
            Math.abs(this.m10()), Math.abs(this.m11()), Math.abs(this.m12()), Math.abs(this.m13()),
            Math.abs(this.m20()), Math.abs(this.m21()), Math.abs(this.m22()), Math.abs(this.m23())
        );
    }

    /**
     * Returns a matrix with the signs of the elements of this matrix.
     *
     * @return A matrix with all elements representing the sign of this matrix.
     */
    public Matrix3x4 sign() {
        return new Matrix3x4(
            Math.signum(this.m00()), Math.signum(this.m01()), Math.signum(this.m02()), Math.signum(this.m03()),
            Math.signum(this.m10()), Math.signum(this.m11()), Math.signum(this.m12()), Math.signum(this.m13()),
            Math.signum(this.m20()), Math.signum(this.m21()), Math.signum(this.m22()), Math.signum(this.m23())
        );
    }

    /**
     * Returns a matrix with all elements rounded to the nearest integer.
     *
     * @return A matrix with all elements rounded to the nearest integer.
     */
    public Matrix3x4 round() {
        return new Matrix3x4(
            Math.round(this.m00()), Math.round(this.m01()), Math.round(this.m02()), Math.round(this.m03()),
            Math.round(this.m10()), Math.round(this.m11()), Math.round(this.m12()), Math.round(this.m13()),
            Math.round(this.m20()), Math.round(this.m21()), Math.round(this.m22()), Math.round(this.m23())
        );
    }

    /**
     * Returns a matrix with all elements rounded up.
     *
     * @return A matrix with all elements rounded up.
     */
    public Matrix3x4 ceil() {
        return new Matrix3x4(
            (float) Math.ceil(this.m00()), (float) Math.ceil(this.m01()), (float) Math.ceil(this.m02()), (float) Math.ceil(this.m03()),
            (float) Math.ceil(this.m10()), (float) Math.ceil(this.m11()), (float) Math.ceil(this.m12()), (float) Math.ceil(this.m13()),
            (float) Math.ceil(this.m20()), (float) Math.ceil(this.m21()), (float) Math.ceil(this.m22()), (float) Math.ceil(this.m23())
        );
    }

    /**
     * Returns a matrix with all elements rounded down.
     *
     * @return A matrix with all elements rounded down.
     */
    public Matrix3x4 floor() {
        return new Matrix3x4(
            (float) Math.floor(this.m00()), (float) Math.floor(this.m01()), (float) Math.floor(this.m02()), (float) Math.floor(this.m03()),
            (float) Math.floor(this.m10()), (float) Math.floor(this.m11()), (float) Math.floor(this.m12()), (float) Math.floor(this.m13()),
            (float) Math.floor(this.m20()), (float) Math.floor(this.m21()), (float) Math.floor(this.m22()), (float) Math.floor(this.m23())
        );
    }

    /**
     * Constructs an orthonormal matrix from the columns of this matrix using the Gram-Schmidt procedure.
     *
     * @return This matrix with orthogonal columns of unit length.
     */
    public Matrix3x4 orthonormalized() {
        var v0 = this.column0();
        var v1 = this.column1();
        var v2 = this.column2();
        var v3 = this.column3();
        v1 = v1.minus(v1.project(v0));
        v2 = v2.minus(v2.project(v0)).minus(v2.project(v1));
        v3 = v3.minus(v3.project(v0)).minus(v3.project(v1)).minus(v3.project(v2));
        return Matrix3x4.fromColumns(v0.normalized(), v1.normalized(), v2.normalized(), v3.normalized());
    }

    /**
     * Checks if this matrix is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param m The second matrix.
     * @return True if this matrix is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(Matrix3x4 m) {
        return MathUtils.equalsApprox(this.m00(), m.m00()) && MathUtils.equalsApprox(this.m01(), m.m01()) && MathUtils.equalsApprox(this.m02(), m.m02()) && MathUtils.equalsApprox(this.m03(), m.m03())
            && MathUtils.equalsApprox(this.m10(), m.m10()) && MathUtils.equalsApprox(this.m11(), m.m11()) && MathUtils.equalsApprox(this.m12(), m.m12()) && MathUtils.equalsApprox(this.m13(), m.m13())
            && MathUtils.equalsApprox(this.m20(), m.m20()) && MathUtils.equalsApprox(this.m21(), m.m21()) && MathUtils.equalsApprox(this.m22(), m.m22()) && MathUtils.equalsApprox(this.m23(), m.m23());
    }

    /**
     * Returns a 3x4 matrix from the given rows.
     *
     * @param row0 The first row.
     * @param row1 The second row.
     * @return A 3x4 matrix from the given rows.
     */
    public static Matrix3x4 fromRows(Vector4 row0, Vector4 row1, Vector4 row2) {
        return new Matrix3x4(row0.x(), row0.y(), row0.z(), row0.w(), row1.x(), row1.y(), row1.z(), row1.w(), row2.x(), row2.y(), row2.z(), row2.w());
    }

    /**
     * Returns a 3x4 matrix from the given columns.
     *
     * @param column0 The first column.
     * @param column1 The second column.
     * @return A 3x4 matrix from the given columns.
     */
    public static Matrix3x4 fromColumns(Vector3 column0, Vector3 column1, Vector3 column2, Vector3 column3) {
        return new Matrix3x4(column0.x(), column1.x(), column2.x(), column3.x(), column0.y(), column1.y(), column2.y(), column3.y(), column0.z(), column1.z(), column2.z(), column3.z());
    }

    /**
     * Returns a 3x4 matrix representing a translation in a 3D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     * @param z Translation on the z axis.
     * @return A 3x4 matrix representing a translation in a 3D space.
     */
    public static Matrix3x4 translation(float x, float y, float z) {
        return new Matrix3x4(1.0f, 0.0f, 0.0f, x, 0.0f, 1.0f, 0.0f, y, 0.0f, 0.0f, 1.0f, z);
    }

    /**
     * Returns a 3x4 matrix representing a translation in a 3D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param t The translation vector.
     * @return A 3x4 matrix representing a translation in a 3D space.
     */
    public static Matrix3x4 translation(Vector3 t) {
        return translation(t.x(), t.y(), t.z());
    }

    /**
     * Returns a 3x4 matrix representing a translation in a 2D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 0.0f, 1.0f)}.
     * </p>
     *
     * @param t The translation vector.
     * @return A 3x4 matrix representing a translation in a 2D space.
     */
    public static Matrix3x4 translation(Vector2 t) {
        return translation(t.x(), t.y(), 0.0f);
    }

    /**
     * Returns a 3x4 rotation matrix with the given rotation on the x axis.
     *
     * @param x Rotation angle in radians.
     * @return A 3x4 rotation matrix.
     */
    public static Matrix3x4 rotationX(double x) {
        var sin = (float) Math.sin(x);
        var cos = (float) Math.cos(x);
        return new Matrix3x4(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f);
    }

    /**
     * Returns a 3x4 rotation matrix with the given rotation on the y axis.
     *
     * @param y Rotation angle in radians.
     * @return A 3x4 rotation matrix.
     */
    public static Matrix3x4 rotationY(double y) {
        var sin = (float) Math.sin(y);
        var cos = (float) Math.cos(y);
        return new Matrix3x4(cos, 0.0f, sin, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -sin, 0.0f, cos, 0.0f);
    }

    /**
     * Returns a 3x4 rotation matrix with the given rotation on the z axis.
     *
     * @param z Rotation angle in radians.
     * @return A 3x4 rotation matrix.
     */
    public static Matrix3x4 rotationZ(double z) {
        var sin = (float) Math.sin(z);
        var cos = (float) Math.cos(z);
        return new Matrix3x4(cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    /**
     * Returns a 3x4 rotation matrix with the given rotation.
     * The rotation is first applied on the z axis, then on the y axis, then on the x axis.
     *
     * @param x Rotation angle in radians on the x axis.
     * @param y Rotation angle in radians on the y axis.
     * @param z Rotation angle in radians on the z axis.
     * @return A 3x4 rotation matrix with the given rotation.
     */
    public static Matrix3x4 rotation(double x, double y, double z) {
        var sx = (float) Math.sin(x);
        var cx = (float) Math.cos(x);
        var sy = (float) Math.sin(y);
        var cy = (float) Math.cos(y);
        var sz = (float) Math.sin(z);
        var cz = (float) Math.cos(z);
        return new Matrix3x4(
            cy * cz, cy * -sz, sy, 0.0f,
            -sx * -sy * cz + cx * sz, -sx * -sy * -sz + cx * cz, -sx * cy, 0.0f,
            cx * -sy * cz + sx * sz, cx * -sy * -sz + sx * cz, cx * cy, 0.0f
        );
    }

    /**
     * Returns a 3x4 rotation matrix with the given rotation.
     * The rotation is first applied on the z axis, then on the y axis, then on the x axis.
     *
     * @param r A vector representing the rotation angle on the x, y, and z axes.
     * @return A 3x4 rotation matrix with the given rotation.
     */
    public static Matrix3x4 rotation(Vector3 r) {
        return rotation(r.x(), r.y(), r.z());
    }

    /**
     * Returns a 3x4 rotation matrix with a rotation of the given angle around the given axis.
     *
     * @param axis A unit vector representing the rotation axis. The result is undefined if this vector is not [[Vector3.normalized]].
     * @param angle The rotation angle in radians.
     * @return A 3x4 rotation matrix with the given rotation.
     */
    public static Matrix3x4 rotation(Vector3 axis, double angle) {
        var sin = (float) Math.sin(angle);
        var cos = (float) Math.cos(angle);
        return new Matrix3x4(
            cos + axis.x() * axis.x() * (1.0f - cos), axis.x() * axis.y() * (1.0f - cos) - axis.z() * sin, axis.x() * axis.z() * (1.0f - cos) + axis.y() * sin, 0.0f,
            axis.y() * axis.x() * (1.0f - cos) + axis.z() * sin, cos + axis.y() * axis.y() * (1.0f - cos), axis.y() * axis.z() * (1.0f - cos) - axis.x() * sin, 0.0f,
            axis.z() * axis.x() * (1.0f - cos) - axis.y() * sin, axis.z() * axis.y() * (1.0f - cos) + axis.x() * sin, cos + axis.z() * axis.z() * (1.0f - cos), 0.0f
        );
    }

    /**
     * Returns a 3x4 rotation matrix with the rotation expressed by the given quaternion.
     *
     * @param q The rotation quaternion. Must be a unit quaternion.
     * @return A 3x4 rotation matrix with the rotation expressed by the given quaternion.
     */
    public static Matrix3x4 rotation(Quaternion q) {
        var s = 2.0f / q.lengthSquared();
        return new Matrix3x4(
            1.0f - s * (q.y() * q.y() + q.z() * q.z()), s * (q.x() * q.y() - q.z() * q.w()), s * (q.x() * q.z() + q.y() * q.w()), 0.0f,
            s * (q.x() * q.y() + q.z() * q.w()), 1.0f - s * (q.x() * q.x() + q.z() * q.z()), s * (q.y() * q.z() - q.x() * q.w()), 0.0f,
            s * (q.x() * q.z() - q.y() * q.w()), s * (q.y() * q.z() + q.x() * q.w()), 1.0f - s * (q.x() * q.x() + q.y() * q.y()), 0.0f
        );
    }

    /**
     * Returns a 3x4 matrix representing a scaling by the given factor in a 3D space.
     *
     * @param x Scale factor on the x axis.
     * @param y Scale factor on the y axis.
     * @param z Scale factor on the z axis.
     * @return A 3x4 matrix representing a scaling by the given factor in a 3D space.
     */
    public static Matrix3x4 scaling(float x, float y, float z) {
        return new Matrix3x4(x, 0.0f, 0.0f, 0.0f, 0.0f, y, 0.0f, 0.0f, 0.0f, 0.0f, z, 0.0f);
    }

    /**
     * Returns a 3x4 matrix representing a scaling by the given factor in a 3D space.
     *
     * @param s A vector representing the scale factor on the x, y, and z axes.
     * @return A 3x4 matrix representing a scaling by the given factor in a 3D space.
     */
    public static Matrix3x4 scaling(Vector3 s) {
        return scaling(s.x(), s.y(), s.z());
    }

    /**
     * Returns a 3x4 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param s A vector representing the scale factor on the x and y axes.
     * @return A 3x4 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix3x4 scaling(Vector2 s) {
        return scaling(s.x(), s.y(), 1.0f);
    }
}