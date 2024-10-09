package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInputEventScroll {

    @Test
    public void testScroll() {
        var scrollEvent1 = new InputEventScroll(0.0f, 1.0f);
        var scroll = new Vector2(0.0f, 1.0f);
        Assertions.assertEquals(scroll, scrollEvent1.scroll());
        var scrollEvent2 = new InputEventScroll(scroll);
        Assertions.assertEquals(scroll, scrollEvent2.scroll());
    }

    @Test
    public void testMatches() {
        var scrollEvent1 = new InputEventScroll(0.0f, 1.0f);
        var scrollEvent2 = new InputEventScroll(0.0f, 1.0f);
        var scrollEvent3 = new InputEventScroll(1.0f, 0.0f);
        Assertions.assertTrue(scrollEvent1.matches(scrollEvent2));
        Assertions.assertFalse(scrollEvent1.matches(scrollEvent3));
    }
}