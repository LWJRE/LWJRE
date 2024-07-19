package io.github.ardentengine.demo;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.input.InputEventKey;
import io.github.ardentengine.core.input.Keyboard;
import io.github.ardentengine.core.rendering.RenderingServer;
import io.github.ardentengine.core.scene.Node2D;

public class TestNode extends Node2D {

    @Override
    protected void onEnter() {
        RenderingServer.setDefaultClearColor(0.5f, 0.5f, 1.0f);
    }

    @Override
    protected void onInput(InputEvent event) {
        if(event instanceof InputEventKey inputEventKey) {
            if(inputEventKey.keyCode() == Keyboard.KEY_ESCAPE) {
                Application.quit();
            }
        }
    }
}