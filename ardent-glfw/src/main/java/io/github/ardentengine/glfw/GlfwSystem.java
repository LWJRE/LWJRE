package io.github.ardentengine.glfw;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.EngineSystem;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class GlfwSystem extends EngineSystem {

    @Override
    protected void initialize() {
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        // FIXME: Reimplement window hints
        MainWindow.getInstance().show();
    }

    @Override
    protected void process() {
        if(!MainWindow.getInstance().shouldClose()) {
            MainWindow.getInstance().update();
            GLFW.glfwPollEvents();
        } else {
            Application.quit();
        }
    }

    @Override
    protected void terminate() {
        MainWindow.getInstance().destroy();
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    @Override
    protected int priority() {
        return 0;
    }
}