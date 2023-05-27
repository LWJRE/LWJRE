package gamma.engine.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vecmatlib.vector.Vec3f;

public class TestRigidBody3D {

	@Test
	public void testForceTorque() {
		RigidBody3D rigidBody = new RigidBody3D();
		rigidBody.applyForce(0.0f, 10.0f, 0.0f, Vec3f.Right());
		Assertions.assertEquals(new Vec3f(0.0f, 0.0f, 10.0f), rigidBody.torque());
	}

	@Test
	public void testPositionedForce() {
		RigidBody3D rigidBodyA = new RigidBody3D();
		RigidBody3D rigidBodyB = new RigidBody3D();
		rigidBodyA.applyForce(new Vec3f(0.0f, 10.0f, 0.0f), 1.0f, 0.0f, 0.0f);
		rigidBodyB.applyForce(0.0f, 10.0f, 0.0f, 1.0f, 0.0f, 0.0f);
		Assertions.assertEquals(rigidBodyA.torque(), rigidBodyB.torque());
	}

	@Test
	public void testImpulseVelocity() {
		RigidBody3D rigidBody = new RigidBody3D();
		rigidBody.inertia = new Vec3f(5.0f, 5.0f, 5.0f);
		rigidBody.applyImpulse(0.0f, 10.0f, 0.0f, Vec3f.Right());
		Assertions.assertEquals(new Vec3f(0.0f, 0.0f, 2.0f), rigidBody.angularVelocity);
	}

	@Test
	public void testPositionedImpulse() {
		RigidBody3D rigidBodyA = new RigidBody3D();
		RigidBody3D rigidBodyB = new RigidBody3D();
		rigidBodyA.applyImpulse(new Vec3f(0.0f, 10.0f, 0.0f), 1.0f, 0.0f, 0.0f);
		rigidBodyB.applyImpulse(0.0f, 10.0f, 0.0f, 1.0f, 0.0f, 0.0f);
		Assertions.assertEquals(rigidBodyA.angularVelocity, rigidBodyB.angularVelocity);
	}

	@Test
	public void testApplyTorque() {
		RigidBody3D rigidBody = new RigidBody3D();
		rigidBody.inertia = new Vec3f(10.0f, 10.0f, 10.0f);
		rigidBody.applyTorque(1.0f, 0.0f, 1.0f);
		rigidBody.applyTorque(1.0f, 1.0f, 0.0f);
		Assertions.assertEquals(new Vec3f(2.0f, 1.0f, 1.0f), rigidBody.torque());
		rigidBody.process(0.1f);
		Assertions.assertEquals(new Vec3f(0.2f, 0.1f, 0.1f), rigidBody.angularAcceleration);
	}
}
