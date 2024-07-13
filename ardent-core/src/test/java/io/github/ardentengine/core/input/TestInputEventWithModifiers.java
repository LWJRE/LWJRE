package io.github.ardentengine.core.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInputEventWithModifiers {

    private record MockInputEventWithModifiers(int modifiers) implements InputEventWithModifiers {

        @Override
        public boolean isPressed() {
            return false;
        }

        @Override
        public boolean isReleased() {
            return false;
        }

        @Override
        public boolean isEcho() {
            return false;
        }

        @Override
        public boolean matches(InputEvent event, boolean exact) {
            return false;
        }
    }

    @Test
    public void testShiftPressed() {
        var input = new MockInputEventWithModifiers(Keyboard.MOD_SHIFT);
        Assertions.assertTrue(input.shiftPressed());
        Assertions.assertFalse(input.ctrlPressed());
        Assertions.assertFalse(input.altPressed());
        Assertions.assertFalse(input.metaPressed());
    }

    @Test
    public void testCtrlPressed() {
        var input = new MockInputEventWithModifiers(Keyboard.MOD_CONTROL);
        Assertions.assertFalse(input.shiftPressed());
        Assertions.assertTrue(input.ctrlPressed());
        Assertions.assertFalse(input.altPressed());
        Assertions.assertFalse(input.metaPressed());
    }

    @Test
    public void testAltPressed() {
        var input = new MockInputEventWithModifiers(Keyboard.MOD_ALT);
        Assertions.assertFalse(input.shiftPressed());
        Assertions.assertFalse(input.ctrlPressed());
        Assertions.assertTrue(input.altPressed());
        Assertions.assertFalse(input.metaPressed());
    }

    @Test
    public void testMetaPressed() {
        var input = new MockInputEventWithModifiers(Keyboard.MOD_META);
        Assertions.assertFalse(input.shiftPressed());
        Assertions.assertFalse(input.ctrlPressed());
        Assertions.assertFalse(input.altPressed());
        Assertions.assertTrue(input.metaPressed());
    }

    @Test
    public void testShiftCtrlPressed() {
        var input = new MockInputEventWithModifiers(Keyboard.MOD_SHIFT | Keyboard.MOD_CONTROL);
        Assertions.assertTrue(input.shiftPressed());
        Assertions.assertTrue(input.ctrlPressed());
        Assertions.assertFalse(input.altPressed());
        Assertions.assertFalse(input.metaPressed());
    }

    @Test
    public void testAltShiftPressed() {
        var input = new MockInputEventWithModifiers(Keyboard.MOD_ALT | Keyboard.MOD_SHIFT);
        Assertions.assertTrue(input.shiftPressed());
        Assertions.assertFalse(input.ctrlPressed());
        Assertions.assertTrue(input.altPressed());
        Assertions.assertFalse(input.metaPressed());
    }
}
