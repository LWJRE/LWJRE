package gamma.engine.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestReflection {

	@Test
	public void testInstanceFromName() {
		Object object = Reflection.instance("gamma.engine.core.utils.TestReflection$TestObject", "string", 42);
		Assertions.assertTrue(object instanceof TestObject);
		Assertions.assertEquals("string", ((TestObject) object).testString);
		Assertions.assertEquals(42, ((TestObject) object).testInt);
	}

	@Test
	public void testInstanceFromClass() {
		TestObject object = Reflection.instance(TestObject.class, "string", 42);
		Assertions.assertEquals("string", object.testString);
		Assertions.assertEquals(42, object.testInt);
	}

	@Test
	public void testSetField() {
		TestObject testObject = new TestObject("test", -1);
		Reflection.setField(testObject, "testString", "string");
		Reflection.setField(testObject, "testInt", 42);
		Assertions.assertEquals("string", testObject.testString);
		Assertions.assertEquals(42, testObject.testInt);
	}

	@Test
	public void testSetFieldSuperclass() {
		TestSubclass testObject = new TestSubclass("test", -1);
		Reflection.setField(testObject, "testString", "string");
		Reflection.setField(testObject, "testInt", 42);
		Reflection.setField(testObject, "testFloat", 0.1f);
		Assertions.assertEquals("string", testObject.testString);
		Assertions.assertEquals(42, testObject.testInt);
		Assertions.assertEquals(0.1f, testObject.testFloat);
	}

	private static class TestObject {

		public final String testString;
		public final Integer testInt;

		public TestObject(String testString, Integer testInt) {
			this.testString = testString;
			this.testInt = testInt;
		}
	}

	private static class TestSubclass extends TestObject {

		public final Float testFloat = 0.0f;

		public TestSubclass(String testString, Integer testInt) {
			super(testString, testInt);
		}
	}
}
