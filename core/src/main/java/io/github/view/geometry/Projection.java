package io.github.view.geometry;

public record Projection(float max, float min) {

	public boolean overlaps(Projection projection) {
		return this.overlaps(projection.max(), projection.min());
	}

	public boolean overlaps(float max, float min) {
		return !(max < this.min() || this.max() < min);
	}
}
