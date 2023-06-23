package io.github.lwjre.engine.physics;

import io.github.lwjre.engine.nodes.CollisionObject3D;

import java.util.Set;

public record CollisionPair3D(CollisionObject3D colliderA, CollisionObject3D colliderB) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CollisionPair3D that = (CollisionPair3D) o;
		return (this.colliderA == that.colliderA && this.colliderB == that.colliderB) || (this.colliderA == that.colliderB && this.colliderB == that.colliderA);
	}

	@Override
	public int hashCode() {
		return Set.of(this.colliderA, this.colliderB).hashCode();
	}
}
