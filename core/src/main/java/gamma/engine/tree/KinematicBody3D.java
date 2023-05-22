package gamma.engine.tree;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

/**
 * Collision object that moves through kinematic equations.
 * Contains a velocity and an acceleration.
 * The node's position and velocity are updated every frame accordingly.
 *
 * @author Nico
 */
public class KinematicBody3D extends CollisionObject3D {

	/**
	 * The body's current velocity.
	 */
	@EditorVariable(name = "Velocity")
	public Vec3f velocity = Vec3f.Zero();

	/**
	 * The body's current acceleration.
	 */
	@EditorVariable(name = "Acceleration")
	public Vec3f acceleration = new Vec3f(0.0f, -9.81f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		// TODO: Ask the physics system to process an object only after it moves
		this.velocity = this.velocity.plus(this.acceleration.multipliedBy(delta));
		this.position = this.position.plus(this.velocity.multipliedBy(delta));
		super.onUpdate(delta);
	}

	@Override
	public void onCollision(Collision3D collision) {
		this.position = this.position.plus(collision.normal().multipliedBy(collision.depth()));
		this.velocity = this.velocity.slide(collision.normal());
	}
}
