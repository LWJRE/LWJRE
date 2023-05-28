package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.input.InputEvent;
import gamma.engine.input.Keyboard;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

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
	public Vec3f inertia = new Vec3f(1.0f, 1.0f, 1.0f);

	/** Current torque */
	private Vec3f torque = new Vec3f(0.0f, 0.0f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		this.angularAcceleration = this.angularAcceleration.plus(this.torque.divide(this.inertia));
		this.angularVelocity = this.angularVelocity.plus(this.angularAcceleration.multipliedBy(delta));
		this.rotation = this.rotation.plus(this.angularVelocity.multipliedBy(delta));
		super.onUpdate(delta);
	}

	@Override
	protected void onInput(InputEvent event) {
		super.onInput(event);
		if(event.isKeyPressed(Keyboard.KEY_K)) {
			this.applyForce(0.0f, 10.0f, 0.0f, Vec3f.Right());
		}
	}

	@Override
	public void onCollision(CollisionObject3D collider, Vec3f normal, float depth) {
		super.onCollision(collider, normal, depth);
	}

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
