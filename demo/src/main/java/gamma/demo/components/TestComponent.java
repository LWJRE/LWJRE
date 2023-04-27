package gamma.demo.components;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.components.Transform3D;
import gamma.engine.scene.Component;
import gamma.engine.scene.Entity;
import gamma.engine.scene.EntityResource;
import vecmatlib.vector.Vec3f;

import java.util.Random;

public class TestComponent extends Component {

	@EditorVariable
	public int count = 1000;

	@Override
	protected void onStart() {
		Random random = new Random();
		for(int i = 0; i < this.count; i++) {
			Entity dragon = EntityResource.getOrLoad("scenes/dragon.yaml").instance();
			dragon.getComponent(Transform3D.class).ifPresent(transform -> transform.position = new Vec3f(
					random.nextFloat(-100.0f, 100.0f),
					random.nextFloat(-100.0f, 100.0f),
					random.nextFloat(-100.0f, 100.0f)
			));
			this.entity().addChild(dragon);
		}
		super.onStart();
	}
}
