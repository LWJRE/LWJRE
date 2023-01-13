package gamma.engine.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestFileUtils {

	@Test
	public void testInputStream() {
		String result = FileUtils.inputStream("/text/test.txt", inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = reader.readLine();
			reader.close();
			return line;
		});
		Assertions.assertEquals("This is a test file", result);
	}

	@Test
	public void testReadAsString() {
		String result = FileUtils.readAsString("/text/test.txt");
		Assertions.assertEquals("This is a test file\nYou're welcome", result);
	}

	// TODO: Test load images
}
