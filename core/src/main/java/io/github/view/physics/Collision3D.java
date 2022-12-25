package io.github.view.physics;

import io.github.view.core.PhysicObject3D;
import io.github.view.math.Vector3;

public record Collision3D(PhysicObject3D collider, Vector3 normal, float depth) {
}
