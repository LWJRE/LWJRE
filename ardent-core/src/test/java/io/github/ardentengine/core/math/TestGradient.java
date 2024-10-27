package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGradient {

    @Test
    public void testSampleColor() {
        var gradient = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(0.5f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.sample(0.25f).equals(1.0f, 0.5f, 0.0f));
        Assertions.assertTrue(gradient.sample(0.5f).equals(1.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.sample(0.375f).equals(1.0f, 0.75f, 0.0f));
        Assertions.assertTrue(gradient.sample(-0.1f).equals(1.0f, 0.0f, 0.0f));
        Assertions.assertTrue(gradient.sample(0.75f).equals(0.5f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.sample(0.625f).equals(0.75f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.sample(1.0f).equals(0.0f, 1.0f, 0.0f));
    }

    @Test
    public void testwithPoints() {
        var g1 = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(0.5f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        var g2 = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(0.25f, new Color(1.0f, 0.0f, 1.0f))
            .withPoint(0.5f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(0.75f, new Color(0.0f, 0.0f, 1.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        var res = g1
            .withPoint(new Color(0.0f, 0.0f, 1.0f), 0.75f)
            .withPoint(0.25f, new Color(1.0f, 0.0f, 1.0f));
        Assertions.assertEquals(res, g2);
    }

    @Test
    public void testGetPointsCount() {
        var gradient = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(1.0f / 6.0f, new Color(1.0f, 0.5f, 0.0f))
            .withPoint(2.0f / 6.0f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(3.0f / 6.0f, new Color(0.0f, 1.0f, 0.0f))
            .withPoint(4.0f / 6.0f, new Color(0.0f, 0.0f, 1.0f))
            .withPoint(5.0f / 6.0f, new Color(0.5f, 0.0f, 1.0f))
            .withPoint(1.0f, new Color(1.0f, 1.0f, 1.0f));
        Assertions.assertEquals(7, gradient.getPointCount());
    }

    @Test
    public void testGetColor() {
        var gradient = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(0.5f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(0.0f).equals(1.0f, 0.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(0.25f).equals(1.0f, 0.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(0.5f).equals(1.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(0.75f).equals(1.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(1.0f).equals(0.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(1.2f).equals(0.0f, 1.0f, 0.0f));
        Assertions.assertTrue(gradient.getColor(-0.2f).equals(1.0f, 0.0f, 0.0f));
    }

    @Test
    public void testRemovePoint() {
        var g1 = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(0.5f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        var g2 = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(0.25f, new Color(1.0f, 0.0f, 1.0f))
            .withPoint(0.5f, new Color(1.0f, 1.0f, 0.0f))
            .withPoint(0.75f, new Color(0.0f, 0.0f, 1.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        var res = g2.removePoint(0.25f).removePoint(0.75f);
        Assertions.assertEquals(res, g1);
    }

    @Test
    public void testGradientBetweenTwoColors() {
        var g1 = new Gradient()
            .withPoint(0.0f, new Color(1.0f, 0.0f, 0.0f))
            .withPoint(1.0f, new Color(0.0f, 1.0f, 0.0f));
        var g2 = Gradient.between(new Color(1.0f, 0.0f, 0.0f), new Color(0.0f, 1.0f, 0.0f));
        Assertions.assertEquals(g1, g2);
    }
}