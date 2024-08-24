package io.github.ardentengine.opengl;

import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.EngineSystemProvider;
import io.github.ardentengine.core.RenderingSystem;
import io.github.ardentengine.core.RenderingSystemProvider;

public class OpenGLSystemProvider implements EngineSystemProvider, RenderingSystemProvider {

    @Override
    public EngineSystem getEngineSystem() {
        return OpenGLSystem.getInstance();
    }

    @Override
    public RenderingSystem getRenderingSystem() {
        return OpenGLSystem.getInstance();
    }
}