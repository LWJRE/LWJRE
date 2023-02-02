package gamma.engine.graphics;

import gamma.engine.core.ApplicationListener;
import gamma.engine.core.WindowListener;
import gamma.engine.graphics.resources.DeletableResource;
import org.lwjgl.opengl.GL;

/**
 * Main class for the graphics module. Listens to application and window events.
 *
 * @author Nico
 */
public final class GraphicsModule implements ApplicationListener, WindowListener {

	@Override
	public void onStart() {
		GL.createCapabilities();
		Graphics.clearColor(0.0f, 0.5f, 1.0f, 1.0f);
	}

	@Override
	public void onUpdate() {
		Graphics.clearFramebuffer();
		RenderingSystem3D.renderingProcess();
	}

	@Override
	public void onResize(int width, int height) {
		// TODO: Give the user different options for resizing
		Graphics.setViewport(width, height);
	}

	@Override
	public void onTerminate() {
		DeletableResource.deleteAll();
	}
}
