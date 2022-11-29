package io.github.view.core;

import io.github.view.resources.Mesh;

import java.util.List;

public abstract class Renderer3D extends Script {

	public Renderer3D(SceneObject object) {
		super(object);
	}

	public abstract void render(Mesh.DrawableMesh mesh, List<Light> lights);

	public abstract Mesh getMesh();
}
