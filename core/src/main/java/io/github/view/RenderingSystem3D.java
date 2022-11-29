package io.github.view;

import io.github.view.core.ModelRenderer;
import io.github.view.core.Renderer3D;
import io.github.view.resources.Mesh;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class RenderingSystem3D {

	private static final HashMap<Mesh, ArrayList<Renderer3D>> BATCH = new HashMap<>();

	public static void addToBatch(ModelRenderer renderer) {
		if(BATCH.containsKey(renderer.getMesh())) {
			BATCH.get(renderer.getMesh()).add(renderer);
		} else {
			ArrayList<Renderer3D> list = new ArrayList<>();
			list.add(renderer);
			BATCH.put(renderer.getMesh(), list);
		}
	}

	public static void renderingProcess() {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Rendering process can only be called from rendering thread");
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		BATCH.keySet().forEach(mesh -> mesh.bind(drawableMesh -> BATCH.get(mesh).forEach(renderer -> renderer.render(drawableMesh, List.of()))));
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
}
