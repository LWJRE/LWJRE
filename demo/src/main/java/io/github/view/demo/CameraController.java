package io.github.view.demo;

import io.github.view.core.Camera3D;
import io.github.view.core.Position3D;
import io.github.view.input.Keyboard;
import io.github.view.math.Vector3;
import io.github.view.scene.SceneObject;
import org.lwjgl.glfw.GLFW;

public class CameraController extends Camera3D {

	private final Position3D position;

	private float speed = 0.5f;

	public CameraController(SceneObject object) {
		super(object);
		this.position = this.object.getScript(Position3D.class);
	}

	@Override
	public void onUpdate(float time) {
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE))
			this.position.translate(Vector3.UP.multipliedBy(this.speed * time));
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT))
			this.position.translate(Vector3.DOWN.multipliedBy(this.speed * time));
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_W))
			this.position.translate(Vector3.FORWARD.multipliedBy(this.speed * time));
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_A))
			this.position.translate(Vector3.LEFT.multipliedBy(this.speed * time));
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_S))
			this.position.translate(Vector3.BACKWARDS.multipliedBy(this.speed * time));
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_D))
			this.position.translate(Vector3.RIGHT.multipliedBy(this.speed * time));
		super.onUpdate(time);
	}
}
