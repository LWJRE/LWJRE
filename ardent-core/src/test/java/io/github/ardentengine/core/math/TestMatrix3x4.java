package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMatrix3x4 {

    @Test
    public void testMatrixFromSubmatrixAndColumn() {
        var m = new Matrix3(1.0f, 2.0f, 3.0f, 5.0f, 6.0f, 7.0f, 9.0f, 10.0f, 11.0f);
        var v = new Vector3(4.0f, 8.0f, 12.0f);
        var res = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        Assertions.assertEquals(res, new Matrix3x4(m, v));
    }

    @Test
    public void testMatrixFromColumnAndSubmatrix() {
        var m = new Matrix3(2.0f, 3.0f, 4.0f, 6.0f, 7.0f, 8.0f, 10.0f, 11.0f, 12.0f);
        var v = new Vector3(1.0f, 5.0f, 9.0f);
        var res = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        Assertions.assertEquals(res, new Matrix3x4(v, m));
    }

    @Test
    public void testMatrixSum() {
        var m1 = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var m2 = new Matrix3x4(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f, 11.0f, 12.0f, 10.0f);
        var res = new Matrix3x4(4.0f, 6.0f, 4.0f, 6.0f, 12.0f, 11.0f, 16.0f, 14.0f, 17.0f, 21.0f, 23.0f, 22.0f);
        Assertions.assertEquals(res, m1.plus(m2));
    }

    @Test
    public void testMatrixSubtraction() {
        var m1 = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var m2 = new Matrix3x4(3.0f, 4.0f, 1.0f, 2.0f, 7.0f, 5.0f, 9.0f, 6.0f, 8.0f, 11.0f, 12.0f, 10.0f);
        var res = new Matrix3x4(2.0f, 2.0f, -2.0f, -2.0f, 2.0f, -1.0f, 2.0f, -2.0f, -1.0f, 1.0f, 1.0f, -2.0f);
        Assertions.assertEquals(res, m2.minus(m1));
    }

    @Test
    public void testNegated() {
        var m = new Matrix3x4(1.0f, 2.0f, -2.0f, -1.0f, 3.0f, -2.0f, 0.0f, -9.0f, 4.0f, 2.0f, -0.0f, -5.0f);
        var r = new Matrix3x4(-1.0f, -2.0f, 2.0f, 1.0f, -3.0f, 2.0f, -0.0f, 9.0f, -4.0f, -2.0f, 0.0f, 5.0f);
        Assertions.assertEquals(r, m.negated());
    }

    @Test
    public void testMatrixMultipliedByScalar() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var r = new Matrix3x4(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 10.5f, 12.0f, 13.5f, 15.0f, 16.5f, 18.0f);
        Assertions.assertEquals(r, m.multiply(1.5f));
    }

    @Test
    public void testMatrixDividedByScalar() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var r = new Matrix3x4(0.5f, 1.0f, 1.5f, 2.0f, 2.5f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 6.0f);
        Assertions.assertEquals(r, m.divide(2.0f));
    }

    @Test
    public void testGetRows() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var r0 = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var r1 = new Vector4(5.0f, 6.0f, 7.0f, 8.0f);
        var r2 = new Vector4(9.0f, 10.0f, 11.0f, 12.0f);
        Assertions.assertEquals(r0, m.row0());
        Assertions.assertEquals(r1, m.row1());
        Assertions.assertEquals(r2, m.row2());
    }

    @Test
    public void testGetRowAtIndex() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var r0 = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var r1 = new Vector4(5.0f, 6.0f, 7.0f, 8.0f);
        var r2 = new Vector4(9.0f, 10.0f, 11.0f, 12.0f);
        Assertions.assertEquals(r0, m.row(0));
        Assertions.assertEquals(r1, m.row(1));
        Assertions.assertEquals(r2, m.row(2));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.row(3));
    }

    @Test
    public void testGetColumns() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var c0 = new Vector3(1.0f, 5.0f, 9.0f);
        var c1 = new Vector3(2.0f, 6.0f, 10.0f);
        var c2 = new Vector3(3.0f, 7.0f, 11.0f);
        var c3 = new Vector3(4.0f, 8.0f, 12.0f);
        Assertions.assertEquals(c0, m.column0());
        Assertions.assertEquals(c1, m.column1());
        Assertions.assertEquals(c2, m.column2());
        Assertions.assertEquals(c3, m.column3());
    }

    @Test
    public void testGetColumnAtIndex() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var c0 = new Vector3(1.0f, 5.0f, 9.0f);
        var c1 = new Vector3(2.0f, 6.0f, 10.0f);
        var c2 = new Vector3(3.0f, 7.0f, 11.0f);
        var c3 = new Vector3(4.0f, 8.0f, 12.0f);
        Assertions.assertEquals(c0, m.column(0));
        Assertions.assertEquals(c1, m.column(1));
        Assertions.assertEquals(c2, m.column(2));
        Assertions.assertEquals(c3, m.column(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> m.column(4));
    }

    @Test
    public void testMatrixVectorProduct() {
        var mat = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var vec = new Vector4(1.5f, 2.5f, 3.5f, 4.5f);
        var res = new Vector3(35.0f, 83.0f, 131.0f);
        Assertions.assertEquals(res, mat.multiply(vec));
    }

    @Test
    public void testMatrixProduct4x4() {
        var m1 = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var m2 = new Matrix4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f, 13.0f, 14.0f, 15.0f, 16.0f);
        var res = new Matrix3x4(90.0f, 100.0f, 110.0f, 120.0f, 202.0f, 228.0f, 254.0f, 280.0f, 314.0f, 356.0f, 398.0f, 440.0f);
        Assertions.assertEquals(res, m1.multiply(m2));
    }

    @Test
    public void testMatrixProduct3x4() {
        var m1 = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var m2 = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var res = new Matrix3x4(90.0f, 100.0f, 110.0f, 120.0f, 202.0f, 228.0f, 254.0f, 280.0f, 314.0f, 356.0f, 398.0f, 440.0f);
        Assertions.assertEquals(res, m1.multiply(m2, 13.0f, 14.0f, 15.0f, 16.0f));
    }

    @Test
    public void testAffineInverse() {
        var transform = Matrix3x4.translation(4.0f, 3.0f, 2.0f).multiply(Matrix4.rotation(Math.PI / 4.0, 0.0, Math.PI / 2.0)).multiply(Matrix4.scaling(2.0f, 1.0f, 2.0f));
        var point = new Vector3(8.0f, 6.0f, 9.0f);
        var res = transform.multiply(point, 1.0f);
        TestVector3.assertEquals(point, transform.affineInverse().multiply(res, 1.0f));
    }

    @Test
    public void testLerpElements() {
        var from = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var to = new Matrix3x4(2.0f, 4.0f, 6.0f, 8.0f, 10.0f, 12.0f, 14.0f, 16.0f, 18.0f, 20.0f, 22.0f, 24.0f);
        var res = new Matrix3x4(1.5f, 3.0f, 4.5f, 6.0f, 7.5f, 9.0f, 10.5f, 12.0f, 13.5f, 15.0f, 16.5f, 18.0f);
        Assertions.assertEquals(res, from.lerp(to, 0.5f));
    }

    @Test
    public void testMatrixAbsoluteValue() {
        var mat = new Matrix3x4(1.0f, 2.0f, 0.0f, -2.0f, 3.0f, -1.0f, -3.0f, 0.0f, 1.0f, -7.0f, 4.0f, 0.0f);
        var abs = new Matrix3x4(1.0f, 2.0f, 0.0f, 2.0f, 3.0f, 1.0f, 3.0f, 0.0f, 1.0f, 7.0f, 4.0f, 0.0f);
        Assertions.assertEquals(abs, mat.abs());
    }

    @Test
    public void testMatrixSign() {
        var mat = new Matrix3x4(1.0f, 2.0f, 0.0f, -2.0f, 3.0f, -1.0f, -3.0f, 0.0f, 1.0f, -7.0f, 4.0f, 0.0f);
        var sign = new Matrix3x4(1.0f, 1.0f, 0.0f, -1.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 1.0f, 0.0f);
        Assertions.assertEquals(sign, mat.sign());
    }

    @Test
    public void testMatrixRound() {
        var mat = new Matrix3x4(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f, 1.1f, 0.9f, 2.6f);
        var round = new Matrix3x4(1.0f, 2.0f, 0.0f, 1.0f, 2.0f, 3.0f, 3.0f, 2.0f, 1.0f, 1.0f, 1.0f, 3.0f);
        Assertions.assertEquals(round, mat.round());
    }

    @Test
    public void testMatrixCeil() {
        var mat = new Matrix3x4(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f, 1.1f, 0.9f, 2.6f);
        var ceil = new Matrix3x4(2.0f, 2.0f, 1.0f, 1.0f, 3.0f, 3.0f, 3.0f, 3.0f, 1.0f, 2.0f, 1.0f, 3.0f);
        Assertions.assertEquals(ceil, mat.ceil());
    }

    @Test
    public void testMatrixFloor() {
        var mat = new Matrix3x4(1.1f, 1.9f, 0.2f, 0.8f, 2.1f, 2.9f, 2.7f, 2.3f, 0.9f, 1.1f, 0.9f, 2.6f);
        var floor = new Matrix3x4(1.0f, 1.0f, 0.0f, 0.0f, 2.0f, 2.0f, 2.0f, 2.0f, 0.0f, 1.0f, 0.0f, 2.0f);
        Assertions.assertEquals(floor, mat.floor());
    }

    // TODO: Orthonormalize matrix

    @Test
    public void testMatrixEqualsApprox() {
        var m1 = new Matrix3x4(1.00000001f, 1.99999999f, 3.00000001f, 3.99999999f, 5.00000001f, 5.99999999f, 7.00000001f, 7.99999999f, 9.00000001f, 2.00000001f, 1.99999999f, 3.00000001f);
        var m2 = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 2.0f, 2.0f, 3.0f);
        Assertions.assertTrue(m1.equalsApprox(m2));
    }

    @Test
    public void testMatrixFromRows() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var r0 = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var r1 = new Vector4(5.0f, 6.0f, 7.0f, 8.0f);
        var r2 = new Vector4(9.0f, 10.0f, 11.0f, 12.0f);
        Assertions.assertEquals(m, Matrix3x4.fromRows(r0, r1, r2));
    }

    @Test
    public void testMatrixFromColumns() {
        var m = new Matrix3x4(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f, 12.0f);
        var c0 = new Vector3(1.0f, 5.0f, 9.0f);
        var c1 = new Vector3(2.0f, 6.0f, 10.0f);
        var c2 = new Vector3(3.0f, 7.0f, 11.0f);
        var c3 = new Vector3(4.0f, 8.0f, 12.0f);
        Assertions.assertEquals(m, Matrix3x4.fromColumns(c0, c1, c2, c3));
    }

    @Test
    public void testTranslationMatrix() {
        var v = new Vector3(1.0f, 2.0f, 3.0f);
        var t = new Vector3(3.0f, 4.0f, 2.0f);
        var m = Matrix3x4.translation(t);
        Assertions.assertEquals(v.plus(t), m.multiply(v, 1.0f));
    }

    @Test
    public void testRotationMatrixAroundX() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(1.0f, 0.70710678f, 2.12132034f);
        var m = Matrix3x4.rotationX(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f));
    }

    @Test
    public void testRotationMatrixAroundY() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(1.41421356f, 2.0f, 0.0f);
        var m = Matrix3x4.rotationY(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f));
    }

    @Test
    public void testRotationMatrixAroundZ() {
        var v1 = new Vector3(1.0f, 2.0f, 1.0f);
        var v2 = new Vector3(-0.70710678f, 2.12132034f, 1.0f);
        var m = Matrix3x4.rotationZ(Math.PI / 4.0);
        Assertions.assertEquals(v2, m.multiply(v1, 0.0f));
    }

    @Test
    public void testRotationMatrixAroundArbitraryAxis() {
        var axis = Vector3.ONE.normalized();
        var point = new Vector3(1.0f, 2.0f, 1.0f);
        var res = new Vector3(0.68938278f, 1.80473785f, 1.50587936f);
        var matrix = Matrix3x4.rotation(axis, Math.PI / 4.0);
        Assertions.assertEquals(res, matrix.multiply(point, 1.0f));
    }

    @Test
    public void testRotationMatrixFromQuaternion() {
//        var quaternion = new Quatf(0.7233174f, 0.3919038f, 0.2005621f, 0.5319757f);
//        var matrix = Matrix3x4.rotation(quaternion)
//        var point = new Vector3(1.0f, 2.0f, 1.0f);
//        Assertions.assertEquals(quaternion.rotate(point, matrix * (point, 0.0f));)
    }

    @Test
    public void testScalingMatrix() {
        var v = new Vector3(1.5f, 1.5f, 1.5f);
        var s = new Vector3(2.0f, 3.0f, 4.0f);
        var m = Matrix3x4.scaling(s);
        Assertions.assertEquals(v.multiply(s), m.multiply(v, 1.0f));
    }

//    @Test
//    public void testScaling matrix from 2D vector() {
//        var v = new Vector2(1.5f, 1.5f);
//        var s = new Vector2(2.0f, 3.0f);
//        var m = Matrix4.scaling(s)
//        Assertions.assertEquals(v * s, (m * (v, 0.0f, 0.0f)).xy);
//    }
}