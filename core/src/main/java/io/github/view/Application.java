package io.github.view;

import io.github.view.graphics.Graphics;
import io.github.view.graphics.RenderingSystem3D;
import io.github.view.graphics.Window;
import io.github.view.input.Keyboard;
import io.github.view.physics.PhysicsSystem3D;
import io.github.view.resources.Resource;
import io.github.view.scene.Scene;
import io.github.view.scene.SceneLoader;
import io.github.view.utils.PropertiesFile;
import org.lwjgl.glfw.GLFW;

public final class Application {

	private static Application application;

	public static Window window() {
		return application.window;
	}

	public static void changeScene(String sceneFile) {
		application.currentScene = SceneLoader.loadScene(sceneFile);
	}

	private final Window window;
	private Scene currentScene;

	private Application() {
		if(!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
		PropertiesFile applicationProperties = PropertiesFile.loadFromFile("/application.properties");
		this.window = new Window(PropertiesFile.loadFromFile("/window.properties"));
		this.window.setKeyCallback(((window1, key, scancode, action, mods) -> {
			Keyboard.registerKeyEvent(key, action);
		}));
		this.currentScene = SceneLoader.loadScene(applicationProperties.getString("startScene"));
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
