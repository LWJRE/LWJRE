package gamma.engine.components;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.physics.Collision3D;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;

public class DynamicBody3D extends KinematicBody3D {

	@EditorVariable(name = "Mass")
	@EditorRange(min = Float.MIN_VALUE)
	public float mass = 1.0f;

	@EditorVariable(name = "Restitution")
	@EditorRange(min = 0.0f, max = 1.0f, slider = true)
	public float restitution = 0.0f;

	private transient Vec3f force = Vec3f.Zero();

	private transient final ArrayList<Impulse> impulses = new ArrayList<>();

	@Override
	protected void onUpdate(float delta) {
		Vec3f resultantForce = this.impulses.stream().map(impulse -> {
			impulse.delta -= delta;
			return impulse.force;
		}).reduce(Vec3f::plus).orElse(Vec3f.Zero()).plus(this.force);
		Vec3f deltaAcceleration = resultantForce.dividedBy(this.mass);
		this.acceleration = this.acceleration.plus(deltaAcceleration);
		this.impulses.removeIf(impulse -> impulse.delta <= 0.0f);
		super.onUpdate(delta);
	}

	@Override
	protected void onCollision(Collision3D collision) {
		if(collision.collider() instanceof DynamicBody3D collider) {
			float mass = 1.0f / (1.0f / this.mass + 1.0f / collider.mass);
			float restitution = Math.min(this.restitution, collider.restitution);
			float impact = collision.normal().dot(this.velocity.minus(collider.velocity));
			float impulse = (1 + restitution) * mass * impact;
			this.velocity = this.velocity.minus(collision.normal().multipliedBy(impulse / this.mass));
			collider.velocity = collider.velocity.plus(collision.normal().multipliedBy(impulse / collider.mass));
		} else {
			super.onCollision(collision);
		}
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
