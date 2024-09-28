package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector2i {

    @Test
    public void testSumOfVectors() {
        var a = new Vector2i(1, 2);
        var b = new Vector2i(2, 3);
        var res = new Vector2i(3, 5);
        Assertions.assertEquals(res, a.plus(b));
    }

    @Test
    public void testSubtractionOfVectors() {
        var a = new Vector2i(3, 5);
        var b = new Vector2i(2, 3);
        var res = new Vector2i(1, 2);
        Assertions.assertEquals(res, a.minus(b));
    }

    @Test
    public void testNegated() {
        var vec = new Vector2i(1, -2);
        var res = new Vector2i(-1, 2);
        Assertions.assertEquals(res, vec.negated());
    }

    @Test
    public void testComponentWiseMultiplication() {
        var a = new Vector2i(2, 3);
        var b = new Vector2i(3, 4);
        var res = new Vector2i(6, 12);
        Assertions.assertEquals(res, a.multiply(b));
    }

    @Test
    public void testMultiplicationOfVectorByScalar() {
        var vec = new Vector2i(1, 2);
        var res = vec.multiply(2);
        var exp = new Vector2i(2, 4);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testComponentWiseDivision() {
        var a = new Vector2i(6, 12);
        var b = new Vector2i(3, 4);
        var res = new Vector2i(2, 3);
        Assertions.assertEquals(res, a.divide(b));
    }

    @Test
    public void testDivisionOfVectorByScalar() {
        var vec = new Vector2i(1, 2);
        var res = vec.divide(2);
        var exp = new Vector2i(0, 1);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testDotProduct() {
        var a = new Vector2i(1, 2);
        var b = new Vector2i(2, 3);
        var res = a.dot(b);
        Assertions.assertEquals(8, res);
    }

    @Test
    public void testLengthSquared() {
        Assertions.assertEquals(2, Vector2i.ONE.lengthSquared());
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(Math.sqrt(2), Vector2i.ONE.length(), MathUtils.EPSILON);
    }

    @Test
    public void testCheckIfVectorIsNormalized() {
        Assertions.assertFalse(Vector2i.ONE.isNormalized());
        Assertions.assertTrue(Vector2i.LEFT.isNormalized());
    }

    @Test
    public void testVectorAbsoluteValue() {
        var vec = new Vector2i(-1, 3);
        var abs = new Vector2i(1, 3);
        Assertions.assertEquals(abs, vec.abs());
    }

    @Test
    public void testVectorSign() {
        var vec = new Vector2i(-1, 3);
        var sign = new Vector2i(-1, 1);
        Assertions.assertEquals(sign, vec.sign());
    }

    @Test
    public void testDistanceSquaredBetweenVectors() {
        var a = new Vector2i(-1, 1);
        var b = new Vector2i(1, 1);
        Assertions.assertEquals(4, a.distanceSquaredTo(b));
    }

    @Test
    public void testDistanceBetweenVectors() {
        var a = new Vector2i(-1, 1);
        var b = new Vector2i(1, 1);
        Assertions.assertEquals(2.0f, a.distanceTo(b), 1e-12f);
    }

    @Test
    public void testAngleBetweenVectorAndAxis() {
        var vec = new Vector2i(2, 2);
        Assertions.assertEquals(Math.PI / 4.0, vec.angle(), MathUtils.EPSILON);
    }

    @Test
    public void testComponentWiseMod() {
        var a = new Vector2i(1, 8);
        var b = new Vector2i(2, 3);
        var res = new Vector2i(1, 2);
        Assertions.assertEquals(res, a.mod(b));
    }

    @Test
    public void testVectorModWithScalar() {
        var vec = new Vector2i(1, 8);
        var mod = vec.mod(3);
        var res = new Vector2i(1, 2);
        Assertions.assertEquals(res, mod);
    }

    @Test
    public void testVectorAspect() {
        var vec = new Vector2i(16, 9);
        Assertions.assertEquals(16.0f / 9.0f, vec.aspect(), 1e-12f);
    }

    @Test
    public void testOrthogonalVector() {
        var a = new Vector2i(2, 3);
        var b = new Vector2i(3, -2);
        Assertions.assertEquals(b, a.orthogonal());
    }

    @Test
    public void testCrossProductWithVector3() {
        var vec = Vector2i.RIGHT;
        var cross = vec.cross(Vector3i.UP);
        var res = Vector3i.FORWARD;
        Assertions.assertEquals(res, cross);
    }

    @Test
    public void testCrossProductWithVector2i() {
        var vec = Vector2i.RIGHT;
        var cross = vec.cross(Vector2i.UP);
        var res = Vector3i.FORWARD;
        Assertions.assertEquals(res, cross);
    }

    @Test
    public void testVectorEqualsValues() {
        var vec = new Vector2i(1, 2);
        Assertions.assertTrue(vec.equals(1, 2));
    }
}