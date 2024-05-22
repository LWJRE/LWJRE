package io.github.hexagonnico.glfw;

import io.github.hexagonnico.core.Application;
import io.github.hexagonnico.core.EngineSystem;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class DisplaySystem implements EngineSystem {

    @Override
    public void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        MainWindow.getInstance().show();
    }

    @Override
    public void process() {
        if(!MainWindow.getInstance().isCloseRequested()) {
            MainWindow.getInstance().update();
            GLFW.glfwPollEvents();
        } else {
            Application.quit();
        }
    }

    @Override
    public void terminate() {
        MainWindow.getInstance().destroy();
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
