package engine.core;

import engine.core.resources.GLResource;
import engine.core.tree.SceneTree;
import engine.core.window.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

public final class Application {

	private static Application application;

	public static Window window() {
		return application.window;
	}

	private final Window window;

	private Application() {
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		this.window = new Window();
	}

	private void run() {
		try {
			this.window.makeContextCurrent();
			this.window.show();
			// TODO: Allow for multithreading ?
			GL.createCapabilities();
			// TODO: Get from application properties
			Graphics.depthTest(true);
			Graphics.clearColor(0.0f, 0.5f, 1.0f, 0.0f);
			SceneTree.loadScene("/scenes/test_scene.yaml");
			while(!this.window.isCloseRequested()) {
				Graphics.clearFramebuffer();
				SceneTree.process();
				RenderingSystem3D.renderingProcess();
				this.window.update();
				GLFW.glfwPollEvents();
			}
			this.window.destroy();
		} catch(Exception e) {
			System.err.println("Uncaught exception");
			e.printStackTrace();
		} finally {
			GLResource.cleanUp();
			GLFW.glfwTerminate();
		}
	}

	// TODO: Change scene

	public static void main(String[] args) {
		application = new Application();
		application.run();
	}
}
