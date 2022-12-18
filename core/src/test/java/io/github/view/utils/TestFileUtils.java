package io.github.view.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestFileUtils {

	@Test
	public void testReadFile() {
		FileUtils.readFile("/files/test.txt", inputStream -> {
			Assertions.assertTrue(true);
		}, exception -> Assertions.fail());
	}

	@Test
	public void testReadFileException() {
		FileUtils.readFile("/files/test.txt", inputStream -> {
			throw new IOException("Test exception");
		}, exception -> {
			Assertions.assertEquals("Test exception", exception.getMessage());
		});
	}

	@Test
	public void testReadFileNotFound() {
		FileUtils.readFile("/files/_.txt", inputStream -> Assertions.fail(), exception -> {
			Assertions.assertEquals("Could not find file /files/_.txt", exception.getMessage());
		});
	}

	@Test
	public void testReadFileToValue() {
		Assertions.assertTrue(() -> FileUtils.readFile("/files/test.txt", inputStream -> true, exception -> false));
	}

	@Test
	public void testReadFileToValueException() {
		Assertions.assertEquals("Test exception", FileUtils.readFile("/files/test.txt", inputStream -> {
			throw new IOException("Test exception");
		}, exception -> {
			return exception.getMessage();
		}));
	}

	@Test
	public void testReadFileToValueNotFound() {
		Assertions.assertEquals("Could not find file /files/_.txt", FileUtils.readFile("/files/_.txt", inputStream -> "", Throwable::getMessage));
	}

	@Test
	public void testStreamLines() {
		FileUtils.streamLines("/files/lines.txt", stringStream -> {
			String[] expected = {"Line 1", "Line 2", "Line 3"};
			String[] actual = stringStream.toArray(String[]::new);
			Assertions.assertArrayEquals(expected, actual);
		}, exception -> Assertions.fail());
	}

	@Test
	public void testStreamLinesToValue() {
		String[] expected = {"Line 1", "Line 2", "Line 3"};
		Assertions.assertArrayEquals(expected, FileUtils.streamLines("/files/lines.txt", stringStream -> stringStream.toArray(String[]::new), exception -> new String[0]));
	}

	@Test
	public void testStreamLinesToValueNotFound() {
		Assertions.assertEquals("Could not find file /files/_.txt", FileUtils.streamLines("/files/_.txt", stringStream -> "", Throwable::getMessage));
	}

	@Test
	public void testReadString() {
		Assertions.assertEquals("This is a test file", FileUtils.readString("/files/test.txt", Throwable::getMessage));
	}

	@Test
	public void testReadStringNotFound() {
		Assertions.assertEquals("Could not find file /files/_.txt", FileUtils.readString("/files/_.txt", Throwable::getMessage));
	}
}
