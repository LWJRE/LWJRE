package gamma.engine.tree;

import gamma.engine.annotations.EditorVariable;
import gamma.engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

public class KinematicBody3D extends CollisionObject3D {

	@EditorVariable(name = "Velocity")
	public Vec3f velocity = Vec3f.Zero();

	@EditorVariable(name = "Acceleration")
	public Vec3f acceleration = new Vec3f(0.0f, -9.81f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		this.velocity = this.velocity.plus(this.acceleration.multipliedBy(delta));
		this.position = this.position.plus(this.velocity.multipliedBy(delta));
		super.onUpdate(delta);
	}

	@Override
	protected void onCollision(Collision3D collision) {
		this.position = this.position.plus(collision.normal().multipliedBy(collision.depth()));
		this.velocity = this.velocity.slide(collision.normal());
	}
}
