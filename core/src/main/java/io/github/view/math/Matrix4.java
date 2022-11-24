package io.github.view.math;

public record Matrix4(
		float m00, float m01, float m02, float m03,
		float m10, float m11, float m12, float m13,
		float m20, float m21, float m22, float m23,
		float m30, float m31, float m32, float m33
) implements Matrix<Matrix4, Vector4> {

	public static final Matrix4 IDENTITY = new Matrix4(
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f
	);

	@Override
	public Matrix4 plus(Matrix4 matrix) {
		return new Matrix4(
				this.m00() + matrix.m00(), this.m01() + matrix.m01(), this.m02() + matrix.m02(), this.m03() + matrix.m03(),
				this.m10() + matrix.m10(), this.m11() + matrix.m11(), this.m12() + matrix.m12(), this.m13() + matrix.m13(),
				this.m20() + matrix.m20(), this.m21() + matrix.m21(), this.m22() + matrix.m22(), this.m23() + matrix.m23(),
				this.m30() + matrix.m30(), this.m31() + matrix.m31(), this.m32() + matrix.m32(), this.m33() + matrix.m33()
		);
	}

	public Vector4 row0() {
		return new Vector4(this.m00(), this.m01(), this.m02(), this.m03());
	}

	public Vector4 row1() {
		return new Vector4(this.m10(), this.m11(), this.m12(), this.m13());
	}

	public Vector4 row2() {
		return new Vector4(this.m20(), this.m21(), this.m22(), this.m23());
	}

	public Vector4 row3() {
		return new Vector4(this.m30(), this.m31(), this.m32(), this.m33());
	}

	public Vector4 column0() {
		return new Vector4(this.m00(), this.m10(), this.m20(), this.m30());
	}

	public Vector4 column1() {
		return new Vector4(this.m01(), this.m11(), this.m21(), this.m31());
	}

	public Vector4 column2() {
		return new Vector4(this.m02(), this.m12(), this.m22(), this.m32());
	}

	public Vector4 column3() {
		return new Vector4(this.m03(), this.m13(), this.m23(), this.m33());
	}

	@Override
	public Vector4 multiply(Vector4 vector) {
		return new Vector4(
				this.row0().dotProduct(vector),
				this.row1().dotProduct(vector),
				this.row2().dotProduct(vector),
				this.row3().dotProduct(vector)
		);
	}

	@Override
	public Matrix4 multiply(Matrix4 matrix) {
		return new Matrix4(
				this.row0().dotProduct(matrix.column0()), this.row0().dotProduct(matrix.column1()), this.row0().dotProduct(matrix.column2()), this.row0().dotProduct(matrix.column3()),
				this.row1().dotProduct(matrix.column0()), this.row1().dotProduct(matrix.column1()), this.row1().dotProduct(matrix.column2()), this.row1().dotProduct(matrix.column3()),
				this.row2().dotProduct(matrix.column0()), this.row2().dotProduct(matrix.column1()), this.row2().dotProduct(matrix.column2()), this.row2().dotProduct(matrix.column3()),
				this.row3().dotProduct(matrix.column0()), this.row3().dotProduct(matrix.column1()), this.row3().dotProduct(matrix.column2()), this.row3().dotProduct(matrix.column3())
		);
	}
}
