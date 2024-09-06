package io.github.ardentengine.demo;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.RenderingServer;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.input.InputEventKey;
import io.github.ardentengine.core.input.Keyboard;
import io.github.ardentengine.core.scene.Node2D;

public class ExampleScene2D extends Node2D {

    @Override
    protected void onEnter() {
        RenderingServer.getInstance().setDefaultClearColor(0.5f, 0.5f, 1.0f);
    }

    @Override
    protected void onInput(InputEvent event) {
        if(event.isPressed() && event instanceof InputEventKey inputEventKey) {
            if(inputEventKey.keyCode() == Keyboard.KEY_ESCAPE) {
                Application.quit();
            } else if(inputEventKey.keyCode() == Keyboard.KEY_PAGE_UP || inputEventKey.keyCode() == Keyboard.KEY_PAGE_DOWN) {
                this.sceneTree().changeScene("scenes/example_scene_3d.yaml");
            }
        }
    }
}