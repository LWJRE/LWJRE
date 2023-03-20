package gamma.engine.core.rendering;

import gamma.engine.core.ApplicationListener;
import gamma.engine.core.resources.DeletableResource;
import gamma.engine.core.window.WindowListener;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public final class RenderingSystem implements ApplicationListener, WindowListener {

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
		// TODO: Render batches
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