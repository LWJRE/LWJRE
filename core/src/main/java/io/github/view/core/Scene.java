package io.github.view.core;

import io.github.view.math.Vector3;

import java.util.ArrayList;

public final class Scene {

	private final ArrayList<SceneObject> sceneObjects = new ArrayList<>();

	private long previousTime = System.nanoTime();

	public Scene() {
		SceneObject object = new SceneObject();
		Position3D objectPosition = object.addScript(Position3D::new);
		object.addScript(Rotation3D::new);
		object.addScript(Scale3D::new);
		object.addScript(Transform3D::new);
		object.addScript(ModelRenderer::new);
		object.addScript(KinematicBody3D::new);
		objectPosition.set(objectPosition.get().plus(0.0f, 50.0f, 0.0f));
		SceneObject camera = new SceneObject();
		Position3D cameraPosition = camera.addScript(Position3D::new);
		cameraPosition.set(new Vector3(0.0f, 1.0f, 6.0f));
		Camera3D cameraScript = camera.addScript(Camera3D::new);
		cameraScript.makeCurrent();
		SceneObject pointLight = new SceneObject();
		Position3D lightPosition = pointLight.addScript(Position3D::new);
		pointLight.addScript(PointLight3D::new);
		lightPosition.set(new Vector3(0.0f, -10.0f, 0.0f));
		this.sceneObjects.add(object);
		this.sceneObjects.add(camera);
		this.sceneObjects.add(pointLight);
	}

	public void process() {
		long time = System.nanoTime();
		this.sceneObjects.forEach(sceneObject -> sceneObject.process((this.previousTime - time) / 1_000_000_000.0f));
		this.previousTime = time;
	}

	public void render() {
		this.sceneObjects.forEach(SceneObject::render);
	}
}
