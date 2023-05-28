package gamma.engine.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

public class TestKinematicBody3D {

	@Test
	public void testVelocity() {
		KinematicBody3D kinematicBody = new KinematicBody3D();
		kinematicBody.velocity = new Vec3f(1.0f, 1.0f, 0.0f);
		kinematicBody.acceleration = Vec3f.Zero();
		Assertions.assertEquals(Vec3f.Zero(), kinematicBody.position);
		kinematicBody.process(0.1f);
		Assertions.assertEquals(new Vec3f(0.1f, 0.1f, 0.0f), kinematicBody.position);
	}

	@Test
	public void testAcceleration() {
		KinematicBody3D kinematicBody = new KinematicBody3D();
		Assertions.assertEquals(Vec3f.Zero(), kinematicBody.position);
		Assertions.assertEquals(Vec3f.Zero(), kinematicBody.velocity);
		kinematicBody.process(2.0f);
		Assertions.assertEquals(new Vec3f(0.0f, -9.81f * 2, 0.0f), kinematicBody.velocity);
		Assertions.assertEquals(new Vec3f(0.0f, -9.81f * 4, 0.0f), kinematicBody.position);
	}
}
