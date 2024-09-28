package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector4 {

    @Test
    public void testConstructFromVector3() {
        var a = new Vector3(1.1f, 2.2f, 3.3f);
        var b = new Vector4(a, 4.4f);
        var exp = new Vector4(1.1f, 2.2f, 3.3f, 4.4f);
        Assertions.assertEquals(exp, b);
    }

    @Test
    public void testConstructFromVector2() {
        var a = new Vector2(1.1f, 2.2f);
        var b = new Vector4(a, 3.3f, 4.4f);
        var exp = new Vector4(1.1f, 2.2f, 3.3f, 4.4f);
        Assertions.assertEquals(exp, b);
    }

    @Test
    public void testAccessXY() {
        var a = new Vector4(1.1f, 2.2f, 3.3f, 4.4f);
        var b = new Vector2(1.1f, 2.2f);
        Assertions.assertEquals(b, a.xy());
    }

    @Test
    public void testAccessXYZ() {
        var a = new Vector4(1.1f, 2.2f, 3.3f, 4.4f);
        var b = new Vector3(1.1f, 2.2f, 3.3f);
        Assertions.assertEquals(b, a.xyz());
    }

    @Test
    public void testSumOfVectors() {
        var a = new Vector4(1.0f, 2.0f, 3.0f, 3.0f);
        var b = new Vector4(2.0f, 3.0f, 1.0f, -1.0f);
        var res = new Vector4(3.0f, 5.0f, 4.0f, 2.0f);
        Assertions.assertEquals(res, a.plus(b));
    }

    @Test
    public void testSubtractionOfVectors() {
        var a = new Vector4(3.0f, 5.0f, 4.0f, 2.0f);
        var b = new Vector4(2.0f, 3.0f, 1.0f, 4.0f);
        var res = new Vector4(1.0f, 2.0f, 3.0f, -2.0f);
        Assertions.assertEquals(res, a.minus(b));
    }

    @Test
    public void testNegated() {
        var vec = new Vector4(1.1f, -2.2f, 0.0f, -4.4f);
        var res = new Vector4(-1.1f, 2.2f, -0.0f, 4.4f);
        Assertions.assertEquals(res, vec.negated());
    }

    @Test
    public void testComponentWiseMultiplication() {
        var a = new Vector4(2.0f, 3.0f, 4.0f, 1.0f);
        var b = new Vector4(3.0f, 4.0f, 2.0f, 1.0f);
        var res = new Vector4(6.0f, 12.0f, 8.0f, 1.0f);
        Assertions.assertEquals(res, a.multiply(b));
    }

    @Test
    public void testMultiplicationOfVectorByScalar() {
        var vec = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var res = vec.multiply(1.5f);
        var exp = new Vector4(1.5f, 3.0f, 4.5f, 6.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testComponentWiseDivision() {
        var a = new Vector4(6.0f, 12.0f, 8.0f, 1.0f);
        var b = new Vector4(3.0f, 4.0f, 2.0f, 2.0f);
        var res = new Vector4(2.0f, 3.0f, 4.0f, 0.5f);
        Assertions.assertEquals(res, a.divide(b));
    }

    @Test
    public void testDivisionOfVectorByScalar() {
        var vec = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var res = vec.divide(2.0f);
        var exp = new Vector4(0.5f, 1.0f, 1.5f, 2.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testInverseOfVector() {
        var vec = new Vector4(2.0f, 4.0f, 8.0f, 10.0f);
        var inv = new Vector4(0.5f, 0.25f, 0.125f, 0.1f);
        Assertions.assertEquals(inv, vec.inverse());
    }

    @Test
    public void testDotProduct() {
        var a = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var b = new Vector4(2.0f, 3.0f, 1.0f, 4.0f);
        var res = a.dot(b);
        Assertions.assertEquals(27.0f, res, 1e-12f);
    }

    @Test
    public void testLengthSquared() {
        Assertions.assertEquals(4.0f, Vector4.ONE.lengthSquared(), 1e-12f);
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(Math.sqrt(4), Vector4.ONE.length(), MathUtils.EPSILON);
    }

    @Test
    public void testNormalizedVector() {
        var vec = new Vector4(0.0f, 3.0f, 0.0f, 0.0f);
        var res = vec.normalized();
        var exp = new Vector4(0.0f, 1.0f, 0.0f, 0.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testCheckIfVectorIsNormalized() {
        Assertions.assertFalse(Vector4.ONE.isNormalized());
        Assertions.assertTrue(Vector4.ONE.normalized().isNormalized());
    }

    @Test
    public void testLimitLengthOfVector() {
        var a = new Vector4(6.0f, 9.0f, 18.0f, 0.0f);
        var b = a.limitLength(7.0f);
        var res = new Vector4(2.0f, 3.0f, 6.0f, 0.0f);
        Assertions.assertEquals(res, b);
    }

    @Test
    public void testVectorAbsoluteValue() {
        var vec = new Vector4(-1.5f, -2.1f, 3.7f, 0.0f);
        var abs = new Vector4(1.5f, 2.1f, 3.7f, 0.0f);
        Assertions.assertEquals(abs, vec.abs());
    }

    @Test
    public void testVectorSign() {
        var vec = new Vector4(-1.5f, 0.0f, 3.7f, 2.0f);
        var sign = new Vector4(-1.0f, 0.0f, 1.0f, 1.0f);
        Assertions.assertEquals(sign, vec.sign());
    }

    @Test
    public void testVectorRound() {
        var vec = new Vector4(1.9f, 1.1f, 2.9f, 2.1f);
        var res = new Vector4(2.0f, 1.0f, 3.0f, 2.0f);
        Assertions.assertEquals(res, vec.round());
    }

    @Test
    public void testVectorCeil() {
        var vec = new Vector4(1.9f, 1.1f, 2.9f, 2.1f);
        var res = new Vector4(2.0f, 2.0f, 3.0f, 3.0f);
        Assertions.assertEquals(res, vec.ceil());
    }

    @Test
    public void testVectorFloor() {
        var vec = new Vector4(1.9f, 1.1f, 2.9f, 2.1f);
        var res = new Vector4(1.0f, 1.0f, 2.0f, 2.0f);
        Assertions.assertEquals(res, vec.floor());
    }

    @Test
    public void testMoveVectorTowardVector() {
        var vec = Vector4.ONE.moveToward(Vector4.ZERO, 0.5f);
        Assertions.assertTrue(vec.length() < Vector4.ONE.length());
        Assertions.assertTrue(vec.length() > 0.0f);
    }

    @Test
    public void testLinearInterpolationBetweenVectors() {
        var from = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var to = new Vector4(2.0f, 4.0f, 6.0f, 8.0f);
        var v1 = new Vector4(1.5f, 3.0f, 4.5f, 6.0f);
        var v2 = new Vector4(1.25f, 2.5f, 3.75f, 5.0f);
        var v3 = new Vector4(1.75f, 3.5f, 5.25f, 7.0f);
        Assertions.assertEquals(v1, from.lerp(to, 0.5f));
        Assertions.assertEquals(v2, from.lerp(to, 0.25f));
        Assertions.assertEquals(v3, from.lerp(to, 0.75f));
    }

    @Test
    public void testDirectionToVector() {
        var from = Vector4.ZERO;
        var to = new Vector4(0.0f, 0.0f, 10.0f, 0.0f);
        var res = new Vector4(0.0f, 0.0f, 1.0f, 0.0f);
        Assertions.assertEquals(res, from.directionTo(to));
    }

    @Test
    public void testDistanceSquaredBetweenVectors() {
        var a = new Vector4(-1.0f, 1.0f, -1.0f, 2.0f);
        var b = new Vector4(1.0f, 1.0f, 1.0f, 0.0f);
        Assertions.assertEquals(12.0f, a.distanceSquaredTo(b), 1e-12f);
    }

    @Test
    public void testDistanceBetweenVectors() {
        var a = new Vector4(-1.0f, 1.0f, -1.0f, 2.0f);
        var b = new Vector4(1.0f, 1.0f, 1.0f, 0.0f);
        Assertions.assertEquals((float) Math.sqrt(12.0), a.distanceTo(b), 1e-12f);
    }

    @Test
    public void testAngleBetweenVectors() {
        var a = new Vector4(1.0f, 0.0f, 1.0f, 0.0f);
        var b = new Vector4(0.0f, 2.0f, 0.0f, 0.0f);
        Assertions.assertEquals(Math.PI / 2.0, a.angleTo(b), MathUtils.EPSILON);
    }

    @Test
    public void testProjectVector() {
        var v = new Vector4(2.0f, 2.0f, 1.0f, 0.0f);
        var n = new Vector4(0.0f, 1.0f, 0.0f, 0.0f);
        var res = new Vector4(0.0f, 2.0f, 0.0f, 0.0f);
        Assertions.assertEquals(res, v.project(n));
    }

    @Test
    public void testReflectVector() {
        var v = new Vector4(1.0f, 2.0f, 3.0f, 0.0f);
        var n = new Vector4(0.0f, 1.0f, 0.0f, 0.0f);
        var res = new Vector4(1.0f, -2.0f, 3.0f, 0.0f);
        Assertions.assertEquals(res, v.reflect(n));
    }

    @Test
    public void testBounceVector() {
        var v = new Vector4(1.0f, 2.0f, 3.0f, 0.0f);
        var n = new Vector4(0.0f, 1.0f, 0.0f, 0.0f);
        var res = new Vector4(-1.0f, 2.0f, -3.0f, 0.0f);
        Assertions.assertEquals(res, v.bounce(n));
    }

    @Test
    public void testSlideVector() {
        var a = new Vector4(1.0f, 1.0f, 0.0f, 0.0f);
        var b = new Vector4(0.0f, 1.0f, 0.0f, 0.0f);
        var res = new Vector4(1.0f, 0.0f, 0.0f, 0.0f);
        Assertions.assertEquals(res, a.slide(b));
    }

    @Test
    public void testComponentWiseMod() {
        var a = new Vector4(1.0f, 3.0f, 8.0f, 8.0f);
        var b = new Vector4(2.0f, 2.0f, 3.0f, 4.0f);
        var res = new Vector4(1.0f, 1.0f, 2.0f, 0.0f);
        Assertions.assertEquals(res, a.mod(b));
    }

    @Test
    public void testVectorModWithScalar() {
        var vec = new Vector4(1.0f, 3.0f, 8.0f, 9.0f);
        var mod = vec.mod(3.0f);
        var res = new Vector4(1.0f, 0.0f, 2.0f, 0.0f);
        Assertions.assertEquals(res, mod);
    }

    @Test
    public void testOuterProductWithVector4() {
        var a = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var b = new Vector4(2.0f, 3.0f, 4.0f, 5.0f);
        var res = new Matrix4(2.0f, 3.0f, 4.0f, 5.0f, 4.0f, 6.0f, 8.0f, 10.0f, 6.0f, 9.0f, 12.0f, 15.0f, 8.0f, 12.0f, 16.0f, 20.0f);
        Assertions.assertEquals(res, a.outer(b));
    }

    @Test
    public void testVectorEqualsValues() {
        var vec = new Vector4(1.1f, 2.2f, 3.3f, 4.4f);
        Assertions.assertTrue(vec.equals(1.1f, 2.2f, 3.3f, 4.4f));
    }

    @Test
    public void testVectorEqualsApprox() {
        var a = new Vector4(1.0f, 2.0f, 3.0f, 4.0f);
        var b = new Vector4(0.99999999f, 1.99999999f, 3.00000001f, 3.99999999f);
        Assertions.assertTrue(a.equalsApprox(b));
    }
}