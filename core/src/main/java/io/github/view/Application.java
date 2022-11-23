package io.github.view;

import org.lwjgl.glfw.GLFW;

public class Application {

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

	protected Application() {
		this.mainThread = Thread.currentThread();
		this.renderingThread = new Thread(this::renderingThread);
	}

	private void start() {
		if(GLFW.glfwInit()) {
			this.window = new Window("Hello", 300, 300);
			this.renderingThread.start();
			while(!this.window.shouldClose()) {
				GLFW.glfwPollEvents();
			}
			try {
				this.renderingThread.join();
			} catch (InterruptedException e) {
				// TODO: Terminate program
				throw new RuntimeException(e);
			}
			this.window.destroy();
		} else {
			System.err.println("Could not initialize GLFW");
		}
		GLFW.glfwTerminate();
	}

	private void renderingThread() {
		this.window.makeContextCurrent();
		while(!this.window.shouldClose()) {
			Rendering.renderingProcess();
			this.window.update();
		}
		this.window.makeContextNonCurrent();
	}

	public static void main(String[] args) {
		application = new Application();
		application.start();
	}
}
