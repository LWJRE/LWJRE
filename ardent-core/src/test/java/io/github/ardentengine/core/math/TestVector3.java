package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector3 {

    public static void assertEquals(Vector3 expected, Vector3 actual) {
        Assertions.assertEquals(expected.x(), actual.x(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.y(), actual.y(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.z(), actual.z(), MathUtils.EPSILON);
    }

    @Test
    public void testConstructFromVector2() {
        var a = new Vector2(1.1f, 2.2f);
        var b = new Vector3(a, 3.3f);
        var exp = new Vector3(1.1f, 2.2f, 3.3f);
        Assertions.assertEquals(exp, b);
    }

    @Test
    public void testAccessXY() {
        var a = new Vector3(1.1f, 2.2f, 3.3f);
        var b = new Vector2(1.1f, 2.2f);
        Assertions.assertEquals(b, a.xy());
    }

    @Test
    public void testSumOfVectors() {
        var a = new Vector3(1.0f, 2.0f, 3.0f);
        var b = new Vector3(2.0f, 3.0f, 1.0f);
        var res = new Vector3(3.0f, 5.0f, 4.0f);
        Assertions.assertEquals(res, a.plus(b));
    }

    @Test
    public void testSubtractionOfVectors() {
        var a = new Vector3(3.0f, 5.0f, 4.0f);
        var b = new Vector3(2.0f, 3.0f, 1.0f);
        var res = new Vector3(1.0f, 2.0f, 3.0f);
        Assertions.assertEquals(res, a.minus(b));
    }

    @Test
    public void testNegated() {
        var vec = new Vector3(1.1f, -2.2f, 3.3f);
        var res = new Vector3(-1.1f, 2.2f, -3.3f);
        Assertions.assertEquals(res, vec.negated());
    }

    @Test
    public void testComponentWiseMultiplication() {
        var a = new Vector3(2.0f, 3.0f, 4.0f);
        var b = new Vector3(3.0f, 4.0f, 2.0f);
        var exp = new Vector3(6.0f, 12.0f, 8.0f);
        Assertions.assertEquals(exp, a.multiply(b));
    }

    @Test
    public void testMultiplicationOfVectorByScalar() {
        var vec = new Vector3(1.0f, 2.0f, 3.0f);
        var res = vec.multiply(1.5f);
        var exp = new Vector3(1.5f, 3.0f, 4.5f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testComponentWiseDivision() {
        var a = new Vector3(6.0f, 12.0f, 8.0f);
        var b = new Vector3(3.0f, 4.0f, 2.0f);
        var exp = new Vector3(2.0f, 3.0f, 4.0f);
        Assertions.assertEquals(exp, a.divide(b));
    }

    @Test
    public void testDivisionOfVectorByScalar() {
        var vec = new Vector3(1.0f, 2.0f, 3.0f);
        var res = vec.divide(2.0f);
        var exp = new Vector3(0.5f, 1.0f, 1.5f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testInverseOfVector() {
        var vec = new Vector3(2.0f, 4.0f, 8.0f);
        var inv = new Vector3(0.5f, 0.25f, 0.125f);
        Assertions.assertEquals(inv, vec.inverse());
    }

    @Test
    public void testDotProduct() {
        var a = new Vector3(1.0f, 2.0f, 3.0f);
        var b = new Vector3(2.0f, 3.0f, 1.0f);
        var res = a.dot(b);
        Assertions.assertEquals(11.0f, res, 1e-12f);
    }

    @Test
    public void testCrossProductWithVector3() {
        var a = Vector3.RIGHT;
        var b = Vector3.UP;
        var res = a.cross(b);
        Assertions.assertEquals(Vector3.FORWARD, res);
    }

    @Test
    public void testCrossProductWithVector2() {
        var a = Vector3.RIGHT;
        var b = Vector2.UP;
        var res = a.cross(b);
        Assertions.assertEquals(Vector3.FORWARD, res);
    }

    @Test
    public void testLengthSquared() {
        Assertions.assertEquals(3.0f, Vector3.ONE.lengthSquared(), 1e-12f);
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(Math.sqrt(3), Vector3.ONE.length(), MathUtils.EPSILON);
    }

    @Test
    public void testNormalizedVector() {
        var vec = new Vector3(0.0f, 3.0f, 0.0f);
        var res = vec.normalized();
        var exp = new Vector3(0.0f, 1.0f, 0.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testCheckIfVectorIsNormalized() {
        Assertions.assertFalse(Vector3.ONE.isNormalized());
        Assertions.assertTrue(Vector3.FORWARD.isNormalized());
        Assertions.assertTrue(Vector3.ONE.normalized().isNormalized());
    }

    @Test
    public void testLimitLengthOfVector() {
        var a = new Vector3(6.0f, 9.0f, 18.0f);
        var b = a.limitLength(7.0f);
        var res = new Vector3(2.0f, 3.0f, 6.0f);
        Assertions.assertEquals(res, b);
    }

    @Test
    public void testVectorAbsoluteValue() {
        var vec = new Vector3(-1.5f, -2.1f, 3.7f);
        var abs = new Vector3(1.5f, 2.1f, 3.7f);
        Assertions.assertEquals(abs, vec.abs());
    }

    @Test
    public void testVectorSign() {
        var vec = new Vector3(-1.5f, 0.0f, 3.7f);
        var sign = new Vector3(-1.0f, 0.0f, 1.0f);
        Assertions.assertEquals(sign, vec.sign());
    }

    @Test
    public void testVectorRound() {
        var vec = new Vector3(1.9f, 1.1f, 2.9f);
        var res = new Vector3(2.0f, 1.0f, 3.0f);
        Assertions.assertEquals(res, vec.round());
    }

    @Test
    public void testVectorCeil() {
        var vec = new Vector3(1.9f, 1.1f, 2.9f);
        var res = new Vector3(2.0f, 2.0f, 3.0f);
        Assertions.assertEquals(res, vec.ceil());
    }

    @Test
    public void testVectorFloor() {
        var vec = new Vector3(1.9f, 1.1f, 2.9f);
        var res = new Vector3(1.0f, 1.0f, 2.0f);
        Assertions.assertEquals(res, vec.floor());
    }

    @Test
    public void testMoveVectorTowardVector() {
        var vec = Vector3.ONE.moveToward(Vector3.ZERO, 0.5f);
        Assertions.assertTrue(vec.length() < Vector3.ONE.length());
        Assertions.assertTrue(vec.length() > 0.0f);
    }

    @Test
    public void testLinearInterpolationBetweenVectors() {
        var from = new Vector3(1.0f, 2.0f, 3.0f);
        var to = new Vector3(2.0f, 4.0f, 6.0f);
        var v1 = new Vector3(1.5f, 3.0f, 4.5f);
        var v2 = new Vector3(1.25f, 2.5f, 3.75f);
        var v3 = new Vector3(1.75f, 3.5f, 5.25f);
        Assertions.assertEquals(v1, from.lerp(to, 0.5f));
        Assertions.assertEquals(v2, from.lerp(to, 0.25f));
        Assertions.assertEquals(v3, from.lerp(to, 0.75f));
    }

    @Test
    public void testSphericalLinearInterpolationBetweenVectors() {
        var from = Vector3.LEFT;
        var to = Vector3.BACKWARDS;
        var vec = new Vector3((float) -Math.sin(Math.PI / 4.0), 0.0f, (float) -Math.sin(Math.PI / 4.0f));
        assertEquals(vec, from.slerp(to, 0.5f));
    }

    @Test
    public void testSphericalLinearInterpolationWithZeroVector() {
        var from = Vector3.ZERO;
        var to = Vector3.ONE;
        assertEquals(from.lerp(to, 0.5f), from.slerp(to, 0.5f));
    }

    @Test
    public void testDirectionToVector() {
        var from = Vector3.ZERO;
        var to = new Vector3(0.0f, 10.0f, 0.0f);
        Assertions.assertEquals(Vector3.UP, from.directionTo(to));
    }

    @Test
    public void testDistanceSquaredBetweenVectors() {
        var a = new Vector3(-1.0f, 1.0f, -1.0f);
        var b = new Vector3(1.0f, 1.0f, 1.0f);
        Assertions.assertEquals(8.0f, a.distanceSquaredTo(b), 1e-12f);
    }

    @Test
    public void testDistanceBetweenVectors() {
        var a = new Vector3(-1.0f, 1.0f, -1.0f);
        var b = new Vector3(1.0f, 1.0f, 1.0f);
        Assertions.assertEquals((float) Math.sqrt(8.0), a.distanceTo(b), 1e-12f);
    }

    @Test
    public void testAngleBetweenVectors() {
        var a = new Vector3(1.0f, 0.0f, 1.0f);
        var b = new Vector3(0.0f, 2.0f, 0.0f);
        Assertions.assertEquals(Math.PI / 2.0, a.angleTo(b), MathUtils.EPSILON);
    }

    @Test
    public void testSignedAngleToVector() {
        var a = new Vector3(1.0f, 1.0f, 0.0f);
        var b = new Vector3(0.0f, 1.0f, 1.0f);
        Assertions.assertEquals(-Math.PI / 3.0, a.signedAngleTo(b, Vector3.UP), MathUtils.EPSILON);
        Assertions.assertEquals(Math.PI / 3.0, a.signedAngleTo(b, Vector3.DOWN), MathUtils.EPSILON);
    }

    @Test
    public void testProjectVector() {
        var vec = new Vector3(2.0f, 2.0f, 1.0f);
        var proj = vec.project(Vector3.RIGHT);
        var res = new Vector3(2.0f, 0.0f, 0.0f);
        Assertions.assertEquals(res, proj);
    }

    @Test
    public void testReflectVector() {
        var vec = new Vector3(1.0f, 2.0f, 3.0f);
        var ref = vec.reflect(Vector3.UP);
        var res = new Vector3(1.0f, -2.0f, 3.0f);
        Assertions.assertEquals(res, ref);
    }

    @Test
    public void testBounceVector() {
        var vec = new Vector3(1.0f, 2.0f, 3.0f);
        var bounce = vec.bounce(Vector3.UP);
        var res = new Vector3(-1.0f, 2.0f, -3.0f);
        Assertions.assertEquals(res, bounce);
    }

    @Test
    public void testSlideVector() {
        var a = new Vector3(1.0f, 1.0f, 0.0f);
        var b = new Vector3(0.0f, 1.0f, 0.0f);
        var res = new Vector3(1.0f, 0.0f, 0.0f);
        Assertions.assertEquals(res, a.slide(b));
    }

    @Test
    public void testComponentWiseMod() {
        var a = new Vector3(1.0f, 3.0f, 8.0f);
        var b = new Vector3(2.0f, 2.0f, 3.0f);
        var res = new Vector3(1.0f, 1.0f, 2.0f);
        Assertions.assertEquals(res, a.mod(b));
    }

    @Test
    public void testVectorModWithScalar() {
        var vec = new Vector3(1.0f, 3.0f, 8.0f);
        var mod = vec.mod(3.0f);
        var res = new Vector3(1.0f, 0.0f, 2.0f);
        Assertions.assertEquals(res, mod);
    }

    @Test
    public void testVectorRotatedByAxisAndAngle() {
        var vec = Vector3.RIGHT;
        var rot = vec.rotated(Vector3.UP, Math.PI / 2.0);
        var res = Vector3.BACKWARDS;
        assertEquals(res, rot);
    }

    @Test
    public void testOuterProductWithVector3() {
        var a = new Vector3(1.0f, 2.0f, 3.0f);
        var b = new Vector3(2.0f, 3.0f, 4.0f);
        var res = new Matrix3(2.0f, 3.0f, 4.0f, 4.0f, 6.0f, 8.0f, 6.0f, 9.0f, 12.0f);
        Assertions.assertEquals(res, a.outer(b));
    }

    @Test
    public void testOuterProductWithVector4() {
        var a = new Vector3(1.0f, 2.0f, 3.0f);
        var b = new Vector4(2.0f, 3.0f, 4.0f, 5.0f);
        var res = new Matrix3x4(2.0f, 3.0f, 4.0f, 5.0f, 4.0f, 6.0f, 8.0f, 10.0f, 6.0f, 9.0f, 12.0f, 15.0f);
        Assertions.assertEquals(res, a.outer(b));
    }

    @Test
    public void testQuadraticBezierInterpolation() {
        var p0 = new Vector3(1.0f, 1.5f, 0.5f);
        var p2 = new Vector3(5.0f, 6.0f, 4.0f);
        var p1 = new Vector3(2.0f, 3.0f, 2.5f);
        var t = 0.35f;
        var l1 = p0.lerp(p1, t);
        var l2 = p1.lerp(p2, t);
        var res = l1.lerp(l2, t);
        assertEquals(res, p0.bezierInterpolate(p2, p1, t));
    }

    @Test
    public void testQuadraticBezierDerivative() {
        var p0 = new Vector3(0.0f, 0.0f, 0.0f);
        var p1 = new Vector3(2.0f, 0.0f, 0.0f);
        var control = new Vector3(1.0f, 1.0f, 0.0f);
        var res = new Vector3(2.0f, 0.0f, 0.0f);
        assertEquals(res, p0.bezierDerivative(p1, control, 0.5f));
    }

    @Test
    public void testCubicBezierInterpolation() {
        var p0 = new Vector3(1.0f, 1.5f, 0.5f);
        var p3 = new Vector3(5.0f, 6.0f, 4.0f);
        var p1 = new Vector3(1.5f, 2.5f, 1.0f);
        var p2 = new Vector3(3.0f, 4.0f, 2.0f);
        var t = 0.35f;
        var m1 = p0.lerp(p1, t);
        var m2 = p1.lerp(p2, t);
        var m3 = p2.lerp(p3, t);
        var l1 = m1.lerp(m2, t);
        var l2 = m2.lerp(m3, t);
        var res = l1.lerp(l2, t);
        assertEquals(res, p0.bezierInterpolate(p3, p1, p2, t));
    }

    @Test
    public void testCubicBezierDerivative() {
        var p0 = new Vector3(0.0f, 0.0f, 0.0f);
        var p1 = new Vector3(2.0f, 0.0f, 0.0f);
        var control1 = new Vector3(0.0f, 1.0f, 0.0f);
        var control2 = new Vector3(2.0f, 1.0f, 0.0f);
        var res = new Vector3(3.0f, 0.0f, 0.0f);
        assertEquals(res, p0.bezierDerivative(p1, control1, control2, 0.5f));
    }

    @Test
    public void testVectorEqualsValues() {
        var vec = new Vector3(1.1f, 2.2f, 3.3f);
        Assertions.assertTrue(vec.equals(1.1f, 2.2f, 3.3f));
    }

    @Test
    public void testVectorEqualsApprox() {
        var a = new Vector3(1.0f, 2.0f, 3.0f);
        var b = new Vector3(0.99999999f, 1.99999999f, 3.00000001f);
        Assertions.assertTrue(a.equalsApprox(b));
    }
}