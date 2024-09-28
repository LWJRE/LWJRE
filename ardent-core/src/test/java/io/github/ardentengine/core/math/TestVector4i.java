package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestVector4i {

    @Test
    public void testConstructFromVector3i() {
        var a = new Vector3i(1, 2, 3);
        var b = new Vector4i(a, 4);
        var exp = new Vector4i(1, 2, 3, 4);
        Assertions.assertEquals(exp, b);
    }

    @Test
    public void testConstructFromVector2i() {
        var a = new Vector2i(1, 2);
        var b = new Vector4i(a, 3, 4);
        var exp = new Vector4i(1, 2, 3, 4);
        Assertions.assertEquals(exp, b);
    }

    @Test
    public void testAccessXY() {
        var a = new Vector4i(1, 2, 3, 4);
        var b = new Vector2i(1, 2);
        Assertions.assertEquals(b, a.xy());
    }

    @Test
    public void testAccessXYZ() {
        var a = new Vector4i(1, 2, 3, 4);
        var b = new Vector3i(1, 2, 3);
        Assertions.assertEquals(b, a.xyz());
    }

    @Test
    public void testSumOfVectors() {
        var a = new Vector4i(1, 2, 3, 3);
        var b = new Vector4i(2, 3, 1, -1);
        var res = new Vector4i(3, 5, 4, 2);
        Assertions.assertEquals(res, a.plus(b));
    }

    @Test
    public void testSubtractionOfVectors() {
        var a = new Vector4i(3, 5, 4, 2);
        var b = new Vector4i(2, 3, 1, 4);
        var res = new Vector4i(1, 2, 3, -2);
        Assertions.assertEquals(res, a.minus(b));
    }

    @Test
    public void testNegated() {
        var vec = new Vector4i(1, -2, 0, -4);
        var res = new Vector4i(-1, 2, -0, 4);
        Assertions.assertEquals(res, vec.negated());
    }

    @Test
    public void testComponentWiseMultiplication() {
        var a = new Vector4i(2, 3, 4, 1);
        var b = new Vector4i(3, 4, 2, 1);
        var res = new Vector4i(6, 12, 8, 1);
        Assertions.assertEquals(res, a.multiply(b));
    }

    @Test
    public void testMultiplicationOfVectorByScalar() {
        var vec = new Vector4i(1, 2, 3, 4);
        var res = vec.multiply(2);
        var exp = new Vector4i(2, 4, 6, 8);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testComponentWiseDivision() {
        var a = new Vector4i(6, 12, 8, 1);
        var b = new Vector4i(3, 4, 2, 2);
        var res = new Vector4i(2, 3, 4, 0);
        Assertions.assertEquals(res, a.divide(b));
    }

    @Test
    public void testDivisionOfVectorByScalar() {
        var vec = new Vector4i(1, 2, 3, 4);
        var res = vec.divide(2);
        var exp = new Vector4i(0, 1, 1, 2);
        Assertions.assertEquals(exp, res);
    }

    @Test
    public void testDotProduct() {
        var a = new Vector4i(1, 2, 3, 4);
        var b = new Vector4i(2, 3, 1, 4);
        var res = a.dot(b);
        Assertions.assertEquals(27, res);
    }

    @Test
    public void testLengthSquared() {
        Assertions.assertEquals(4, Vector4i.ONE.lengthSquared());
    }

    @Test
    public void testLength() {
        Assertions.assertEquals(Math.sqrt(4), Vector4i.ONE.length(), MathUtils.EPSILON);
    }

    @Test
    public void testVectorAbsoluteValue() {
        var vec = new Vector4i(-1, -2, 3, 0);
        var abs = new Vector4i(1, 2, 3, 0);
        Assertions.assertEquals(abs, vec.abs());
    }

    @Test
    public void testVectorSign() {
        var vec = new Vector4i(-1, 0, 3, 2);
        var sign = new Vector4i(-1, 0, 1, 1);
        Assertions.assertEquals(sign, vec.sign());
    }

    @Test
    public void testDistanceSquaredBetweenVectors() {
        var a = new Vector4i(-1, 1, -1, 2);
        var b = new Vector4i(1, 1, 1, 0);
        Assertions.assertEquals(12, a.distanceSquaredTo(b));
    }

    @Test
    public void testDistanceBetweenVectors() {
        var a = new Vector4i(-1, 1, -1, 2);
        var b = new Vector4i(1, 1, 1, 0);
        Assertions.assertEquals((float) Math.sqrt(12.0), a.distanceTo(b), 1e-12f);
    }

    @Test
    public void testComponentWiseMod() {
        var a = new Vector4i(1, 3, 8, 8);
        var b = new Vector4i(2, 2, 3, 4);
        var res = new Vector4i(1, 1, 2, 0);
        Assertions.assertEquals(res, a.mod(b));
    }

    @Test
    public void testVectorModWithScalar() {
        var vec = new Vector4i(1, 3, 8, 9);
        var mod = vec.mod(3);
        var res = new Vector4i(1, 0, 2, 0);
        Assertions.assertEquals(res, mod);
    }

    @Test
    public void testVectorEqualsValues() {
        var vec = new Vector4i(1, 2, 3, 4);
        Assertions.assertTrue(vec.equals(1, 2, 3, 4));
    }
}