package io.github.view.math;

public record Color(float r, float g, float b) implements Float3 {

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
