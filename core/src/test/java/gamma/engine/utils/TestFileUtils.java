package gamma.engine.utils;

import gamma.engine.resources.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Properties;

public class TestFileUtils {

	@Test
	public void testReadFile() {
		String firstLine = FileUtils.readFile("src/test/resources/test.txt", reader -> {
			BufferedReader bufferedReader = new BufferedReader(reader);
			String line = bufferedReader.readLine();
			bufferedReader.close();
			return line;
		});
		Assertions.assertEquals("This is a test file.", firstLine);
	}

	@Test
	public void testFileNotFound() {
		Assertions.assertThrows(UncheckedIOException.class, () -> FileUtils.readFile(":(", reader -> null));
	}

	@Test
	public void testReadResource() {
		String firstLine = FileUtils.readResource("test.txt", inputStream -> {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line = bufferedReader.readLine();
			bufferedReader.close();
			return line;
		});
		Assertions.assertEquals("This is a test file.", firstLine);
	}

	@Test
	public void testResourceNotFound() {
		Assertions.assertThrows(UncheckedIOException.class, () -> FileUtils.readResource(":(", inputStream -> null));
	}

	@Test
	public void testFileToString() {
		String fileContent = FileUtils.readFileAsString("src/test/resources/test.txt");
		Assertions.assertEquals("This is a test file.\nYou're welcome.", fileContent);
	}

	@Test
	public void testResourceToString() {
		String fileContent = FileUtils.readResourceAsString("test.txt");
		Assertions.assertEquals("This is a test file.\nYou're welcome.", fileContent);
	}

	@Test
	public void testReadPropertiesFile() {
		Properties properties = FileUtils.readPropertiesFile("src/test/resources/test.properties");
		Assertions.assertEquals("Test successful", properties.getProperty("testProperties"));
	}

	@Test
	public void testReadPropertiesResource() {
		Properties properties = FileUtils.readPropertiesResource("test.properties");
		Assertions.assertEquals("Test successful", properties.getProperty("testProperties"));
	}

	@Test
	public void testReadFileBytes() {
		byte[] bytes = FileUtils.readFileBytes("src/test/resources/test.txt");
		String fileContent = new String(bytes);
		Assertions.assertEquals("This is a test file.\nYou're welcome.", fileContent);
	}

	@Test
	public void testReadFileBytesNotFound() {
		Assertions.assertThrows(UncheckedIOException.class, () -> FileUtils.readFileBytes(":("));
	}

	@Test
	public void testReadResourceBytes() {
		byte[] bytes = FileUtils.readResourceBytes("test.txt");
		String fileContent = new String(bytes);
		Assertions.assertEquals("This is a test file.\nYou're welcome.", fileContent);
	}
}
