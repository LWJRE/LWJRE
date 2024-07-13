package io.github.ardentengine.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestApplicationProperties {

    @Test
    public void testGetStringOrDefault() {
        Assertions.assertEquals("Hello world", ApplicationProperties.getString("test.stringProperty", "default value"));
        Assertions.assertEquals("default value", ApplicationProperties.getString("nonexistent.property", "default value"));
        Assertions.assertEquals("42", ApplicationProperties.getString("test.intProperty", "default value"));
    }

    @Test
    public void testGetString() {
        Assertions.assertEquals("Hello world", ApplicationProperties.getString("test.stringProperty"));
        Assertions.assertEquals("", ApplicationProperties.getString("nonexistent.property")); // TODO: Assert that an error is logged
        Assertions.assertEquals("42", ApplicationProperties.getString("test.intProperty")); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetIntOrDefault() {
        Assertions.assertEquals(42, ApplicationProperties.getInt("test.intProperty", -2));
        Assertions.assertEquals(-2, ApplicationProperties.getInt("nonexistent.property", -2));
        Assertions.assertEquals(-2, ApplicationProperties.getInt("test.stringProperty", -2)); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetInt() {
        Assertions.assertEquals(42, ApplicationProperties.getInt("test.intProperty"));
        Assertions.assertEquals(0, ApplicationProperties.getInt("nonexistent.property")); // TODO: Assert that an error is logged
        Assertions.assertEquals(0, ApplicationProperties.getInt("test.stringProperty")); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetFloatOrDefault() {
        Assertions.assertEquals(3.5f, ApplicationProperties.getFloat("test.floatProperty", -1.0f));
        Assertions.assertEquals(42.0f, ApplicationProperties.getFloat("test.intProperty", -1.0f));
        Assertions.assertEquals(-1.0f, ApplicationProperties.getFloat("nonexistent.property", -1.0f));
        Assertions.assertEquals(-1.0f, ApplicationProperties.getFloat("test.stringProperty", -1.0f)); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetFloat() {
        Assertions.assertEquals(3.5f, ApplicationProperties.getFloat("test.floatProperty"));
        Assertions.assertEquals(42.0f, ApplicationProperties.getFloat("test.intProperty"));
        Assertions.assertEquals(0.0f, ApplicationProperties.getFloat("nonexistent.property")); // TODO: Assert that an error is logged
        Assertions.assertEquals(0.0f, ApplicationProperties.getFloat("test.stringProperty")); // TODO: Assert that an error is logged
    }

    @Test
    public void testGetBooleanOrDefault() {
        Assertions.assertTrue(ApplicationProperties.getBoolean("test.booleanProperty", false));
        Assertions.assertFalse(ApplicationProperties.getBoolean("test.intProperty", true));
        Assertions.assertTrue(ApplicationProperties.getBoolean("nonexistent.property", true));
    }

    @Test
    public void testGetBoolean() {
        Assertions.assertTrue(ApplicationProperties.getBoolean("test.booleanProperty"));
        Assertions.assertFalse(ApplicationProperties.getBoolean("test.intProperty"));
        Assertions.assertFalse(ApplicationProperties.getBoolean("nonexistent.property")); // TODO: Assert that an error is logged
    }
}
