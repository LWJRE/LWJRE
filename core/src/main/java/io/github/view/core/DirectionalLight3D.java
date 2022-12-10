package io.github.view.core;

import io.github.view.math.Vector3;
import io.github.view.scene.SceneObject;

public class DirectionalLight3D extends Light {

	private Vector3 direction = Vector3.DOWN;

	public DirectionalLight3D(SceneObject object) {
		super(object);
	}
}
