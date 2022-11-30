package io.github.view;

import io.github.view.core.Light;
import io.github.view.core.ModelRenderer;
import io.github.view.core.Renderer3D;
import io.github.view.resources.Mesh;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public final class RenderingSystem3D {

	private static final HashMap<Mesh, ArrayList<Renderer3D>> BATCH = new HashMap<>();
	private static final ArrayList<Light> LIGHTS = new ArrayList<>();

	public static void addToBatch(ModelRenderer renderer) {
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
		if(!Application.isRenderingThread())
			throw new RuntimeException("Rendering process can only be called from rendering thread");
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
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
