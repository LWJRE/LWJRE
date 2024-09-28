package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMatrix3 {

    @Test
    public void testMatrixFromSubmatrixAndRow() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var v = new Vector3(7.0f, 8.0f, 9.0f);
        var res = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(res, new Matrix3(m, v));
    }

    @Test
    public void testMatrixFromRowAndSubmatrix() {
        var m = new Matrix2x3(4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var v = new Vector3(1.0f, 2.0f, 3.0f);
        var res = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(res, new Matrix3(v, m));
    }

    @Test
    public void testMatrixSum() {
        var m1 = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var m2 = new Matrix3(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f);
        var res = new Matrix3(4.0f, 6.0f, 4.0f, 6.0f, 12.0f, 11.0f, 16.0f, 14.0f, 17.0f);
        Assertions.assertEquals(res, m1.plus(m2));
    }

    @Test
    public void testMatrixSubtraction() {
        var m1 = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var m2 = new Matrix3(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f);
        var res = new Matrix3(2.0f, 2.0f, -2.0f, -2.0f, 2.0f, -1.0f, 2.0f, -2.0f, -1.0f);
        Assertions.assertEquals(res, m2.minus(m1));
    }

    @Test
    public void testNegated() {
        var m = new Matrix3(1.0f, 2.0f, -2.0f, -1.0f, 3.0f, -2.0f, 0.0f, -9.0f, 4.0f);
        var r = new Matrix3(-1.0f, -2.0f, 2.0f, 1.0f, -3.0f, 2.0f, -0.0f, 9.0f, -4.0f);
        Assertions.assertEquals(r, m.negated());
    }

    @Test
    public void testMatrixMultipliedByScalar() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var r = new Matrix3(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 10.5f, 12.0f, 13.5f);
        Assertions.assertEquals(r, m.multiply(1.5f));
    }

    @Test
    public void testMatrixDividedByScalar() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var r = new Matrix3(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f);
        Assertions.assertEquals(r, m.divide(2.0f));
    }

    @Test
    public void testGetRows() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var r0 = new Vector3(1.0f, 2.0f, 3.0f);
        var r1 = new Vector3(4.0f, 5.0f, 6.0f);
        var r2 = new Vector3(7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(r0, m.row0());
        Assertions.assertEquals(r1, m.row1());
        Assertions.assertEquals(r2, m.row2());
    }

    @Test
    public void testGetRowAtIndex() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var r0 = new Vector3(1.0f, 2.0f, 3.0f);
        var r1 = new Vector3(4.0f, 5.0f, 6.0f);
        var r2 = new Vector3(7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(r0, m.row(0));
        Assertions.assertEquals(r1, m.row(1));
        Assertions.assertEquals(r2, m.row(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.row(3));
    }

    @Test
    public void testGetColumns() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var c0 = new Vector3(1.0f, 4.0f, 7.0f);
        var c1 = new Vector3(2.0f, 5.0f, 8.0f);
        var c2 = new Vector3(3.0f, 6.0f, 9.0f);
        Assertions.assertEquals(c0, m.column0());
        Assertions.assertEquals(c1, m.column1());
        Assertions.assertEquals(c2, m.column2());
    }

    @Test
    public void testGetColumnAtIndex() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var c0 = new Vector3(1.0f, 4.0f, 7.0f);
        var c1 = new Vector3(2.0f, 5.0f, 8.0f);
        var c2 = new Vector3(3.0f, 6.0f, 9.0f);
        Assertions.assertEquals(c0, m.column(0));
        Assertions.assertEquals(c1, m.column(1));
        Assertions.assertEquals(c2, m.column(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.column(3));
    }

    @Test
    public void testMatrixVectorProduct() {
        var mat = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var vec = new Vector3(1.5f, 2.5f, 3.5f);
        var res = new Vector3(17.0f, 39.5f, 62.0f);
        Assertions.assertEquals(res, mat.multiply(vec));
    }

    @Test
    public void testMatrixProduct3x3() {
        var m1 = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var m2 = new Matrix3(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f);
        var res = new Matrix3(34.0f, 36.0f, 35.0f, 76.0f, 87.0f, 77.0f, 118.0f, 138.0f, 119.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testMatrixProduct3x4() {
        var m1 = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var m2 = new Matrix3x4(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f, 10.0f, 11.0f, 12.0f);
        var res = new Matrix3x4(41.0f, 44.0f, 52.0f, 50.0f, 95.0f, 101.0f, 115.0f, 110.0f, 149.0f, 158.0f, 178.0f, 170.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testTransposed() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var t = new Matrix3(1.0f, 4.0f, 7.0f, 2.0f, 5.0f, 8.0f, 3.0f, 6.0f, 9.0f);
        Assertions.assertEquals(t, m.transposed());
    }

    @Test
    public void testIsSymmetric() {
        var m1 = new Matrix3(1.0f, 2.0f, 3.0f, 2.0f, 4.0f, 5.0f, 3.0f, 5.0f, 6.0f);
        var m2 = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertTrue(m1.isSymmetric());
        Assertions.assertFalse(m2.isSymmetric());
    }

    @Test
    public void testIsSkewSymmetric() {
        var m1 = new Matrix3(1.0f, 2.0f, 3.0f, 2.0f, 4.0f, 5.0f, 3.0f, 5.0f, 6.0f);
        var m2 = new Matrix3(0.0f, 1.0f, 2.0f, -1.0f, 0.0f, 3.0f, -2.0f, -3.0f, 0.0f);
        Assertions.assertFalse(m1.isSkewSymmetric());
        Assertions.assertTrue(m2.isSkewSymmetric());
    }

    @Test
    public void testDeterminant() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 0.0f, 0.0f, 1.0f);
        Assertions.assertEquals(-3.0f, m.determinant(), 1e-12f);
    }

    @Test
    public void testInverse() {
        var mat = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 0.0f, 0.0f, 1.0f);
        var inv = new Matrix3(-5.0f / 3.0f, 2.0f / 3.0f, 1.0f, 4.0f / 3.0f, -1.0f / 3.0f, -2.0f, -0.0f, -0.0f, 1.0f);
        Assertions.assertEquals(inv, mat.inverse());
    }

    @Test
    public void testPowerWithPositiveExponent() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(m.multiply(m).multiply(m), m.power(3));
    }

    @Test
    public void testPowerWithZeroExponent() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(Matrix3.IDENTITY, m.power(0));
    }

    @Test
    public void testPowerOfOne() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(m, m.power(1));
    }

    @Test
    public void testPowerWithNegativeExponent() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var i = m.inverse();
        Assertions.assertEquals(i.multiply(i).multiply(i), m.power(-3));
    }

    @Test
    public void testLerpElements() {
        var from = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var to = new Matrix3(2.0f, 4.0f, 6.0f, 8.0f, 10.0f, 12.0f, 14.0f, 16.0f, 18.0f);
        var res = new Matrix3(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 10.5f, 12.0f, 13.5f);
        Assertions.assertEquals(res, from.lerp(to, 0.5f));
    }

    @Test
    public void testMatrixAbsoluteValue() {
        var mat = new Matrix3(1.0f, 2.0f, 0.0f, -2.0f, 3.0f, -1.0f, -3.0f, 0.0f, 1.0f);
        var abs = new Matrix3(1.0f, 2.0f, 0.0f, 2.0f, 3.0f, 1.0f, 3.0f, 0.0f, 1.0f);
        Assertions.assertEquals(abs, mat.abs());
    }

    @Test
    public void testMatrixSign() {
        var mat = new Matrix3(1.0f, 2.0f, 0.0f, -2.0f, 3.0f, -1.0f, -3.0f, 0.0f, 1.0f);
        var sign = new Matrix3(1.0f, 1.0f, 0.0f, -1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f);
        Assertions.assertEquals(sign, mat.sign());
    }

    @Test
    public void testMatrixRound() {
        var mat = new Matrix3(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f);
        var round = new Matrix3(1.0f, 2.0f, 0.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f);
        Assertions.assertEquals(round, mat.round());
    }

    @Test
    public void testMatrixCeil() {
        var mat = new Matrix3(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f);
        var ceil = new Matrix3(2.0f, 2.0f, 1.0f, 1.0f, 3.0f, 3.0f, 3.0f, 3.0f, 1.0f);
        Assertions.assertEquals(ceil, mat.ceil());
    }

    @Test
    public void testMatrixFloor() {
        var mat = new Matrix3(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f);
        var floor = new Matrix3(1.0f, 1.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f);
        Assertions.assertEquals(floor, mat.floor());
    }

    // TODO: Orthonormalize

    @Test
    public void testMatrixEqualsApprox() {
        var m1 = new Matrix3(1.00000001f, 1.99999999f, 3.00000001f, 3.99999999f, 5.00000001f, 5.99999999f, 7.00000001f, 7.99999999f, 9.00000001f);
        var m2 = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        Assertions.assertTrue(m1.equalsApprox(m2));
    }

    @Test
    public void testMatrixFromRows() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var r0 = new Vector3(1.0f, 2.0f, 3.0f);
        var r1 = new Vector3(4.0f, 5.0f, 6.0f);
        var r2 = new Vector3(7.0f, 8.0f, 9.0f);
        Assertions.assertEquals(m, Matrix3.fromRows(r0, r1, r2));
    }

    @Test
    public void testMatrixFromColumns() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f);
        var c0 = new Vector3(1.0f, 4.0f, 7.0f);
        var c1 = new Vector3(2.0f, 5.0f, 8.0f);
        var c2 = new Vector3(3.0f, 6.0f, 9.0f);
        Assertions.assertEquals(m, Matrix3.fromColumns(c0, c1, c2));
    }

    @Test
    public void testTranslationMatrix() {
        var v = new Vector2(1.0f, 2.0f);
        var t = new Vector2(3.0f, 4.0f);
        var m = Matrix3.translation(t);
        Assertions.assertEquals(v.plus(t), m.multiply(v, 1.0f).xy());
    }

    @Test
    public void testRotationMatrixAroundX() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(1.0f, 0.70710678f, 2.12132034f);
        var m = Matrix3.rotationX(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1));
    }

    @Test
    public void testRotationMatrixAroundY() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(1.41421356f, 2.0f, 0.0f);
        var m = Matrix3.rotationY(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1));
    }

    @Test
    public void testRotationMatrixAroundZ() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(-0.70710678f, 2.12132034f, 1.0f);
        var m = Matrix3.rotationZ(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1));
    }

    @Test
    public void testRotationMatrix() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(0.61237244f, 0.42161062f, 2.33393327f);
        var m = Matrix3.rotation(Math.PI / 3.0, Math.PI / 4.0, Math.PI / 6.0);
        TestVector3.assertEquals(v2, m.multiply(v1));
    }

    @Test
    public void testRotationMatrixAroundArbitraryAxis() {
        var axis = Vector3.ONE.normalized();
        var point = new Vector3(1.0f, 2.0f, 1.0f);
        var res = new Vector3(0.68938278f, 1.80473785f, 1.50587936f);
        var matrix = Matrix3.rotation(axis, Math.PI / 4.0);
        Assertions.assertEquals(res, matrix.multiply(point));
    }

    // TODO: Rotation matrix from quaternion

    @Test
    public void testScalingMatrix() {
        var v = new Vector3(1.5f, 1.5f, 1.5f);
        var s = new Vector3(2.0f, 3.0f, 4.0f);
        var m = Matrix3.scaling(s);
        Assertions.assertEquals(v.multiply(s), m.multiply(v));
    }

    @Test
    public void testScalingMatrix2D() {
        var v = new Vector2(1.5f, 1.5f);
        var s = new Vector2(2.0f, 3.0f);
        var m = Matrix3.scaling(s);
        Assertions.assertEquals(v.multiply(s), m.multiply(v, 0.0f).xy());
    }
}