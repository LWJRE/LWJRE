package io.github.view.graphics;

import io.github.view.resources.Mesh3D;

import java.util.ArrayList;
import java.util.HashMap;

public final class RenderingSystem3D {

	private static final HashMap<Mesh3D, HashMap<Object, ArrayList<Runnable>>> BATCH = new HashMap<>();

	public static void addToBatch(Mesh3D mesh, Object key, Runnable renderer) {
		if(!BATCH.containsKey(mesh)) {
			BATCH.put(mesh, new HashMap<>());
		}
		if(!BATCH.get(mesh).containsKey(key)) {
			BATCH.get(mesh).put(key, new ArrayList<>());
		}
		BATCH.get(mesh).get(key).add(renderer);
	}

	public static void renderingProcess() {
		// TODO: Load lights in all shaders
		BATCH.keySet().forEach(mesh -> BATCH.get(mesh).values().forEach(list -> list.forEach(mesh::draw)));
	}

	public static void removeFromBatch(Object key) {
		BATCH.values().forEach(map -> map.remove(key));
	}
}
