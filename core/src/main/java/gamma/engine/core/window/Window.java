package gamma.engine.core.window;

import gamma.engine.core.ApplicationProperties;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import vecmatlib.vector.Vec2i;

import java.nio.IntBuffer;
import java.util.ServiceLoader;

public class Window {

	public static Window getCurrent() {
		long handle = GLFW.glfwGetCurrentContext();
		if(handle == MemoryUtil.NULL)
			throw new IllegalStateException("There is now window current in the calling thread");
		return new Window(handle);
	}

	private static final ServiceLoader<WindowListener> LISTENERS = ServiceLoader.load(WindowListener.class);

	protected long handle;
	protected int width = 0;
	protected int height = 0;

	public Window(String title, int width, int height) {
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationProperties.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationProperties.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationProperties.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationProperties.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationProperties.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		this.width = width;
		this.height = height;
		GLFW.glfwSetWindowSizeCallback(this.handle, ((window, width1, height1) -> {
			LISTENERS.forEach(windowListener -> windowListener.onResize(width1, height1));
			this.width = width1;
			this.height = height1;
		}));
	}

	public Window() {
		this(ApplicationProperties.get("window/title", "untitled"), ApplicationProperties.get("window/size/width", 400), ApplicationProperties.get("window/size/height", 300));
	}

	private Window(long handle) {
		this.handle = handle;
	}

	// TODO: Check for main thread

	private void checkState() {
		if(this.handle == MemoryUtil.NULL)
			throw new IllegalStateException("Window was already destroyed");
	}

	public void makeContextCurrent() {
		this.checkState();
		GLFW.glfwMakeContextCurrent(this.handle);
	}

	public void show() {
		this.checkState();
		GLFW.glfwShowWindow(this.handle);
	}

	public void setSize(int width, int height) {
		this.checkState();
		GLFW.glfwSetWindowSize(this.handle, width, height);
	}

	public void setWidth(int width) {
		this.setSize(width, this.height);
	}

	public void setHeight(int height) {
		this.setSize(this.width, height);
	}

	public void setSize(Vec2i size) {
		this.setSize(size.x(), size.y());
	}

	public Vec2i getSize() {
		return new Vec2i(this.width, this.height);
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public float getAspect() {
		return (float) this.width / this.height;
	}

	public void setPosition(int x, int y) {
		this.checkState();
		GLFW.glfwSetWindowPos(this.handle, x, y);
	}

	// TODO: Change this?
	public Vec2i getPosition() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer x = stack.mallocInt(1);
			IntBuffer y = stack.mallocInt(1);
			GLFW.glfwGetWindowPos(this.handle, x, y);
			return new Vec2i(x.get(), y.get());
		}
	}

	public boolean isCloseRequested() {
		this.checkState();
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public void requestClose() {
		this.checkState();
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	public void update() {
		this.checkState();
		GLFW.glfwSwapBuffers(this.handle);
	}

	public void destroy() {
		this.checkState();
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
		this.handle = MemoryUtil.NULL;
	}

	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
