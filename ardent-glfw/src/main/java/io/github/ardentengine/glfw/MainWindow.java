package io.github.ardentengine.glfw;

import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.display.CursorMode;
import io.github.ardentengine.core.display.CursorShape;
import io.github.ardentengine.core.display.WindowMode;
import io.github.ardentengine.core.input.*;
import io.github.ardentengine.core.math.Vector2;
import io.github.ardentengine.core.math.Vector2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import java.util.HashMap;

public class MainWindow {

    // TODO: Create a Window class to add support for multiple windows

    private static MainWindow instance;

    private static final HashMap<CursorShape, Long> CURSORS = new HashMap<>();

    public static synchronized MainWindow getInstance() {
        return instance == null ? instance = new MainWindow() : instance;
    }

    private static long getCursor(CursorShape cursorShape) {
        return CURSORS.computeIfAbsent(cursorShape, shape -> GLFW.glfwCreateStandardCursor(switch (shape) {
            case ARROW -> GLFW.GLFW_ARROW_CURSOR;
            case IBEAM -> GLFW.GLFW_IBEAM_CURSOR;
            case CROSSHAIR -> GLFW.GLFW_CROSSHAIR_CURSOR;
            case POINTING_HAND -> GLFW.GLFW_POINTING_HAND_CURSOR;
            case RESIZE_EW -> GLFW.GLFW_RESIZE_EW_CURSOR;
            case RESIZE_NS -> GLFW.GLFW_RESIZE_NS_CURSOR;
            case RESIZE_NWSE -> GLFW.GLFW_RESIZE_NWSE_CURSOR;
            case RESIZE_NESW -> GLFW.GLFW_RESIZE_NESW_CURSOR;
            case RESIZE_ALL -> GLFW.GLFW_RESIZE_ALL_CURSOR;
            case NOT_ALLOWED -> GLFW.GLFW_NOT_ALLOWED_CURSOR;
        }));
    }

    private final long handle;

    private int xPos;
    private int yPos;

    private int width;
    private int height;

    /** Mouse x position. */
    private float mouseX = 0.0f;
    /** Mouse y position. */
    private float mouseY = 0.0f;

    private MainWindow() {
        // FIXME: Reimplement window hints
        this.handle = GLFW.glfwCreateWindow(
            this.width = ApplicationProperties.getInt("window.size.width", 400),
            this.height = ApplicationProperties.getInt("window.size.height", 300),
            ApplicationProperties.getString("window.title", "untitled"),
            MemoryUtil.NULL,
            MemoryUtil.NULL
        );
        if(this.handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        GLFW.glfwSetKeyCallback(this.handle, (window, key, scancode, action, mods) -> Input.parseEvent(new InputEventKey(
            key,
            mods,
            action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT,
            action == GLFW.GLFW_REPEAT
        )));
        GLFW.glfwSetMouseButtonCallback(this.handle, (window, button, action, mods) -> {
            Input.parseEvent(new InputEventMouseButton(button, mods, this.mouseX, this.mouseY, action == GLFW.GLFW_PRESS));
        });
        GLFW.glfwSetCursorPosCallback(this.handle, (window, xpos, ypos) -> this.cursorPosCallback((float) xpos, (float) ypos));
        GLFW.glfwSetScrollCallback(this.handle, (window, xoffset, yoffset) -> {
            Input.parseEvent(new InputEventScroll((float) xoffset, (float) yoffset));
        });
        GLFW.glfwSetWindowPosCallback(this.handle, (window, xpos, ypos) -> this.windowPosCallback(xpos, ypos));
        GLFW.glfwSetWindowSizeCallback(this.handle, (window, width, height) -> this.windowSizeCallback(width, height));
        GLFW.glfwMakeContextCurrent(this.handle);
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

    public String getTitle() {
        return GLFW.glfwGetWindowTitle(this.handle);
    }

    public void setSize(int width, int height) {
        GLFW.glfwSetWindowSize(this.handle, width, height);
    }

    public Vector2i getSize() {
        return new Vector2i(this.width, this.height);
    }

    private void windowSizeCallback(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(int x, int y) {
        GLFW.glfwSetWindowPos(this.handle, x, y);
    }

    public Vector2i getPosition() {
        return new Vector2i(this.xPos, this.yPos);
    }

    private void windowPosCallback(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    public void setOpacity(float alpha) {
        GLFW.glfwSetWindowOpacity(this.handle, alpha);
    }

    public float getOpacity() {
        return GLFW.glfwGetWindowOpacity(this.handle);
    }

    public void setWindowMode(WindowMode windowMode) {
        if(windowMode != WindowMode.FULLSCREEN && GLFW.glfwGetWindowMonitor(this.handle) != MemoryUtil.NULL) {
            // TODO: These must be saved before entering full screen
            GLFW.glfwSetWindowMonitor(this.handle, MemoryUtil.NULL, 0, 0, 128, 128, 60);
        }
        switch (windowMode) {
            case WINDOWED -> GLFW.glfwRestoreWindow(this.handle);
            case MAXIMIZED -> GLFW.glfwMaximizeWindow(this.handle);
            case MINIMIZED -> GLFW.glfwIconifyWindow(this.handle);
            case FULLSCREEN -> {
                var monitor = GLFW.glfwGetPrimaryMonitor();
                var videoMode = GLFW.glfwGetVideoMode(monitor);
                GLFW.glfwSetWindowMonitor(this.handle, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());
            }
        }
    }

    // TODO: Window icon

    public void grabFocus() {
        GLFW.glfwFocusWindow(this.handle);
    }

    public void requestAttention() {
        GLFW.glfwRequestWindowAttention(this.handle);
    }

    public void setCursorPosition(float x, float y) {
        GLFW.glfwSetCursorPos(this.handle, x, y);
        this.mouseX = x;
        this.mouseY = y;
    }

    public Vector2 getCursorPosition() {
        return new Vector2(this.mouseX, this.mouseY);
    }

    private void cursorPosCallback(float x, float y) {
        var mx = x - this.mouseX;
        var my = y - this.mouseY;
        this.mouseX = x;
        this.mouseY = y;
        Input.parseEvent(new InputEventMouseMotion(this.mouseX, this.mouseY, mx, my));
    }

    public void setCursorMode(CursorMode cursorMode) {
        GLFW.glfwSetInputMode(this.handle, GLFW.GLFW_CURSOR, switch (cursorMode) {
            case VISIBLE -> GLFW.GLFW_CURSOR_NORMAL;
            case HIDDEN -> GLFW.GLFW_CURSOR_HIDDEN;
            case CAPTURED -> GLFW.GLFW_CURSOR_DISABLED;
            case CONFINED -> GLFW.GLFW_CURSOR_CAPTURED;
        });
    }

    public CursorMode getCursorMode() {
        return switch(GLFW.glfwGetInputMode(this.handle, GLFW.GLFW_CURSOR)) {
            case GLFW.GLFW_CURSOR_NORMAL -> CursorMode.VISIBLE;
            case GLFW.GLFW_CURSOR_HIDDEN -> CursorMode.HIDDEN;
            case GLFW.GLFW_CURSOR_DISABLED -> CursorMode.CAPTURED;
            case GLFW.GLFW_CURSOR_CAPTURED -> CursorMode.CONFINED;
            default -> throw new RuntimeException(); // TODO: Can this happen?
        };
    }

    public void setCursorShape(CursorShape cursorShape) {
        GLFW.glfwSetCursor(this.handle, getCursor(cursorShape));
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