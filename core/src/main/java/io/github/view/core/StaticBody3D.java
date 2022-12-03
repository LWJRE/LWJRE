package io.github.view.core;

import io.github.view.graphics.Debug;
import io.github.view.math.Color;
import io.github.view.math.Vector3;
import io.github.view.physics.PhysicsSystem3D;
import io.github.view.geometry.BoundingBox3D;
import io.github.view.resources.Shader;

public class StaticBody3D extends Script {

	protected final Transform3D transform;
	protected final BoundingBox3D boundingBox = new BoundingBox3D(new Vector3(-0.5f, -0.5f, -0.5f), Vector3.RIGHT, Vector3.UP, Vector3.FORWARD);

	private Shader tempShader;

	public StaticBody3D(SceneObject object) {
		super(object);
		this.transform = this.object.getScript(Transform3D.class);
	}

	@Override
	public void onStart() {
		PhysicsSystem3D.addObject(this);
		this.tempShader = Shader.main().create();
		super.onStart();
	}

	@Override
	public void onUpdate(float time) {
		this.tempShader.start();
		this.tempShader.loadUniform("transformation_matrix", this.transform.matrix());
		this.tempShader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
		this.tempShader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
		Debug.drawQuads(
				this.boundingBox.origin(),
				this.boundingBox.origin().plus(this.boundingBox.v1()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v2()),
				this.boundingBox.origin().plus(this.boundingBox.v2()),

				this.boundingBox.origin(),
				this.boundingBox.origin().plus(this.boundingBox.v1()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v3()),

				this.boundingBox.origin(),
				this.boundingBox.origin().plus(this.boundingBox.v2()),
				this.boundingBox.origin().plus(this.boundingBox.v2()).plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v3()),

				this.boundingBox.origin().plus(this.boundingBox.v1()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v2()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v2()).plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v3()),

				this.boundingBox.origin().plus(this.boundingBox.v2()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v2()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v2()).plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v2()).plus(this.boundingBox.v3()),

				this.boundingBox.origin().plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v1()).plus(this.boundingBox.v2()).plus(this.boundingBox.v3()),
				this.boundingBox.origin().plus(this.boundingBox.v2()).plus(this.boundingBox.v3())
		);
		super.onUpdate(time);
	}

	@Override
	public void onExit() {
		PhysicsSystem3D.removeObject(this);
		super.onExit();
	}

	public BoundingBox3D worldSpaceBoundingBox() {
		return this.boundingBox.transformed(this.transform.matrix());
	}

	public boolean isColliding(StaticBody3D collider) {
		return this.worldSpaceBoundingBox().intersects(collider.worldSpaceBoundingBox());
	}
}
