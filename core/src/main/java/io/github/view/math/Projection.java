package io.github.view.math;

public record Projection(float min, float max) implements Float2 {

	@Override
	public float x() {
		return this.min();
	}

	@Override
	public float y() {
		return this.max();
	}

	public float length() {
		return this.max() - this.min();
	}

	public boolean overlaps(Projection projection) {
		return this.overlaps(projection.min(), projection.max());
	}

	public boolean overlaps(float min, float max) {
		return this.min() <= max && this.max() >= min;
	}

	public Projection getOverlap(Projection projection) {
		return this.getOverlap(projection.min(), projection.max());
	}

	public Projection getOverlap(float min, float max) {
		return new Projection(Math.max(this.min(), min), Math.min(this.max(), max));
	}
}
