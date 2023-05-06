package gamma.demo.components;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.tree.DynamicBody3D;
import gamma.engine.tree.Node;
import gamma.engine.tree.Node3D;
import gamma.engine.tree.NodeResource;
import vecmatlib.vector.Vec3f;

import java.util.Random;

public class TestNode extends Node3D {

	@EditorVariable
	public int count = 1000;

	@Override
	protected void onEnter() {
		Random random = new Random();
		for(int i = 0; i < this.count; i++) {
			Node node = NodeResource.getOrLoad("scenes/dragon.yaml").instantiate();
			if(node instanceof DynamicBody3D dragon) {
				dragon.position = new Vec3f(
						random.nextFloat(-100.0f, 100.0f),
						random.nextFloat(-100.0f, 100.0f),
						random.nextFloat(-100.0f, 100.0f)
				);
			}
			this.getParent().queueChild(node);
		}
		super.onEnter();
	}
}
