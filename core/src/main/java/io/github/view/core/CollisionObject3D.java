package io.github.view.core;

import io.github.view.geometry.BoundingBox3D;
import io.github.view.math.Vector3;

public class CollisionObject3D extends Transform3D {

	private final BoundingBox3D boundingBox = new BoundingBox3D(new Vector3(-0.5f, -0.5f, -0.5f), Vector3.RIGHT, Vector3.UP, Vector3.FORWARD);
}
