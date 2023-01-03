package engine.physics.tree;

import engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

public class KinematicBody3D extends CollisionObject3D {

	private Vec3f velocity = Vec3f.Zero();
	private Vec3f acceleration = new Vec3f(0.0f, -9.81f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		Vec3f velocityDelta = this.acceleration.multipliedBy(delta);
		this.velocity = this.velocity.plus(velocityDelta);
		Vec3f movement = this.velocity.multipliedBy(delta);
		this.moveAndCollide(movement);
		super.onUpdate(delta);
	}

	@Override
	protected void onCollision(Collision3D collision) {
		this.position = this.position.plus(collision.normal().multipliedBy(collision.depth()));
		this.velocity = Vec3f.Zero(); // TODO: Better velocity result
	}
}
