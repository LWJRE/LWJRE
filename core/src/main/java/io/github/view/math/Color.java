package io.github.view.math;

public record Color(float r, float g, float b) implements Float3 {

	public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
	public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);

	@Override
	public float x() {
		return this.r();
	}

	@Override
	public float y() {
		return this.g();
	}

	@Override
	public float z() {
		return this.b();
	}
}
