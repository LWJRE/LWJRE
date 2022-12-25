package io.github.view.physics;

import io.github.view.core.CollisionObject3D;
import io.github.view.math.Vector3;

public record Collision3D(CollisionObject3D collider, Vector3 normal, float depth) {
}
