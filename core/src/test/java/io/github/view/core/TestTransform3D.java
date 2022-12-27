package io.github.view.core;

import io.github.view.math.Vector3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTransform3D {

	@Test
	public void testLocalPosition() {
		Transform3D transform = new Transform3D();
		transform.setPosition(2.0f, 2.0f, 2.0f);
		assertEqualsApprox(new Vector3(2.0f, 2.0f, 2.0f), transform.localPosition());
	}

	@Test
	public void testNullPosition() {
		Transform3D transform = new Transform3D();
		Assertions.assertThrows(NullPointerException.class, () -> transform.setPosition(null));
	}

	@Test
	public void testGlobalPosition() {
		Transform3D parent = new Transform3D();
		Transform3D child = new Transform3D();
		parent.addChild(child);
		parent.setPosition(1.0f, 2.0f, 3.0f);
		child.setPosition(2.0f, 3.0f, 1.0f);
		assertEqualsApprox(new Vector3(3.0f, 5.0f, 4.0f), child.globalPosition());
	}

	@Test
	public void testRotationDegrees() {
		Transform3D transform = new Transform3D();
		transform.setRotationDegrees(90.0f, 45.0f, 30.0f);
		assertEqualsApprox(new Vector3((float) Math.toRadians(90.0f), (float) Math.toRadians(45.0f), (float) Math.toRadians(30.0f)), transform.rotationRadians());
	}

	@Test
	public void testRotationRadians() {
		Transform3D transform = new Transform3D();
		transform.setRotationRadians((float) Math.toRadians(90.0f), (float) Math.toRadians(45.0f), (float) Math.toRadians(30.0f));
		assertEqualsApprox(new Vector3(90.0f, 45.0f, 30.0f), transform.rotationDegrees());
	}

	@Test
	public void testGlobalPositionRotated() {
		Transform3D parent = new Transform3D();
		Transform3D child = new Transform3D();
		parent.addChild(child);
		child.setPosition(2.0f, 0.0f, 0.0f);
		parent.setRotationDegrees(0.0f, 90.0f, 0.0f);
		assertEqualsApprox(new Vector3(0.0f, 0.0f, 2.0f), child.globalPosition());
	}

	@Test
	public void testScale() {
		Transform3D transform = new Transform3D();
		transform.setScale(1.1f, 2.0f, 1.5f);
		assertEqualsApprox(new Vector3(1.1f, 2.0f, 1.5f), transform.localScale());
	}

	@Test
	public void testGlobalPositionScaled() {
		Transform3D parent = new Transform3D();
		Transform3D child = new Transform3D();
		parent.addChild(child);
		child.setPosition(1.0f, 2.0f, 3.0f);
		parent.setScale(2.0f, 2.5f, 3.0f);
		assertEqualsApprox(new Vector3(2.0f, 5.0f, 9.0f), child.globalPosition());
	}

	private static void assertEqualsApprox(Vector3 expected, Vector3 actual) {
		Assertions.assertEquals(expected.x(), actual.x(), 0.000001f);
		Assertions.assertEquals(expected.y(), actual.y(), 0.000001f);
		Assertions.assertEquals(expected.z(), actual.z(), 0.000001f);
	}
}
