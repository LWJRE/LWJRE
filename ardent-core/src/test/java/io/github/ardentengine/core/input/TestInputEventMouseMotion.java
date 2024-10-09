package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInputEventMouseMotion {

    @Test
    public void testPosition() {
        var mouseEvent1 = new InputEventMouseMotion(15.0f, 20.0f, 1.0f, 0.0f);
        var position = new Vector2(15.0f, 20.0f);
        Assertions.assertEquals(position, mouseEvent1.position());
        var mouseEvent2 = new InputEventMouseMotion(position, 1.0f, 0.0f);
        Assertions.assertEquals(position, mouseEvent2.position());
    }

    @Test
    public void testMotion() {
        var mouseEvent1 = new InputEventMouseMotion(15.0f, 20.0f, 1.0f, 0.0f);
        var motion = new Vector2(1.0f, 0.0f);
        Assertions.assertEquals(motion, mouseEvent1.motion());
        var mouseEvent2 = new InputEventMouseMotion(15.0f, 20.0f, motion);
        Assertions.assertEquals(motion, mouseEvent2.motion());
    }
}