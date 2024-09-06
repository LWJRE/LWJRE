package io.github.ardentengine.demo;

import io.github.ardentengine.core.input.Input;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.input.InputEventMouseMotion;
import io.github.ardentengine.core.input.Keyboard;
import io.github.ardentengine.core.scene.Camera3D;

public class DemoCamera3D extends Camera3D {

    public float speed = 1.0f;

    @Override
    public void onUpdate(float delta) {
        var horizontalVelocity = Input.getVector("up", "down", "left", "right").multipliedBy(this.speed).rotated(-this.rotation().y());
        this.translate(horizontalVelocity.x() * delta, 0.0f, -horizontalVelocity.y() * delta);
        if(Input.isKeyPressed(Keyboard.KEY_SPACE)) {
            this.translate(0.0f, this.speed * delta, 0.0f);
        }
        if(Input.isKeyPressed(Keyboard.KEY_LEFT_SHIFT)) {
            this.translate(0.0f, -this.speed * delta, 0.0f);
        }
        super.onUpdate(delta);
    }

    @Override
    public void onInput(InputEvent event) {
        if(event instanceof InputEventMouseMotion mouseMotion) {
            this.rotate(mouseMotion.motion().y() * 0.01f, mouseMotion.motion().x() * 0.01f, 0.0f);
            if(this.rotation().x() > Math.PI / 2.0) {
                this.setRotation((float) (Math.PI / 2.0), this.rotation().y(), this.rotation().z());
            } else if(this.rotation().x() < -Math.PI / 2.0) {
                this.setRotation((float) (-Math.PI / 2.0), this.rotation().y(), this.rotation().z());
            }
        }
    }
}