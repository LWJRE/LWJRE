package gamma.engine.core.rendering;

import gamma.engine.core.ApplicationListener;
import gamma.engine.core.resources.DeletableResource;
import gamma.engine.core.resources.Mesh;
import gamma.engine.core.scene.Component;
import gamma.engine.core.window.WindowListener;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public final class RenderingSystem implements ApplicationListener, WindowListener {

	private static final HashMap<Mesh, HashMap<Component, Runnable>> RENDER_BATCH = new HashMap<>();

	public static void addToBatch(Component key, Mesh mesh, Runnable renderFunction) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).put(key, renderFunction);
		} else {
			HashMap<Component, Runnable> batch = new HashMap<>();
			batch.put(key, renderFunction);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	public static void removeFromBatch(Component key, Mesh mesh) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).remove(key);
		}
	}

	@Override
	public void onStart() {
		GL.createCapabilities();
	}

	@Override
	public void onUpdate() {
		// TODO: Give option for depth test and backface culling
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// TODO: Give default clear color option
		GL11.glClearColor(0.0f, 0.5f, 1.0f, 1.0f);
		RENDER_BATCH.forEach((mesh, batch) -> {
			mesh.bind();
			batch.values().forEach(Runnable::run);
		});
	}

	@Override
	public void onResize(int width, int height) {
		// TODO: Give the user different options for resizing
		GL11.glViewport(0, 0, width, height);
	}

	@Override
	public void onTerminate() {
		DeletableResource.deleteAll();
	}
}