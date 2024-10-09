package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInputEventMouseButton {

    @Test
    public void testPosition() {
        var mouseEvent1 = new InputEventMouseButton(InputEventMouseButton.BUTTON_LEFT, 15.0f, 20.0f, true);
        var position = new Vector2(15.0f, 20.0f);
        Assertions.assertEquals(position, mouseEvent1.position());
        var mouseEvent2 = new InputEventMouseButton(InputEventMouseButton.BUTTON_LEFT, position, true);
        Assertions.assertEquals(position, mouseEvent2.position());
    }

    @Test
    public void testMatches() {
        var mouseEvent1 = new InputEventMouseButton(InputEventMouseButton.BUTTON_LEFT, true);
        var mouseEvent2 = new InputEventMouseButton(InputEventMouseButton.BUTTON_LEFT, InputEventMouseButton.MOD_SHIFT, false);
        Assertions.assertTrue(mouseEvent1.matches(mouseEvent2));
        var mouseEvent3 = new InputEventMouseButton(InputEventMouseButton.BUTTON_RIGHT, true);
        Assertions.assertFalse(mouseEvent1.matches(mouseEvent3));
    }

    @Test
    public void testMatchesExact() {
        var mouseEvent1 = new InputEventMouseButton(InputEventMouseButton.BUTTON_RIGHT, true);
        var mouseEvent2 = new InputEventMouseButton(InputEventMouseButton.BUTTON_MIDDLE, true);
        Assertions.assertFalse(mouseEvent1.matches(mouseEvent2, true));
        var mouseEvent3 = new InputEventMouseButton(InputEventMouseButton.BUTTON_RIGHT, InputEventMouseButton.MOD_CONTROL, true);
        Assertions.assertFalse(mouseEvent1.matches(mouseEvent3, true));
        var mouseEvent4 = new InputEventMouseButton(InputEventMouseButton.BUTTON_MIDDLE, true);
        Assertions.assertTrue(mouseEvent2.matches(mouseEvent4, true));
        var mouseEvent5 = new InputEventMouseButton(InputEventMouseButton.BUTTON_RIGHT, InputEventMouseButton.MOD_CONTROL, true);
        Assertions.assertTrue(mouseEvent3.matches(mouseEvent5, true));
    }
}