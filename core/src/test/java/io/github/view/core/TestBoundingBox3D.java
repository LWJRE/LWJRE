package io.github.view.core;

import io.github.view.math.Projection;
import io.github.view.math.Vector3;
import io.github.view.physics.Collision3D;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBoundingBox3D {

	@Test
	public void testProject() {
		BoundingBox3D boundingBox = new BoundingBox3D();
		Projection projection = boundingBox.project(Vector3.RIGHT);
		Assertions.assertEquals(new Projection(-0.5f, 0.5f), projection);
	}

	@Test
	public void testProjectExtended() {
		BoundingBox3D boundingBox = new BoundingBox3D();
		boundingBox.setExtents(0.1f, 5.0f, 1.0f);
		Projection xAxis = boundingBox.project(Vector3.RIGHT);
		Projection yAxis = boundingBox.project(Vector3.UP);
		Projection zAxis = boundingBox.project(Vector3.FORWARD);
		Assertions.assertEquals(new Projection(-0.05f, 0.05f), xAxis);
		Assertions.assertEquals(new Projection(-2.5f, 2.5f), yAxis);
		Assertions.assertEquals(new Projection(-0.5f, 0.5f), zAxis);
	}

	@Test
	public void testProjectRotated() {
		BoundingBox3D boundingBox = new BoundingBox3D();
		boundingBox.setRotationDegrees(0.0f, 0.0f, 45.0f);
		Projection projection = boundingBox.project(Vector3.RIGHT);
		Assertions.assertEquals(-1.0f / (float) Math.sqrt(2), projection.min(), 0.000001f);
		Assertions.assertEquals(1.0f / (float) Math.sqrt(2), projection.max(), 0.000001f);
	}

	@Test
	public void testIntersects() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(0.5f, 0.5f, 0.5f);
		Assertions.assertTrue(boxA.intersects(boxB));
		Assertions.assertTrue(boxB.intersects(boxA));
	}

	@Test
	public void testDoesNotIntersect() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(0.0f, -5.0f, 0.1f);
		Assertions.assertFalse(boxA.intersects(boxB));
		Assertions.assertFalse(boxB.intersects(boxA));
	}

	@Test
	public void testIntersectsExtended() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(0.5f, 0.5f, 2.5f);
		boxA.setExtents(1.0f, 1.0f, 5.0f);
		Assertions.assertTrue(boxA.intersects(boxB));
		Assertions.assertTrue(boxB.intersects(boxA));
	}

	@Test
	public void testIntersectsRotated() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(1.1f, 0.0f, 0.0f);
		boxB.setRotationDegrees(0.0f, 0.0f, 45.0f);
		Assertions.assertTrue(boxA.intersects(boxB));
		Assertions.assertTrue(boxB.intersects(boxA));
	}

	@Test
	public void testCollides() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(0.5f, 0.5f, 0.5f);
		Assertions.assertNotNull(boxA.computeCollision(boxB));
	}

	@Test
	public void testDoesNotCollide() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(0.5f, 0.5f, 3.5f);
		Assertions.assertNull(boxA.computeCollision(boxB));
	}

	@Test
	public void testCollision() {
		BoundingBox3D boxA = new BoundingBox3D();
		BoundingBox3D boxB = new BoundingBox3D();
		boxB.setPosition(0.75f, 0.0f, 0.0f);
		Collision3D collision = boxA.computeCollision(boxB);
		Assertions.assertNotNull(collision);
		Assertions.assertEquals(Vector3.LEFT, collision.normal());
		Assertions.assertEquals(0.25f, collision.depth());
	}
}
