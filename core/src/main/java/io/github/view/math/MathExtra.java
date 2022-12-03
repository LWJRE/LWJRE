package io.github.view.math;

public final class MathExtra {

	public static float floor(float value) {
		return (float) Math.floor(value);
	}

	public static float ceil(float value) {
		return (float) Math.ceil(value);
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(min, Math.min(max, value));
	}
}
