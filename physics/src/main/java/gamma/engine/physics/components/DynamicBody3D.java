package gamma.engine.physics.components;

import gamma.engine.core.components.Transform3D;
import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.physics.Collision3D;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;

public class DynamicBody3D extends KinematicBody3D {

	@EditorVariable(name = "Mass")
	@EditorRange(min = 0.0f)
	public float mass = 1.0f;

	@EditorVariable(name = "Restitution")
	@EditorRange(min = 0.0f, max = 1.0f)
	public float restitution = 0.0f;

	@EditorVariable(name = "Is immovable")
	public boolean immovable = false;

	private Vec3f force = Vec3f.Zero();

	private final ArrayList<Impulse> impulses = new ArrayList<>();

	@Override
	protected void onUpdate(float delta) {
		if(this.immovable) {
			this.acceleration = Vec3f.Zero();
			this.velocity = Vec3f.Zero();
		}
		Vec3f deltaAcceleration = this.resultantForce().dividedBy(this.mass);
		this.acceleration = this.acceleration.plus(deltaAcceleration);
		this.impulses.removeIf(impulse -> {
			impulse.delta -= delta;
			return impulse.delta <= 0.0f;
		});
		super.onUpdate(delta);
	}

	@Override
	protected void onCollision(Collision3D collision) {
		if(collision.collider() instanceof DynamicBody3D collider) {
			Vec3f relativeVelocity = collider.velocity.minus(this.velocity);
			float restitution = Math.min(this.restitution, collider.restitution);
			float impulse = -(1 + restitution) * relativeVelocity.dot(collision.normal()) / (1.0f / this.mass + 1.0f / collider.mass);
			if(collider.immovable) {
				this.getComponent(Transform3D.class).ifPresent(transform -> {
					Vec3f penetration = collision.normal().multipliedBy(collision.depth());
					transform.position = transform.position.plus(penetration);
				});
			}
			this.velocity = this.velocity.minus(collision.normal().multipliedBy(impulse / this.mass));
			collider.velocity = collider.velocity.plus(collision.normal().multipliedBy(impulse / collider.mass));
		} else {
			super.onCollision(collision);
		}
	}

	public final Vec3f resultantForce() {
		return this.impulses.stream().map(impulse -> impulse.force).reduce(Vec3f::plus).orElse(Vec3f.Zero()).plus(this.force);
	}

	public void applyForce(Vec3f force) {
		this.force = this.force.plus(force);
	}

	public final void applyForce(float x, float y, float z) {
		this.applyForce(new Vec3f(x, y, z));
	}

	public final void applyForce(Vec2f force) {
		this.applyForce(new Vec3f(force, 0.0f));
	}

	public final void applyForce(float x, float y) {
		this.applyForce(x, y, 0.0f);
	}

	public void applyImpulse(Vec3f force, float time) {
		this.impulses.add(new Impulse(force, time));
	}

	public final void applyImpulse(float x, float y, float z, float time) {
		this.applyImpulse(new Vec3f(x, y, z), time);
	}

	public final void applyImpulse(Vec2f force, float time) {
		this.applyImpulse(new Vec3f(force, 0.0f), time);
	}

	public final void applyImpulse(float x, float y, float time) {
		this.applyImpulse(x, y, 0.0f, time);
	}

	private static class Impulse {

		private final Vec3f force;
		private float delta;

		private Impulse(Vec3f force, float delta) {
			this.force = force;
			this.delta = delta;
		}
	}
}
