package io.github.hexagonnico.glfw;

import io.github.hexagonnico.core.DisplayAPI;

public class DisplayImplementation implements DisplayAPI {

    @Override
    public void setWindowTitle(String title) {
        MainWindow.getInstance().setTitle(title);
    }

    @Override
    public void setWindowSize(int width, int height) {
        MainWindow.getInstance().setSize(width, height);
    }
}
