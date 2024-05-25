package io.github.hexagonnico.glfw;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

/**
 * Singleton class used to manage the GLFW main window.
 */
public final class MainWindow {

    /**
     * Singleton instance.
     */
    private static MainWindow instance;

    /**
     * Gets the main window instance.
     * The main window is created the first time this method is called.
     *
     * @return The main window instance.
     */
    public static MainWindow getInstance() {
        if(instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    /**
     * Window handle.
     */
    private final long handle;

    /**
     * Creates the GLFW window.
     *
     * @throws RuntimeException If the GLFW window couldn't be created.
     */
    private MainWindow() {
        this.handle = GLFW.glfwCreateWindow(300, 300, "Hello world", MemoryUtil.NULL, MemoryUtil.NULL);
        if(this.handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwMakeContextCurrent(this.handle);
    }

    /**
     * Makes the window visible if it was previously hidden.
     *
     * @see GLFW#glfwShowWindow(long)
     */
    public void show() {
        GLFW.glfwShowWindow(this.handle);
    }

    /**
     * Sets the title of the window.
     *
     * @param title The title of the window.
     *
     * @see GLFW#glfwSetWindowTitle(long, CharSequence)
     */
    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(this.handle, title);
    }

    /**
     * Sets the size of the window.
     *
     * @param width Window width.
     * @param height Window size.
     *
     * @see GLFW#glfwSetWindowSize(long, int, int)
     */
    public void setSize(int width, int height) {
        GLFW.glfwSetWindowSize(this.handle, width, height);
    }

    /**
     * Updates the window.
     *
     * @see GLFW#glfwSwapBuffers(long)
     */
    public void update() {
        GLFW.glfwSwapBuffers(this.handle);
    }

    /**
     * Checks if the user has attempted to close the window.
     *
     * @return True if the window was requested to be closed, otherwise false.
     *
     * @see GLFW#glfwWindowShouldClose(long)
     */
    public boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(this.handle);
    }

    /**
     * Destroys the GLFW window.
     *
     * @see Callbacks#glfwFreeCallbacks(long)
     * @see GLFW#glfwDestroyWindow(long)
     */
    public void destroy() {
        Callbacks.glfwFreeCallbacks(this.handle);
        GLFW.glfwDestroyWindow(this.handle);
    }
}
