package io.github.ardentengine.demo;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.RenderingServer;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.input.InputEventKey;
import io.github.ardentengine.core.input.Keyboard;
import io.github.ardentengine.core.scene.Node3D;

public class ExampleScene3D extends Node3D {

    @Override
    protected void onEnter() {
        RenderingServer.getInstance().setDefaultClearColor(0.3f, 0.3f, 0.3f);
    }

    @Override
    protected void onInput(InputEvent event) {
        if(event.isPressed() && event instanceof InputEventKey inputEventKey) {
            if(inputEventKey.keyCode() == Keyboard.KEY_ESCAPE) {
                Application.quit();
            } else if(inputEventKey.keyCode() == Keyboard.KEY_PAGE_UP || inputEventKey.keyCode() == Keyboard.KEY_PAGE_DOWN) {
                this.sceneTree().changeScene("scenes/example_scene_2d.yaml");
            }
        }
    }
}