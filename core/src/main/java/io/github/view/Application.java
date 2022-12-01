package io.github.view;

import io.github.view.core.Scene;
import io.github.view.resources.Resource;
import org.lwjgl.glfw.GLFW;

public final class Application {

	private static Application application;

	public static boolean isMainThread() {
		return Thread.currentThread().equals(application.mainThread);
	}

	public static boolean isRenderingThread() {
		return Thread.currentThread().equals(application.renderingThread);
	}

	public static Window window() {
		return application.window;
	}

	private Window window;

	private final Thread mainThread;
	private final Thread renderingThread;

	private Scene currentScene;

	private Application() {
		this.mainThread = Thread.currentThread();
		this.renderingThread = new Thread(this::renderingThread);
		this.mainThread.setName("Main-Thread");
		this.renderingThread.setName("Rendering-Thread");
		this.currentScene = new Scene();
	}

	private void start() {
		try {
			if(GLFW.glfwInit()) {
				this.window = new Window("Hello", 960, 540);
				this.renderingThread.start();
				while(!this.window.shouldClose()) {
					this.currentScene.process();
					GLFW.glfwPollEvents();
				}
				this.renderingThread.join();
				this.window.destroy();
			} else {
				System.err.println("Could not initialize GLFW");
			}
		} catch(Exception e) {

		} finally {
			GLFW.glfwTerminate();
		}
	}

	private void renderingThread() {
		this.window.makeContextCurrent();
		while(!this.window.shouldClose()) {
			RenderingSystem3D.renderingProcess();
			this.currentScene.render();
			this.window.update();
		}
		Resource.cleanUp();
		this.window.makeContextNonCurrent();
	}

	public static void main(String[] args) {
		application = new Application();
		application.start();
	}
}
