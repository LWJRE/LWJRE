package io.github.ardentengine.glfw;

import io.github.ardentengine.core.display.CursorMode;
import io.github.ardentengine.core.display.CursorShape;
import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.display.WindowMode;
import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

/**
 * GLFW implementation of the display server.
 */
public class GlfwDisplayServer extends DisplayServer {

    @Override
    public void setWindowTitle(String title) {
        MainWindow.getInstance().setTitle(title);
    }

    @Override
    public String getWindowTitle() {
        return MainWindow.getInstance().getTitle();
    }

    @Override
    public void setWindowSize(int width, int height) {
        MainWindow.getInstance().setSize(width, height);
    }

    @Override
    public Vec2i getWindowSize() {
        return MainWindow.getInstance().getSize();
    }

    @Override
    public void setWindowPosition(int x, int y) {
        MainWindow.getInstance().setPosition(x, y);
    }

    @Override
    public Vec2i getWindowPosition() {
        return MainWindow.getInstance().getPosition();
    }

    @Override
    public void setOpacity(float alpha) {
        MainWindow.getInstance().setOpacity(alpha);
    }

    @Override
    public float getOpacity() {
        return MainWindow.getInstance().getOpacity();
    }

    @Override
    public void setWindowMode(WindowMode windowMode) {
        MainWindow.getInstance().setWindowMode(windowMode);
    }

    @Override
    public void focusMainWindow() {
        MainWindow.getInstance().grabFocus();
    }

    @Override
    public void requestAttentionOnMainWindow() {
        MainWindow.getInstance().requestAttention();
    }

    @Override
    public void showWindow() {
        MainWindow.getInstance().show();
    }

    @Override
    public void hideWindow() {
        MainWindow.getInstance().hide();
    }

    @Override
    public void setCursorPosition(float x, float y) {
        MainWindow.getInstance().setCursorPosition(x, y);
    }

    @Override
    public Vec2f getCursorPosition() {
        return MainWindow.getInstance().getCursorPosition();
    }

    @Override
    public void setCursorMode(CursorMode cursorMode) {
        MainWindow.getInstance().setCursorMode(cursorMode);
    }

    @Override
    public CursorMode getCursorMode() {
        return MainWindow.getInstance().getCursorMode();
    }

    @Override
    public void setCursorShape(CursorShape cursorShape) {
        MainWindow.getInstance().setCursorShape(cursorShape);
    }

    @Override
    public void setVSync(boolean enabled) {
        GLFW.glfwSwapInterval(enabled ? 1 : 0);
    }

    @Override
    public String getKeyLabel(int key) {
        var keyName = GLFW.glfwGetKeyName(key, 0);
        return keyName == null ? "" : keyName;
    }

    @Override
    public void setClipboard(CharSequence clipboard) {
        GLFW.glfwSetClipboardString(MemoryUtil.NULL, clipboard);
    }

    @Override
    public String getClipboard() {
        return GLFW.glfwGetClipboardString(MemoryUtil.NULL);
    }
}