package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestColor {

    private static void assertEquals(Color expected, Color actual) {
        Assertions.assertEquals(expected.r(), actual.r(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.g(), actual.g(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.b(), actual.b(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.a(), actual.a(), MathUtils.EPSILON);
    }

    @Test
    public void testColorFromIntegers() {
        var color = new Color(95, 158, 160, 128);
        Assertions.assertEquals(95, color.r8());
        Assertions.assertEquals(158, color.g8());
        Assertions.assertEquals(160, color.b8());
        Assertions.assertEquals(128, color.a8());
    }

    @Test
    public void testColorPlus() {
        var c1 = new Color(0.3f, 0.4f, 0.5f, 0.6f);
        var c2 = new Color(0.1f, 0.2f, 0.3f, 0.4f);
        var res = new Color(0.4f, 0.6f, 0.8f, 1.0f);
        assertEquals(res, c1.plus(c2));
    }

    @Test
    public void testColorMinus() {
        var c1 = new Color(0.4f, 0.6f, 0.8f, 1.0f);
        var c2 = new Color(0.3f, 0.4f, 0.5f, 0.6f);
        var res = new Color(0.1f, 0.2f, 0.3f, 0.4f);
        assertEquals(res, c1.minus(c2));
    }

    @Test
    public void testColorMultiply() {
        var c1 = new Color(2.0f, 3.0f, 1.5f, 1.0f);
        var c2 = new Color(0.2f, 0.3f, 0.4f, 1.0f);
        var res = new Color(0.4f, 0.9f, 0.6f, 1.0f);
        assertEquals(res, c1.multiply(c2));
    }

    @Test
    public void testFloatMultiply() {
        var color = new Color(0.2f, 0.4f, 0.6f, 1.0f);
        var res = new Color(0.3f, 0.6f, 0.9f, 1.5f);
        assertEquals(res, color.multiply(1.5f));
    }

    @Test
    public void testColorDivide() {
        var c1 = new Color(1.0f, 0.8f, 0.6f, 1.0f);
        var c2 = new Color(2.0f, 4.0f, 3.0f, 5.0f);
        var res = new Color(0.5f, 0.2f, 0.2f, 0.2f);
        assertEquals(res, c1.divide(c2));
    }

    @Test
    public void testFloatDivide() {
        var color = new Color(0.2f, 0.4f, 0.6f, 1.0f);
        var res = new Color(0.1f, 0.2f, 0.3f, 0.5f);
        assertEquals(res, color.divide(2.0f));
    }

    @Test
    public void testBlend() {
        var c1 = new Color(1.0f, 0.0f, 0.0f, 1.0f);
        var c2 = new Color(1.0f, 1.0f, 0.0f, 0.5f);
        var res = new Color(1.0f, 0.5f, 0.0f, 1.0f);
        assertEquals(res, c1.blend(c2));
    }

    @Test
    public void testInvert() {
        var color = new Color(0.2f, 0.5f, 0.7f, 1.0f);
        var res = new Color(0.8f, 0.5f, 0.3f, 1.0f);
        assertEquals(res, color.inverted());
    }

    @Test
    public void testLuminance() {
        var color = new Color(0.372549f, 0.619608f, 0.627451f);
        var res = 0.2126f * 0.372549f + 0.7152f * 0.619608f + 0.0722f * 0.627451f;
        Assertions.assertEquals(res, color.luminance(), MathUtils.EPSILON);
    }

    @Test
    public void testDarker() {
        var color = new Color(0.372549f, 0.619608f, 0.627451f);
        Assertions.assertTrue(color.darker(0.1f).luminance() < color.luminance());
    }

    @Test
    public void testLighter() {
        var color = new Color(0.372549f, 0.619608f, 0.627451f);
        Assertions.assertTrue(color.lighter(0.1f).luminance() > color.luminance());
    }

    @Test
    public void testLerp() {
        var c1 = new Color(0.2f, 0.4f, 0.2f, 0.5f);
        var c2 = new Color(0.4f, 0.8f, 0.6f, 1.0f);
        var res1 = new Color(0.3f, 0.6f, 0.4f, 0.75f);
        var res2 = new Color(0.25f, 0.5f, 0.3f, 0.625f);
        var res3 = new Color(0.35f, 0.7f, 0.5f, 0.875f);
        assertEquals(res1, c1.lerp(c2, 0.5f));
        assertEquals(res2, c1.lerp(c2, 0.25f));
        assertEquals(res3, c1.lerp(c2, 0.75f));
    }

    @Test
    public void testEqualsApprox() {
        var c1 = new Color(0.9999999f, 0.4999999f, 0.7500001f, 0.9999999f);
        var c2 = new Color(1.0f, 0.5f, 0.75f, 1.0f);
        Assertions.assertTrue(c1.equalsApprox(c2));
    }

    @Test
    public void testEqualsApproxRGBA() {
        var color = new Color(0.372549f, 0.619608f, 0.627451f);
        Assertions.assertTrue(color.equalsApprox(0x5f9ea0ff));
    }

    @Test
    public void testColorFromRGBA() {
        var color = Color.rgba(0x5f9ea0ff);
        Assertions.assertEquals(0x5f9ea0ff, color.rgba());
    }

    @Test
    public void testColorFromARGB() {
        var color = Color.argb(0xff5f9ea0);
        Assertions.assertEquals(0xff5f9ea0, color.argb());
    }

    @Test
    public void testColorFromHSV() {
        var color = Color.hsv(0.5f, 0.41f, 0.63f);
        Assertions.assertEquals(0.5f, color.h(), MathUtils.EPSILON);
        Assertions.assertEquals(0.41f, color.s(), MathUtils.EPSILON);
        Assertions.assertEquals(0.63f, color.v(), MathUtils.EPSILON);
    }
}