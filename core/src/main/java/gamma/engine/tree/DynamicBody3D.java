package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import io.github.hexagonnico.vecmatlib.vector.Vec2f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

/**
 * Node that represents a collider that moves through forces, but is not affected by rotation.
 *
 * @see RigidBody3D
 *
 * @author Nico
 */
public class DynamicBody3D extends KinematicBody3D {

	/**
	 * The object's mass is used to compute the acceleration caused by the applied force.
	 */
	@EditorVariable(name = "Mass")
	@EditorRange(min = 0.0f)
	public float mass = 1.0f;

	/**
	 * The restitution value is the coefficient of restitution used when this object collides with another one.
	 */
	@EditorVariable(name = "Restitution")
	@EditorRange(min = 0.0f, max = 1.0f)
	public float restitution = 0.0f;

	/** Current applied force */
	private Vec3f force = Vec3f.Zero();

	@Override
	protected void onUpdate(float delta) {
		this.acceleration = this.force.dividedBy(this.mass);
		super.onUpdate(delta);
	}

	@Override
	public void onCollision(CollisionObject3D collider, Vec3f normal, float depth) {
		this.position = this.position.plus(normal.multipliedBy(depth));
		if(collider instanceof KinematicBody3D kinematicBody) {
			Vec3f relativeVelocity = kinematicBody.velocity.minus(this.velocity);
			if(kinematicBody instanceof DynamicBody3D dynamicBody) {
				float restitution = Math.min(this.restitution, dynamicBody.restitution);
				float impulse = -(1 + restitution) * relativeVelocity.dot(normal) / (1.0f / this.mass + 1.0f / dynamicBody.mass);
				this.applyImpulse(normal.multipliedBy(-impulse));
				dynamicBody.applyImpulse(normal.multipliedBy(impulse));
			} else {
				float impulse = -(1 + this.restitution) * relativeVelocity.dot(normal) / (1.0f / this.mass);
				this.applyImpulse(normal.multipliedBy(-impulse));
			}
		} else {
			float impulse = -(1 + this.restitution) * this.velocity.negated().dot(normal) / (1.0f / this.mass);
			this.applyImpulse(normal.multipliedBy(-impulse));
		}
	}

	/**
	 * Returns the resultant force applied to this object.
	 *
	 * @return The resultant force applied to this object
	 */
	public final Vec3f resultantForce() {
		return this.force;
	}

	/**
	 * Applies the given force at the object's center of mass.
	 *
	 * @param force The force to apply
	 */
	public void applyForce(Vec3f force) {
		this.force = this.force.plus(force);
	}

	/**
	 * Applies the given force at the object's center of mass.
	 *
	 * @param x The force to apply on the x axis
	 * @param y The force to apply on the y axis
	 * @param z The force to apply on the z axis
	 */
	public final void applyForce(float x, float y, float z) {
		this.applyForce(new Vec3f(x, y, z));
	}

	/**
	 * Applies the given force at the object's center of mass on the x and y axes.
	 *
	 * @param force The force to apply
	 */
	public final void applyForce(Vec2f force) {
		this.applyForce(new Vec3f(force, 0.0f));
	}

	/**
	 * Applies the given force at the object's center of mass on the x and y axes.
	 *
	 * @param x The force to apply on the x axis
	 * @param y The force to apply on the y axis
	 */
	public final void applyForce(float x, float y) {
		this.applyForce(x, y, 0.0f);
	}

	/**
	 * Applies the given impulse to this object's center of mass, a time-independent force that simulates a one-time impact.
	 *
	 * @param impulse The impulse to apply
	 */
	public void applyImpulse(Vec3f impulse) {
		this.velocity = this.velocity.plus(impulse.dividedBy(this.mass));
	}

	/**
	 * Applies the given impulse to this object's center of mass, a time-independent force that simulates a one-time impact.
	 *
	 * @param x The impulse to apply on the x axis
	 * @param y The impulse to apply on the y axis
	 * @param z The impulse to apply on the z axis
	 */
	public final void applyImpulse(float x, float y, float z) {
		this.applyImpulse(new Vec3f(x, y, z));
	}

	/**
	 * Applies the given impulse to this object's center of mass on the x and y axes, a time-independent force that simulates a one-time impact.
	 *
	 * @param impulse The impulse to apply
	 */
	public final void applyImpulse(Vec2f impulse) {
		this.applyImpulse(new Vec3f(impulse, 0.0f));
	}

	/**
	 * Applies the given impulse to this object's center of mass on the x and y axes, a time-independent force that simulates a one-time impact.
	 *
	 * @param x The impulse to apply on the x axis
	 * @param y The impulse to apply on the y axis
	 */
	public final void applyImpulse(float x, float y) {
		this.applyImpulse(x, y, 0.0f);
	}
}
