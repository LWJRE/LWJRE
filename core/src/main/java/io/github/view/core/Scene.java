package io.github.view.core;

import io.github.view.math.Vector3;

import java.util.ArrayList;

public final class Scene {

	private final ArrayList<SceneObject> sceneObjects = new ArrayList<>();

	public Scene() {
		SceneObject object = new SceneObject();
		object.addScript(Position3D::new);
		object.addScript(Rotation3D::new);
		object.addScript(Scale3D::new);
		object.addScript(Transform3D::new);
		object.addScript(ModelRenderer::new);
		SceneObject camera = new SceneObject();
		Position3D cameraPosition = camera.addScript(Position3D::new);
		cameraPosition.set(new Vector3(0.0f, 0.0f, 6.0f));
		Camera3D cameraScript = camera.addScript(Camera3D::new);
		cameraScript.makeCurrent();
		this.sceneObjects.add(object);
		this.sceneObjects.add(camera);
	}

	public void process() {
		this.sceneObjects.forEach(SceneObject::process);
	}

	public void render() {
		this.sceneObjects.forEach(SceneObject::render);
	}
}
