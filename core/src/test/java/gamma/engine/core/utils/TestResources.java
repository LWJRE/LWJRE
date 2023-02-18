package gamma.engine.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

public class TestResources {

	@Test
	public void testReadAs() {
		String wholeTextFile = Resources.readAs("/test.txt", inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String result = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
			return result;
		});
		Assertions.assertEquals("This is a test file.\nYou're welcome.", wholeTextFile);
	}

	@Test
	public void testProperties() {
		Properties properties = Resources.readProperties("/test.properties");
		Assertions.assertEquals("Test successful", properties.getProperty("testProperties"));
	}
}
