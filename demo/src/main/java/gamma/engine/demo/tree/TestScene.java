package gamma.engine.demo.tree;

import gamma.engine.core.input.Input;
import gamma.engine.core.node.Transform3D;

public class TestScene extends Transform3D {

	@Override
	protected void onUpdate(float delta) {
		int horizontal = Input.getAxis("right", "left");
		int vertical = Input.getAxis("up", "down");
		this.position = this.position.plus(horizontal * 0.2f, 0.0f, vertical * 0.2f);
		if(Input.isActionPressed("test"))
			System.out.println("Pew");
		super.onUpdate(delta);
	}
}
