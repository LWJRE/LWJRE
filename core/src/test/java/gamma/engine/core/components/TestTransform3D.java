package gamma.engine.core.components;

import gamma.engine.core.scene.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vecmatlib.vector.Vec3f;

public class TestTransform3D {

	@Test
	public void testLocalPosition() {
		Transform3D transform = new Transform3D();
		transform.position = new Vec3f(0.5f, 0.5f, 0.5f);
		assertEqualsApprox(new Vec3f(0.5f, 0.5f, 0.5f), transform.localPosition());
	}

	@Test
	public void testGlobalPosition() {
		Entity parentEntity = new Entity();
		Entity childEntity = new Entity();
		parentEntity.addChild(childEntity);
		Transform3D parentTransform = new Transform3D();
		Transform3D childTransform = new Transform3D();
		parentEntity.addComponent(parentTransform);
		childEntity.addComponent(childTransform);
		parentTransform.position = new Vec3f(1.0f, 0.0f, 0.5f);
		childTransform.position = new Vec3f(2.0f, 1.0f, 2.0f);
		assertEqualsApprox(new Vec3f(3.0f, 1.0f, 2.5f), childTransform.globalPosition());
	}

	@Test
	public void testRotationDegrees() {
		Transform3D transform = new Transform3D();
		transform.setRotationDegrees(new Vec3f(90.0f, 45.0f, 30.0f));
		Assertions.assertEquals(new Vec3f((float) Math.toRadians(90.0f), (float) Math.toRadians(45.0f), (float) Math.toRadians(30.0f)), transform.rotation);
	}

	@Test
	public void testRotationRadians() {
		Transform3D transform = new Transform3D();
		transform.rotation = new Vec3f((float) Math.toRadians(90.0f), (float) Math.toRadians(45.0f), (float) Math.toRadians(30.0f));
		Assertions.assertEquals(new Vec3f(90.0f, 45.0f, 30.0f), transform.rotationDegrees());
	}

	@Test
	public void testGlobalPositionRotated() {
		Entity parentEntity = new Entity();
		Entity childEntity = new Entity();
		parentEntity.addChild(childEntity);
		Transform3D parentTransform = new Transform3D();
		Transform3D childTransform = new Transform3D();
		parentEntity.addComponent(parentTransform);
		childEntity.addComponent(childTransform);
		childTransform.position = new Vec3f(2.0f, 0.0f, 0.0f);
		parentTransform.setRotationDegrees(0.0f, 90.0f, 0.0f);
		assertEqualsApprox(new Vec3f(0.0f, 0.0f, 2.0f), childTransform.globalPosition());
	}

	@Test
	public void testRotateDegrees() {
		Transform3D transform = new Transform3D();
		transform.setRotationDegrees(10.0f, 10.0f, 10.0f);
		transform.rotateDegrees(new Vec3f(10.f, 10.0f, 10.0f));
		assertEqualsApprox(new Vec3f(20.0f, 20.0f, 20.0f), transform.rotationDegrees());
	}

	@Test
	public void testGlobalPositionScaled() {
		Entity parentEntity = new Entity();
		Entity childEntity = new Entity();
		parentEntity.addChild(childEntity);
		Transform3D parentTransform = new Transform3D();
		Transform3D childTransform = new Transform3D();
		parentEntity.addComponent(parentTransform);
		childEntity.addComponent(childTransform);
		childTransform.position = new Vec3f(1.0f, 2.0f, 3.0f);
		parentTransform.scale = new Vec3f(2.0f, 2.5f, 3.0f);
		Assertions.assertEquals(new Vec3f(2.0f, 5.0f, 9.0f), childTransform.globalPosition());
	}

	private static void assertEqualsApprox(Vec3f expected, Vec3f actual) {
		Assertions.assertEquals(expected.x(), actual.x(), 0.000001f);
		Assertions.assertEquals(expected.y(), actual.y(), 0.000001f);
		Assertions.assertEquals(expected.z(), actual.z(), 0.000001f);
	}
}