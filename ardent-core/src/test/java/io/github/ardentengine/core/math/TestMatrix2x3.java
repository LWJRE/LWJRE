package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMatrix2x3 {

    @Test
    public void testMatrixFromSubmatrixAndColumn() {
        var m = new Matrix2(1.0f, 2.0f, 4.0f, 5.0f);
        var v = new Vector2(3.0f, 6.0f);
        var res = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        Assertions.assertEquals(res, new Matrix2x3(m, v));
    }

    @Test
    public void testMatrixFromColumnAndSubmatrix() {
        var m = new Matrix2(2.0f, 3.0f, 5.0f, 6.0f);
        var v = new Vector2(1.0f, 4.0f);
        var res = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        Assertions.assertEquals(res, new Matrix2x3(v, m));
    }

    @Test
    public void testMatrixSum() {
        var m1 = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var m2 = new Matrix2x3(3.0f, 4.0f, 1.0f, 2.0f, 6.0f, 5.0f);
        var res = new Matrix2x3(4.0f, 6.0f, 4.0f, 6.0f, 11.0f, 11.0f);
        Assertions.assertEquals(res, m1.plus(m2));
    }

    @Test
    public void testMatrixSubtraction() {
        var m1 = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var m2 = new Matrix2x3(3.0f, 4.0f, 1.0f, 2.0f, 6.0f, 5.0f);
        var res = new Matrix2x3(2.0f, 2.0f, -2.0f, -2.0f, 1.0f, -1.0f);
        Assertions.assertEquals(res, m2.minus(m1));
    }

    @Test
    public void testNegated() {
        var m = new Matrix2x3(1.0f, 2.0f, -2.0f, -1.0f, 3.0f, -2.0f);
        var r = new Matrix2x3(-1.0f, -2.0f, 2.0f, 1.0f, -3.0f, 2.0f);
        Assertions.assertEquals(r, m.negated());
    }

    @Test
    public void testMatrixMultipliedByScalar() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var r = new Matrix2x3(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f);
        Assertions.assertEquals(r, m.multiply(1.5f));
    }

    @Test
    public void testMatrixDividedByScalar() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var r = new Matrix2x3(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f);
        Assertions.assertEquals(r, m.divide(2.0f));
    }

    @Test
    public void testGetRows() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var r0 = new Vector3(1.0f, 2.0f, 3.0f);
        var r1 = new Vector3(4.0f, 5.0f, 6.0f);
        Assertions.assertEquals(r0, m.row0());
        Assertions.assertEquals(r1, m.row1());
    }

    @Test
    public void testGetRowAtIndex() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var r0 = new Vector3(1.0f, 2.0f, 3.0f);
        var r1 = new Vector3(4.0f, 5.0f, 6.0f);
        Assertions.assertEquals(r0, m.row(0));
        Assertions.assertEquals(r1, m.row(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.row(2));
    }

    @Test
    public void testGetColumns() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var c0 = new Vector2(1.0f, 4.0f);
        var c1 = new Vector2(2.0f, 5.0f);
        var c2 = new Vector2(3.0f, 6.0f);
        Assertions.assertEquals(c0, m.column0());
        Assertions.assertEquals(c1, m.column1());
        Assertions.assertEquals(c2, m.column2());
    }

    @Test
    public void testGetColumnAtIndex() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var c0 = new Vector2(1.0f, 4.0f);
        var c1 = new Vector2(2.0f, 5.0f);
        var c2 = new Vector2(3.0f, 6.0f);
        Assertions.assertEquals(c0, m.column(0));
        Assertions.assertEquals(c1, m.column(1));
        Assertions.assertEquals(c2, m.column(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.column(3));
    }

    @Test
    public void testMatrixVectorProduct() {
        var mat = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var vec = new Vector3(2.0f, 3.0f, 4.0f);
        var res = new Vector2(20.0f, 47.0f);
        Assertions.assertEquals(res, mat.multiply(vec));
    }

    @Test
    public void testMatrixProduct3x3() {
        var m1 = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var m2 = new Matrix3(3.0f, 4.0f, 1.0f, 2.0f, 6.0f, 5.0f, -2.0f, 8.0f, -1.0f);
        var res = new Matrix2x3(1.0f, 40.0f, 8.0f, 10.0f, 94.0f, 23.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testMatrixProduct2x3() {
        var m1 = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var m2 = new Matrix2x3(3.0f, 4.0f, 1.0f, 2.0f, 6.0f, 5.0f);
        var res = new Matrix2x3(1.0f, 40.0f, 8.0f, 10.0f, 94.0f, 23.0f);
        Assertions.assertEquals(res, m1.multiply(m2, -2.0f, 8.0f, -1.0f));
    }

    @Test
    public void testAffineInverse() {
        var transform = Matrix2x3.translation(4.0f, 3.0f)
            .multiply(Matrix3.rotationZ(Math.PI / 4.0))
            .multiply(Matrix3.scaling(2.0f, 2.0f, 1.0f));
        var point = new Vector2(8.0f, 6.0f);
        var res = transform.multiply(point, 1.0f);
        TestVector2.assertEquals(point, transform.affineInverse().multiply(res, 1.0f));
    }

    @Test
    public void testLerpElements() {
        var from = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var to = new Matrix2x3(2.0f, 4.0f, 6.0f, 8.0f, 10.0f, 12.0f);
        var res = new Matrix2x3(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f);
        Assertions.assertEquals(res, from.lerp(to, 0.5f));
    }

    @Test
    public void testMatrixAbsoluteValue() {
        var mat = new Matrix2x3(1.0f, 2.0f, 0.0f, -2.0f, 0.0f, -1.0f);
        var abs = new Matrix2x3(1.0f, 2.0f, 0.0f, 2.0f, 0.0f, 1.0f);
        Assertions.assertEquals(abs, mat.abs());
    }

    @Test
    public void testMatrixSign() {
        var mat = new Matrix2x3(1.0f, 2.0f, 0.0f, -2.0f, 0.0f, -1.0f);
        var sign = new Matrix2x3(1.0f, 1.0f, 0.0f, -1.0f, 0.0f, -1.0f);
        Assertions.assertEquals(sign, mat.sign());
    }

    @Test
    public void testMatrixRound() {
        var mat = new Matrix2x3(1.1f, 1.9f, 0.2f, 0.8f, 2.7f, 2.2f);
        var round = new Matrix2x3(1.0f, 2.0f, 0.0f, 1.0f, 3.0f, 2.0f);
        Assertions.assertEquals(round, mat.round());
    }

    @Test
    public void testMatrixCeil() {
        var mat = new Matrix2x3(1.1f, 1.9f, 0.2f, 0.8f, 2.7f, 2.2f);
        var ceil = new Matrix2x3(2.0f, 2.0f, 1.0f, 1.0f, 3.0f, 3.0f);
        Assertions.assertEquals(ceil, mat.ceil());
    }

    @Test
    public void testMatrixFloor() {
        var mat = new Matrix2x3(1.1f, 1.9f, 0.2f, 0.8f, 2.7f, 2.2f);
        var floor = new Matrix2x3(1.0f, 1.0f, 0.0f, 0.0f, 2.0f, 2.0f);
        Assertions.assertEquals(floor, mat.floor());
    }

    // TODO: Orthonormalize

    @Test
    public void testMatrixEqualsApprox() {
        var m1 = new Matrix2x3(1.00000001f, 1.99999999f, 3.00000001f, 3.99999999f, 0.00000001f, -0.00000001f);
        var m2 = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 0.0f, 0.0f);
        Assertions.assertTrue(m1.equalsApprox(m2));
    }

    @Test
    public void testMatrixFromRows() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var r0 = new Vector3(1.0f, 2.0f, 3.0f);
        var r1 = new Vector3(4.0f, 5.0f, 6.0f);
        Assertions.assertEquals(m, Matrix2x3.fromRows(r0, r1));
    }

    @Test
    public void testMatrixFromColumns() {
        var m = new Matrix2x3(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
        var c0 = new Vector2(1.0f, 4.0f);
        var c1 = new Vector2(2.0f, 5.0f);
        var c2 = new Vector2(3.0f, 6.0f);
        Assertions.assertEquals(m, Matrix2x3.fromColumns(c0, c1, c2));
    }

    @Test
    public void testTranslationMatrix() {
        var v1 = new Vector2(1.0f, 1.0f);
        var v2 = new Vector2(3.0f, 4.0f);
        var m = Matrix2x3.translation(v2);
        Assertions.assertEquals(v1.plus(v2), m.multiply(v1, 1.0f));
    }

    @Test
    public void testRotationMatrix() {
        var v1 = new Vector2(2.0f, 0.0f);
        var v2 = new Vector2(0.0f, 2.0f);
        var m = Matrix2x3.rotation(Math.PI / 2.0);
        TestVector2.assertEquals(v2, m.multiply(v1, 0.0f));
    }

    @Test
    public void testScalingMatrix() {
        var v1 = new Vector2(1.5f, 1.5f);
        var v2 = new Vector2(3.0f, 4.5f);
        var scale = new Vector2(2.0f, 3.0f);
        var m = Matrix2x3.scaling(scale);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f));
    }

    @Test
    public void testScalingMatrixFromEqualScale() {
        var v1 = new Vector2(1.5f, 2.0f);
        var v2 = new Vector2(3.0f, 4.0f);
        var m = Matrix2x3.scaling(2.0f);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f));
    }

    // TODO: Shearing
}