package gamma.engine.core.tree;

import gamma.engine.core.node.Transform3D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vecmatlib.vector.Vec3f;

public class TestTransform3D {

	@Test
	public void testGlobalPosition() {
		Transform3D parent = new Transform3D();
		Transform3D child = new Transform3D();
		parent.addChild(child);
		parent.position = new Vec3f(1.0f, 2.0f, 3.0f);
		child.position = new Vec3f(2.0f, 3.0f, 1.0f);
		Assertions.assertEquals(new Vec3f(3.0f, 5.0f, 4.0f), child.globalPosition());
	}

	@Test
	public void testRotationDegrees() {
		Transform3D transform = new Transform3D();
		transform.setRotationDegrees(90.0f, 45.0f, 30.0f);
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
		Transform3D parent = new Transform3D();
		Transform3D child = new Transform3D();
		parent.addChild(child);
		child.position = new Vec3f(2.0f, 0.0f, 0.0f);
		parent.setRotationDegrees(0.0f, 90.0f, 0.0f);
		assertEqualsApprox(new Vec3f(0.0f, 0.0f, 2.0f), child.globalPosition());
	}

	@Test
	public void testGlobalPositionScaled() {
		Transform3D parent = new Transform3D();
		Transform3D child = new Transform3D();
		parent.addChild(child);
		child.position = new Vec3f(1.0f, 2.0f, 3.0f);
		parent.scale = new Vec3f(2.0f, 2.5f, 3.0f);
		Assertions.assertEquals(new Vec3f(2.0f, 5.0f, 9.0f), child.globalPosition());
	}

	private static void assertEqualsApprox(Vec3f expected, Vec3f actual) {
		Assertions.assertEquals(expected.x(), actual.x(), 0.000001f);
		Assertions.assertEquals(expected.y(), actual.y(), 0.000001f);
		Assertions.assertEquals(expected.z(), actual.z(), 0.000001f);
	}
}
