package gamma.engine.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestReflection {

	@Test
	public void testSetField() {
		TestClass test = new TestClass("Hello", 0.5f);
		Reflection.setField(test, "arg2", 2.0f);
		Assertions.assertEquals(2.0f, test.arg2);
	}

	@Test
	public void testSetFieldInSuperclass() {
		TestClass2 test = new TestClass2();
		Reflection.setField(test, "arg1", ":)");
		Assertions.assertEquals(":)", test.arg1);
	}

	@Test
	public void testNoSuchField() {
		TestClass2 test = new TestClass2();
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.setField(test, "no", 0));
	}

	@Test
	public void testGetAllFields() {
		List<String> fields = Reflection.getAllFields(TestClass.class).toList();
		Assertions.assertTrue(fields.contains("arg1"));
		Assertions.assertTrue(fields.contains("arg2"));
	}

	@Test
	public void testGetAllFieldsIncludingSuperclass() {
		List<String> fields = Reflection.getAllFields(TestClass2.class).toList();
		Assertions.assertTrue(fields.contains("arg1"));
		Assertions.assertTrue(fields.contains("arg2"));
		Assertions.assertTrue(fields.contains("arg3"));
	}

	@Test
	public void testGetAllFieldsExcludingSuperclass() {
		List<String> fields = Reflection.getAllFields(TestClass2.class, false).toList();
		Assertions.assertFalse(fields.contains("arg1"));
		Assertions.assertFalse(fields.contains("arg2"));
		Assertions.assertTrue(fields.contains("arg3"));
	}

	@Test
	public void testInstantiate() {
		TestClass test = Reflection.instantiate(TestClass.class, "Hello", 0.5f);
		Assertions.assertEquals("Hello", test.arg1);
		Assertions.assertEquals(0.5f, test.arg2);
	}

	@Test
	public void testInstantiateAbstract() {
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.instantiate(TestAbstract.class));
	}

	@Test
	public void testInstantiateNoConstructor() {
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.instantiate(TestClass.class, 0.5f, "Hello"));
	}

	@Test
	public void testInstantiateException() {
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.instantiate(ExceptionConstructor.class));
	}

	@Test
	public void testInstantiateFromName() {
		Object object = Reflection.instantiate("gamma.engine.utils.TestReflection$TestClass", "Hello", 0.5f);
		if(object instanceof TestClass test) {
			Assertions.assertEquals("Hello", test.arg1);
			Assertions.assertEquals(0.5f, test.arg2);
		} else {
			Assertions.fail();
		}
	}

	@Test
	public void testClassNotFound() {
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.instantiate("class.does.not.Exist"));
	}

	@Test
	public void testCallMethod() {
		TestClass2 test = new TestClass2();
		Object result = Reflection.callMethod(test, "getMethod", 3);
		Assertions.assertEquals(test.getMethod(3), result);
	}

	@Test
	public void testCallMethodInSuperclass() {
		TestClass2 test = new TestClass2("One", 0.0f);
		Object result = Reflection.callMethod(test, "method", "Two");
		Assertions.assertEquals(test.method("Two"), result);
	}

	@Test
	public void testNoSuchMethod() {
		TestClass test = new TestClass("", 0.0f);
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.callMethod(test, ":("));
	}

	@Test
	public void testMethodException() {
		TestClass test = new TestClass("", 0.0f);
		Assertions.assertThrows(UnsupportedOperationException.class, test::exception);
		Assertions.assertThrows(ReflectionException.class, () -> Reflection.callMethod(test, "exception"));
	}

	public static class TestClass {

		public String arg1;
		public Float arg2;

		private TestClass(String arg1, Float arg2) {
			this.arg1 = arg1;
			this.arg2 = arg2;
		}

		public String method(String val) {
			return this.arg1 + val;
		}

		public void exception() {
			throw new UnsupportedOperationException(":(");
		}
	}

	public static class TestClass2 extends TestClass {

		public int arg3 = 2;

		private TestClass2(String arg1, Float arg2) {
			super(arg1, arg2);
		}

		private TestClass2() {
			this("", 0.0f);
		}

		public int getMethod(Integer someArg) {
			return this.arg3 * someArg;
		}
	}

	public static abstract class TestAbstract {

	}

	public static class ExceptionConstructor {

		public ExceptionConstructor() {
			throw new UnsupportedOperationException(":(");
		}
	}
}
