package gamma.engine.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class TestYamlParser {

	@Test
	public void testLoadAs() {
		TestObject testObject = YamlParser.loadAs("/yaml/test.yaml", TestObject.class);
		Assertions.assertEquals("string", testObject.testString);
		Assertions.assertEquals(42, testObject.testInt);
	}

	@Test
	public void testLoadAsMap() {
		Map<String, Object> map = YamlParser.loadAsMap("/yaml/test.yaml");
		Assertions.assertEquals("string", map.get("testString"));
		Assertions.assertEquals(42, map.get("testInt"));
	}

	@Test
	public void testParseAs() {
		TestObject testObject = YamlParser.parseAs("testString: \"string\"\ntestInt: 42", TestObject.class);
		Assertions.assertEquals("string", testObject.testString);
		Assertions.assertEquals(42, testObject.testInt);
	}

	@Test
	public void testParseAsMap() {
		Map<String, Object> map = YamlParser.parseAsMap("key1: 42\nkey2: \"test\"");
		Assertions.assertEquals(42, map.get("key1"));
		Assertions.assertEquals("test", map.get("key2"));
	}

	public static class TestObject {

		private String testString = "";
		private int testInt = 0;
	}
}
