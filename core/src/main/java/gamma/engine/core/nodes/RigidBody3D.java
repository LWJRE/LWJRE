package gamma.engine.core.nodes;

import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.hexagonnico.vecmatlib.vector.Vec4f;

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

	/**
	 * The body's moment of inertia.
	 */
	@EditorVariable(name = "Inertia")
	@EditorRange(min = 0.001f)
	public Vec3f inertia = new Vec3f(10.0f, 10.0f, 10.0f);

	/** Current torque */
	private Vec3f torque = new Vec3f(0.0f, 0.0f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		this.angularAcceleration = this.angularAcceleration.plus(this.torque.divide(this.inertia));
		this.angularVelocity = this.angularVelocity.plus(this.angularAcceleration.multipliedBy(delta));
		this.rotation = this.rotation.plus(this.angularVelocity.multipliedBy(delta));
		super.onUpdate(delta);
	}

	// TODO: Refactor this code

	@Override
	public void onCollision(CollisionObject3D collider, Vec3f normal, float depth) {
		this.position = this.position.plus(normal.multipliedBy(depth));
		HashSet<Vec3f> intersectionPoints = this.intersectionPoints(collider);
		Vec3f globalPosition = this.globalPosition();
		if(collider instanceof KinematicBody3D kinematicBody) {
			Vec3f relativeVelocity = kinematicBody.velocity.minus(this.velocity);
			if(collider instanceof DynamicBody3D dynamicBody) {
				float restitution = Math.min(this.restitution, dynamicBody.restitution);
				float impulse = -(1 + restitution) * relativeVelocity.dot(normal) / (1.0f / this.mass + 1.0f / dynamicBody.mass);
				if(collider instanceof RigidBody3D rigidBody) {
					Vec3f colliderPosition = collider.globalPosition();
					intersectionPoints.forEach(point -> {
						Vec3f radiusA = globalPosition.minus(point);
						Vec3f radiusB = colliderPosition.minus(point);
						this.applyImpulse(normal.multipliedBy(-impulse / intersectionPoints.size()), radiusA);
						rigidBody.applyImpulse(normal.multipliedBy(impulse / intersectionPoints.size()), radiusB);
					});
				} else {
					intersectionPoints.forEach(point -> {
						Vec3f radius = globalPosition.minus(point);
						this.applyImpulse(normal.multipliedBy(-impulse / intersectionPoints.size()), radius);
					});
					dynamicBody.applyImpulse(normal.multipliedBy(impulse));
				}
			} else {
				float impulse = -(1 + this.restitution) * relativeVelocity.dot(normal) / (1.0f / this.mass);
				intersectionPoints.forEach(point -> {
					Vec3f radius = globalPosition.minus(point);
					this.applyImpulse(normal.multipliedBy(-impulse / intersectionPoints.size()), radius);
				});
			}
		} else {
			float impulse = -(1 + this.restitution) * this.velocity.negated().dot(normal) / (1.0f / this.mass);
			intersectionPoints.forEach(point -> {
				Vec3f radius = globalPosition.minus(point);
				this.applyImpulse(normal.multipliedBy(-impulse / intersectionPoints.size()), radius);
			});
		}
	}

	private HashSet<Vec3f> intersectionPoints(CollisionObject3D collider) {
		HashSet<Vec3f> intersectionPoints = new HashSet<>();
		Mat4f transformB = collider.globalTransformation();
		Mat4f transformA = this.globalTransformation();
		Mat4f rotationA = this.globalRotation();
		Mat4f rotationB = collider.globalRotation();
		Vec3f[] pointsA = new Vec3f[] {
				transformA.multiply(new Vec4f(this.boundingBox.dividedBy(-2.0f), 1.0f)).xyz(),
				transformA.multiply(new Vec4f(this.boundingBox.dividedBy(2.0f), 1.0f)).xyz()
		};
		Vec3f[] pointsB = new Vec3f[] {
				transformB.multiply(new Vec4f(collider.boundingBox.dividedBy(-2.0f), 1.0f)).xyz(),
				transformB.multiply(new Vec4f(collider.boundingBox.dividedBy(2.0f), 1.0f)).xyz()
		};
		Vec3f[] normalsA = new Vec3f[] {
				rotationA.col0().xyz(),
				rotationA.col1().xyz(),
				rotationA.col2().xyz()
		};
		Vec3f[] normalsB = new Vec3f[] {
				rotationB.col0().xyz(),
				rotationB.col1().xyz(),
				rotationB.col2().xyz()
		};
		List<Vec3f> edgesA = this.getEdges();
		List<Vec3f> edgesB = collider.getEdges();
		for(Vec3f normalB : normalsB) {
			for(Vec3f point : pointsB) {
				float d = normalB.dot(point);
				for(int i = 0; i < edgesA.size(); i += 2) {
					Vec3f direction = edgesA.get(i + 1).minus(edgesA.get(i));
					if(Math.abs(normalB.dot(direction)) > 0.0f) {
						float t = (d - normalB.dot(edgesA.get(i))) / normalB.dot(direction);
						Vec3f candidate = edgesA.get(i).plus(direction.multipliedBy(t));
						if(t >= -0.00001f && t <= 1.00001f && isPointValid(candidate, collider)) {
							intersectionPoints.add(candidate);
						}
					}
				}
			}
		}
		for(Vec3f normalA : normalsA) {
			for(Vec3f pointA : pointsA) {
				float d = normalA.dot(pointA);
				for(int i = 0; i < edgesB.size(); i += 2) {
					Vec3f direction = edgesB.get(i + 1).minus(edgesB.get(i));
					if(Math.abs(normalA.dot(direction)) > 0.0f) {
						float t = (d - normalA.dot(edgesB.get(i))) / normalA.dot(direction);
						Vec3f candidate = edgesB.get(i).plus(direction.multipliedBy(t));
						if(t >= -0.00001f && t <= 1.00001f && isPointValid(candidate, this)) {
							intersectionPoints.add(candidate);
						}
					}
				}
			}
		}
		return intersectionPoints;
	}

	private static boolean isPointValid(Vec3f point, CollisionObject3D collider) {
		Vec3f v = point.minus(collider.globalPosition());
		Mat4f rotation = collider.globalRotation();
		Vec3f boundingBox = collider.boundingBox.multiply(collider.globalScale());
		float px = Math.abs(v.dot(rotation.col0().xyz()));
		float py = Math.abs(v.dot(rotation.col1().xyz()));
		float pz = Math.abs(v.dot(rotation.col2().xyz()));
		return 2 * px <= boundingBox.x() && 2 * py <= boundingBox.y() && 2 * pz <= boundingBox.z();
	}

	/**
	 *
	 * @return
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
		this.angularVelocity = this.angularVelocity.plus(radius.cross(impulse).divide(this.inertia));
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
