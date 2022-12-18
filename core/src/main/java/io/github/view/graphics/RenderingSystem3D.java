package io.github.view.graphics;

import io.github.view.core.Light;
import io.github.view.core.Renderer3D;
import io.github.view.resources.Mesh;
import io.github.view.resources.Mesh3D;

import java.util.ArrayList;
import java.util.HashMap;

public final class RenderingSystem3D {

	private static final HashMap<Mesh3D, ArrayList<Runnable>> BATCH = new HashMap<>();
	private static final ArrayList<Light> LIGHTS = new ArrayList<>();

	public static void addToBatch(Mesh3D mesh, Runnable renderer) {
		if(BATCH.containsKey(mesh)) {
			BATCH.get(mesh).add(renderer);
		} else {
			ArrayList<Runnable> list = new ArrayList<>();
			list.add(renderer);
			BATCH.put(mesh, list);
		}
	}

	public static void addLight(Light light) {
		LIGHTS.add(light);
	}

	public static void renderingProcess() {
		BATCH.keySet().forEach(mesh -> BATCH.get(mesh).forEach(mesh::draw));
	}

	public static void removeFromBatch(Renderer3D renderer) {
		/*if(BATCH.containsKey(renderer.getMesh())) {
			ArrayList<Renderer3D> list = BATCH.get(renderer.getMesh());
			list.remove(renderer);
			if(list.isEmpty()) {
				BATCH.remove(renderer.getMesh());
			}
		}*/
	}

	public static void removeLight(Light light) {
		LIGHTS.remove(light);
	}
}
