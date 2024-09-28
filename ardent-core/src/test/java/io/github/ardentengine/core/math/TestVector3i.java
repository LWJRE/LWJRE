package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector3i {

    @Test
    public void testConstructFromVector2i() {
        var a = new Vector2i(1, 2);
        var b = new Vector3i(a, 3);
        var exp = new Vector3i(1, 2, 3);
        Assertions.assertEquals(exp, b);
    }

    @Test
    public void testAccessXY() {
        var a = new Vector3i(1, 2, 3);
        var b = new Vector2i(1, 2);
        Assertions.assertEquals(b, a.xy());
    }

    @Test
    public void testSumOfVectors() {
        var a = new Vector3i(1, 2, 3);
        var b = new Vector3i(2, 3, 1);
        var res = new Vector3i(3, 5, 4);
        Assertions.assertEquals(res, a.plus(b));
    }

    @Test
    public void testSubtractionOfVectors() {
        var a = new Vector3i(3, 5, 4);
        var b = new Vector3i(2, 3, 1);
        var res = new Vector3i(1, 2, 3);
        Assertions.assertEquals(res, a.minus(b));
    }

    @Test
    public void testNegated() {
        var vec = new Vector3i(1, -2, 3);
        var res = new Vector3i(-1, 2, -3);
        Assertions.assertEquals(res, vec.negated());
    }

    @Test
    public void testComponentWiseMultiplication() {
        var a = new Vector3i(2, 3, 4);
        var b = new Vector3i(3, 4, 2);
        var exp = new Vector3i(6, 12, 8);
        Assertions.assertEquals(exp, a.multiply(b));
    }

    @Test
    public void testMultiplicationOfVectorByScalar() {
        var vec = new Vector3i(1, 2, 3);
        var res = vec.multiply(2);
        var exp = new Vector3i(2, 4, 6);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testComponentWiseDivision() {
        var a = new Vector3i(6, 12, 8);
        var b = new Vector3i(3, 4, 2);
        var exp = new Vector3i(2, 3, 4);
        Assertions.assertEquals(exp, a.divide(b));
    }

    @Test
    public void testDivisionOfVectorByScalar() {
        var vec = new Vector3i(1, 2, 3);
        var res = vec.divide(2);
        var exp = new Vector3i(0, 1, 1);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testDotProduct() {
        var a = new Vector3i(1, 2, 3);
        var b = new Vector3i(2, 3, 1);
        var res = a.dot(b);
        Assertions.assertEquals(11, res);
    }

    @Test
    public void testCrossProductWithVector3i() {
        var a = Vector3i.RIGHT;
        var b = Vector3i.UP;
        var res = a.cross(b);
        Assertions.assertEquals(Vector3i.FORWARD, res);
    }

    @Test
    public void testCrossProductWithVector2i() {
        var a = Vector3i.RIGHT;
        var b = Vector2i.UP;
        var res = a.cross(b);
        Assertions.assertEquals(Vector3i.FORWARD, res);
    }

    @Test
    public void testLengthSquared() {
        Assertions.assertEquals(3, Vector3i.ONE.lengthSquared());
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(Math.sqrt(3), Vector3i.ONE.length(), MathUtils.EPSILON);
    }

    @Test
    public void testCheckIfVectorIsNormalized() {
        Assertions.assertFalse(Vector3i.ONE.isNormalized());
        Assertions.assertTrue(Vector3i.FORWARD.isNormalized());
    }

    @Test
    public void testVectorAbsoluteValue() {
        var vec = new Vector3i(-1, -2, 3);
        var abs = new Vector3i(1, 2, 3);
        Assertions.assertEquals(abs, vec.abs());
    }

    @Test
    public void testVectorSign() {
        var vec = new Vector3i(-1, 0, 3);
        var sign = new Vector3i(-1, 0, 1);
        Assertions.assertEquals(sign, vec.sign());
    }

    @Test
    public void testDistanceSquaredBetweenVectors() {
        var a = new Vector3i(-1, 1, -1);
        var b = new Vector3i(1, 1, 1);
        Assertions.assertEquals(8, a.distanceSquaredTo(b));
    }

    @Test
    public void testDistanceBetweenVectors() {
        var a = new Vector3i(-1, 1, -1);
        var b = new Vector3i(1, 1, 1);
        Assertions.assertEquals((float) Math.sqrt(8.0), a.distanceTo(b), 1e-12f);
    }

    @Test
    public void testComponentWiseMod() {
        var a = new Vector3i(1, 3, 8);
        var b = new Vector3i(2, 2, 3);
        var res = new Vector3i(1, 1, 2);
        Assertions.assertEquals(res, a.mod(b));
    }

    @Test
    public void testVectorModWithScalar() {
        var vec = new Vector3i(1, 3, 8);
        var mod = vec.mod(3);
        var res = new Vector3i(1, 0, 2);
        Assertions.assertEquals(res, mod);
    }

    @Test
    public void testVectorEqualsValues() {
        var vec = new Vector3i(1, 2, 3);
        Assertions.assertTrue(vec.equals(1, 2, 3));
    }
}