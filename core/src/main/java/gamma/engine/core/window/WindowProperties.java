package gamma.engine.core.window;

import gamma.engine.core.utils.PropertiesFile;
import org.lwjgl.glfw.GLFW;

public class WindowProperties extends PropertiesFile {

	public WindowProperties() {
		super();
	}

	public WindowProperties(String filePath) {
		super(filePath);
	}

	public final void setWindowHints() {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, this.get("visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, this.get("resizeable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, this.get("decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, this.get("focused", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, this.get("maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
	}

	public final WindowProperties setTitle(String title) {
		return (WindowProperties) this.set("title", title);
	}

	public final WindowProperties setSize(int width, int height) {
		return (WindowProperties) this.set("width", width).set("height", height);
	}

	public final String getTitle() {
		return this.get("title", "untitled");
	}

	public final int getWidth() {
		return this.get("width", 300);
	}

	public final int getHeight() {
		return this.get("height", 300);
	}
}
