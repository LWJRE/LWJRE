package io.github.view.core;

import java.util.ArrayList;

public final class Scene {

	private final ArrayList<SceneObject> sceneObjects = new ArrayList<>();

	private long previousTime = System.nanoTime();

	public Scene() {
//		SceneObject object = new SceneObject(this);
//		object.addScript(Position3D::new).set(new Vector3(0.0f, 10.0f, 0.0f));
//		object.addScript(Rotation3D::new)/*.set(new Vector3(1.0f, 1.0f, 1.0f))*/;
//		object.addScript(Scale3D::new).set(new Vector3(1.5f, 0.5f, 1.0f));
//		object.addScript(Transform3D::new);
//		//object.addScript(ModelRenderer::new);
//		object.addScript(KinematicBody3D::new);
//		SceneObject camera = new SceneObject(this);
//		camera.addScript(Position3D::new).set(new Vector3(0.0f, 1.0f, 6.0f));
//		camera.addScript(Camera3D::new).makeCurrent();
//		SceneObject pointLight = new SceneObject(this);
//		pointLight.addScript(Position3D::new).set(new Vector3(0.0f, -10.0f, 0.0f));
//		pointLight.addScript(PointLight3D::new);
//		SceneObject cube = new SceneObject(this);
//		cube.addScript(Position3D::new);
//		cube.addScript(Rotation3D::new);
//		cube.addScript(Scale3D::new).set(new Vector3(5.0f, 1.0f, 5.0f));
//		cube.addScript(Transform3D::new);
//		//cube.addScript(CubeRenderer::new);
//		cube.addScript(StaticBody3D::new);
//		this.sceneObjects.add(object);
//		this.sceneObjects.add(camera);
//		this.sceneObjects.add(pointLight);
//		this.sceneObjects.add(cube);
	}

	public SceneObject createObject() {
		SceneObject sceneObject = new SceneObject(this);
		this.sceneObjects.add(sceneObject);
		return sceneObject;
	}

	public void process() {
		long time = System.nanoTime();
		this.sceneObjects.forEach(sceneObject -> sceneObject.process((this.previousTime - time) / 1_000_000_000.0f));
		this.previousTime = time;
	}
}
