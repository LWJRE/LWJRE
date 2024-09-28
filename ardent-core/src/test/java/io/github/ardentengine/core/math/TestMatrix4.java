package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMatrix4 {

    private static void assertEquals(Matrix4 expected, Matrix4 actual) {
        Assertions.assertEquals(expected.m00(), actual.m00(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m01(), actual.m01(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m02(), actual.m02(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m03(), actual.m03(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m10(), actual.m10(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m11(), actual.m11(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m12(), actual.m12(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m13(), actual.m13(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m20(), actual.m20(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m21(), actual.m21(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m22(), actual.m22(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m23(), actual.m23(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m30(), actual.m30(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m31(), actual.m31(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m32(), actual.m32(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m33(), actual.m33(), MathUtils.EPSILON);
    }

    @Test
    public void testMatrixFromSubmatrixAndRow() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var v = new Vector4(13.0f, 14.0f, 15.0f, 16.0f);
        var res = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        Assertions.assertEquals(res, new Matrix4(m, v));
    }

    @Test
    public void testMatrixFromRowAndSubmatrix() {
        var m = new Matrix3x4(5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var v = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var res = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        Assertions.assertEquals(res, new Matrix4(v, m));
    }

    @Test
    public void testMatrixSum() {
        var m1 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var m2 = new Matrix4(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f, 11.0f, 14.0f, 16.0f, 15.0f, 13.0f, 12.0f, 10.0f);
        var res = new Matrix4(4.0f, 6.0f, 4.0f, 6.0f, 12.0f, 11.0f, 16.0f, 14.0f, 17.0f, 21.0f, 25.0f, 28.0f, 28.0f, 27.0f, 27.0f, 26.0f);
        Assertions.assertEquals(res, m1.plus(m2));
    }

    @Test
    public void testMatrixSubtraction() {
        var m1 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var m2 = new Matrix4(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f, 11.0f, 14.0f, 16.0f, 15.0f, 13.0f, 12.0f, 10.0f);
        var res = new Matrix4(2.0f, 2.0f, -2.0f, -2.0f, 2.0f, -1.0f, 2.0f, -2.0f, -1.0f, 1.0f, 3.0f, 4.0f, 2.0f, -1.0f, -3.0f, -6.0f);
        Assertions.assertEquals(res, m2.minus(m1));
    }

    @Test
    public void testNegated() {
        var m = new Matrix4(1.0f, 2.0f, -2.0f, -1.0f, 3.0f, -2.0f, 0.0f, -9.0f, 4.0f, 2.0f, -5.0f, -4.0f, 7.0f, 2.0f, 8.0f, -0.0f);
        var r = new Matrix4(-1.0f, -2.0f, 2.0f, 1.0f, -3.0f, 2.0f, -0.0f, 9.0f, -4.0f, -2.0f, 5.0f, 4.0f, -7.0f, -2.0f, -8.0f, 0.0f);
        Assertions.assertEquals(r, m.negated());
    }

    @Test
    public void testMatrixMultipliedByScalar() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var r = new Matrix4(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 10.5f, 12.0f, 13.5f, 15.0f, 16.5f, 18.0f, 19.5f, 21.0f, 22.5f, 24.0f);
        Assertions.assertEquals(r, m.multiply(1.5f));
    }

    @Test
    public void testMatrixDividedByScalar() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var r = new Matrix4(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f);
        Assertions.assertEquals(r, m.divide(2.0f));
    }

    @Test
    public void testGetRows() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var r0 = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var r1 = new Vector4(5.0f, 6.0f, 7.0f, 8.0f);
        var r2 = new Vector4(9.0f, 10.0f, 11.0f, 12.0f);
        var r3 = new Vector4(13.0f, 14.0f, 15.0f, 16.0f);
        Assertions.assertEquals(r0, m.row0());
        Assertions.assertEquals(r1, m.row1());
        Assertions.assertEquals(r2, m.row2());
        Assertions.assertEquals(r3, m.row3());
    }

    @Test
    public void testGetRowAtIndex() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var r0 = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var r1 = new Vector4(5.0f, 6.0f, 7.0f, 8.0f);
        var r2 = new Vector4(9.0f, 10.0f, 11.0f, 12.0f);
        var r3 = new Vector4(13.0f, 14.0f, 15.0f, 16.0f);
        Assertions.assertEquals(r0, m.row(0));
        Assertions.assertEquals(r1, m.row(1));
        Assertions.assertEquals(r2, m.row(2));
        Assertions.assertEquals(r3, m.row(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.row(4));
    }

    @Test
    public void testGetColumns() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var c0 = new Vector4(1.0f, 5.0f, 9.0f, 13.0f);
        var c1 = new Vector4(2.0f, 6.0f, 10.0f, 14.0f);
        var c2 = new Vector4(3.0f, 7.0f, 11.0f, 15.0f);
        var c3 = new Vector4(4.0f, 8.0f, 12.0f, 16.0f);
        Assertions.assertEquals(c0, m.column0());
        Assertions.assertEquals(c1, m.column1());
        Assertions.assertEquals(c2, m.column2());
        Assertions.assertEquals(c3, m.column3());
    }

    @Test
    public void testGetColumnAtIndex() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var c0 = new Vector4(1.0f, 5.0f, 9.0f, 13.0f);
        var c1 = new Vector4(2.0f, 6.0f, 10.0f, 14.0f);
        var c2 = new Vector4(3.0f, 7.0f, 11.0f, 15.0f);
        var c3 = new Vector4(4.0f, 8.0f, 12.0f, 16.0f);
        Assertions.assertEquals(c0, m.column(0));
        Assertions.assertEquals(c1, m.column(1));
        Assertions.assertEquals(c2, m.column(2));
        Assertions.assertEquals(c3, m.column(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.column(4));
    }

    @Test
    public void testMatrixVectorProduct() {
        var mat = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var vec = new Vector4(1.5f, 2.5f, 3.5f, 4.5f);
        var res = new Vector4(35.0f, 83.0f, 131.0f, 179.0f);
        Assertions.assertEquals(res, mat.multiply(vec));
    }

    @Test
    public void testMatrixProduct4x4() {
        var m1 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var m2 = new Matrix4(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f, 11.0f, 14.0f, 16.0f, 15.0f, 13.0f, 12.0f, 10.0f);
        var res = new Matrix4(101.0f, 99.0f, 109.0f, 102.0f, 233.0f, 231.0f, 253.0f, 238.0f, 365.0f, 363.0f, 397.0f, 374.0f, 497.0f, 495.0f, 541.0f, 510.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testTransposed() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var t = new Matrix4(1.0f, 5.0f, 9.0f, 13.0f, 2.0f, 6.0f, 10.0f, 14.0f, 3.0f, 7.0f, 11.0f, 15.0f, 4.0f, 8.0f, 12.0f, 16.0f);
        Assertions.assertEquals(t, m.transposed());
    }

    @Test
    public void testIsSymmetric() {
        var m1 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 2.0f, 5.0f, 6.0f, 7.0f, 3.0f, 6.0f, 8.0f, 9.0f, 4.0f, 7.0f, 9.0f, 10.0f);
        var m2 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        Assertions.assertTrue(m1.isSymmetric());
        Assertions.assertFalse(m2.isSymmetric());
    }

    @Test
    public void testIsSkewSymmetric() {
        var m1 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 2.0f, 5.0f, 6.0f, 7.0f, 3.0f, 6.0f, 8.0f, 9.0f, 4.0f, 7.0f, 9.0f, 10.0f);
        var m2 = new Matrix4(0.0f, 1.0f, 2.0f, 3.0f, -1.0f, 0.0f, 4.0f, 5.0f, -2.0f, -4.0f, 0.0f, 6.0f, -3.0f, -5.0f, -6.0f, 0.0f);
        Assertions.assertFalse(m1.isSkewSymmetric());
        Assertions.assertTrue(m2.isSkewSymmetric());
    }

    @Test
    public void testDeterminant() {
        var m = new Matrix4(1.0f, 3.0f, 5.0f, 9.0f, 1.0f, 3.0f, 1.0f, 7.0f, 4.0f, 3.0f, 9.0f, 7.0f, 5.0f, 2.0f, 0.0f, 9.0f);
        Assertions.assertEquals(-376.0f, m.determinant());
    }

    @Test
    public void testInverse() {
        var mat = new Matrix4(1.0f, 3.0f, 5.0f, 9.0f, 1.0f, 3.0f, 1.0f, 7.0f, 4.0f, 3.0f, 9.0f, 7.0f, 5.0f, 2.0f, 0.0f, 9.0f);
        var inv = new Matrix4(-13.0f / 47.0f, 2.0f / 47.0f, 7.0f / 47.0f, 6.0f / 47.0f, -5.0f / 8.0f, 7.0f / 8.0f, 1.0f / 4.0f, -1.0f / 4.0f, 39.0f / 376.0f, -53.0f / 376.0f, 13.0f / 188.0f, -9.0f / 188.0f, 55.0f / 188.0f, -41.0f / 188.0f, -13.0f / 94.0f, 9.0f / 94.0f);
        Assertions.assertEquals(inv, mat.inverse());
    }

    @Test
    public void testPowerWithPositiveExponent() {
        var m = new Matrix4(1.0f, 3.0f, 5.0f, 9.0f, 1.0f, 3.0f, 1.0f, 7.0f, 4.0f, 3.0f, 9.0f, 7.0f, 5.0f, 2.0f, 0.0f, 9.0f);
        Assertions.assertEquals(m.multiply(m).multiply(m), m.power(3));
    }

    @Test
    public void testPowerWithZeroExponent() {
        var m = new Matrix4(1.0f, 3.0f, 5.0f, 9.0f, 1.0f, 3.0f, 1.0f, 7.0f, 4.0f, 3.0f, 9.0f, 7.0f, 5.0f, 2.0f, 0.0f, 9.0f);
        Assertions.assertEquals(Matrix4.IDENTITY, m.power(0));
    }

    @Test
    public void testPowerOfOne() {
        var m = new Matrix4(1.0f, 3.0f, 5.0f, 9.0f, 1.0f, 3.0f, 1.0f, 7.0f, 4.0f, 3.0f, 9.0f, 7.0f, 5.0f, 2.0f, 0.0f, 9.0f);
        Assertions.assertEquals(m, m.power(1));
    }

    @Test
    public void testPowerWithNegativeExponent() {
        var m = new Matrix4(1.0f, 3.0f, 5.0f, 9.0f, 1.0f, 3.0f, 1.0f, 7.0f, 4.0f, 3.0f, 9.0f, 7.0f, 5.0f, 2.0f, 0.0f, 9.0f);
        var i = m.inverse();
        assertEquals(i.multiply(i).multiply(i), m.power(-3));
    }

    @Test
    public void testLerpElements() {
        var from = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var to = new Matrix4(2.0f, 4.0f, 6.0f, 8.0f, 10.0f, 12.0f, 14.0f, 16.0f, 18.0f, 20.0f, 22.0f, 24.0f, 26.0f, 28.0f, 30.0f, 32.0f);
        var res = new Matrix4(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 10.5f, 12.0f, 13.5f, 15.0f, 16.5f, 18.0f, 19.5f, 21.0f, 22.5f, 24.0f);
        Assertions.assertEquals(res, from.lerp(to, 0.5f));
    }

    @Test
    public void testMatrixAbsoluteValue() {
        var mat = new Matrix4(1.0f, 2.0f, 0.0f, -2.0f, 3.0f, -1.0f, -3.0f, 0.0f, 1.0f, -3.0f, 2.0f, 6.0f, -1.0f, -7.0f, 4.0f, 0.0f);
        var abs = new Matrix4(1.0f, 2.0f, 0.0f, 2.0f, 3.0f, 1.0f, 3.0f, 0.0f, 1.0f, 3.0f, 2.0f, 6.0f, 1.0f, 7.0f, 4.0f, 0.0f);
        Assertions.assertEquals(abs, mat.abs());
    }

    @Test
    public void testMatrixSign() {
        var mat = new Matrix4(1.0f, 2.0f, 0.0f, -2.0f, 3.0f, -1.0f, -3.0f, 0.0f, 1.0f, -3.0f, 2.0f, 6.0f, -1.0f, -7.0f, 4.0f, 0.0f);
        var sign = new Matrix4(1.0f, 1.0f, 0.0f, -1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 0.0f);
        Assertions.assertEquals(sign, mat.sign());
    }

    @Test
    public void testMatrixRound() {
        var mat = new Matrix4(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f, 5.9f, 5.4f, 6.6f, 1.2f, 1.1f, 0.9f, 2.6f);
        var round = new Matrix4(1.0f, 2.0f, 0.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f, 6.0f, 5.0f, 7.0f, 1.0f, 1.0f, 1.0f, 3.0f);
        Assertions.assertEquals(round, mat.round());
    }

    @Test
    public void testMatrixCeil() {
        var mat = new Matrix4(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f, 5.9f, 5.4f, 6.6f, 1.2f, 1.1f, 0.9f, 2.6f);
        var ceil = new Matrix4(2.0f, 2.0f, 1.0f, 1.0f, 3.0f, 3.0f, 3.0f, 3.0f, 1.0f, 6.0f, 6.0f, 7.0f, 2.0f, 2.0f, 1.0f, 3.0f);
        Assertions.assertEquals(ceil, mat.ceil());
    }

    @Test
    public void testMatrixFloor() {
        var mat = new Matrix4(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f, 5.9f, 5.4f, 6.6f, 1.2f, 1.1f, 0.9f, 2.6f);
        var floor = new Matrix4(1.0f, 1.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 5.0f, 5.0f, 6.0f, 1.0f, 1.0f, 0.0f, 2.0f);
        Assertions.assertEquals(floor, mat.floor());
    }

    // TODO: Orthonormalize

    @Test
    public void testMatrixEqualsApprox() {
        var m1 = new Matrix4(1.00000001f, 1.99999999f, 3.00000001f, 3.99999999f, 5.00000001f, 5.99999999f, 7.00000001f, 7.99999999f, 9.00000001f, 0.00000001f, 1.00000001f, 0.99999999f, 3.99999999f, 4.00000001f, 2.99999999f, 1.00000001f);
        var m2 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 0.0f, 1.0f, 1.0f, 4.0f, 4.0f, 3.0f, 1.0f);
        Assertions.assertTrue(m1.equalsApprox(m2));
    }

    @Test
    public void testMatrixFromRows() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var r0 = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var r1 = new Vector4(5.0f, 6.0f, 7.0f, 8.0f);
        var r2 = new Vector4(9.0f, 10.0f, 11.0f, 12.0f);
        var r3 = new Vector4(13.0f, 14.0f, 15.0f, 16.0f);
        Assertions.assertEquals(m, Matrix4.fromRows(r0, r1, r2, r3));
    }

    @Test
    public void testMatrixFromColumns() {
        var m = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var c0 = new Vector4(1.0f, 5.0f, 9.0f, 13.0f);
        var c1 = new Vector4(2.0f, 6.0f, 10.0f, 14.0f);
        var c2 = new Vector4(3.0f, 7.0f, 11.0f, 15.0f);
        var c3 = new Vector4(4.0f, 8.0f, 12.0f, 16.0f);
        Assertions.assertEquals(m, Matrix4.fromColumns(c0, c1, c2, c3));
    }

    @Test
    public void testTranslationMatrix() {
        var v = new Vector3(1.0f, 2.0f, 3.0f);
        var t = new Vector3(3.0f, 4.0f, 2.0f);
        var m = Matrix4.translation(t);
        Assertions.assertEquals(v.plus(t), m.multiply(v, 1.0f).xyz());
    }

    @Test
    public void testTranslationMatrix2D() {
        var v = new Vector2(1.0f, 2.0f);
        var t = new Vector2(3.0f, 4.0f);
        var m = Matrix4.translation(t);
        Assertions.assertEquals(v.plus(t), m.multiply(v, 0.0f, 1.0f).xy());
    }

    @Test
    public void testRotationMatrixAroundX() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(1.0f, 0.70710678f, 2.12132034f);
        var m = Matrix4.rotationX(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f).xyz());
    }

    @Test
    public void testRotationMatrixAroundY() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(1.41421356f, 2.0f, 0.0f);
        var m = Matrix4.rotationY(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f).xyz());
    }

    @Test
    public void testRotationMatrixAroundZ() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(-0.70710678f, 2.12132034f, 1.0f);
        var m = Matrix4.rotationZ(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f).xyz());
    }

    @Test
    public void testRotationMatrix() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(0.61237244f, 0.42161062f, 2.33393327f);
        var r = new Vector3(Math.PI / 3.0, Math.PI / 4.0, Math.PI / 6.0);
        var m = Matrix4.rotation(r);
        TestVector3.assertEquals(v2, m.multiply(v1, 0.0f).xyz());
    }

    @Test
    public void testRotationMatrixAroundArbitraryAxis() {
        var axis = Vector3.ONE.normalized();
        var point = new Vector3(1.0f, 2.0f, 1.0f);
        var res = new Vector3(0.68938278f, 1.80473785f, 1.50587936f);
        var matrix = Matrix4.rotation(axis, Math.PI / 4.0);
        Assertions.assertEquals(res, matrix.multiply(point, 0.0f).xyz());
    }

    // TODO: Rotation matrix from quaternion

    @Test
    public void testScalingMatrix3D() {
        var v = new Vector3(1.5f, 1.5f, 1.5f);
        var s = new Vector3(2.0f, 3.0f, 4.0f);
        var m = Matrix4.scaling(s);
        Assertions.assertEquals(v.multiply(s), m.multiply(v, 0.0f).xyz());
    }

    @Test
    public void testScalingMatrix2D() {
        var v = new Vector2(1.5f, 1.5f);
        var s = new Vector2(2.0f, 3.0f);
        var m = Matrix4.scaling(s);
        Assertions.assertEquals(v.multiply(s), m.multiply(v, 0.0f, 0.0f).xy());
    }

    // TODO: Projection matrix
}