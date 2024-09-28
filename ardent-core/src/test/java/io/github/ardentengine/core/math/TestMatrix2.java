package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMatrix2 {

    private static void assertEquals(Matrix2 expected, Matrix2 actual) {
        Assertions.assertEquals(expected.m00(), actual.m00(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m01(), actual.m01(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m10(), actual.m10(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.m11(), actual.m11(), MathUtils.EPSILON);
    }

    @Test
    public void testMatrixSum() {
        var m1 = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var m2 = new Matrix2(3.0f, 4.0f, 1.0f, 2.0f);
        var res = new Matrix2(4.0f, 6.0f, 4.0f, 6.0f);
        Assertions.assertEquals(res, m1.plus(m2));
    }

    @Test
    public void testMatrixSubtraction() {
        var m1 = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var m2 = new Matrix2(3.0f, 4.0f, 1.0f, 2.0f);
        var res = new Matrix2(2.0f, 2.0f, -2.0f, -2.0f);
        Assertions.assertEquals(res, m2.minus(m1));
    }

    @Test
    public void testNegated() {
        var m = new Matrix2(1.0f, 2.0f, -2.0f, -1.0f);
        var r = new Matrix2(-1.0f, -2.0f, 2.0f, 1.0f);
        Assertions.assertEquals(r, m.negated());
    }

    @Test
    public void testMatrixMultipliedByScalar() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var r = new Matrix2(1.5f, 3.0f, 4.5f, 6.0f);
        Assertions.assertEquals(r, m.multiply(1.5f));
    }

    @Test
    public void testMatrixDividedByScalar() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var r = new Matrix2(0.5f, 1.0f, 1.5f, 2.0f);
        Assertions.assertEquals(r, m.divide(2.0f));
    }

    @Test
    public void testGetRows() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var r0 = new Vector2(1.0f, 2.0f);
        var r1 = new Vector2(3.0f, 4.0f);
        Assertions.assertEquals(r0, m.row0());
        Assertions.assertEquals(r1, m.row1());
    }

    @Test
    public void testGetRowAtIndex() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var r0 = new Vector2(1.0f, 2.0f);
        var r1 = new Vector2(3.0f, 4.0f);
        Assertions.assertEquals(r0, m.row(0));
        Assertions.assertEquals(r1, m.row(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.row(2));
    }

    @Test
    public void testGetColumns() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var c0 = new Vector2(1.0f, 3.0f);
        var c1 = new Vector2(2.0f, 4.0f);
        Assertions.assertEquals(c0, m.column0());
        Assertions.assertEquals(c1, m.column1());
    }

    @Test
    public void testGetColumnAtIndex() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var c0 = new Vector2(1.0f, 3.0f);
        var c1 = new Vector2(2.0f, 4.0f);
        Assertions.assertEquals(c0, m.column(0));
        Assertions.assertEquals(c1, m.column(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.column(2));
    }

    @Test
    public void testMatrixVectorProduct() {
        var mat = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var vec = new Vector2(1.5f, 2.5f);
        var res = new Vector2(6.5f, 14.5f);
        Assertions.assertEquals(res, mat.multiply(vec));
    }

    @Test
    public void testMatrixProduct2x2() {
        var m1 = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var m2 = new Matrix2(3.0f, 4.0f, 1.0f, 2.0f);
        var res = new Matrix2(5.0f, 8.0f, 13.0f, 20.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testMatrixProduct2x3() {
        var m1 = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var m2 = new Matrix2x3(3.0f, 4.0f, 1.0f, 2.0f, 6.0f, 5.0f);
        var res = new Matrix2x3(7.0f, 16.0f, 11.0f, 17.0f, 36.0f, 23.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testTransposed() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var t = new Matrix2(1.0f, 3.0f, 2.0f, 4.0f);
        Assertions.assertEquals(t, m.transposed());
    }

    @Test
    public void testIsSymmetric() {
        var m1 = new Matrix2(1.0f, 2.0f, 2.0f, 1.0f);
        var m2 = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertTrue(m1.isSymmetric());
        Assertions.assertFalse(m2.isSymmetric());
    }

    @Test
    public void testIsSkewSymmetric() {
        var m1 = new Matrix2(1.0f, 2.0f, 2.0f, 1.0f);
        var m2 = new Matrix2(0.0f, 1.0f, -1.0f, 0.0f);
        Assertions.assertFalse(m1.isSkewSymmetric());
        Assertions.assertTrue(m2.isSkewSymmetric());
    }

    @Test
    public void testDeterminant() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertEquals(-2.0f, m.determinant(), 1e-12f);
    }

    @Test
    public void testInverse() {
        var mat = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var inv = new Matrix2(-2.0f, 1.0f, 3.0f / 2.0f, -0.5f);
        Assertions.assertEquals(inv, mat.inverse());
    }

    @Test
    public void testPowerWithPositiveExponent() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertEquals(m.multiply(m).multiply(m), m.power(3));
    }

    @Test
    public void testPowerWithZeroExponent() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertEquals(Matrix2.IDENTITY, m.power(0));
    }

    @Test
    public void testPowerOfOne() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertEquals(m, m.power(1));
    }

    @Test
    public void testPowerWithNegativeExponent() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var i = m.inverse();
        Assertions.assertEquals(i.multiply(i).multiply(i), m.power(-3));
    }

    @Test
    public void testLerpElements() {
        var from = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var to = new Matrix2(2.0f, 4.0f, 6.0f, 8.0f);
        var res = new Matrix2(1.5f, 3.0f, 4.5f, 6.0f);
        Assertions.assertEquals(res, from.lerp(to, 0.5f));
    }

    @Test
    public void testMatrixAbsoluteValue() {
        var mat = new Matrix2(1.0f, 2.0f, 0.0f, -2.0f);
        var abs = new Matrix2(1.0f, 2.0f, 0.0f, 2.0f);
        Assertions.assertEquals(abs, mat.abs());
    }

    @Test
    public void testMatrixSign() {
        var mat = new Matrix2(1.0f, 2.0f, 0.0f, -2.0f);
        var sign = new Matrix2(1.0f, 1.0f, 0.0f, -1.0f);
        Assertions.assertEquals(sign, mat.sign());
    }

    @Test
    public void testMatrixRound() {
        var mat = new Matrix2(1.1f, 1.9f, 0.2f, 0.8f);
        var round = new Matrix2(1.0f, 2.0f, 0.0f, 1.0f);
        Assertions.assertEquals(round, mat.round());
    }

    @Test
    public void testMatrixCeil() {
        var mat = new Matrix2(1.1f, 1.9f, 0.2f, 0.8f);
        var ceil = new Matrix2(2.0f, 2.0f, 1.0f, 1.0f);
        Assertions.assertEquals(ceil, mat.ceil());
    }

    @Test
    public void testMatrixFloor() {
        var mat = new Matrix2(1.1f, 1.9f, 0.2f, 0.8f);
        var floor = new Matrix2(1.0f, 1.0f, 0.0f, 0.0f);
        Assertions.assertEquals(floor, mat.floor());
    }

    @Test
    public void testOrthonormalize() {
        var mat = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var res = new Matrix2(1.0f, 3.0f, 3.0f, -1.0f).divide((float) Math.sqrt(10.0));
        assertEquals(res, mat.orthonormalized());
    }

    @Test
    public void testMatrixEqualsApprox() {
        var m1 = new Matrix2(1.00000001f, 1.99999999f, 3.00000001f, 3.99999999f);
        var m2 = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        Assertions.assertTrue(m1.equalsApprox(m2));
    }

    @Test
    public void testMatrixFromRows() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var r0 = new Vector2(1.0f, 2.0f);
        var r1 = new Vector2(3.0f, 4.0f);
        Assertions.assertEquals(m, Matrix2.fromRows(r0, r1));
    }

    @Test
    public void testMatrixFromColumns() {
        var m = new Matrix2(1.0f, 2.0f, 3.0f, 4.0f);
        var c0 = new Vector2(1.0f, 3.0f);
        var c1 = new Vector2(2.0f, 4.0f);
        Assertions.assertEquals(m, Matrix2.fromColumns(c0, c1));
    }

    @Test
    public void testRotationMatrix() {
        var v1 = new Vector2(2.0f, 0.0f);
        var v2 = new Vector2(0.0f, 2.0f);
        var m = Matrix2.rotation(Math.PI / 2.0);
        TestVector2.assertEquals(v2, m.multiply(v1));
    }

    @Test
    public void testScalingMatrix() {
        var v1 = new Vector2(1.5f, 1.5f);
        var v2 = new Vector2(3.0f, 4.5f);
        var scale = new Vector2(2.0f, 3.0f);
        var m = Matrix2.scaling(scale);
        Assertions.assertEquals(v2, m.multiply(v1));
    }

    @Test
    public void testScalingMatrixFromEqualScale() {
        var v1 = new Vector2(1.5f, 2.0f);
        var v2 = new Vector2(3.0f, 4.0f);
        var m = Matrix2.scaling(2.0f);
        Assertions.assertEquals(v2, m.multiply(v1));
    }

    // TODO: Shearing
}