package io.github.hexagonnico.demo;

import io.github.hexagonnico.core.rendering.RenderingServer;
import io.github.hexagonnico.core.scene.Node2D;

public class TestNode extends Node2D {

    @Override
    protected void onEnter() {
        RenderingServer.setDefaultClearColor(0.5f, 0.5f, 1.0f);
    }
}
