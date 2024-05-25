package io.github.hexagonnico.glfw;

import io.github.hexagonnico.core.DisplayApi;

/**
 * GLFW implementation of the {@link DisplayApi}.
 */
public class DisplayImplementation implements DisplayApi {

    @Override
    public void setWindowTitle(String title) {
        MainWindow.getInstance().setTitle(title);
    }

    @Override
    public void setWindowSize(int width, int height) {
        MainWindow.getInstance().setSize(width, height);
    }
}
