package io.github.view.graphics;

import io.github.view.core.Light;
import io.github.view.core.Renderer3D;
import io.github.view.resources.Mesh;

import java.util.ArrayList;
import java.util.HashMap;

public final class RenderingSystem3D {

	private static final HashMap<Mesh, ArrayList<Renderer3D>> BATCH = new HashMap<>();
	private static final ArrayList<Light> LIGHTS = new ArrayList<>();

	public static void addToBatch(Renderer3D renderer) {
		if(BATCH.containsKey(renderer.getMesh())) {
			BATCH.get(renderer.getMesh()).add(renderer);
		} else {
			ArrayList<Renderer3D> list = new ArrayList<>();
			list.add(renderer);
			BATCH.put(renderer.getMesh(), list);
		}
	}

	public static void addLight(Light light) {
		LIGHTS.add(light);
	}

	public static void renderingProcess() {
		BATCH.keySet().forEach(mesh -> mesh.bind(drawableMesh -> BATCH.get(mesh).forEach(renderer -> renderer.render(drawableMesh, LIGHTS))));
	}

	public static void removeFromBatch(Renderer3D renderer) {
		if(BATCH.containsKey(renderer.getMesh())) {
			ArrayList<Renderer3D> list = BATCH.get(renderer.getMesh());
			list.remove(renderer);
			if(list.isEmpty()) {
				BATCH.remove(renderer.getMesh());
			}
		}
	}

	public static void removeLight(Light light) {
		LIGHTS.remove(light);
	}
}
