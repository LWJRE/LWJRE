package io.github.view.core;

import io.github.view.RenderingSystem3D;
import io.github.view.math.Color;

public class Light extends Script {

	public Color color = new Color(1.0f, 1.0f, 1.0f);

	public Light(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		RenderingSystem3D.addLight(this);
		super.onStart();
	}

	@Override
	public void onExit() {
		RenderingSystem3D.removeLight(this);
		super.onExit();
	}
}
