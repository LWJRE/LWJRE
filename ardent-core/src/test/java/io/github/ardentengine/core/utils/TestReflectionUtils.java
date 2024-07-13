package io.github.ardentengine.core.utils;

import io.github.ardentengine.core.util.ReflectionException;
import io.github.ardentengine.core.util.ReflectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class TestReflectionUtils {

    public static class MockClass {

    }

    @Test
    public void testNewInstance() {
        var instance = ReflectionUtils.newInstance("io.github.ardentengine.core.utils.TestReflectionUtils$MockClass");
        Assertions.assertEquals(MockClass.class, instance.getClass());
    }

    @Test
    public void testNewInstanceClassNotFound() {
        Assertions.assertThrows(ReflectionException.class, () -> ReflectionUtils.newInstance(">:3"));
    }

    public static class MockClassWithConstructor {
        
        private int test;
        
        public MockClassWithConstructor(int test) {
            this.test = test;
        }

        public int getTest() {
            return this.test;
        }

        @SuppressWarnings("unused")
        public void setTest(int test) {
            this.test = test;
        }
    }

    @Test
    public void testNewInstanceWithNoPublicNoArgsConstructor() {
        Assertions.assertThrows(ReflectionException.class, () -> ReflectionUtils.newInstance("io.github.ardentengine.core.utils.TestReflectionUtils$MockClassWithConstructor"));
    }

    @SuppressWarnings("unused")
    public static class MockClassWithException {

        public MockClassWithException() {
            throw new RuntimeException();
        }
    }

    @Test
    public void testNewInstanceWithException() {
        Assertions.assertThrows(ReflectionException.class, () -> ReflectionUtils.newInstance("io.github.ardentengine.core.utils.TestReflectionUtils$MockClassWithException"));
    }

    public static class ParentClass {

        private String parentClassField;

        public String getParentClassField() {
            return this.parentClassField;
        }

        public void setParentClassField(String parentClassField) {
            this.parentClassField = parentClassField;
        }
    }

    public static class ChildClass extends ParentClass {

        private String childClassField;

        public String getChildClassField() {
            return this.childClassField;
        }

        public void setChildClassField(String childClassField) {
            this.childClassField = childClassField;
        }
    }

    @Test
    public void testSetField() {
        var instance = new ChildClass();
        Assertions.assertTrue(ReflectionUtils.setField(instance, "childClassField", "Ok!"));
        Assertions.assertEquals("Ok!", instance.getChildClassField());
        Assertions.assertTrue(ReflectionUtils.setField(instance, "parentClassField", "Also ok!"));
        Assertions.assertEquals("Also ok!", instance.getParentClassField());
    }

    @Test
    public void testSetNonExistingField() {
        var instance = new MockClass();
        Assertions.assertFalse(ReflectionUtils.setField(instance, "fieldName", 42));
    }

    @Test
    public void testSetNumericField() {
        var instance = new MockClassWithConstructor(0);
        Assertions.assertTrue(ReflectionUtils.setField(instance, "test", 42));
        Assertions.assertEquals(42, instance.getTest());
        Assertions.assertTrue(ReflectionUtils.setField(instance, "test", 42.0f));
        Assertions.assertEquals(42, instance.getTest());
    }

    public static class ContainerClass {

        @SuppressWarnings("unused")
        private ParentClass thing;
    }

    @Test
    public void testSetFieldTypeMismatch() {
        var instance = new ContainerClass();
        Assertions.assertFalse(ReflectionUtils.setField(instance, "thing", new MockClass()));
        Assertions.assertTrue(ReflectionUtils.setField(instance, "thing", new ParentClass()));
        Assertions.assertTrue(ReflectionUtils.setField(instance, "thing", new ChildClass()));
    }

    @Test
    public void testGetAllFields() {
        var instance = new ChildClass();
        instance.setChildClassField("Child field value");
        instance.setParentClassField("Parent field value");
        var expected = Map.of("parentClassField", "Parent field value", "childClassField", "Child field value");
        var result = new HashMap<String, Object>();
        ReflectionUtils.getAllFields(instance, result);
        Assertions.assertEquals(expected, result);
    }
}
