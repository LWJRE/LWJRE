package io.github.ardentengine.core.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInputEventAction {

    @Test
    public void testIsAction() {
        var actionEvent = new InputEventAction("action", true);
        Assertions.assertTrue(actionEvent.isAction("action"));
        Assertions.assertFalse(actionEvent.isAction("not_action"));
    }

    @Test
    public void testStrength() {
        var actionEvent1 = new InputEventAction("action", true);
        Assertions.assertEquals(actionEvent1.strength(), 1.0f);
        var actionEvent2 = new InputEventAction("action", false);
        Assertions.assertEquals(actionEvent2.strength(), 0.0f);
        var actionEvent3 = new InputEventAction("action", true, 0.5f);
        Assertions.assertEquals(actionEvent3.strength(), 0.5f);
    }

    @Test
    public void testMatches() {
        var actionEvent1 = new InputEventAction("action", true);
        var actionEvent2 = new InputEventAction("action", true);
        var actionEvent3 = new InputEventAction("not_action", true);
        Assertions.assertTrue(actionEvent1.matches(actionEvent2));
        Assertions.assertFalse(actionEvent1.matches(actionEvent3));
    }
}