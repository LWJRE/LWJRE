package io.github.hexagonnico.core;

import java.util.ServiceLoader;

public final class DisplayServer {

    private static final DisplayAPI API = ServiceLoader.load(DisplayAPI.class).findFirst().orElse(null);

    public static void setWindowTitle(String title) {
        if(API != null) {
            API.setWindowTitle(title);
        }
    }

    public static void setWindowSize(int width, int height) {
        if(API != null) {
            API.setWindowSize(width, height);
        }
    }
}
