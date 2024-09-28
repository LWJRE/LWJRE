package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestEulerOrder {

    // https://www.andre-gaschler.com/rotationconverter/

    private static void assertEquals(Quaternion expected, Quaternion actual) {
        Assertions.assertEquals(expected.w(), actual.w(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.x(), actual.x(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.y(), actual.y(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.z(), actual.z(), MathUtils.EPSILON);
    }

    private static void assertEquals(Vector3 expected, Vector3 actual) {
        Assertions.assertEquals(expected.x(), actual.x(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.y(), actual.y(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.z(), actual.z(), MathUtils.EPSILON);
    }

    @Test
    public void testEulerToQuaternionXYZ() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var quaternion = new Quaternion(0.7233174, 0.3919038, 0.2005621, 0.5319757);
        assertEquals(quaternion, EulerOrder.XYZ.toQuaternion(euler));
    }

    @Test
    public void testEulerToQuaternionXZY() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var quaternion = new Quaternion(0.8223632, 0.02226, 0.2005621, 0.5319757);
        assertEquals(quaternion, EulerOrder.XZY.toQuaternion(euler));
    }

    @Test
    public void testEulerToQuaternionYXZ() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var quaternion = new Quaternion(0.8223632, 0.3919038, 0.2005621, 0.3604234);
        assertEquals(quaternion, EulerOrder.YXZ.toQuaternion(euler));
    }

    @Test
    public void testEulerToQuaternionYZX() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var quaternion = new Quaternion(0.7233174, 0.3919038, 0.4396797, 0.3604234);
        assertEquals(quaternion, EulerOrder.YZX.toQuaternion(euler));
    }

    @Test
    public void testEulerToQuaternionZXY() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var quaternion = new Quaternion(0.7233174, 0.02226, 0.4396797, 0.5319757);
        assertEquals(quaternion, EulerOrder.ZXY.toQuaternion(euler));
    }

    @Test
    public void testEulerToQuaternionZYX() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var quaternion = new Quaternion(0.8223632, 0.02226, 0.4396797, 0.3604234);
        assertEquals(quaternion, EulerOrder.ZYX.toQuaternion(euler));
    }

    @Test
    public void testQuaternionToEulerXYZ() {
        var quaternion = new Quaternion(0.7233174, 0.3919038, 0.2005621, 0.5319757);
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        assertEquals(euler, EulerOrder.XYZ.toEulerAngles(quaternion));
    }

    @Test
    public void testQuaternionToEulerXZY() {
        var quaternion = new Quaternion(0.8223632, 0.02226, 0.2005621, 0.5319757);
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        assertEquals(euler, EulerOrder.XZY.toEulerAngles(quaternion));
    }

    @Test
    public void testQuaternionToEulerYXZ() {
        var quaternion = new Quaternion(0.8223632, 0.3919038, 0.2005621, 0.3604234);
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        assertEquals(euler, EulerOrder.YXZ.toEulerAngles(quaternion));
    }

    @Test
    public void testQuaternionToEulerYZX() {
        var quaternion = new Quaternion(0.7233174, 0.3919038, 0.4396797, 0.3604234);
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        assertEquals(euler, EulerOrder.YZX.toEulerAngles(quaternion));
    }

    @Test
    public void testQuaternionToEulerZXY() {
        var quaternion = new Quaternion(0.7233174, 0.02226, 0.4396797, 0.5319757);
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        assertEquals(euler, EulerOrder.ZXY.toEulerAngles(quaternion));
    }

    @Test
    public void testQuaternionToEulerZYX() {
        var quaternion = new Quaternion(0.8223632, 0.02226, 0.4396797, 0.3604234);
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        assertEquals(euler, EulerOrder.ZYX.toEulerAngles(quaternion));
    }
}