package gamma.engine.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

public class TestNode3D {

	@Test
	public void testGlobalPosition() {
		Node3D parent = new Node3D();
		Node3D child = new Node3D();
		parent.addChild(child);
		parent.position = new Vec3f(2.0f, 0.0f, 0.0f);
		parent.rotation = new Vec3f(0.0f, (float) (Math.PI * 0.5), 0.0f);
		child.position = new Vec3f(2.0f, 0.0f, 0.0f);
		assertEqualApprox(new Vec3f(2.0f, 0.0f, 2.0f), child.globalPosition());
	}

	@Test
	public void testLocalTranslation() {
		Node3D node = new Node3D();
		node.position = new Vec3f(2.0f, 3.0f, 1.0f);
		Mat4f translation = node.localTranslation();
		Vec3f v = new Vec3f(3.0f, 1.0f, 0.0f);
		Vec3f w = translation.multiply(new Vec4f(v, 1.0f)).xyz();
		assertEqualApprox(node.position.plus(v), w);
	}

	@Test
	public void testRotationDegrees() {
		Node3D node = new Node3D();
		node.rotation = new Vec3f((float) (Math.PI * 0.5), (float) Math.PI, 0.0f);
		assertEqualApprox(new Vec3f(90.0f, 180.0f, 0.0f), node.rotationDegrees());
	}

	@Test
	public void testSetRotationDegrees() {
		Node3D node = new Node3D();
		Vec3f degrees = new Vec3f(90.0f, 180.0f, 0.0f);
		Vec3f radians = new Vec3f((float) (Math.PI * 0.5), (float) Math.PI, 0.0f);
		node.setRotationDegrees(degrees);
		assertEqualApprox(radians, node.rotation);
	}

	@Test
	public void testRotateDegrees() {
		Node3D node = new Node3D();
		node.rotation = new Vec3f((float) (Math.PI * 0.5), (float) Math.PI, 0.0f);
		node.rotateDegrees(new Vec3f(90.0f, 90.0f, 45.0f));
		Vec3f expected = new Vec3f((float) Math.PI, (float) (Math.PI * 1.5), (float) (Math.PI * 0.25));
		assertEqualApprox(expected, node.rotation);
	}

	@Test
	public void testLocalRotation() {
		Node3D node = new Node3D();
		node.rotation = new Vec3f((float) (Math.PI * 0.5), (float) Math.PI, 0.0f);
		Mat4f matrix = node.localRotation();
		assertEqualApprox(new Vec3f(-1.0f, -1.0f, -1.0f), matrix.multiply(1.0f, 1.0f, 1.0f, 1.0f).xyz());
	}

	@Test
	public void testGlobalRotation() {
		Node3D parent = new Node3D();
		Node3D child = new Node3D();
		parent.addChild(child);
		Vec3f v = new Vec3f((float) (Math.PI * 0.5), (float) Math.PI, 0.0f);
		Vec3f w = new Vec3f((float) Math.PI, (float) (Math.PI * 1.5), (float) (Math.PI * 0.25));
		parent.rotation = v;
		child.rotation = w;
		Mat4f matrix = child.globalRotation();
		assertEqualApprox(new Vec3f(-1.0f, (float) -Math.sqrt(2), 0.0f), matrix.multiply(1.0f, 1.0f, 1.0f, 1.0f).xyz());
	}

	@Test
	public void testGlobalScale() {
		Node3D parent = new Node3D();
		Node3D child = new Node3D();
		parent.addChild(child);
		child.scale = new Vec3f(2.0f, 0.5f, 3.0f);
		parent.scale = new Vec3f(0.5f, 3.0f, 2.0f);
		assertEqualApprox(new Vec3f(1.0f, 1.5f, 6.0f), child.globalScale());
	}

	@Test
	public void testScalingMatrix() {
		Node3D node = new Node3D();
		node.scale = new Vec3f(1.0f, 2.0f, 3.0f);
		Mat4f matrix = node.scalingMatrix();
		assertEqualApprox(new Vec3f(2.0f, 4.0f, 6.0f), matrix.multiply(2.0f, 2.0f, 2.0f, 1.0f).xyz());
	}

	@Test
	public void testLocalTransformation() {
		Node3D node = new Node3D();
		node.position = new Vec3f(2.0f, 0.0f, 0.0f);
		node.rotation = new Vec3f((float) (Math.PI * 0.5), 0.0f, (float) Math.PI);
		node.scale = new Vec3f(1.5f, 1.5f, 1.5f);
		Mat4f matrix = node.localTransformation();
		assertEqualApprox(new Vec3f(2.0f, 0.0f, 0.0f), matrix.multiply(0.0f, 0.0f, 0.0f, 1.0f).xyz());
	}

	@Test
	public void testGlobalTransformation() {
		Node3D parent = new Node3D();
		Node3D child = new Node3D();
		parent.addChild(child);
		parent.position = new Vec3f(2.0f, 0.0f, 0.0f);
		parent.rotation = new Vec3f((float) (Math.PI * 0.5), 0.0f, (float) Math.PI);
		child.position = new Vec3f(0.0f, 0.0f, 2.0f);
		Mat4f matrix = child.globalTransformation();
		assertEqualApprox(new Vec3f(2.0f, 2.0f, 0.0f), matrix.multiply(0.0f, 0.0f, 0.0f, 1.0f).xyz());
	}

	private static void assertEqualApprox(Vec3f v, Vec3f w) {
		Assertions.assertEquals(v.x(), w.x(), 0.000001f);
		Assertions.assertEquals(v.y(), w.y(), 0.000001f);
		Assertions.assertEquals(v.z(), w.z(), 0.000001f);
	}
}
