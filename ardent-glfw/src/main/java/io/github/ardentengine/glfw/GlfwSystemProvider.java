package io.github.ardentengine.glfw;

import io.github.ardentengine.core.DisplaySystem;
import io.github.ardentengine.core.DisplaySystemProvider;
import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.EngineSystemProvider;

public class GlfwSystemProvider implements EngineSystemProvider, DisplaySystemProvider {

    @Override
    public DisplaySystem getDisplaySystem() {
        return GlfwSystem.getInstance();
    }

    @Override
    public EngineSystem getEngineSystem() {
        return GlfwSystem.getInstance();
    }
}