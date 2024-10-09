package io.github.ardentengine.core.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInputEventKey {

    @Test
    public void testMatches() {
        var keyEvent1 = new InputEventKey(InputEventKey.KEY_A, true);
        var keyEvent2 = new InputEventKey(InputEventKey.KEY_A, InputEventKey.MOD_SHIFT, false);
        Assertions.assertTrue(keyEvent1.matches(keyEvent2));
        var keyEvent3 = new InputEventKey(InputEventKey.KEY_B, true);
        Assertions.assertFalse(keyEvent1.matches(keyEvent3));
    }

    @Test
    public void testMatchesExact() {
        var keyEvent1 = new InputEventKey(InputEventKey.KEY_C, true);
        var keyEvent2 = new InputEventKey(InputEventKey.KEY_D, true);
        Assertions.assertFalse(keyEvent1.matches(keyEvent2, true));
        var keyEvent3 = new InputEventKey(InputEventKey.KEY_C, InputEventKey.MOD_SHIFT, true);
        Assertions.assertFalse(keyEvent1.matches(keyEvent3, true));
        var keyEvent4 = new InputEventKey(InputEventKey.KEY_D, true);
        Assertions.assertTrue(keyEvent2.matches(keyEvent4, true));
        var keyEvent5 = new InputEventKey(InputEventKey.KEY_C, InputEventKey.MOD_SHIFT, true);
        Assertions.assertTrue(keyEvent3.matches(keyEvent5, true));
    }
}