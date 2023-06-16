package io.github.lwjre;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.vector.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestApplicationProperties {

	private static Stream<Arguments> testGetPropertySource() {
		return Stream.of(
				Arguments.of("Test", "test.string", (Function<String, ?>) ApplicationProperties::getString),
				Arguments.of(42, "test.int", (Function<String, ?>) ApplicationProperties::getInt),
				Arguments.of(true, "test.boolean", (Function<String, ?>) ApplicationProperties::getBoolean),
				Arguments.of(4.2f, "test.float", (Function<String, ?>) ApplicationProperties::getFloat),
				Arguments.of(new Vec2f(4.2f, 2.1f), "test.vec2f", (Function<String, ?>) ApplicationProperties::getVec2f),
				Arguments.of(new Vec3f(1.5f, 2.5f, 3.5f), "test.vec3f", (Function<String, ?>) ApplicationProperties::getVec3f),
				Arguments.of(new Vec4f(1.1f, 2.2f, 3.3f, 4.4f), "test.vec4f", (Function<String, ?>) ApplicationProperties::getVec4f),
				Arguments.of(new Vec2i(1, 2), "test.vec2i", (Function<String, ?>) ApplicationProperties::getVec2i),
				Arguments.of(new Vec3i(1, 2, 3), "test.vec3i", (Function<String, ?>) ApplicationProperties::getVec3i),
				Arguments.of(new Vec4i(1, 2, 3, 4), "test.vec4i", (Function<String, ?>) ApplicationProperties::getVec4i),
				Arguments.of(new Color3f(1.5f, 2.5f, 3.5f), "test.vec3f", (Function<String, ?>) ApplicationProperties::getColor3f),
				Arguments.of(new Color4f(1.1f, 2.2f, 3.3f, 4.4f), "test.vec4f", (Function<String, ?>) ApplicationProperties::getColor4f)
		);
	}

	@ParameterizedTest
	@MethodSource("testGetPropertySource")
	public void testGetProperty(Object expected, String key, Function<String, ?> function) {
		Object actual = function.apply(key);
		Assertions.assertEquals(expected, actual);
	}

	private static Stream<Arguments> testNoSuchElementSource() {
		return Stream.of(
				Arguments.of((Consumer<String>) ApplicationProperties::getString),
				Arguments.of((Consumer<String>) ApplicationProperties::getInt),
				Arguments.of((Consumer<String>) ApplicationProperties::getBoolean),
				Arguments.of((Consumer<String>) ApplicationProperties::getFloat),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec2f),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec3f),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec4f),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec2i),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec3i),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec4i),
				Arguments.of((Consumer<String>) ApplicationProperties::getColor3f),
				Arguments.of((Consumer<String>) ApplicationProperties::getColor4f)
		);
	}

	@ParameterizedTest
	@MethodSource("testNoSuchElementSource")
	public void testNoSuchElement(Consumer<String> function) {
		Assertions.assertThrows(NoSuchElementException.class, () -> function.accept("no.such.element"));
	}

	private static Stream<Arguments> testDefaultValueSource() {
		return Stream.of(
				Arguments.of("defaultValue", (Function<String, ?>) key -> ApplicationProperties.get(key, "defaultValue")),
				Arguments.of(0, (Function<String, ?>) key -> ApplicationProperties.get(key, 0)),
				Arguments.of(false, (Function<String, ?>) key -> ApplicationProperties.get(key, false)),
				Arguments.of(0.0f, (Function<String, ?>) key -> ApplicationProperties.get(key, 0.0f)),
				Arguments.of(Vec2f.Zero(), (Function<String, ?>) key -> ApplicationProperties.get(key, Vec2f.Zero())),
				Arguments.of(Vec3f.Zero(), (Function<String, ?>) key -> ApplicationProperties.get(key, Vec3f.Zero())),
				Arguments.of(Vec4f.Zero(), (Function<String, ?>) key -> ApplicationProperties.get(key, Vec4f.Zero())),
				Arguments.of(Vec2i.Zero(), (Function<String, ?>) key -> ApplicationProperties.get(key, Vec2i.Zero())),
				Arguments.of(Vec3i.Zero(), (Function<String, ?>) key -> ApplicationProperties.get(key, Vec3i.Zero())),
				Arguments.of(Vec4i.Zero(), (Function<String, ?>) key -> ApplicationProperties.get(key, Vec4i.Zero())),
				Arguments.of(Color3f.Black(), (Function<String, ?>) key -> ApplicationProperties.get(key, Color3f.Black())),
				Arguments.of(Color4f.Black(), (Function<String, ?>) key -> ApplicationProperties.get(key, Color4f.Black()))
		);
	}

	@ParameterizedTest
	@MethodSource("testDefaultValueSource")
	public void testDefaultValue(Object defaultValue, Function<String, ?> function) {
		Object actual = function.apply("no.such.element");
		Assertions.assertEquals(defaultValue, actual);
	}

	private static Stream<Arguments> testNumberFormatSource() {
		return Stream.of(
				Arguments.of((Consumer<String>) ApplicationProperties::getInt),
				Arguments.of((Consumer<String>) ApplicationProperties::getFloat),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec2f),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec3f),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec4f),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec2i),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec3i),
				Arguments.of((Consumer<String>) ApplicationProperties::getVec4i),
				Arguments.of((Consumer<String>) ApplicationProperties::getColor3f),
				Arguments.of((Consumer<String>) ApplicationProperties::getColor4f)
		);
	}

	@ParameterizedTest
	@MethodSource("testNumberFormatSource")
	public void testNumberFormat(Consumer<String> function) {
		Assertions.assertThrows(NumberFormatException.class, () -> function.accept("test.string"));
	}
}
