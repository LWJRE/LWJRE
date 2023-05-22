package gamma.engine.tree;

import gamma.engine.physics.Projection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vecmatlib.vector.Vec3f;

import java.util.List;

public class TestCollisionObject3D {

	@Test
	public void testProjectBoundingBox() {
		CollisionObject3D collider = new CollisionObject3D();
		collider.position = new Vec3f(0.0f, 2.0f, 0.0f);
		collider.setRotationDegrees(0.0f, 0.0f, 45.0f);
		Projection expected = new Projection((float) (-Math.sqrt(2) * 0.5), (float) (Math.sqrt(2) * 0.5));
		Assertions.assertEquals(expected, collider.projectBoundingBox(Vec3f.Right()));
	}

	@Test
	public void testGetVertices() {
		Node3D parent = new Node3D();
		CollisionObject3D collider = new CollisionObject3D();
		parent.addChild(collider);
		parent.position = new Vec3f(0.0f, 2.0f, 0.0f);
		collider.position = new Vec3f(2.0f, 0.0f, 0.0f);
		collider.boundingBox = new Vec3f(2.0f, 2.0f, 2.0f);
		List<Vec3f> expected = List.of(
				new Vec3f(3.0f, 3.0f, 1.0f),
				new Vec3f(1.0f, 3.0f, 1.0f),
				new Vec3f(3.0f, 1.0f, 1.0f),
				new Vec3f(3.0f, 3.0f, -1.0f),
				new Vec3f(1.0f, 1.0f, 1.0f),
				new Vec3f(3.0f, 1.0f, -1.0f),
				new Vec3f(1.0f, 3.0f, -1.0f),
				new Vec3f(1.0f, 1.0f, -1.0f)
		);
		List<Vec3f> actual = collider.getVertices();
		Assertions.assertEquals(expected.size(), actual.size());
		Assertions.assertTrue(actual.containsAll(expected));
	}

	@Test
	public void testMeanCenter() {
		Node3D parent = new Node3D();
		CollisionObject3D collider = new CollisionObject3D();
		parent.addChild(collider);
		parent.position = new Vec3f(0.0f, 2.0f, 0.0f);
		collider.position = new Vec3f(2.0f, 0.0f, 0.0f);
		collider.boundingBox = new Vec3f(2.0f, 2.0f, 2.0f);
		Assertions.assertEquals(new Vec3f(2.0f, 2.0f, 0.0f), collider.meanCenter());
	}
}
