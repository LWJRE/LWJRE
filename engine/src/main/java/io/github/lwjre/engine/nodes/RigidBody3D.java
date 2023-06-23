package io.github.lwjre.engine.nodes;

import io.github.hexagonnico.vecmatlib.matrix.Mat3f;
import io.github.hexagonnico.vecmatlib.vector.Vec3d;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.lwjre.engine.annotations.EditorRange;
import io.github.lwjre.engine.annotations.EditorVariable;
import io.github.lwjre.engine.physics.CollisionSolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Node that represents a realistic 3D rigid body.
 *
 * @author Nico
 */
public class RigidBody3D extends DynamicBody3D {

	/**
	 * The object's angular velocity in radians per second.
	 */
	@EditorVariable(name = "Angular velocity")
	@EditorRange
	public Vec3f angularVelocity = new Vec3f(0.0f, 0.0f, 0.0f);

	/**
	 * The object's angular acceleration in radians per second squared.
	 */
	@EditorVariable(name = "Angular acceleration")
	@EditorRange
	public Vec3f angularAcceleration = new Vec3f(0.0f, 0.0f, 0.0f);

	/** Current torque */
	private Vec3f torque = new Vec3f(0.0f, 0.0f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		this.angularAcceleration = this.angularAcceleration.plus(this.inverseInertiaTensor().multiply(this.torque));
		this.angularVelocity = this.angularVelocity.plus(this.angularAcceleration.multipliedBy(delta));
		this.rotation = this.rotation.plus(this.angularVelocity.multipliedBy(delta));
		super.onUpdate(delta);
	}

	public Mat3f inertiaTensor() {
		Vec3f shape = this.boundingBox.multiply(this.globalScale());
		return new Mat3f(
				this.mass / 12.0f * (shape.y() * shape.y() + shape.z() * shape.z()), 0.0f, 0.0f,
				0.0f, this.mass / 12.0f * (shape.x() * shape.x() + shape.z() * shape.z()), 0.0f,
				0.0f, 0.0f, this.mass / 12.0f * (shape.x() * shape.x() + shape.y() * shape.y())
		);
	}

	public Mat3f inverseInertiaTensor() {
		Mat3f inertiaTensor = this.inertiaTensor();
		return new Mat3f(
				1.0f / inertiaTensor.m00(), 0.0f, 0.0f,
				0.0f, 1.0f / inertiaTensor.m11(), 0.0f,
				0.0f, 0.0f, 1.0f / inertiaTensor.m22()
		);
	}

	@Override
	protected void onCollision(CollisionObject3D collider, Vec3f normal, float depth) {
		if(collider instanceof RigidBody3D rigidBody) {
			this.position = this.position.minus(normal.multipliedBy(depth / 2));
			collider.position = collider.position.plus(normal.multipliedBy(depth / 2));
			var contactPoints = this.intersectionPoints(collider);
			CollisionSolver.solve(this, rigidBody, contactPoints, normal, depth);
		} else if(collider instanceof DynamicBody3D dynamicBody) {
			CollisionSolver.solve(this, dynamicBody, this.intersectionPoints(collider), normal, depth);
		} else if(collider instanceof KinematicBody3D kinematicBody) {
			CollisionSolver.solve(this, kinematicBody, this.intersectionPoints(collider), normal, depth);
		} else {
			CollisionSolver.solve(this, this.intersectionPoints(collider), normal, depth);
		}
	}

	/**
	 * Finds the intersection points between this collider and the given one.
	 *
	 * @param collider The other collider
	 * @return A {@link HashSet} containing the points of intersection
	 */
	private ArrayList<Vec3f> intersectionPoints(CollisionObject3D collider) {
		ArrayList<Vec3f> intersectionPoints = new ArrayList<>();
		contactPoints(this.getEdges(), collider.getFaces(), intersectionPoints);
		contactPoints(collider.getEdges(), this.getFaces(), intersectionPoints);
		return intersectionPoints;
	}

	private static void contactPoints(List<Vec3f> edges, List<Vec3f> faces, List<Vec3f> contactPoints) {
		for(int f = 0; f < faces.size(); f += 4) {
			Vec3d ab = faces.get(f + 1).minus(faces.get(f)).toDouble();
			Vec3d bc = faces.get(f + 2).minus(faces.get(f + 1)).toDouble();
			Vec3d cd = faces.get(f + 3).minus(faces.get(f + 2)).toDouble();
			Vec3d ad = faces.get(f).minus(faces.get(f + 3)).toDouble();
			Vec3d faceNormal = ab.cross(bc);
			for(int e = 0; e < edges.size(); e += 2) {
				Vec3d direction = edges.get(e + 1).minus(edges.get(e)).toDouble();
				if(Math.abs(direction.dot(faceNormal)) > 0.01f) {
					double d = faces.get(f).minus(edges.get(e)).toDouble().dot(faceNormal) / direction.dot(faceNormal);
					Vec3d candidate = edges.get(e).toDouble().plus(direction.multipliedBy(d));
					if(d >= -0.01f && d <= 1.01f) {
						Vec3d ap = candidate.minus(faces.get(f).toDouble());
						Vec3d bp = candidate.minus(faces.get(f + 1).toDouble());
						Vec3d cp = candidate.minus(faces.get(f + 2).toDouble());
						Vec3d dp = candidate.minus(faces.get(f + 3).toDouble());
						if(!(ap.dot(ab) < -0.01f || bp.dot(bc) < -0.01f || cp.dot(cd) < -0.01f || dp.dot(ad) < -0.01f)) {
							Vec3d projectionAB = ap.project(ab);
							Vec3d projectionBC = bp.project(bc);
							Vec3d projectionCD = cp.project(cd);
							Vec3d projectionAD = dp.project(ad);
							if(projectionAB.lengthSquared() <= ab.lengthSquared() && projectionBC.lengthSquared() <= bc.lengthSquared() && projectionCD.lengthSquared() <= cd.lengthSquared() && projectionAD.lengthSquared() <= ad.lengthSquared()) {
								if(contactPoints.stream().noneMatch(point -> {
									Vec3d difference = point.toDouble().minus(candidate).abs();
									return difference.x() < 0.01f && difference.y() < 0.01f && difference.z() < 0.01f;
								})) {
									contactPoints.add(candidate.toFloat());
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Gets this object's resulting torque.
	 *
	 * @return This object's resulting torque
	 */
	public final Vec3f torque() {
		return this.torque;
	}

	/**
	 * Applies a positioned force to this object.
	 *
	 * @param force The force to apply
	 * @param radius The vector from the object's center of mass to the point where the force is applied
	 */
	public void applyForce(Vec3f force, Vec3f radius) {
		super.applyForce(force);
		this.torque = this.torque.plus(radius.cross(force));
	}

	/**
	 * Applies a positioned force to this object.
	 *
	 * @param fx The force to apply on the x axis
	 * @param fy The force to apply on the y axis
	 * @param fz The force to apply on the z axis
	 * @param radius The vector from the object's center of mass to the point where the force is applied
	 */
	public final void applyForce(float fx, float fy, float fz, Vec3f radius) {
		this.applyForce(new Vec3f(fx, fy, fz), radius);
	}

	/**
	 * Applies a positioned force to this object.
	 *
	 * @param force The force to apply
	 * @param rx Distance of the point where the force is applied from the object's center of mass on the x axis
	 * @param ry Distance of the point where the force is applied from the object's center of mass on the y axis
	 * @param rz Distance of the point where the force is applied from the object's center of mass on the z axis
	 */
	public final void applyForce(Vec3f force, float rx, float ry, float rz) {
		this.applyForce(force, new Vec3f(rx, ry, rz));
	}

	/**
	 * Applies a positioned force to this object.
	 *
	 * @param fx The force to apply on the x axis
	 * @param fy The force to apply on the y axis
	 * @param fz The force to apply on the z axis
	 * @param rx Distance of the point where the force is applied from the object's center of mass on the x axis
	 * @param ry Distance of the point where the force is applied from the object's center of mass on the y axis
	 * @param rz Distance of the point where the force is applied from the object's center of mass on the z axis
	 */
	public final void applyForce(float fx, float fy, float fz, float rx, float ry, float rz) {
		this.applyForce(new Vec3f(fx, fy, fz), new Vec3f(rx, ry, rz));
	}

	/**
	 * Applies a positioned impulse to this object, a time-independent force that simulates a one-time impact.
	 *
	 * @param impulse The impulse to apply
	 * @param radius The vector from the object's center of mass to the point where the impulse is applied
	 */
	public void applyImpulse(Vec3f impulse, Vec3f radius) {
		super.applyImpulse(impulse);
		this.angularVelocity = this.angularVelocity.plus(this.inverseInertiaTensor().multiply(radius.cross(impulse)));
	}

	/**
	 * Applies a positioned impulse to this object, a time-independent force that simulates a one-time impact.
	 *
	 * @param ix The impulse to apply on the x axis
	 * @param iy The impulse to apply on the y axis
	 * @param iz The impulse to apply on the z axis
	 * @param radius The vector from the object's center of mass to the point where the impulse is applied
	 */
	public final void applyImpulse(float ix, float iy, float iz, Vec3f radius) {
		this.applyImpulse(new Vec3f(ix, iy, iz), radius);
	}

	/**
	 * Applies a positioned impulse to this object, a time-independent force that simulates a one-time impact.
	 *
	 * @param impulse The impulse to apply
	 * @param rx Distance of the point where the impulse is applied from the object's center of mass on the x axis
	 * @param ry Distance of the point where the impulse is applied from the object's center of mass on the y axis
	 * @param rz Distance of the point where the impulse is applied from the object's center of mass on the z axis
	 */
	public final void applyImpulse(Vec3f impulse, float rx, float ry, float rz) {
		this.applyImpulse(impulse, new Vec3f(rx, ry, rz));
	}

	/**
	 * Applies a positioned impulse to this object, a time-independent force that simulates a one-time impact.
	 *
	 * @param ix The impulse to apply on the x axis
	 * @param iy The impulse to apply on the y axis
	 * @param iz The impulse to apply on the z axis
	 * @param rx Distance of the point where the impulse is applied from the object's center of mass on the x axis
	 * @param ry Distance of the point where the impulse is applied from the object's center of mass on the y axis
	 * @param rz Distance of the point where the impulse is applied from the object's center of mass on the z axis
	 */
	public final void applyImpulse(float ix, float iy, float iz, float rx, float ry, float rz) {
		this.applyImpulse(new Vec3f(ix, iy, iz), new Vec3f(rx, ry, rz));
	}

	/**
	 * Applies a rotation force to this object.
	 *
	 * @param torque The rotational force to apply
	 */
	public void applyTorque(Vec3f torque) {
		this.torque = this.torque.plus(torque);
	}

	/**
	 * Applies a rotation force to this object.
	 *
	 * @param x The rotational force to apply on the x axis
	 * @param y The rotational force to apply on the y axis
	 * @param z The rotational force to apply on the z axis
	 */
	public final void applyTorque(float x, float y, float z) {
		this.applyTorque(new Vec3f(x, y, z));
	}
}
