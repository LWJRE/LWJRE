package io.github.view;

import io.github.view.core.Scene;
import io.github.view.core.SceneLoader;
import io.github.view.graphics.Graphics;
import io.github.view.graphics.RenderingSystem3D;
import io.github.view.graphics.Window;
import io.github.view.input.Keyboard;
import io.github.view.physics.PhysicsSystem3D;
import io.github.view.resources.Resource;
import org.lwjgl.glfw.GLFW;

public final class Application {

	private static Application application;

	public static boolean isRenderingThread() {
		return true; // TODO: Remove this
	}

	public static Window window() {
		return application.window;
	}

	private final Window window;

	// TODO: Load starting scene, change scene
	private Scene currentScene = SceneLoader.load("");

	private Application() {
		if(!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		this.window = new Window("Hello", 960, 540); // TODO: Window config
		this.window.setKeyCallback(((window1, key, scancode, action, mods) -> {
			Keyboard.registerKeyEvent(key, action);
		}));
	}

	// TODO: GLFW Error callback

	private void start() {
		try {
			this.window.makeContextCurrent();
			this.window.show();
			Graphics.depthTest(true);
			Graphics.clearColor(0.0f, 0.5f, 1.0f, 0.0f);
			while(!this.window.isCloseRequested()) {
				Graphics.clearFramebuffer();
				this.currentScene.process();
				PhysicsSystem3D.physicsProcess();
				RenderingSystem3D.renderingProcess();
				this.window.update();
				GLFW.glfwPollEvents();
			}
			this.window.destroy();
		} catch(Exception e) {
			System.err.println("Uncaught exception");
			e.printStackTrace();
		} finally {
			Resource.cleanUp(); // TODO: This might not be necessary
			GLFW.glfwTerminate();
		}
	}

	public static void main(String[] args) {
		application = new Application();
		application.start();
	}
}
