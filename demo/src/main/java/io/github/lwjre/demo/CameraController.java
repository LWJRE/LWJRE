package io.github.lwjre.demo;

import io.github.lwjre.engine.input.Keyboard;
import io.github.lwjre.engine.nodes.Camera3D;

public class CameraController extends Camera3D {

	@Override
	protected void onUpdate(float delta) {
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			this.position = this.position.plus(0.0f, delta * 50, 0.0f);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT_SHIFT)) {
			this.position = this.position.minus(0.0f, delta * 50, 0.0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			this.rotateDegrees(0.0f, delta * 20.0f, 0.0f);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			this.rotateDegrees(0.0f, delta * -20.0f, 0.0f);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.rotateDegrees(delta * 20.0f, 0.0f, 0.0f);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.rotateDegrees(delta * -20.0f, 0.0f, 0.0f);
		}
		super.onUpdate(delta);
	}
}
