package gamma.engine.graphics.nodes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCamera3D {

	@Test
	public void testPitch() {
		Camera3D camera = new Camera3D();
		camera.setPitchDegrees(30.0f);
		Assertions.assertEquals(30.0f, camera.pitchDegrees());
	}

	@Test
	public void testYaw() {
		Camera3D camera = new Camera3D();
		camera.setYawDegrees(30.0f);
		Assertions.assertEquals(30.0f, camera.yawDegrees());
	}

	@Test
	public void testRoll() {
		Camera3D camera = new Camera3D();
		camera.setRollDegrees(30.0f);
		Assertions.assertEquals(30.0f, camera.rollDegrees());
	}

	@Test
	public void testCurrent() {
		Camera3D camera1 = new Camera3D();
		Camera3D camera2 = new Camera3D();
		Assertions.assertFalse(camera1.isCurrent());
		Assertions.assertFalse(camera2.isCurrent());
		camera1.makeCurrent();
		Assertions.assertTrue(camera1.isCurrent());
		Assertions.assertFalse(camera2.isCurrent());
		camera2.makeCurrent();
		Assertions.assertFalse(camera1.isCurrent());
		Assertions.assertTrue(camera2.isCurrent());
	}
}
