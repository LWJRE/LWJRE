package io.github.lwjre.engine.nodes;

import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestNode3D {

	@Test
	public void testGlobalPosition() {
		Node3D parent = new Node3D();
		parent.position = new Vec3f(1.0f, 0.0f, 0.0f);
		parent.rotation = new Vec3f(0.0f, (float) (Math.PI / 2), 0.0f);
		Node3D child = new Node3D();
		child.position = new Vec3f(1.0f, 0.0f, 0.0f);
		parent.addChild(child);
		assertEqualApprox(new Vec3f(1.0f, 0.0f, 1.0f), child.globalPosition());
	}

	@Test
	public void testLocalTranslation() {
		Node3D node = new Node3D();
		node.position = new Vec3f(1.0f, 2.0f, 3.0f);
		Assertions.assertEquals(new Mat4f(
			1.0f, 0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 2.0f,
			0.0f, 0.0f, 1.0f, 3.0f,
			0.0f, 0.0f, 0.0f, 1.0f
		), node.localTranslation());
	}

	@Test
	public void testRotationDegrees() {
		Node3D node = new Node3D();
		node.rotation = new Vec3f((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4));
		assertEqualApprox(new Vec3f(90.0f, 180.0f, 45.0f), node.rotationDegrees());
	}

	@Test
	public void testSetRotationDegrees() {
		Node3D node = new Node3D();
		node.setRotationDegrees(90.0f, 180.0f, 45.0f);
		assertEqualApprox(new Vec3f((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4)), node.rotation);
	}

	@Test
	public void testSetRotationDegreesVector() {
		Node3D node = new Node3D();
		node.setRotationDegrees(new Vec3f(90.0f, 180.0f, 45.0f));
		assertEqualApprox(new Vec3f((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4)), node.rotation);
	}

	@Test
	public void testRotateDegrees() {
		Node3D node = new Node3D();
		node.setRotationDegrees(10.0f, 10.0f, 10.0f);
		node.rotateDegrees(10.0f, 20.0f, 30.0f);
		assertEqualApprox(new Vec3f(20.0f, 30.0f, 40.0f), node.rotationDegrees());
	}

	@Test
	public void testRotateDegreesVector() {
		Node3D node = new Node3D();
		node.setRotationDegrees(new Vec3f(10.0f, 10.0f, 10.0f));
		node.rotateDegrees(new Vec3f(10.0f, 20.0f, 30.0f));
		assertEqualApprox(new Vec3f(20.0f, 30.0f, 40.0f), node.rotationDegrees());
	}

	@Test
	public void testLocalRotation() {
		Node3D node = new Node3D();
		node.rotation = new Vec3f((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4));
		Mat4f matrix = Mat4f.rotation((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4));
		Assertions.assertEquals(matrix, node.localRotation());
	}

	@Test
	public void testGlobalRotation() {
		Node3D parent = new Node3D();
		parent.rotation = new Vec3f((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4));
		Node3D child = new Node3D();
		child.rotation = new Vec3f((float) (Math.PI / 2), (float) (Math.PI), (float) (Math.PI / 4));
		parent.addChild(child);
		Assertions.assertEquals(parent.localRotation().multiply(child.localRotation()), child.globalRotation());
	}

	@Test
	public void testGlobalScale() {
		Node3D parent = new Node3D();
		parent.scale = new Vec3f(1.5f, 2.0f, 3.0f);
		Node3D child = new Node3D();
		child.scale = new Vec3f(2.0f, 0.5f, 3.0f);
		parent.addChild(child);
		assertEqualApprox(new Vec3f(3.0f, 1.0f, 9.0f), child.globalScale());
	}

	@Test
	public void testScalingMatrix() {
		Node3D node = new Node3D();
		node.scale = new Vec3f(1.5f, 2.5f, 3.5f);
		Assertions.assertEquals(new Mat4f(
			1.5f, 0.0f, 0.0f, 0.0f,
			0.0f, 2.5f, 0.0f, 0.0f,
			0.0f, 0.0f, 3.5f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f
		), node.localScaling());
	}

	@Test
	public void testLocalTransformation() {
		Node3D node = new Node3D();
		node.position = new Vec3f(0.0f, 1.0f, 2.0f);
		node.rotation = new Vec3f(3.14f, 1.57f, 0.0f);
		node.scale = new Vec3f(1.1f, 1.1f, 1.1f);
		Mat4f translation = Mat4f.translation(0.0f, 1.0f, 2.0f);
		Mat4f rotation = Mat4f.rotation(3.14f, 1.57f, 0.0f);
		Mat4f scale = Mat4f.scaling(1.1f, 1.1f, 1.1f);
		Assertions.assertEquals(translation.multiply(rotation).multiply(scale), node.localTransform());
	}

	@Test
	public void testGlobalTransformation() {
		Node3D parent = new Node3D();
		parent.position = new Vec3f(0.0f, 1.0f, 2.0f);
		parent.rotation = new Vec3f(3.14f, 1.57f, 0.0f);
		parent.scale = new Vec3f(1.1f, 1.1f, 1.1f);
		Node3D child = new Node3D();
		child.position = new Vec3f(1.1f, 0.0f, 3.0f);
		child.rotation = new Vec3f(1.57f, 1.57f, 3.14f);
		child.scale = new Vec3f(1.0f, 2.0f, 1.0f);
		parent.addChild(child);
		Assertions.assertEquals(parent.localTransform().multiply(child.localTransform()), child.globalTransform());
	}

	private static void assertEqualApprox(Vec3f expected, Vec3f actual) {
		Assertions.assertEquals(expected.x(), actual.x(), 0.00001f);
		Assertions.assertEquals(expected.y(), actual.y(), 0.00001f);
		Assertions.assertEquals(expected.z(), actual.z(), 0.00001f);
	}
}
