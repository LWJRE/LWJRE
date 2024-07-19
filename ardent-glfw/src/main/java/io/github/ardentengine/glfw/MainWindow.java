package io.github.ardentengine.glfw;

import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.input.*;
import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
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

    private static void windowHint(int hint, String property, boolean defaultValue) {
        GLFW.glfwWindowHint(hint, ApplicationProperties.getBoolean(property, defaultValue) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
    }

    /**
     * Window handle.
     */
    private final long handle;

    private float mouseX = 0.0f;
    private float mouseY = 0.0f;

    /**
     * Creates the GLFW window.
     *
     * @throws RuntimeException If the GLFW window couldn't be created.
     */
    private MainWindow() {
        windowHint(GLFW.GLFW_FOCUSED, "window.hints.focused", true);
        windowHint(GLFW.GLFW_RESIZABLE, "window.hints.resizable", true);
        // TODO: Window visible?
        windowHint(GLFW.GLFW_DECORATED, "window.hints.decorated", true);
        // TODO: Auto iconify?
        windowHint(GLFW.GLFW_FLOATING, "window.hint.floating", false);
        windowHint(GLFW.GLFW_MAXIMIZED, "window.hint.maximized", false);
        windowHint(GLFW.GLFW_CENTER_CURSOR, "window.hint.centerCursor", false);
        windowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, "window.hint.transparent", false);
        // TODO: Focus on show?
        windowHint(GLFW.GLFW_MOUSE_PASSTHROUGH, "window.hint.passthrough", false);
        this.handle = GLFW.glfwCreateWindow(
            ApplicationProperties.getInt("window.size.width", 400),
            ApplicationProperties.getInt("window.size.height", 300),
            ApplicationProperties.getString("window.title", "untitled"),
            MemoryUtil.NULL,
            MemoryUtil.NULL
        );
        if(this.handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwSetKeyCallback(this.handle, (window, key, scancode, action, mods) -> {
            Input.parseEvent(new InputEventKey(key, mods, action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT, action == GLFW.GLFW_REPEAT));
        });
        GLFW.glfwSetMouseButtonCallback(this.handle, (window, button, action, mods) -> {
            Input.parseEvent(new InputEventMouseButton(button, mods, this.mouseX, this.mouseY, action == GLFW.GLFW_PRESS));
        });
        GLFW.glfwSetCursorPosCallback(this.handle, (window, xpos, ypos) -> {
            var mx = (float) xpos - this.mouseX;
            var my = (float) ypos - this.mouseY;
            this.mouseX = (float) xpos;
            this.mouseY = (float) ypos;
            Input.parseEvent(new InputEventMouseMotion(this.mouseX, this.mouseY, mx, my));
        });
        GLFW.glfwSetScrollCallback(this.handle, (window, xoffset, yoffset) -> {
            Input.parseEvent(new InputEventScroll((float) xoffset, (float) yoffset));
        });
        GLFW.glfwMakeContextCurrent(this.handle);
        // TODO: GLFW.glfwGetKeyName()
    }

    /**
     * Makes the window visible if it was previously hidden.
     *
     * @see GLFW#glfwShowWindow(long)
     */
    public void show() {
        GLFW.glfwShowWindow(this.handle);
    }

    public void hide() {
        GLFW.glfwHideWindow(this.handle);
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

    public Vec2i getSize() {
        try(var memoryStack = MemoryStack.stackPush()) {
            var width = memoryStack.mallocInt(1);
            var height = memoryStack.mallocInt(1);
            GLFW.glfwGetWindowSize(this.handle, width, height);
            return new Vec2i(width.get(0), height.get(0));
        }
    }

    public void setPosition(int x, int y) {
        GLFW.glfwSetWindowPos(this.handle, x, y);
    }

    public void setCursorPosition(float x, float y) {
        GLFW.glfwSetCursorPos(this.handle, x, y);
    }

    public Vec2f getCursorPosition() {
        try(var memoryStack = MemoryStack.stackPush()) {
            var x = memoryStack.mallocDouble(1);
            var y = memoryStack.mallocDouble(1);
            GLFW.glfwGetCursorPos(this.handle, x, y);
            return new Vec2f((float) x.get(0), (float) y.get(0));
        }
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