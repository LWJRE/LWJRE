package io.github.view;

import io.github.view.core.ModelRenderer;
import io.github.view.resources.Mesh;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public final class RenderingSystem3D {

	private static final HashMap<Mesh, ArrayList<ModelRenderer>> BATCH = new HashMap<>();

	public static void addToBatch(ModelRenderer renderer) {
		if(BATCH.containsKey(renderer.getModel())) {
			BATCH.get(renderer.getModel()).add(renderer);
		} else {
			ArrayList<ModelRenderer> list = new ArrayList<>();
			list.add(renderer);
			BATCH.put(renderer.getModel(), list);
		}
	}

	public static void renderingProcess() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		BATCH.keySet().forEach(mesh -> mesh.bind(drawableMesh -> BATCH.get(mesh).forEach(renderer -> renderer.onRender(drawableMesh))));
	}

	public static void removeFromBatch(ModelRenderer renderer) {
		if(BATCH.containsKey(renderer.getModel())) {
			ArrayList<ModelRenderer> list = BATCH.get(renderer.getModel());
			list.remove(renderer);
			if(list.isEmpty()) {
				BATCH.remove(renderer.getModel());
			}
		}
	}
}
