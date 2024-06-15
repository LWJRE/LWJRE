package io.github.hexagonnico.glfw;

import io.github.hexagonnico.core.DisplayApi;
import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;

/**
 * GLFW implementation of the {@link DisplayApi}.
 */
public class GlfwApi implements DisplayApi {

    @Override
    public void setWindowTitle(String title) {
        MainWindow.getInstance().setTitle(title);
    }

    @Override
    public void setWindowSize(int width, int height) {
        MainWindow.getInstance().setSize(width, height);
    }

    @Override
    public void setWindowPosition(int x, int y) {
        MainWindow.getInstance().setPosition(x, y);
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
    public Vec2i getWindowSize() {
        return MainWindow.getInstance().getSize();
    }

    @Override
    public void setCursorPosition(float x, float y) {
        MainWindow.getInstance().setCursorPosition(x, y);
    }

    @Override
    public Vec2f getCursorPosition() {
        return MainWindow.getInstance().getCursorPosition();
    }
}
