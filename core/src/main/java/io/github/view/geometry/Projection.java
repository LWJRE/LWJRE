package io.github.view.geometry;

public record Projection(float max, float min) {

	public boolean overlaps(Projection projection) {
		return this.overlaps(projection.max(), projection.min());
	}

	public boolean overlaps(float max, float min) {
		return !(max < this.min() || this.max() < min);
	}

	public float getOverlap(Projection projection) {
		return this.getOverlap(projection.max(), projection.min());
	}

	public float getOverlap(float max, float min) {
		return Math.min(max - this.min(), this.max() - min);
	}
}
