package io.github.view.core;

import io.github.view.resources.Mesh;
import io.github.view.scene.SceneObject;

import java.util.List;

public abstract class Renderer3D extends Script {

	protected final Transform3D transform;

	public Renderer3D(SceneObject object) {
		super(object);
		this.transform = this.object.getScript(Transform3D.class);
	}

	public abstract void render(Mesh.DrawableMesh mesh, List<Light> lights);

	public abstract Mesh getMesh();
}
