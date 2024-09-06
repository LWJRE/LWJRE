package io.github.ardentengine.glfw;

import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.input.*;
import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class MainWindow {

    // TODO: Create a Window class to add support for multiple windows

    private static MainWindow instance;

    public static synchronized MainWindow getInstance() {
        return instance == null ? instance = new MainWindow() : instance;
    }

    private final long handle;

    /** Mouse x position. */
    private float mouseX = 0.0f;
    /** Mouse y position. */
    private float mouseY = 0.0f;

    private MainWindow() {
        // FIXME: Reimplement window hints
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
            Input.parseEvent(new InputEventMouseMotion(this.handle, this.mouseY, mx, my));
        });
        GLFW.glfwSetScrollCallback(this.handle, (window, xoffset, yoffset) -> {
            Input.parseEvent(new InputEventScroll((float) xoffset, (float) yoffset));
        });
        GLFW.glfwMakeContextCurrent(this.handle);
        // TODO: GLFW.glfwGetKeyName()
    }

    public void show() {
        GLFW.glfwShowWindow(this.handle);
    }

    public void hide() {
        GLFW.glfwHideWindow(this.handle);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(this.handle, title);
    }

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

    public Vec2i getPosition() {
        try(var memoryStack = MemoryStack.stackPush()) {
            var x = memoryStack.mallocInt(1);
            var y = memoryStack.mallocInt(1);
            GLFW.glfwGetWindowPos(this.handle, x, y);
            return new Vec2i(x.get(0), y.get(0));
        }
    }

    public void setCursorPosition(float x, float y) {
        // TODO: GLFW_CURSOR_DISABLED can be used to implement camera controls
        GLFW.glfwSetCursorPos(this.handle, x, y);
    }

    public Vec2f getCursorPosition() {
        try(var memoryStack = MemoryStack.stackPush()) {
            var x = memoryStack.mallocDouble(1);
            var y = memoryStack.mallocDouble(1);
            GLFW.glfwGetCursorPos(this.handle, x, y);
            return new Vec2f((float) Math.floor(x.get(0)), (float) Math.floor(y.get(0)));
        }
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(this.handle);
    }

    public void update() {
        GLFW.glfwSwapBuffers(this.handle);
    }

    public void destroy() {
        Callbacks.glfwFreeCallbacks(this.handle);
        GLFW.glfwDestroyWindow(this.handle);
    }
}