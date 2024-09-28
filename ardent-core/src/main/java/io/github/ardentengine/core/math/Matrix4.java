package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 3x4 float matrix.
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
public record Matrix4(
    float m00, float m01, float m02, float m03,
    float m10, float m11, float m12, float m13,
    float m20, float m21, float m22, float m23,
    float m30, float m31, float m32, float m33
) implements Serializable {

    /** Shorthand for the zero matrix. */
    public static Matrix4 ZERO = new Matrix4(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    /** Shorthand for the identity matrix. */
    public static Matrix4 IDENTITY = new Matrix4(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);

    /**
     * Constructs a 4x4 matrix from a 3x4 matrix and an additional row.
     *
     * @param m The 3x4 matrix representing the first two rows.
     * @param m30 First element of the fourth row.
     * @param m31 Second element of the fourth row.
     * @param m32 Third element of the fourth row.
     * @param m33 Fourth element of the fourth row.
     */
    public Matrix4(Matrix3x4 m, float m30, float m31, float m32, float m33) {
        this(m.m00(), m.m01(), m.m02(), m.m03(), m.m10(), m.m11(), m.m12(), m.m13(), m.m20(), m.m21(), m.m22(), m.m23(), m30, m31, m32, m33);
    }

    /**
     * Constructs a 4x4 matrix from a 3x4 matrix and an additional row.
     *
     * @param m The 3x4 matrix representing the first two rows.
     * @param row3 The fourth row.
     */
    public Matrix4(Matrix3x4 m, Vector4 row3) {
        this(m, row3.x(), row3.y(), row3.z(), row3.w());
    }

    /**
     * Constructs a 4x4 matrix from a 3x4 matrix and an additional row.
     *
     * @param m00 First element of the first row.
     * @param m01 Second element of the first row.
     * @param m02 Third element of the first row.
     * @param m03 Fourth element of the first row.
     * @param m The 3x4 matrix representing the first, second, and third rows.
     */
    public Matrix4(float m00, float m01, float m02, float m03, Matrix3x4 m) {
        this(m00, m01, m02, m03, m.m00(), m.m01(), m.m02(), m.m03(), m.m10(), m.m11(), m.m12(), m.m13(), m.m20(), m.m21(), m.m22(), m.m23());
    }

    /**
     * Constructs a 4x4 matrix from a 3x4 matrix and an additional row.
     *
     * @param row0 The first row.
     * @param m The 3x4 matrix representing the first, second, and third rows.
     */
    public Matrix4(Vector4 row0, Matrix3x4 m) {
        this(row0.x(), row0.y(), row0.z(), row0.w(), m);
    }

    /**
     * Adds the given matrix to this one and returns the result.
     *
     * @param m The matrix to add.
     * @return The sum between this matrix and the given one.
     */
    public Matrix4 plus(Matrix4 m) {
        return new Matrix4(
            this.m00() + m.m00(), this.m01() + m.m01(), this.m02() + m.m02(), this.m03() + m.m03(),
            this.m10() + m.m10(), this.m11() + m.m11(), this.m12() + m.m12(), this.m13() + m.m13(),
            this.m20() + m.m20(), this.m21() + m.m21(), this.m22() + m.m22(), this.m23() + m.m23(),
            this.m30() + m.m30(), this.m31() + m.m31(), this.m32() + m.m32(), this.m33() + m.m33()
        );
    }

    /**
     * Subtracts the given matrix from this one and returns the result.
     *
     * @param m The matrix to subtract.
     * @return The subtraction between this matrix and the given one.
     */
    public Matrix4 minus(Matrix4 m) {
        return new Matrix4(
            this.m00() - m.m00(), this.m01() - m.m01(), this.m02() - m.m02(), this.m03() - m.m03(),
            this.m10() - m.m10(), this.m11() - m.m11(), this.m12() - m.m12(), this.m13() - m.m13(),
            this.m20() - m.m20(), this.m21() - m.m21(), this.m22() - m.m22(), this.m23() - m.m23(),
            this.m30() - m.m30(), this.m31() - m.m31(), this.m32() - m.m32(), this.m33() - m.m33()
        );
    }

    /**
     * Returns the additive inverse of this matrix.
     *
     * @return The additive inverse of this matrix.
     */
    public Matrix4 negated() {
        return new Matrix4(
            -this.m00(), -this.m01(), -this.m02(), -this.m03(),
            -this.m10(), -this.m11(), -this.m12(), -this.m13(),
            -this.m20(), -this.m21(), -this.m22(), -this.m23(),
            -this.m30(), -this.m31(), -this.m32(), -this.m33()
        );
    }

    /**
     * Multiplies this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this matrix by.
     * @return The product between this matrix and the given scalar.
     */
    public Matrix4 multiply(float k) {
        return new Matrix4(
            this.m00() * k, this.m01() * k, this.m02() * k, this.m03() * k,
            this.m10() * k, this.m11() * k, this.m12() * k, this.m13() * k,
            this.m20() * k, this.m21() * k, this.m22() * k, this.m23() * k,
            this.m30() * k, this.m31() * k, this.m32() * k, this.m33() * k
        );
    }

    /**
     * Divides this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to divide this matrix by.
     * @return The division between this matrix and the given scalar.
     */
    public Matrix4 divide(float k) {
        return new Matrix4(
            this.m00() / k, this.m01() / k, this.m02() / k, this.m03() / k,
            this.m10() / k, this.m11() / k, this.m12() / k, this.m13() / k,
            this.m20() / k, this.m21() / k, this.m22() / k, this.m23() / k,
            this.m30() / k, this.m31() / k, this.m32() / k, this.m33() / k
        );
    }

    /**
     * Returns the first row of this matrix as a {@link Vector4}.
     *
     * @return The first row of this matrix.
     * @see Matrix4#row(int)
     */
    public Vector4 row0() {
        return new Vector4(this.m00(), this.m01(), this.m02(), this.m03());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector4}.
     *
     * @return The second row of this matrix.
     * @see Matrix4#row(int)
     */
    public Vector4 row1() {
        return new Vector4(this.m10(), this.m11(), this.m12(), this.m13());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector4}.
     *
     * @return The second row of this matrix.
     * @see Matrix4#row(int)
     */
    public Vector4 row2() {
        return new Vector4(this.m20(), this.m21(), this.m22(), this.m23());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector4}.
     *
     * @return The second row of this matrix.
     * @see Matrix4#row(int)
     */
    public Vector4 row3() {
        return new Vector4(this.m30(), this.m31(), this.m32(), this.m33());
    }

    /**
     * Returns the row at the given index as a {@link Vector4}.
     *
     * @param i The index of the requested row. Must be either 0, 1, 2, or 3.
     * @return The row at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector4 row(int i) {
        return switch (i) {
            case 0 -> this.row0();
            case 1 -> this.row1();
            case 2 -> this.row2();
            case 3 -> this.row3();
            default -> throw new IndexOutOfBoundsException("Row " + i + " is out of bounds for a 4x4 matrix");
        };
    }

    /**
     * Returns the first column of this matrix as a {@link Vector4}.
     *
     * @return The first column of this matrix.
     * @see Matrix4#column(int)
     */
    public Vector4 column0() {
        return new Vector4(this.m00(), this.m10(), this.m20(), this.m30());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector4}.
     *
     * @return The second column of this matrix.
     * @see Matrix4#column(int)
     */
    public Vector4 column1() {
        return new Vector4(this.m01(), this.m11(), this.m21(), this.m31());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector4}.
     *
     * @return The second column of this matrix.
     * @see Matrix4#column(int)
     */
    public Vector4 column2() {
        return new Vector4(this.m02(), this.m12(), this.m22(), this.m32());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector4}.
     *
     * @return The second column of this matrix.
     * @see Matrix4#column(int)
     */
    public Vector4 column3() {
        return new Vector4(this.m03(), this.m13(), this.m23(), this.m33());
    }

    /**
     * Returns the column at the given index as a {@link Vector4}.
     *
     * @param i The index of the requested column. Must be either 0, 1, 2, or 3.
     * @return The column at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector4 column(int i) {
        return switch (i) {
            case 0 -> this.column0();
            case 1 -> this.column1();
            case 2 -> this.column2();
            case 3 -> this.column3();
            default -> throw new IndexOutOfBoundsException("Column " + i + " is out of bounds for a 4x4 matrix");
        };
    }

    /**
     * Multiplies this matrix by the vector with the given components and returns the result.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @return The product of this matrix by the vector with the given components.
     */
    public Vector4 multiply(float x, float y, float z, float w) {
        return new Vector4(
            this.m00() * x + this.m01() * y + this.m02() * z + this.m03() * w,
            this.m10() * x + this.m11() * y + this.m12() * z + this.m13() * w,
            this.m20() * x + this.m21() * y + this.m22() * z + this.m23() * w,
            this.m30() * x + this.m31() * y + this.m32() * z + this.m33() * w
        );
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     *
     * @param v The vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector4 multiply(Vector4 v) {
        return this.multiply(v.x(), v.y(), v.z(), v.w());
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     * <p>
     *     This method can be used to simplify the transformation of a 3D vector by a 4x4 transformation matrix.
     * </p>
     *
     * @param xyz The x, y, and z components of the vector to multiply this matrix by.
     * @param w The w component of the vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector4 multiply(Vector3 xyz, float w) {
        return this.multiply(xyz.x(), xyz.y(), xyz.z(), w);
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     * <p>
     *     This method can be used to simplify the transformation of a 2D vector by a 4x4 transformation matrix.
     * </p>
     *
     * @param xy The x and y components of the vector to multiply this matrix by.
     * @param z The z component of the vector to multiply this matrix by.
     * @param w The w component of the vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector4 multiply(Vector2 xy, float z, float w) {
        return this.multiply(xy.x(), xy.y(), z, w);
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix4 multiply(Matrix4 m) {
        return new Matrix4(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m.m20() + this.m03() * m.m30(), this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m.m21() + this.m03() * m.m31(), this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m.m22() + this.m03() * m.m32(), this.m00() * m.m03() + this.m01() * m.m13() + this.m02() * m.m23() + this.m03() * m.m33(),
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m.m20() + this.m13() * m.m30(), this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m.m21() + this.m13() * m.m31(), this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m.m22() + this.m13() * m.m32(), this.m10() * m.m03() + this.m11() * m.m13() + this.m12() * m.m23() + this.m13() * m.m33(),
            this.m20() * m.m00() + this.m21() * m.m10() + this.m22() * m.m20() + this.m23() * m.m30(), this.m20() * m.m01() + this.m21() * m.m11() + this.m22() * m.m21() + this.m23() * m.m31(), this.m20() * m.m02() + this.m21() * m.m12() + this.m22() * m.m22() + this.m23() * m.m32(), this.m20() * m.m03() + this.m21() * m.m13() + this.m22() * m.m23() + this.m23() * m.m33(),
            this.m30() * m.m00() + this.m31() * m.m10() + this.m32() * m.m20() + this.m33() * m.m30(), this.m30() * m.m01() + this.m31() * m.m11() + this.m32() * m.m21() + this.m33() * m.m31(), this.m30() * m.m02() + this.m31() * m.m12() + this.m32() * m.m22() + this.m33() * m.m32(), this.m30() * m.m03() + this.m31() * m.m13() + this.m32() * m.m23() + this.m33() * m.m33()
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
    public Matrix4 transposed() {
        return new Matrix4(
            this.m00(), this.m10(), this.m20(), this.m30(),
            this.m01(), this.m11(), this.m21(), this.m31(),
            this.m02(), this.m12(), this.m22(), this.m32(),
            this.m03(), this.m13(), this.m23(), this.m33()
        );
    }

    /**
     * Checks if this matrix is symmetric by checking if it is equal to its {@link Matrix4#transposed()}.
     *
     * @return True if this matrix is equal to its transposed, otherwise false.
     */
    public boolean isSymmetric() {
        return this.m01() == this.m10() && this.m02() == this.m20() && this.m03() == this.m30() && this.m12() == this.m21() && this.m13() == this.m31() && this.m23() == this.m32();
    }

    /**
     * Checks if this matrix is skew-symmetric by checking if it is equal to its {@link Matrix4#negated()} {@link Matrix4#transposed()}.
     *
     * @return True if this matrix is equal to its negated transposed, otherwise false.
     */
    public boolean isSkewSymmetric() {
        return this.m01() == -this.m10() && this.m02() == -this.m20() && this.m03() == -this.m30() && this.m12() == -this.m21() && this.m13() == -this.m31() && this.m23() == -this.m32();
    }

    /**
     * Returns the determinant of this matrix.
     *
     * @return The determinant of this matrix.
     */
    public float determinant() {
        var d2323 = this.m22() * this.m33() - this.m23() * this.m32();
        var d1323 = this.m21() * this.m33() - this.m23() * this.m31();
        var d1223 = this.m21() * this.m32() - this.m22() * this.m31();
        var d0323 = this.m20() * this.m33() - this.m23() * this.m30();
        var d0223 = this.m20() * this.m32() - this.m22() * this.m30();
        var d0123 = this.m20() * this.m31() - this.m21() * this.m30();
        return this.m00() * (this.m11() * d2323 - this.m12() * d1323 + this.m13() * d1223)
            - this.m01() * (this.m10() * d2323 - this.m12() * d0323 + this.m13() * d0223)
            + this.m02() * (this.m10() * d1323 - this.m11() * d0323 + this.m13() * d0123)
            - this.m03() * (this.m10() * d1223 - this.m11() * d0223 + this.m12() * d0123);
    }

    /**
     * Returns the inverse of this matrix.
     * <p>
     *     The result is undefined if this matrix is not invertible.
     *     It is possible to check if the matrix is invertible by checking if its {@link Matrix4#determinant()} is not zero.
     * </p>
     *
     * @return The inverse of this matrix.
     */
    public Matrix4 inverse() {
        var d2323 = this.m22() * this.m33() - this.m23() * this.m32();
        var d1323 = this.m21() * this.m33() - this.m23() * this.m31();
        var d1223 = this.m21() * this.m32() - this.m22() * this.m31();
        var d0323 = this.m20() * this.m33() - this.m23() * this.m30();
        var d0223 = this.m20() * this.m32() - this.m22() * this.m30();
        var d0123 = this.m20() * this.m31() - this.m21() * this.m30();
        var d2313 = this.m12() * this.m33() - this.m13() * this.m32();
        var d1313 = this.m11() * this.m33() - this.m13() * this.m31();
        var d1213 = this.m11() * this.m32() - this.m12() * this.m31();
        var d2312 = this.m12() * this.m23() - this.m13() * this.m22();
        var d1312 = this.m11() * this.m23() - this.m13() * this.m21();
        var d1212 = this.m11() * this.m22() - this.m12() * this.m21();
        var d0313 = this.m10() * this.m33() - this.m13() * this.m30();
        var d0213 = this.m10() * this.m32() - this.m12() * this.m30();
        var d0312 = this.m10() * this.m23() - this.m13() * this.m20();
        var d0212 = this.m10() * this.m22() - this.m12() * this.m20();
        var d0113 = this.m10() * this.m31() - this.m11() * this.m30();
        var d0112 = this.m10() * this.m21() - this.m11() * this.m20();
        var det = this.m00() * (this.m11() * d2323 - this.m12() * d1323 + this.m13() * d1223)
            - this.m01() * (this.m10() * d2323 - this.m12() * d0323 + this.m13() * d0223)
            + this.m02() * (this.m10() * d1323 - this.m11() * d0323 + this.m13() * d0123)
            - this.m03() * (this.m10() * d1223 - this.m11() * d0223 + this.m12() * d0123);
        return new Matrix4(
            (this.m11() * d2323 - this.m12() * d1323 + this.m13() * d1223) / det,
            -(this.m01() * d2323 - this.m02() * d1323 + this.m03() * d1223) / det,
            (this.m01() * d2313 - this.m02() * d1313 + this.m03() * d1213) / det,
            -(this.m01() * d2312 - this.m02() * d1312 + this.m03() * d1212) / det,
            -(this.m10() * d2323 - this.m12() * d0323 + this.m13() * d0223) / det,
            (this.m00() * d2323 - this.m02() * d0323 + this.m03() * d0223) / det,
            -(this.m00() * d2313 - this.m02() * d0313 + this.m03() * d0213) / det,
            (this.m00() * d2312 - this.m02() * d0312 + this.m03() * d0212) / det,
            (this.m10() * d1323 - this.m11() * d0323 + this.m13() * d0123) / det,
            -(this.m00() * d1323 - this.m01() * d0323 + this.m03() * d0123) / det,
            (this.m00() * d1313 - this.m01() * d0313 + this.m03() * d0113) / det,
            -(this.m00() * d1312 - this.m01() * d0312 + this.m03() * d0112) / det,
            -(this.m10() * d1223 - this.m11() * d0223 + this.m12() * d0123) / det,
            (this.m00() * d1223 - this.m01() * d0223 + this.m02() * d0123) / det,
            -(this.m00() * d1213 - this.m01() * d0213 + this.m02() * d0113) / det,
            (this.m00() * d1212 - this.m01() * d0212 + this.m02() * d0112) / det
        );
    }

    /**
     * Raises this matrix to the given power by multiplying it with itself {@code exp} times and returns the result.
     * <p>
     *     If the given exponent is zero, the result will be the identity matrix.
     * </p>
     * <p>
     *     A matrix raised to a negative power is defined as the {@link Matrix4#inverse()} matrix raised to {@code -exp}.
     *     The result is undefined if the given exponent is negative and this matrix is not invertible.
     * </p>
     *
     * @param exp The exponent to raise this matrix to.
     * @return This matrix raised to the given power.
     */
    public Matrix4 power(int exp) {
        if(exp == -1) {
            return this.inverse();
        } else if(exp < 0) {
            return this.inverse().power(-exp);
        } else if(exp == 1) {
            return this;
        } else if(exp == 0) {
            return Matrix4.IDENTITY;
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
    public Matrix4 lerp(Matrix4 to, float weight) {
        return new Matrix4(
            MathUtils.lerp(this.m00(), to.m00(), weight), MathUtils.lerp(this.m01(), to.m01(), weight), MathUtils.lerp(this.m02(), to.m02(), weight), MathUtils.lerp(this.m03(), to.m03(), weight),
            MathUtils.lerp(this.m10(), to.m10(), weight), MathUtils.lerp(this.m11(), to.m11(), weight), MathUtils.lerp(this.m12(), to.m12(), weight), MathUtils.lerp(this.m13(), to.m13(), weight),
            MathUtils.lerp(this.m20(), to.m20(), weight), MathUtils.lerp(this.m21(), to.m21(), weight), MathUtils.lerp(this.m22(), to.m22(), weight), MathUtils.lerp(this.m23(), to.m23(), weight),
            MathUtils.lerp(this.m30(), to.m30(), weight), MathUtils.lerp(this.m31(), to.m31(), weight), MathUtils.lerp(this.m32(), to.m32(), weight), MathUtils.lerp(this.m33(), to.m33(), weight)
        );
    }

    /**
     * Returns a matrix with all elements in absolute value.
     *
     * @return A matrix with all elements in absolute value.
     */
    public Matrix4 abs() {
        return new Matrix4(
            Math.abs(this.m00()), Math.abs(this.m01()), Math.abs(this.m02()), Math.abs(this.m03()),
            Math.abs(this.m10()), Math.abs(this.m11()), Math.abs(this.m12()), Math.abs(this.m13()),
            Math.abs(this.m20()), Math.abs(this.m21()), Math.abs(this.m22()), Math.abs(this.m23()),
            Math.abs(this.m30()), Math.abs(this.m31()), Math.abs(this.m32()), Math.abs(this.m33())
        );
    }

    /**
     * Returns a matrix with the signs of the elements of this matrix.
     *
     * @return A matrix with all elements representing the sign of this matrix.
     */
    public Matrix4 sign() {
        return new Matrix4(
            Math.signum(this.m00()), Math.signum(this.m01()), Math.signum(this.m02()), Math.signum(this.m03()),
            Math.signum(this.m10()), Math.signum(this.m11()), Math.signum(this.m12()), Math.signum(this.m13()),
            Math.signum(this.m20()), Math.signum(this.m21()), Math.signum(this.m22()), Math.signum(this.m23()),
            Math.signum(this.m30()), Math.signum(this.m31()), Math.signum(this.m32()), Math.signum(this.m33())
        );
    }

    /**
     * Returns a matrix with all elements rounded to the nearest integer.
     *
     * @return A matrix with all elements rounded to the nearest integer.
     */
    public Matrix4 round() {
        return new Matrix4(
            Math.round(this.m00()), Math.round(this.m01()), Math.round(this.m02()), Math.round(this.m03()),
            Math.round(this.m10()), Math.round(this.m11()), Math.round(this.m12()), Math.round(this.m13()),
            Math.round(this.m20()), Math.round(this.m21()), Math.round(this.m22()), Math.round(this.m23()),
            Math.round(this.m30()), Math.round(this.m31()), Math.round(this.m32()), Math.round(this.m33())
        );
    }

    /**
     * Returns a matrix with all elements rounded up.
     *
     * @return A matrix with all elements rounded up.
     */
    public Matrix4 ceil() {
        return new Matrix4(
            (float) Math.ceil(this.m00()), (float) Math.ceil(this.m01()), (float) Math.ceil(this.m02()), (float) Math.ceil(this.m03()),
            (float) Math.ceil(this.m10()), (float) Math.ceil(this.m11()), (float) Math.ceil(this.m12()), (float) Math.ceil(this.m13()),
            (float) Math.ceil(this.m20()), (float) Math.ceil(this.m21()), (float) Math.ceil(this.m22()), (float) Math.ceil(this.m23()),
            (float) Math.ceil(this.m30()), (float) Math.ceil(this.m31()), (float) Math.ceil(this.m32()), (float) Math.ceil(this.m33())
        );
    }

    /**
     * Returns a matrix with all elements rounded down.
     *
     * @return A matrix with all elements rounded down.
     */
    public Matrix4 floor() {
        return new Matrix4(
            (float) Math.floor(this.m00()), (float) Math.floor(this.m01()), (float) Math.floor(this.m02()), (float) Math.floor(this.m03()),
            (float) Math.floor(this.m10()), (float) Math.floor(this.m11()), (float) Math.floor(this.m12()), (float) Math.floor(this.m13()),
            (float) Math.floor(this.m20()), (float) Math.floor(this.m21()), (float) Math.floor(this.m22()), (float) Math.floor(this.m23()),
            (float) Math.floor(this.m30()), (float) Math.floor(this.m31()), (float) Math.floor(this.m32()), (float) Math.floor(this.m33())
        );
    }

    /**
     * Constructs an orthonormal matrix from the columns of this matrix using the Gram-Schmidt procedure.
     *
     * @return This matrix with orthogonal columns of unit length.
     */
    public Matrix4 orthonormalized() {
        var v0 = this.column0();
        var v1 = this.column1();
        var v2 = this.column2();
        var v3 = this.column3();
        v1 = v1.minus(v1.project(v0));
        v2 = v2.minus(v2.project(v0)).minus(v2.project(v1));
        v3 = v3.minus(v3.project(v0)).minus(v3.project(v1)).minus(v3.project(v2));
        return Matrix4.fromColumns(v0.normalized(), v1.normalized(), v2.normalized(), v3.normalized());
    }

    /**
     * Checks if this matrix is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param m The second matrix.
     * @return True if this matrix is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(Matrix4 m) {
        return MathUtils.equalsApprox(this.m00(), m.m00()) && MathUtils.equalsApprox(this.m01(), m.m01()) && MathUtils.equalsApprox(this.m02(), m.m02()) && MathUtils.equalsApprox(this.m03(), m.m03())
            && MathUtils.equalsApprox(this.m10(), m.m10()) && MathUtils.equalsApprox(this.m11(), m.m11()) && MathUtils.equalsApprox(this.m12(), m.m12()) && MathUtils.equalsApprox(this.m13(), m.m13())
            && MathUtils.equalsApprox(this.m20(), m.m20()) && MathUtils.equalsApprox(this.m21(), m.m21()) && MathUtils.equalsApprox(this.m22(), m.m22()) && MathUtils.equalsApprox(this.m23(), m.m23())
            && MathUtils.equalsApprox(this.m30(), m.m30()) && MathUtils.equalsApprox(this.m31(), m.m31()) && MathUtils.equalsApprox(this.m32(), m.m32()) && MathUtils.equalsApprox(this.m33(), m.m33());
    }

    /**
     * Returns a 4x4 matrix from the given rows.
     *
     * @param row0 The first row.
     * @param row1 The second row.
     * @return A 4x4 matrix from the given rows.
     */
    public static Matrix4 fromRows(Vector4 row0, Vector4 row1, Vector4 row2, Vector4 row3) {
        return new Matrix4(row0.x(), row0.y(), row0.z(), row0.w(), row1.x(), row1.y(), row1.z(), row1.w(), row2.x(), row2.y(), row2.z(), row2.w(), row3.x(), row3.y(), row3.z(), row3.w());
    }

    /**
     * Returns a 4x4 matrix from the given columns.
     *
     * @param column0 The first column.
     * @param column1 The second column.
     * @return A 4x4 matrix from the given columns.
     */
    public static Matrix4 fromColumns(Vector4 column0, Vector4 column1, Vector4 column2, Vector4 column3) {
        return new Matrix4(column0.x(), column1.x(), column2.x(), column3.x(), column0.y(), column1.y(), column2.y(), column3.y(), column0.z(), column1.z(), column2.z(), column3.z(), column0.w(), column1.w(), column2.w(), column3.w());
    }

    /**
     * Returns a 4x4 matrix representing a translation in a 3D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     * @param z Translation on the z axis.
     * @return A 4x4 matrix representing a translation in a 3D space.
     */
    public static Matrix4 translation(float x, float y, float z) {
        return new Matrix4(1.0f, 0.0f, 0.0f, x, 0.0f, 1.0f, 0.0f, y, 0.0f, 0.0f, 1.0f, z, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 4x4 matrix representing a translation in a 3D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param t The translation vector.
     * @return A 4x4 matrix representing a translation in a 3D space.
     */
    public static Matrix4 translation(Vector3 t) {
        return translation(t.x(), t.y(), t.z());
    }

    /**
     * Returns a 4x4 matrix representing a translation in a 2D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 0.0f, 1.0f)}.
     * </p>
     *
     * @param t The translation vector.
     * @return A 4x4 matrix representing a translation in a 2D space.
     */
    public static Matrix4 translation(Vector2 t) {
        return translation(t.x(), t.y(), 0.0f);
    }

    /**
     * Returns a 4x4 rotation matrix with the given rotation on the x axis.
     *
     * @param x Rotation angle in radians.
     * @return A 4x4 rotation matrix.
     */
    public static Matrix4 rotationX(double x) {
        var sin = (float) Math.sin(x);
        var cos = (float) Math.cos(x);
        return new Matrix4(1.0f, 0.0f, 0.0f, 0.0f, 0.0f, cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 4x4 rotation matrix with the given rotation on the y axis.
     *
     * @param y Rotation angle in radians.
     * @return A 4x4 rotation matrix.
     */
    public static Matrix4 rotationY(double y) {
        var sin = (float) Math.sin(y);
        var cos = (float) Math.cos(y);
        return new Matrix4(cos, 0.0f, sin, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -sin, 0.0f, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 4x4 rotation matrix with the given rotation on the z axis.
     *
     * @param z Rotation angle in radians.
     * @return A 4x4 rotation matrix.
     */
    public static Matrix4 rotationZ(double z) {
        var sin = (float) Math.sin(z);
        var cos = (float) Math.cos(z);
        return new Matrix4(cos, -sin, 0.0f, 0.0f, sin, cos, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 4x4 rotation matrix with the given rotation.
     * The rotation is first applied on the z axis, then on the y axis, then on the x axis.
     *
     * @param x Rotation angle in radians on the x axis.
     * @param y Rotation angle in radians on the y axis.
     * @param z Rotation angle in radians on the z axis.
     * @return A 4x4 rotation matrix with the given rotation.
     */
    public static Matrix4 rotation(double x, double y, double z) {
        var sx = (float) Math.sin(x);
        var cx = (float) Math.cos(x);
        var sy = (float) Math.sin(y);
        var cy = (float) Math.cos(y);
        var sz = (float) Math.sin(z);
        var cz = (float) Math.cos(z);
        return new Matrix4(
            cy * cz, cy * -sz, sy, 0.0f,
            -sx * -sy * cz + cx * sz, -sx * -sy * -sz + cx * cz, -sx * cy, 0.0f,
            cx * -sy * cz + sx * sz, cx * -sy * -sz + sx * cz, cx * cy, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    /**
     * Returns a 4x4 rotation matrix with the given rotation.
     * The rotation is first applied on the z axis, then on the y axis, then on the x axis.
     *
     * @param r A vector representing the rotation angle on the x, y, and z axes.
     * @return A 4x4 rotation matrix with the given rotation.
     */
    public static Matrix4 rotation(Vector3 r) {
        return rotation(r.x(), r.y(), r.z());
    }

    /**
     * Returns a 4x4 rotation matrix with a rotation of the given angle around the given axis.
     *
     * @param axis A unit vector representing the rotation axis. The result is undefined if this vector is not [[Vector4.normalized]].
     * @param angle The rotation angle in radians.
     * @return A 4x4 rotation matrix with the given rotation.
     */
    public static Matrix4 rotation(Vector3 axis, double angle) {
        var sin = (float) Math.sin(angle);
        var cos = (float) Math.cos(angle);
        return new Matrix4(
            cos + axis.x() * axis.x() * (1.0f - cos), axis.x() * axis.y() * (1.0f - cos) - axis.z() * sin, axis.x() * axis.z() * (1.0f - cos) + axis.y() * sin, 0.0f,
            axis.y() * axis.x() * (1.0f - cos) + axis.z() * sin, cos + axis.y() * axis.y() * (1.0f - cos), axis.y() * axis.z() * (1.0f - cos) - axis.x() * sin, 0.0f,
            axis.z() * axis.x() * (1.0f - cos) - axis.y() * sin, axis.z() * axis.y() * (1.0f - cos) + axis.x() * sin, cos + axis.z() * axis.z() * (1.0f - cos), 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    /**
     * Returns a 4x4 rotation matrix with the rotation expressed by the given quaternion.
     *
     * @param q The rotation quaternion. Must be a unit quaternion.
     * @return A 4x4 rotation matrix with the rotation expressed by the given quaternion.
     */
    public static Matrix4 rotation(Quaternion q) {
        var s = 2.0f / q.lengthSquared();
        return new Matrix4(
            1.0f - s * (q.y() * q.y() + q.z() * q.z()), s * (q.x() * q.y() - q.z() * q.w()), s * (q.x() * q.z() + q.y() * q.w()), 0.0f,
            s * (q.x() * q.y() + q.z() * q.w()), 1.0f - s * (q.x() * q.x() + q.z() * q.z()), s * (q.y() * q.z() - q.x() * q.w()), 0.0f,
            s * (q.x() * q.z() - q.y() * q.w()), s * (q.y() * q.z() + q.x() * q.w()), 1.0f - s * (q.x() * q.x() + q.y() * q.y()), 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    /**
     * Returns a 4x4 matrix representing a scaling by the given factor in a 3D space.
     *
     * @param x Scale factor on the x axis.
     * @param y Scale factor on the y axis.
     * @param z Scale factor on the z axis.
     * @return A 4x4 matrix representing a scaling by the given factor in a 3D space.
     */
    public static Matrix4 scaling(float x, float y, float z) {
        return new Matrix4(x, 0.0f, 0.0f, 0.0f, 0.0f, y, 0.0f, 0.0f, 0.0f, 0.0f, z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Returns a 4x4 matrix representing a scaling by the given factor in a 3D space.
     *
     * @param s A vector representing the scale factor on the x, y, and z axes.
     * @return A 4x4 matrix representing a scaling by the given factor in a 3D space.
     */
    public static Matrix4 scaling(Vector3 s) {
        return scaling(s.x(), s.y(), s.z());
    }

    /**
     * Returns a 4x4 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param s A vector representing the scale factor on the x and y axes.
     * @return A 4x4 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix4 scaling(Vector2 s) {
        return scaling(s.x(), s.y(), 1.0f);
    }

    /**
     * Returns a 4x4 projection matrix representing perspective screen projection.
     *
     * @param fov Field of view angle in radians.
     * @param aspect Aspect ratio of the screen.
     * @param near Near plane distance.
     * @param far Far plane distance.
     * @return A 4x4 projection matrix for perspective projection.
     */
    public static Matrix4 perspectiveProjection(double fov, float aspect, float near, float far) {
        var focalLength = 1.0f / (float) Math.tan(fov / 2.0);
        return new Matrix4(
            focalLength, 0.0f, 0.0f, 0.0f,
            0.0f, focalLength * aspect, 0.0f, 0.0f,
            0.0f, 0.0f, -(far + near) / (far - near), -(2 * far * near) / (far - near),
            0.0f, 0.0f, -1.0f, 0.0f
        );
    }

    /**
     * Returns a 4x4 projection matrix representing perspective screen projection.
     *
     * @param fov Field of view angle in radians.
     * @param width Screen width.
     * @param height Screen height.
     * @param near Near plane distance.
     * @param far Far plane distance.
     * @return A 4x4 projection matrix for perspective projection.
     */
    public static Matrix4 perspectiveProjection(double fov, float width, float height, float near, float far) {
        return perspectiveProjection(fov, width / height, near, far);
    }

    /**
     * Returns a 4x4 projection matrix representing perspective screen projection.
     *
     * @param fov Field of view angle in radians.
     * @param size A vector representing the size of the screen (width and height).
     * @param near Near plane distance.
     * @param far Far plane distance.
     * @return A 4x4 projection matrix for perspective projection.
     */
    public static Matrix4 perspectiveProjection(double fov, Vector2 size, float near, float far) {
        return perspectiveProjection(fov, size.aspect(), near, far);
    }

    /**
     * Returns a 4x4 projection matrix representing perspective screen projection.
     *
     * @param fov Field of view angle in radians.
     * @param size A vector representing the size of the screen (width and height).
     * @param near Near plane distance.
     * @param far Far plane distance.
     * @return A 4x4 projection matrix for perspective projection.
     */
    public static Matrix4 perspectiveProjection(double fov, Vector2i size, float near, float far) {
        return perspectiveProjection(fov, size.aspect(), near, far);
    }

    /**
     * Returns a 4x4 projection matrix representing orthographic screen projection.
     *
     * @param left Left clipping plane distance.
     * @param right Right clipping plane distance.
     * @param bottom Bottom clipping plane distance.
     * @param top Top clipping plane distance.
     * @param near Near plane distance.
     * @param far Far plane distance.
     * @return A 4x4 projection matrix for orthographic projection.
     */
    public static Matrix4 orthographicProjection(float left, float right, float bottom, float top, float near, float far) {
        return new Matrix4(
            2.0f / (right - left), 0.0f, 0.0f, -(right + left) / (right - left),
            0.0f, 2.0f / (top - bottom), 0.0f, -(top + bottom) / (top - bottom),
            0.0f, 0.0f, -2.0f / (far - near), -(far + near) / (far - near),
            0.0f, 0.0f, 0.0f, 1.0f
        );
    }

    /**
     * Returns a 4x4 projection matrix representing orthographic screen projection.
     *
     * @param size Distance between the left and the right clipping planes. Can be seen as the width of the screen.
     * @param aspect Aspect ratio of the screen.
     * @param near Near plane distance.
     * @param far Far plane distance.
     * @return A 4x4 projection matrix for orthographic projection.
     */
    public static Matrix4 orthographicProjection(float size, float aspect, float near, float far) {
        return orthographicProjection(-size / 2.0f, size / 2.0f, -size / aspect / 2.0f, size / aspect / 2.0f, near, far);
    }
}