package io.github.view.physics;

import io.github.view.core.StaticBody3D;

import java.util.ArrayList;

public final class PhysicsSystem3D {

	private static final ArrayList<StaticBody3D> BATCH = new ArrayList<>();

	public static void addObject(StaticBody3D object) {
		BATCH.add(object);
	}

	public static void physicsProcess() {
		// TODO: Use a tree instead of a list
		BATCH.forEach(object -> BATCH.forEach(otherObject -> {
			if(!object.equals(otherObject)) {
				if(object.isColliding(otherObject))
					System.out.println("Collision!");
			}
		}));
	}

	public static void removeObject(StaticBody3D object) {
		BATCH.remove(object);
	}
}
