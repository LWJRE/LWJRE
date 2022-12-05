package io.github.view.physics;

import io.github.view.core.PhysicObject3D;
import io.github.view.math.Vector3;

import java.util.ArrayList;

public final class PhysicsSystem3D {

	private static final ArrayList<PhysicObject3D> BATCH = new ArrayList<>();

	public static void addObject(PhysicObject3D object) {
		BATCH.add(object);
	}

	public static void physicsProcess() {
		// TODO: Use a tree instead of a list
		BATCH.forEach(object -> BATCH.forEach(otherObject -> {
			if(!object.equals(otherObject)) {
				Vector3 overlap = object.worldSpaceBoundingBox().getIntersection(otherObject.worldSpaceBoundingBox());
				if(overlap.x() != 0 || overlap.y() != 0 || overlap.z() != 0)
					object.onCollision(overlap);
			}
		}));
	}

	public static void removeObject(PhysicObject3D object) {
		BATCH.remove(object);
	}
}
