package io.github.view.core;

import io.github.view.graphics.Debug;
import io.github.view.math.Vector3;
import io.github.view.resources.Shader;

public class StaticBody3D extends PhysicObject3D {

	private Shader tempShader;

	public StaticBody3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		this.tempShader = Shader.main().createOrLoad();
		super.onStart();
	}

	@Override
	public void onCollision(Vector3 normal) {

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
}
