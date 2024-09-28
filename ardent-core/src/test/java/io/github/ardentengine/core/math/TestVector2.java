package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector2 {

    public static void assertEquals(Vector2 expected, Vector2 actual) {
        Assertions.assertEquals(expected.x(), actual.x(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.y(), actual.y(), MathUtils.EPSILON);
    }

    @Test
    public void testSumOfVectors() {
        var a = new Vector2(1.0f, 2.0f);
        var b = new Vector2(2.0f, 3.0f);
        var res = new Vector2(3.0f, 5.0f);
        Assertions.assertEquals(res, a.plus(b));
    }

    @Test
    public void testSubtractionOfVectors() {
        var a = new Vector2(3.0f, 5.0f);
        var b = new Vector2(2.0f, 3.0f);
        var res = new Vector2(1.0f, 2.0f);
        Assertions.assertEquals(res, a.minus(b));
    }

    @Test
    public void testNegated() {
        var vec = new Vector2(1.1f, -2.2f);
        var res = new Vector2(-1.1f, 2.2f);
        Assertions.assertEquals(res, vec.negated());
    }

    @Test
    public void testComponentWiseMultiplication() {
        var a = new Vector2(2.0f, 3.0f);
        var b = new Vector2(3.0f, 4.0f);
        var res = new Vector2(6.0f, 12.0f);
        Assertions.assertEquals(res, a.multiply(b));
    }

    @Test
    public void testMultiplicationOfVectorByScalar() {
        var vec = new Vector2(1.0f, 2.0f);
        var res = vec.multiply(1.5f);
        var exp = new Vector2(1.5f, 3.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testComponentWiseDivision() {
        var a = new Vector2(6.0f, 12.0f);
        var b = new Vector2(3.0f, 4.0f);
        var res = new Vector2(2.0f, 3.0f);
        Assertions.assertEquals(res, a.divide(b));
    }

    @Test
    public void testDivisionOfVectorByScalar() {
        var vec = new Vector2(1.0f, 2.0f);
        var res = vec.divide(2.0f);
        var exp = new Vector2(0.5f, 1.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testInverseOfVector() {
        var vec = new Vector2(2.0f, 4.0f);
        var inv = new Vector2(0.5f, 0.25f);
        Assertions.assertEquals(inv, vec.inverse());
    }

    @Test
    public void testDotProduct() {
        var a = new Vector2(1.0f, 2.0f);
        var b = new Vector2(2.0f, 3.0f);
        var res = a.dot(b);
        Assertions.assertEquals(8.0f, res, 1e-12f);
    }

    @Test
    public void testLengthSquared() {
        Assertions.assertEquals(2.0f, Vector2.ONE.lengthSquared(), 1e-12f);
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(Math.sqrt(2), Vector2.ONE.length(), MathUtils.EPSILON);
    }

    @Test
    public void testNormalizedVector() {
        var vec = new Vector2(0.0f, 3.0f);
        var res = vec.normalized();
        var exp = new Vector2(0.0f, 1.0f);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testCheckIfVectorIsNormalized() {
        Assertions.assertFalse(Vector2.ONE.isNormalized());
        Assertions.assertTrue(Vector2.LEFT.isNormalized());
        Assertions.assertTrue(Vector2.ONE.normalized().isNormalized());
    }

    @Test
    public void testLimitLengthOfVector() {
        var a = new Vector2(6.0f, 8.0f);
        var b = a.limitLength(5.0f);
        var res = new Vector2(3.0f, 4.0f);
        Assertions.assertEquals(res, b);
    }

    @Test
    public void testVectorAbsoluteValue() {
        var vec = new Vector2(-1.5f, 3.7f);
        var abs = new Vector2(1.5f, 3.7f);
        Assertions.assertEquals(abs, vec.abs());
    }

    @Test
    public void testVectorSign() {
        var vec = new Vector2(-1.5f, 3.7f);
        var sign = new Vector2(-1.0f, 1.0f);
        Assertions.assertEquals(sign, vec.sign());
    }

    @Test
    public void testVectorRound() {
        var vec = new Vector2(1.9f, 1.1f);
        var res = new Vector2(2.0f, 1.0f);
        Assertions.assertEquals(res, vec.round());
    }

    @Test
    public void testVectorCeil() {
        var vec = new Vector2(1.9f, 1.1f);
        var res = new Vector2(2.0f, 2.0f);
        Assertions.assertEquals(res, vec.ceil());
    }

    @Test
    public void testVectorFloor() {
        var vec = new Vector2(1.9f, 1.1f);
        var res = new Vector2(1.0f, 1.0f);
        Assertions.assertEquals(res, vec.floor());
    }

    @Test
    public void testMoveVectorTowardVector() {
        var vec = Vector2.ONE.moveToward(Vector2.ZERO, 0.5f);
        Assertions.assertTrue(vec.length() < Vector2.ONE.length());
        Assertions.assertTrue(vec.length() > 0.0f);
    }

    @Test
    public void testLinearInterpolationBetweenVectors() {
        var from = new Vector2(1.0f, 2.0f);
        var to = new Vector2(2.0f, 4.0f);
        var v1 = new Vector2(1.5f, 3.0f);
        var v2 = new Vector2(1.25f, 2.5f);
        var v3 = new Vector2(1.75f, 3.5f);
        Assertions.assertEquals(v1, from.lerp(to, 0.5f));
        Assertions.assertEquals(v2, from.lerp(to, 0.25f));
        Assertions.assertEquals(v3, from.lerp(to, 0.75f));
    }

    @Test
    public void testSphericalLinearInterpolationBetweenVectors() {
        var from = Vector2.LEFT;
        var to = Vector2.RIGHT;
        var vec = Vector2.DOWN;
        assertEquals(vec, from.slerp(to, 0.5f));
    }

    @Test
    public void testSphericalLinearInterpolationWithZeroVector() {
        var from = Vector2.ZERO;
        var to = Vector2.ONE;
        assertEquals(from.lerp(to, 0.5f), from.slerp(to, 0.5f));
    }

    @Test
    public void testDirectionToVector() {
        var from = Vector2.ZERO;
        var to = new Vector2(0.0f, 10.0f);
        Assertions.assertEquals(Vector2.UP, from.directionTo(to));
    }

    @Test
    public void testDistanceSquaredBetweenVectors() {
        var a = new Vector2(-1.0f, 1.0f);
        var b = new Vector2(1.0f, 1.0f);
        Assertions.assertEquals(4.0f, a.distanceSquaredTo(b), 1e-12f);
    }

    @Test
    public void testDistanceBetweenVectors() {
        var a = new Vector2(-1.0f, 1.0f);
        var b = new Vector2(1.0f, 1.0f);
        Assertions.assertEquals(2.0f, a.distanceTo(b), 1e-12f);
    }

    @Test
    public void testAngleBetweenVectors() {
        var a = new Vector2(3.0f, 3.0f);
        var b = new Vector2(0.0f, 2.0f);
        Assertions.assertEquals(Math.PI / 4.0, a.angleTo(b), MathUtils.EPSILON);
    }

    @Test
    public void testAngleBetweenVectorAndAxis() {
        var vec = new Vector2(2.0f, 2.0f);
        Assertions.assertEquals(Math.PI / 4.0, vec.angle(), MathUtils.EPSILON);
    }

    @Test
    public void testAngleToPoint() {
        var a = new Vector2(1.0f, 2.0f);
        var b = new Vector2(2.0f, 3.0f);
        Assertions.assertEquals(Math.PI / 4.0, a.angleToPoint(b), 1e-12);
    }

    @Test
    public void testProjectVector() {
        var vec = new Vector2(2.0f, 2.0f);
        var proj = vec.project(Vector2.RIGHT);
        var res = new Vector2(2.0f, 0.0f);
        Assertions.assertEquals(res, proj);
    }

    @Test
    public void testReflectVector() {
        var vec = new Vector2(1.0f, -1.0f);
        var ref = vec.reflect(Vector2.UP);
        var res = new Vector2(1.0f, 1.0f);
        Assertions.assertEquals(res, ref);
    }

    @Test
    public void testBounceVector() {
        var vec = new Vector2(1.0f, -1.0f);
        var bounce = vec.bounce(Vector2.UP);
        var res = new Vector2(-1.0f, -1.0f);
        Assertions.assertEquals(res, bounce);
    }

    @Test
    public void testSlideVector() {
        var a = new Vector2(1.0f, 1.0f);
        var b = new Vector2(0.0f, 1.0f);
        var res = new Vector2(1.0f, 0.0f);
        Assertions.assertEquals(res, a.slide(b));
    }

    @Test
    public void testComponentWiseMod() {
        var a = new Vector2(1.0f, 8.0f);
        var b = new Vector2(2.0f, 3.0f);
        var res = new Vector2(1.0f, 2.0f);
        Assertions.assertEquals(res, a.mod(b));
    }

    @Test
    public void testVectorModWithScalar() {
        var vec = new Vector2(1.0f, 8.0f);
        var mod = vec.mod(3.0f);
        var res = new Vector2(1.0f, 2.0f);
        Assertions.assertEquals(res, mod);
    }

    @Test
    public void testVectorAspect() {
        var vec = new Vector2(16.0f, 9.0f);
        Assertions.assertEquals(16.0f / 9.0f, vec.aspect(), 1e-12f);
    }

    @Test
    public void testOrthogonalVector() {
        var a = new Vector2(2.0f, 3.0f);
        var b = new Vector2(3.0f, -2.0f);
        Assertions.assertEquals(b, a.orthogonal());
        Assertions.assertEquals(Math.PI / 2.0, a.angleTo(b), 1e-12);
    }

    @Test
    public void testVectorRotatedByAngle() {
        var vec = Vector2.UP;
        var rot = vec.rotated(-Math.PI / 4.0);
        var res = Vector2.ONE.normalized();
        Assertions.assertEquals(res, rot);
    }

    @Test
    public void testCrossProductWithVector3() {
        var vec = Vector2.RIGHT;
        var cross = vec.cross(Vector3.UP);
        var res = Vector3.FORWARD;
        Assertions.assertEquals(res, cross);
    }

    @Test
    public void testCrossProductWithVector2() {
        var vec = Vector2.RIGHT;
        var cross = vec.cross(Vector2.UP);
        var res = Vector3.FORWARD;
        Assertions.assertEquals(res, cross);
    }

    @Test
    public void testOuterProductWithVector2() {
        var a = new Vector2(1.0f, 2.0f);
        var b = new Vector2(2.0f, 3.0f);
        var res = new Matrix2(2.0f, 3.0f, 4.0f, 6.0f);
        Assertions.assertEquals(res, a.outer(b));
    }

    @Test
    public void testOuterProductWithVector3() {
        var a = new Vector2(1.0f, 2.0f);
        var b = new Vector3(2.0f, 3.0f, 4.0f);
        var res = new Matrix2x3(2.0f, 3.0f, 4.0f, 4.0f, 6.0f, 8.0f);
        Assertions.assertEquals(res, a.outer(b));
    }

    @Test
    public void testQuadraticBezierInterpolation() {
        var p0 = new Vector2(1.0f, 1.5f);
        var p2 = new Vector2(5.0f, 6.0f);
        var p1 = new Vector2(2.0f, 3.0f);
        var t = 0.35f;
        var l1 = p0.lerp(p1, t);
        var l2 = p1.lerp(p2, t);
        var res = l1.lerp(l2, t);
        assertEquals(res, p0.bezierInterpolate(p2, p1, t));
    }

    @Test
    public void testQuadraticBezierDerivative() {
        var p0 = new Vector2(0.0f, 0.0f);
        var p1 = new Vector2(2.0f, 0.0f);
        var control = new Vector2(1.0f, 1.0f);
        var res = new Vector2(2.0f, 0.0f);
        assertEquals(res, p0.bezierDerivative(p1, control, 0.5f));
    }

    @Test
    public void testCubicBezierInterpolation() {
        var p0 = new Vector2(1.0f, 1.5f);
        var p3 = new Vector2(5.0f, 6.0f);
        var p1 = new Vector2(1.5f, 2.5f);
        var p2 = new Vector2(3.0f, 4.0f);
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
        var p0 = new Vector2(0.0f, 0.0f);
        var p1 = new Vector2(2.0f, 0.0f);
        var control1 = new Vector2(0.0f, 1.0f);
        var control2 = new Vector2(2.0f, 1.0f);
        var res = new Vector2(3.0f, 0.0f);
        assertEquals(res, p0.bezierDerivative(p1, control1, control2, 0.5f));
    }

    @Test
    public void testVectorEqualsValues() {
        var vec = new Vector2(1.1f, 2.2f);
        Assertions.assertTrue(vec.equals(1.1f, 2.2f));
    }

    @Test
    public void testVectorEqualsApprox() {
        var a = new Vector2(1.0f, 2.0f);
        var b = new Vector2(0.99999999f, 1.99999999f);
        Assertions.assertTrue(a.equalsApprox(b));
    }

    @Test
    public void testUnitVectorFromAngle() {
        var vec = Vector2.fromAngle(Math.PI / 4.0);
        Assertions.assertEquals(vec, Vector2.ONE.normalized());
        Assertions.assertEquals(vec, Vector2.RIGHT.rotated(Math.PI / 4.0));
    }
}