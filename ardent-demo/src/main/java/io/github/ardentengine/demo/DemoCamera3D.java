package io.github.ardentengine.demo;

import io.github.ardentengine.core.display.CursorMode;
import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.input.Input;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.input.InputEventMouseMotion;
import io.github.ardentengine.core.scene.Camera3D;

public class DemoCamera3D extends Camera3D {

    public float speed = 1.0f;

    @Override
    protected void onEnter() {
        DisplayServer.getInstance().setCursorMode(CursorMode.CAPTURED);
    }

    @Override
    public void onUpdate(float delta) {
        // TODO: Find out why this is inverted
        var horizontalVelocity = Input.getVector("up", "down", "left", "right").multipliedBy(this.speed).rotated(-this.yaw());
        this.translate(horizontalVelocity.x() * delta, 0.0f, -horizontalVelocity.y() * delta);
        if(Input.isActionPressed("jump")) {
            this.translate(0.0f, this.speed * delta, 0.0f);
        }
        if(Input.isActionPressed("run")) {
            this.translate(0.0f, -this.speed * delta, 0.0f);
        }
        super.onUpdate(delta);
    }

    @Override
    public void onInput(InputEvent event) {
        if(event instanceof InputEventMouseMotion mouseMotion) {
            this.rotate(mouseMotion.y() * 0.01f, mouseMotion.x() * 0.01f, 0.0f);
            if(this.pitch() > Math.PI / 2.0) {
                this.setPitch((float) (Math.PI / 2.0));
            } else if(this.pitch() < -Math.PI / 2.0) {
                this.setPitch((float) (-Math.PI / 2.0));
            }
        }
    }

    @Override
    protected void onExit() {
        DisplayServer.getInstance().setCursorMode(CursorMode.VISIBLE);
    }
}