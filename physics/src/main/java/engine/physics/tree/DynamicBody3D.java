package engine.physics.tree;

import engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

public class DynamicBody3D extends KinematicBody3D {

	public float mass = 1.0f;

	private Vec3f force = Vec3f.Zero();

	@Override
	protected void onUpdate(float delta) {
		this.acceleration = this.force.dividedBy(this.mass);
		super.onUpdate(delta);
	}

	@Override
	protected void onCollision(Collision3D collision) {
		if(collision.collider() instanceof DynamicBody3D other) {
			this.position = this.position.plus(collision.normal().multipliedBy(collision.depth()));
			Vec3f relativeVelocity = other.velocity.minus(this.velocity);
			float restitution = 1.0f; // Max(a, b)
			float impulse = -(1.0f + restitution) * relativeVelocity.dot(collision.normal());
			impulse /= (1.0f / this.mass + 1.0f / other.mass);
			this.velocity = this.velocity.minus(collision.normal().multipliedBy(impulse / this.mass));
			other.velocity = other.velocity.plus(collision.normal().multipliedBy(impulse / other.mass));
		} else {
			super.onCollision(collision);
		}
	}
}
