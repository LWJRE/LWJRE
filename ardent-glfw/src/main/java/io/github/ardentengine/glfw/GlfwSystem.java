package io.github.ardentengine.glfw;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.DisplaySystem;
import io.github.ardentengine.core.input.*;
import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * GLFW implementation of the display system.
 */
public class GlfwSystem extends DisplaySystem {

    /** Singleton instance. */
    private static GlfwSystem instance;

    /**
     * Returns the singleton instance.
     *
     * @return The singleton instance.
     */
    public static synchronized GlfwSystem getInstance() {
        return instance == null ? instance = new GlfwSystem() : instance;
    }

    // TODO: Create a Window class to add support for multiple windows

    /** The main window. */
    private long window;

    /** Mouse x position. */
    private float mouseX = 0.0f;
    /** Mouse y position. */
    private float mouseY = 0.0f;

    /**
     * Private constructor for singleton class.
     */
    private GlfwSystem() {

    }

    @Override
    public void setWindowTitle(String title) {
        GLFW.glfwSetWindowTitle(this.window, title);
    }

    @Override
    public void setWindowSize(int width, int height) {
        GLFW.glfwSetWindowSize(this.window, width, height);
    }

    @Override
    public Vec2i getWindowSize() {
        try(var memoryStack = MemoryStack.stackPush()) {
            var width = memoryStack.mallocInt(1);
            var height = memoryStack.mallocInt(1);
            GLFW.glfwGetWindowSize(this.window, width, height);
            return new Vec2i(width.get(0), height.get(0));
        }
    }

    @Override
    public void setWindowPosition(int x, int y) {
        GLFW.glfwSetWindowPos(this.window, x, y);
    }

    @Override
    public void showWindow() {
        GLFW.glfwShowWindow(this.window);
    }

    @Override
    public void hideWindow() {
        GLFW.glfwHideWindow(this.window);
    }

    @Override
    public void setCursorPosition(float x, float y) {
        GLFW.glfwSetCursorPos(this.window, x, y);
    }

    @Override
    public Vec2f getCursorPosition() {
        try(var memoryStack = MemoryStack.stackPush()) {
            var x = memoryStack.mallocDouble(1);
            var y = memoryStack.mallocDouble(1);
            GLFW.glfwGetCursorPos(this.window, x, y);
            return new Vec2f((float) x.get(0), (float) y.get(0));
        }
    }

    @Override
    protected void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // FIXME: Reimplement window hints
        this.window = GLFW.glfwCreateWindow(
            ApplicationProperties.getInt("window.size.width", 400),
            ApplicationProperties.getInt("window.size.height", 300),
            ApplicationProperties.getString("window.title", "untitled"),
            MemoryUtil.NULL,
            MemoryUtil.NULL
        );
        if(this.window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
            Input.parseEvent(new InputEventKey(key, mods, action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT, action == GLFW.GLFW_REPEAT));
        });
        GLFW.glfwSetMouseButtonCallback(this.window, (window, button, action, mods) -> {
            Input.parseEvent(new InputEventMouseButton(button, mods, this.mouseX, this.mouseY, action == GLFW.GLFW_PRESS));
        });
        GLFW.glfwSetCursorPosCallback(this.window, (window, xpos, ypos) -> {
            var mx = (float) xpos - this.mouseX;
            var my = (float) ypos - this.mouseY;
            this.mouseX = (float) xpos;
            this.mouseY = (float) ypos;
            Input.parseEvent(new InputEventMouseMotion(this.window, this.mouseY, mx, my));
        });
        GLFW.glfwSetScrollCallback(this.window, (window, xoffset, yoffset) -> {
            Input.parseEvent(new InputEventScroll((float) xoffset, (float) yoffset));
        });
        GLFW.glfwMakeContextCurrent(this.window);
        // TODO: GLFW.glfwGetKeyName()
        this.showWindow();
    }

    @Override
    protected void process() {
        if(!GLFW.glfwWindowShouldClose(this.window)) {
            GLFW.glfwSwapBuffers(this.window);
            GLFW.glfwPollEvents();
        } else {
            Application.quit();
        }
    }

    @Override
    protected void terminate() {
        Callbacks.glfwFreeCallbacks(this.window);
        GLFW.glfwDestroyWindow(this.window);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}