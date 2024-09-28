package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestMathUtils {

    @Test
    public void testClampInt() {
        Assertions.assertEquals(5, MathUtils.clamp(5, 0, 10));
        Assertions.assertEquals(0, MathUtils.clamp(-1, 0, 10));
        Assertions.assertEquals(10, MathUtils.clamp(12, 0, 10));
    }

    @Test
    public void testClampFloat() {
        Assertions.assertEquals(0.5f, MathUtils.clamp(0.5f, 0.0f, 1.0f), 1e-12f);
        Assertions.assertEquals(0.0f, MathUtils.clamp(-1.0f, 0.0f, 1.0f), 1e-12f);
        Assertions.assertEquals(1.0f, MathUtils.clamp(1.2f, 0.0f, 1.0f), 1e-12f);
    }

    @Test
    public void testClampDouble() {
        Assertions.assertEquals(0.5, MathUtils.clamp(0.5, 0.0, 1.0), 1e-12);
        Assertions.assertEquals(0.0, MathUtils.clamp(-1.0, 0.0, 1.0), 1e-12);
        Assertions.assertEquals(1.0, MathUtils.clamp(1.2, 0.0, 1.0), 1e-12);
    }

    @Test
    public void testLerpFloat() {
        Assertions.assertEquals(1.5f, MathUtils.lerp(1.0f, 2.0f, 0.5f), 1e-12f);
        Assertions.assertEquals(1.25f, MathUtils.lerp(1.0f, 2.0f, 0.25f), 1e-12f);
        Assertions.assertEquals(1.75f, MathUtils.lerp(1.0f, 2.0f, 0.75f), 1e-12f);
        Assertions.assertEquals(1.0f, MathUtils.lerp(1.0f, 2.0f, 0.0f), 1e-12f);
        Assertions.assertEquals(2.0f, MathUtils.lerp(1.0f, 2.0f, 1.0f), 1e-12f);
    }

    @Test
    public void testLerpDouble() {
        Assertions.assertEquals(1.5, MathUtils.lerp(1.0, 2.0, 0.5), 1e-12);
        Assertions.assertEquals(1.25, MathUtils.lerp(1.0, 2.0, 0.25), 1e-12);
        Assertions.assertEquals(1.75, MathUtils.lerp(1.0, 2.0, 0.75), 1e-12);
        Assertions.assertEquals(1.0, MathUtils.lerp(1.0, 2.0, 0.0), 1e-12);
        Assertions.assertEquals(2.0, MathUtils.lerp(1.0, 2.0, 1.0), 1e-12);
    }

    @Test
    public void testInverseLerpFloat() {
        Assertions.assertEquals(0.5f, MathUtils.inverseLerp(1.0f, 2.0f, 1.5f), 1e-12f);
        Assertions.assertEquals(0.25f, MathUtils.inverseLerp(1.0f, 2.0f, 1.25f), 1e-12f);
        Assertions.assertEquals(0.75f, MathUtils.inverseLerp(1.0f, 2.0f, 1.75f), 1e-12f);
        Assertions.assertEquals(0.0f, MathUtils.inverseLerp(1.0f, 2.0f, 1.0f), 1e-12f);
        Assertions.assertEquals(1.0f, MathUtils.inverseLerp(1.0f, 2.0f, 2.0f), 1e-12f);
    }

    @Test
    public void testInverseLerpDouble() {
        Assertions.assertEquals(0.5, MathUtils.inverseLerp(1.0, 2.0, 1.5), 1e-12);
        Assertions.assertEquals(0.25, MathUtils.inverseLerp(1.0, 2.0, 1.25), 1e-12);
        Assertions.assertEquals(0.75, MathUtils.inverseLerp(1.0, 2.0, 1.75), 1e-12);
        Assertions.assertEquals(0.0, MathUtils.inverseLerp(1.0, 2.0, 1.0), 1e-12);
        Assertions.assertEquals(1.0, MathUtils.inverseLerp(1.0, 2.0, 2.0), 1e-12);
    }

    @Test
    public void testIntMoveToward() {
        Assertions.assertEquals(3, MathUtils.moveToward(1, 5, 2));
        Assertions.assertEquals(5, MathUtils.moveToward(1, 5, 6));
        Assertions.assertEquals(3, MathUtils.moveToward(5, 1, 2));
        Assertions.assertEquals(1, MathUtils.moveToward(5, 1, 6));
    }

    @Test
    public void testLongMoveToward() {
        Assertions.assertEquals(3L, MathUtils.moveToward(1L, 5L, 2L));
        Assertions.assertEquals(5L, MathUtils.moveToward(1L, 5L, 6L));
        Assertions.assertEquals(3L, MathUtils.moveToward(5L, 1L, 2L));
        Assertions.assertEquals(1L, MathUtils.moveToward(5L, 1L, 6L));
    }

    @Test
    public void testFloatMoveToward() {
        Assertions.assertEquals(1.6f, MathUtils.moveToward(1.0f, 2.0f, 0.6f), 1e-12f);
        Assertions.assertEquals(2.0f, MathUtils.moveToward(1.0f, 2.0f, 3.0f), 1e-12f);
        Assertions.assertEquals(1.4f, MathUtils.moveToward(2.0f, 1.0f, 0.6f), 1e-12f);
        Assertions.assertEquals(1.0f, MathUtils.moveToward(2.0f, 1.0f, 3.0f), 1e-12f);
    }

    @Test
    public void testDoubleMoveToward() {
        Assertions.assertEquals(1.6, MathUtils.moveToward(1.0, 2.0, 0.6), 1e-12);
        Assertions.assertEquals(2.0, MathUtils.moveToward(1.0, 2.0, 3.0), 1e-12);
        Assertions.assertEquals(1.4, MathUtils.moveToward(2.0, 1.0, 0.6), 1e-12);
        Assertions.assertEquals(1.0, MathUtils.moveToward(2.0, 1.0, 3.0), 1e-12);
    }

    @Test
    public void testMapIntRange() {
        Assertions.assertEquals(50, MathUtils.map(5, 0, 10, 0, 100));
        Assertions.assertEquals(120, MathUtils.map(12, 0, 10, 0, 100));
        Assertions.assertEquals(-10, MathUtils.map(-1, 0, 10, 0, 100));
    }

    @Test
    public void testMapLongRange() {
        Assertions.assertEquals(50L, MathUtils.map(5L, 0L, 10L, 0L, 100L));
        Assertions.assertEquals(120L, MathUtils.map(12L, 0L, 10L, 0L, 100L));
        Assertions.assertEquals(-10L, MathUtils.map(-1L, 0L, 10L, 0L, 100L));
    }

    @Test
    public void testMapFloatRange() {
        Assertions.assertEquals(0.5f, MathUtils.map(5.0f, 0.0f, 10.0f, 0.0f, 1.0f), 1e-12f);
        Assertions.assertEquals(1.2f, MathUtils.map(12.0f, 0.0f, 10.0f, 0.0f, 1.0f), 1e-12f);
        Assertions.assertEquals(-0.1f, MathUtils.map(-1.0f, 0.0f, 10.0f, 0.0f, 1.0f), 1e-12f);
    }

    @Test
    public void testMapDoubleRange() {
        Assertions.assertEquals(0.5, MathUtils.map(5.0, 0.0, 10.0, 0.0, 1.0), 1e-12);
        Assertions.assertEquals(1.2, MathUtils.map(12.0, 0.0, 10.0, 0.0, 1.0), 1e-12);
        Assertions.assertEquals(-0.1, MathUtils.map(-1.0, 0.0, 10.0, 0.0, 1.0), 1e-12);
    }

    @Test
    public void testSmoothstepFloat() {
        Assertions.assertEquals(0.5f, MathUtils.smoothstep(3.0f, 4.0f, 3.5f), 1e-12f);
        Assertions.assertEquals(0.0f, MathUtils.smoothstep(3.0f, 4.0f, 2.0f), 1e-12f);
        Assertions.assertEquals(1.0f, MathUtils.smoothstep(3.0f, 4.0f, 5.0f), 1e-12f);
    }

    @Test
    public void testSmoothstepDouble() {
        Assertions.assertEquals(0.5, MathUtils.smoothstep(3.0, 4.0, 3.5), 1e-12);
        Assertions.assertEquals(0.0, MathUtils.smoothstep(3.0, 4.0, 2.0), 1e-12);
        Assertions.assertEquals(1.0, MathUtils.smoothstep(3.0, 4.0, 5.0), 1e-12);
    }

    @Test
    public void testFloatQuadraticBezierCurve() {
        var p0 = 2.0f;
        var p1 = 5.0f;
        var control = 4.0f;
        var t = 0.35f;
        var l1 = MathUtils.lerp(p0, control, t);
        var l2 = MathUtils.lerp(control, p1, t);
        var res = MathUtils.lerp(l1, l2, t);
        Assertions.assertEquals(res, MathUtils.bezierInterpolate(p0, p1, control, t), 1e-12f);
    }

    @Test
    public void testDoubleQuadraticBezierCurve() {
        var p0 = 2.0;
        var p1 = 5.0;
        var control = 4.0;
        var t = 0.35;
        var l1 = MathUtils.lerp(p0, control, t);
        var l2 = MathUtils.lerp(control, p1, t);
        var res = MathUtils.lerp(l1, l2, t);
        Assertions.assertEquals(res, MathUtils.bezierInterpolate(p0, p1, control, t), 1e-12);
    }

    // TODO: Quadratic Bézier derivative

    @Test
    public void testFloatCubicBezierCurve() {
        var p0 = 1.0f;
        var p3 = 5.0f;
        var p1 = 2.0f;
        var p2 = 4.0f;
        var t = 0.35f;
        var m1 = MathUtils.lerp(p0, p1, t);
        var m2 = MathUtils.lerp(p1, p2, t);
        var m3 = MathUtils.lerp(p2, p3, t);
        var l1 = MathUtils.lerp(m1, m2, t);
        var l2 = MathUtils.lerp(m2, m3, t);
        var res = MathUtils.lerp(l1, l2, t);
        Assertions.assertEquals(res, MathUtils.bezierInterpolate(p0, p3, p1, p2, t), 1e-12f);
    }

    @Test
    public void testDoubleCubicBezierCurve() {
        var p0 = 1.0;
        var p3 = 5.0;
        var p1 = 2.0;
        var p2 = 4.0;
        var t = 0.35;
        var m1 = MathUtils.lerp(p0, p1, t);
        var m2 = MathUtils.lerp(p1, p2, t);
        var m3 = MathUtils.lerp(p2, p3, t);
        var l1 = MathUtils.lerp(m1, m2, t);
        var l2 = MathUtils.lerp(m2, m3, t);
        var res = MathUtils.lerp(l1, l2, t);
        Assertions.assertEquals(res, MathUtils.bezierInterpolate(p0, p3, p1, p2, t), 1e-12);
    }

    // TODO: Cubic Bézier derivative

    @Test
    public void testFloatEqualsApprox() {
        Assertions.assertTrue(MathUtils.equalsApprox(1.0f, 1.0f));
        Assertions.assertTrue(MathUtils.equalsApprox(1.0f, 0.9999999f));
        Assertions.assertFalse(MathUtils.equalsApprox(1.0f, 0.8999999f));
        Assertions.assertTrue(MathUtils.equalsApprox(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
        Assertions.assertTrue(MathUtils.equalsApprox(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
        Assertions.assertFalse(MathUtils.equalsApprox(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
        Assertions.assertFalse(MathUtils.equalsApprox(Float.NaN, Float.NaN));
    }

    @Test
    public void testDoubleEqualsApprox() {
        Assertions.assertTrue(MathUtils.equalsApprox(1.0, 1.0));
        Assertions.assertTrue(MathUtils.equalsApprox(1.0, 0.9999999));
        Assertions.assertFalse(MathUtils.equalsApprox(1.0, 0.8999999));
        Assertions.assertTrue(MathUtils.equalsApprox(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        Assertions.assertTrue(MathUtils.equalsApprox(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        Assertions.assertFalse(MathUtils.equalsApprox(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
        Assertions.assertFalse(MathUtils.equalsApprox(Double.NaN, Double.NaN));
    }
}