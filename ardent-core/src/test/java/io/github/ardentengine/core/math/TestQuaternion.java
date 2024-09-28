package io.github.ardentengine.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestQuaternion {

    // https://www.andre-gaschler.com/rotationconverter/

    private static void assertEquals(Quaternion expected, Quaternion actual, double epsilon) {
        Assertions.assertEquals(expected.w(), actual.w(), epsilon);
        Assertions.assertEquals(expected.x(), actual.x(), epsilon);
        Assertions.assertEquals(expected.y(), actual.y(), epsilon);
        Assertions.assertEquals(expected.z(), actual.z(), epsilon);
    }

    private static void assertEquals(Quaternion expected, Quaternion actual) {
        assertEquals(expected, actual, MathUtils.EPSILON);
    }

    private static void assertEquals(Vector3 expected, Vector3 actual) {
        Assertions.assertEquals(expected.x(), actual.x(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.y(), actual.y(), MathUtils.EPSILON);
        Assertions.assertEquals(expected.z(), actual.z(), MathUtils.EPSILON);
    }

    @Test
    public void testQuaternionFromScalarAndVector() {
        var q1 = new Quaternion(1.0f, 2.0f, 3.0f, 4.0f);
        var v = new Vector3(2.0f, 3.0f, 4.0f);
        var q2 = new Quaternion(1.0f, v);
        assertEquals(q1, q2);
    }

    @Test
    public void testQuaternionFromAxisAngle() {
        var q1 = new Quaternion(Vector3.UP, Math.PI / 4.0);
        var q2 = new Quaternion(0.9238795f, 0.0f, 0.3826834f, 0.0f);
        assertEquals(q2, q1);
    }

    @Test
    public void testQuaternionSum() {
        var q1 = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var q2 = new Quaternion(0.3f, -1.5f, 1.1f, 0.0f);
        var res = new Quaternion(1.5f, -0.1f, -1.0f, 3.0f);
        assertEquals(res, q1.plus(q2));
    }

    @Test
    public void testQuaternionSubtraction() {
        var q1 = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var q2 = new Quaternion(0.3f, -1.5f, 1.1f, 0.0f);
        var res = new Quaternion(0.9f, 2.9f, -3.2f, 3.0f);
        assertEquals(res, q1.minus(q2));
    }

    @Test
    public void testNegated() {
        var q = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var p = new Quaternion(-1.2f, -1.4f, 2.1f, -3.0f);
        assertEquals(p, q.negated());
    }

    @Test
    public void testQuaternionMultipliedByScalar() {
        var q = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var res = new Quaternion(1.44f, 1.68f, -2.52f, 3.6f);
        assertEquals(res, q.multiply(1.2f));
    }

    @Test
    public void testQuaternionDividedByScalar() {
        var q = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var res = new Quaternion(0.6f, 0.7f, -1.05f, 1.5f);
        assertEquals(res, q.divide(2.0f));
    }

    @Test
    public void testQuaternionProduct() {
        var q1 = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var q2 = new Quaternion(0.3f, -1.5f, 1.1f, 0.0f);
        var res = new Quaternion(4.77f, -4.68f, -3.81f, -0.71f);
        assertEquals(res, q1.multiply(q2));
    }

    @Test
    public void testConjugate() {
        var q = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var res = new Quaternion(1.2f, -1.4f, 2.1f, -3.0f);
        assertEquals(res, q.conjugate());
    }

    @Test
    public void testQuaternionDotProduct() {
        var q1 = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var q2 = new Quaternion(0.3f, -1.5f, 1.1f, 0.0f);
        Assertions.assertEquals(-4.05f, q1.dot(q2), MathUtils.EPSILON);
    }

    @Test
    public void testSquaredNorm() {
        var q = new Quaternion(2.0f, 2.0f, -2.0f, 2.0f);
        Assertions.assertEquals(16.0f, q.lengthSquared(), MathUtils.EPSILON);
    }

    @Test
    public void testNorm() {
        var q = new Quaternion(2.0f, 2.0f, -2.0f, 2.0f);
        Assertions.assertEquals(4.0f, q.length(), MathUtils.EPSILON);
    }

    @Test
    public void testNormalized() {
        var q = new Quaternion(2.0f, 2.0f, -2.0f, 2.0f);
        var res = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f);
        assertEquals(res, q.normalized());
    }

    @Test
    public void testQuaternionIsNormalized() {
        var q1 = new Quaternion(2.0f, 2.0f, -2.0f, 2.0f);
        var q2 = new Quaternion(0.5f, 0.5f, -0.5f, 0.5f);
        Assertions.assertFalse(q1.isNormalized());
        Assertions.assertTrue(q2.isNormalized());
    }

    @Test
    public void testInverse() {
        var q = new Quaternion(1.0f, 0.0f, 1.0f, 0.0f);
        var res = new Quaternion(0.5f, 0.0f, -0.5f, 0.0f);
        assertEquals(res, q.inverse());
    }

    @Test
    public void testQuaternionDivision() {
        var q1 = new Quaternion(1.0f, 1.0f, 1.0f, 1.0f);
        var q2 = new Quaternion(1.0f, 0.0f, 1.0f, 0.0f);
        var res = new Quaternion(1.0f, 1.0f, 0.0f, 0.0f);
        assertEquals(res, q1.divide(q2));
    }

    @Test
    public void testGetVectorPart() {
        var q = new Quaternion(1.0f, 2.0f, 3.0f, 4.0f);
        var v = new Vector3(2.0f, 3.0f, 4.0f);
        assertEquals(v, q.vector());
    }

    @Test
    public void testGetRotationAngle() {
        var q = new Quaternion(Vector3.RIGHT, Math.PI / 4.0);
        Assertions.assertEquals(Math.PI / 4.0, q.angle(), MathUtils.EPSILON);
    }

    @Test
    public void testGetRotationAxis() {
        var q = new Quaternion(Vector3.LEFT, Math.PI / 4.0);
        assertEquals(Vector3.LEFT, q.axis());
    }

    @Test
    public void testExponential() {
        // https://www.mathworks.com/help/fusion/ref/quaternion.exp.html
        var q = new Quaternion(4.0f, 14.0f, 15.0f, 1.0f);
        var res = new Quaternion(-6.66f, 36.931f, 39.569f, 2.6379f);
        assertEquals(res, q.exp(), 1e-3);
    }

    @Test
    public void testLogarithm() {
        // https://www.mathworks.com/help/fusion/ref/quaternion.log.html
        var q = new Quaternion(0.5367f, 0.86217f, -0.43359f, 2.7694f);
        var res = new Quaternion(1.0925f, 0.40858f, -0.20543f, 1.3121f);
        assertEquals(res, q.log(), 1e-3);
    }

    @Test
    public void testPower() {
        var q = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        var res = q.multiply(q).multiply(q);
        assertEquals(res, q.pow(3), 1e-3);
        assertEquals(q.inverse().pow(3), q.pow(-3), 1e-3);
    }

    // TODO: Angle between two quaternions

    @Test
    public void testSlerp() {
        var sq2 = (float) Math.sqrt(2.0) / 2.0;
        var q1 = new Quaternion(sq2, 0.0f, 0.0f, sq2);
        var q2 = new Quaternion(0.0f, sq2, sq2, 0.0f);
        var res = new Quaternion(0.5f, 0.5f, 0.5f, 0.5f);
        assertEquals(res, q1.slerp(q2, 0.5f));
    }

    @Test
    public void testEulerAngles() {
        var q = new Quaternion(0.9253338f, 0.0f, 0.3791534f, 0.0f);
        var res = new Vector3(0.0f, 0.7777625f, 0.0f);
        assertEquals(res, q.euler());
    }

    @Test
    public void testEqualsFourValues() {
        var q = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        Assertions.assertTrue(q.equals(1.2f, 1.4f, -2.1f, 3.0f));
    }

    @Test
    public void testEqualsApprox() {
        var q1 = new Quaternion(1.20000001f, 1.39999999f, -2.09999999f, 3.00000001f);
        var q2 = new Quaternion(1.2f, 1.4f, -2.1f, 3.0f);
        Assertions.assertTrue(q1.equalsApprox(q2));
    }

    @Test
    public void testQuaternionFromEulerVector() {
        var euler = new Vector3(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var res = new Quaternion(0.8223632f, 0.02226f, 0.4396797f, 0.3604234f);
        assertEquals(res, Quaternion.fromEuler(euler));
    }

    @Test
    public void testQuaternionFromEulerAngles() {
        var q = Quaternion.fromEuler(Math.PI / 6.0, Math.PI / 4.0, Math.PI / 3.0);
        var res = new Quaternion(0.8223632f, 0.02226f, 0.4396797f, 0.3604234f);
        assertEquals(res, q);
    }

    // TODO: Quaternion from rotation matrix

    // TODO: Quaternion from shortest arc
}