package io.github.view.core;

public class PointLight3D extends Light {

	private Position3D position;
	// TODO: Attenuation

	public PointLight3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		this.position = this.object.getScript(Position3D.class);
		super.onStart();
	}
}
