package gamma.engine.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.hexagonnico.vecmatlib.vector.Vec2f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

public class TestDynamicBody3D {

	@Test
	public void testApplyForce() {
		DynamicBody3D dynamicBody = new DynamicBody3D();
		dynamicBody.acceleration = Vec3f.Zero();
		dynamicBody.applyForce(1.0f, 0.0f, 1.0f);
		dynamicBody.applyForce(1.0f, 1.0f);
		dynamicBody.applyForce(new Vec2f(1.0f, 0.0f));
		Assertions.assertEquals(new Vec3f(3.0f, 1.0f, 1.0f), dynamicBody.resultantForce());
	}

	@Test
	public void testAcceleration() {
		DynamicBody3D dynamicBody = new DynamicBody3D();
		dynamicBody.acceleration = Vec3f.Zero();
		dynamicBody.mass = 10.0f;
		dynamicBody.applyForce(1.0f, 0.0f, 1.0f);
		dynamicBody.process(0.1f);
		Assertions.assertEquals(new Vec3f(0.1f, 0.0f, 0.1f), dynamicBody.acceleration);
	}

	@Test
	public void testImpulse() {
		DynamicBody3D dynamicBody = new DynamicBody3D();
		dynamicBody.acceleration = Vec3f.Zero();
		dynamicBody.velocity = new Vec3f(1.0f, 0.0f, 1.0f);
		dynamicBody.mass = 10.0f;
		dynamicBody.applyImpulse(10.0f, 0.0f, 0.0f);
		Assertions.assertEquals(new Vec3f(2.0f, 0.0f, 1.0f), dynamicBody.velocity);
		dynamicBody.applyImpulse(0.0f, 10.0f);
		Assertions.assertEquals(new Vec3f(2.0f, 1.0f, 1.0f), dynamicBody.velocity);
	}
}
