package io.github.view.physics;

import io.github.view.core.PhysicObject3D;

import java.util.ArrayList;
import java.util.Objects;

public final class PhysicsSystem3D {

	private static final ArrayList<PhysicObject3D> BATCH = new ArrayList<>();
	private static long previousTime = System.nanoTime();

	public static void addObject(PhysicObject3D object) {
		BATCH.add(object);
	}

	public static void physicsProcess() {
		// TODO: Use a tree instead of a list
		long time = System.nanoTime();
		BATCH.forEach(object -> {
			object.onPhysicsUpdate((time - previousTime) / 1_000_000_000.0f);
			BATCH.stream()
					.filter(otherObject -> !otherObject.equals(object))
					.map(object::computeCollision)
					.filter(Objects::nonNull)
					.findFirst()
					.ifPresent(object::onCollision);
		});
		previousTime = time;
	}

	public static void removeObject(PhysicObject3D object) {
		BATCH.remove(object);
	}
}
