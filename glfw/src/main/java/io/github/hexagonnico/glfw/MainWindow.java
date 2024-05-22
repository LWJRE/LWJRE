package io.github.hexagonnico.glfw;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

public final class MainWindow {

    private static MainWindow instance;

    public static MainWindow getInstance() {
        if(instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    private final long handle;

    private MainWindow() {
        this.handle = GLFW.glfwCreateWindow(300, 300, "Hello world", MemoryUtil.NULL, MemoryUtil.NULL);
        if(this.handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwMakeContextCurrent(this.handle);
    }

    public void show() {
        GLFW.glfwShowWindow(this.handle);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(this.handle, title);
    }

    public void setSize(int width, int height) {
        GLFW.glfwSetWindowSize(this.handle, width, height);
    }

    public void update() {
        GLFW.glfwSwapBuffers(this.handle);
    }

    public boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(this.handle);
    }

    public void destroy() {
        Callbacks.glfwFreeCallbacks(this.handle);
        GLFW.glfwDestroyWindow(this.handle);
    }
}
