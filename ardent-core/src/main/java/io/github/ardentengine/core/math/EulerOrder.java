package io.github.ardentengine.core.math;

/**
 * Enumeration used to specify a rotation order when converting rotations.
 *
 * @see Quaternion#fromEuler(Vector3, EulerOrder)
 * @see Quaternion#euler(EulerOrder)
 */
public enum EulerOrder {
    /** Specifies that the euler angles should be in the {@code XYZ} order. */
    XYZ {
        @Override
        public Vector3 toEulerAngles(Matrix3 m) {
            return new Vector3(Math.atan2(-m.m12(), m.m22()), Math.asin(m.m02()), Math.atan2(-m.m01(), m.m00()));
        }

        @Override
        protected Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz) {
            // (cx sx 0 0) * (cy 0 sy 0) * (cz 0 0 sz)
            return new Quaternion(
                cx * cy * cz - sx * sy * sz,
                sx * cy * cz + cx * sy * sz,
                cx * sy * cz - sx * cy * sz,
                cx * cy * sz + sx * sy * cz
            );
        }
    },
    /** Specifies that the euler angles should be in the {@code XZY} order. */
    XZY {
        @Override
        public Vector3 toEulerAngles(Matrix3 m) {
            return new Vector3(Math.atan2(m.m21(), m.m11()), Math.atan2(m.m02(), m.m00()), Math.asin(-m.m01()));
        }

        @Override
        protected Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz) {
            return new Quaternion(
                cx * cz * cy + sx * sz * sy,
                sx * cz * cy - cx * sz * sy,
                cx * cz * sy - sx * sz * cy,
                sx * cz * sy + cx * sz * cy
            );
        }
    },
    /** Specifies that the euler angles should be in the {@code YXZ} order. */
    YXZ {
        @Override
        public Vector3 toEulerAngles(Matrix3 m) {
            return new Vector3(Math.asin(-m.m12()), Math.atan2(m.m02(), m.m22()), Math.atan2(m.m10(), m.m11()));
        }

        @Override
        protected Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz) {
            return new Quaternion(
                cy * cx * cz + sy * sx * sz,
                cy * sx * cz + sy * cx * sz,
                sy * cx * cz - cy * sx * sz,
                cy * cx * sz - sy * sx * cz
            );
        }
    },
    /** Specifies that the euler angles should be in the {@code YZX} order. */
    YZX {
        @Override
        public Vector3 toEulerAngles(Matrix3 m) {
            return new Vector3(Math.atan2(-m.m12(), m.m11()), Math.atan2(-m.m20(), m.m00()), Math.asin(m.m10()));
        }

        @Override
        protected Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz) {
            // (cy 0 sy 0) * (cz 0 0 sz) * (cx sx 0 0)
            return new Quaternion(
                cy * cz * cx - sy * sz * sx,
                cy * cz * sx + sy * sz * cx,
                sy * cz * cx + cy * sz * sx,
                cy * sz * cx - sy * cz * sx
            );
        }
    },
    /** Specifies that the euler angles should be in the {@code XZY} order. */
    ZXY {
        @Override
        public Vector3 toEulerAngles(Matrix3 m) {
            return new Vector3(Math.asin(m.m21()), Math.atan2(-m.m20(), m.m22()), Math.atan2(-m.m01(), m.m11()));
        }

        @Override
        protected Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz) {
            // (cz 0 0 sz) * (cx sx 0 0) * (cy 0 sy 0)
            return new Quaternion(
                cz * cx * cy - sz * sx * sy,
                cz * sx * cy - sz * cx * sy,
                cz * cx * sy + sz * sx * cy,
                cz * sx * sy + sz * cx * cy
            );
        }
    },
    /** Specifies that the euler angles should be in the {@code ZYX} order. */
    ZYX {
        @Override
        public Vector3 toEulerAngles(Matrix3 m) {
            return new Vector3(Math.atan2(m.m21(), m.m22()), Math.asin(-m.m20()), Math.atan2(m.m10(), m.m00()));
        }

        @Override
        protected Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz) {
            // (cz 0 0 sz) * (cy 0 sy 0) * (cx sx 0 0)
            return new Quaternion(
                cz * cy * cx + sz * sy * sx,
                cz * cy * sx - sz * sy * cx,
                cz * sy * cx + sz * cy * sx,
                sz * cy * cx - cz * sy * sx
            );
        }
    };

    /**
     * Converts the given rotation matrix to euler angles.
     * The given matrix must be a pure rotation matrix.
     *
     * @param m The rotation matrix.
     * @return A vector representing the matrix's rotation in form of euler angles.
     */
    public abstract Vector3 toEulerAngles(Matrix3 m);

    /**
     * Converts the given quaternion to euler angles.
     *
     * @param q The quaternion.
     * @return A vector representing the quaternion's rotation in form of euler angles.
     * @see Quaternion#euler(EulerOrder)
     */
    public Vector3 toEulerAngles(Quaternion q) {
        return this.toEulerAngles(Matrix3.rotation(q));
    }

    /**
     * Converts the given euler angles to a quaternion.
     * Private method used to avoid recomputing the sine and cosine of the angles for every enum value.
     *
     * @param cx Cosine of the angle on the x axis.
     * @param sx Sine of the angle on the x axis.
     * @param cy Cosine of the angle on the y axis.
     * @param sy Sine of the angle on the y axis.
     * @param cz Cosine of the angle on the z axis.
     * @param sz Sine of the angle on the z axis.
     * @return A quaternion constructed from the given euler angles.
     */
    protected abstract Quaternion toQuaternion(double cx, double sx, double cy, double sy, double cz, double sz);

    /**
     * Converts the given euler angles to a quaternion.
     *
     * @param x Angle on the x axis.
     * @param y Angle on the y axis.
     * @param z Angle on the z axis.
     * @return A quaternion constructed from the given euler angles.
     * @see Quaternion#fromEuler(double, double, double, EulerOrder)
     */
    public Quaternion toQuaternion(double x, double y, double z) {
        x = x / 2.0;
        y = y / 2.0;
        z = z / 2.0;
        return this.toQuaternion(Math.cos(x), Math.sin(x), Math.cos(y), Math.sin(y), Math.cos(z), Math.sin(z));
    }

    /**
     * Converts the given rotation in euler angles to a quaternion.
     *
     * @param v A vector representing the rotation in form of euler angles.
     * @return A quaternion constructed from the given euler angles.
     * @see Quaternion#fromEuler(Vector3, EulerOrder)
     */
    public Quaternion toQuaternion(Vector3 v) {
        return this.toQuaternion(v.x(), v.y(), v.z());
    }

    /**
     * Converts the given rotation matrix to a quaternion.
     * The given matrix must be a pure rotation matrix.
     *
     * @param m The rotation matrix.
     * @return A quaternion constructed from the given rotation matrix.
     * @see Matrix3#rotation(Quaternion)
     */
    public Quaternion toQuaternion(Matrix3 m) {
        return this.toQuaternion(this.toEulerAngles(m));
    }
}