package gamma.demo;

import gamma.engine.core.input.Keyboard;
import gamma.engine.core.nodes.Camera3D;

public class CameraController extends Camera3D {

	@Override
	protected void onUpdate(float delta) {
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.position = this.position.plus(0.0f, delta * 50, 0.0f);
		} else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			this.position = this.position.minus(0.0f, delta * 50, 0.0f);
		}
		super.onUpdate(delta);
	}
}
