package io.github.view.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TestProjection {

	@Test
	public void testLength() {
		Projection projection = new Projection(-1.0f, 1.0f);
		Assertions.assertEquals(2.0f, projection.length());
	}

	@ParameterizedTest
	@CsvSource({"-0.75f,0.25f, 0.0f,1.0f, true", "-2.0f,2.0f, -1.0f,1.0f, true", "-1.0f,0.0f, 1.0f,2.0f, false", "1.0f,2.0f, -1.0f,0.0f, false"})
	public void testOverlaps(float minA, float maxA, float minB, float maxB, boolean expected) {
		Projection a = new Projection(minA, maxA);
		Projection b = new Projection(minB, maxB);
		Assertions.assertEquals(expected, a.overlaps(b));
	}

	@ParameterizedTest
	@CsvSource({"-0.75f,0.25f, 0.0f,1.0f, 0.0f,0.25f", "-2.0f,2.0f, -1.0f,1.0f, -1.0f,1.0f", "-2.0f,-1.0f, 1.0f,2.0f, 1.0f,-1.0f"})
	public void testGetOverlap(float minA, float maxA, float minB, float maxB, float overlapA, float overlapB) {
		Projection a = new Projection(minA, maxA);
		Projection b = new Projection(minB, maxB);
		Assertions.assertEquals(new Projection(overlapA, overlapB), a.getOverlap(b));
	}
}
