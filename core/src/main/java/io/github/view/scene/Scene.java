package io.github.view.scene;

import java.util.ArrayList;

public final class Scene {

	private final ArrayList<SceneObject> sceneObjects = new ArrayList<>();

	private long previousTime = System.nanoTime();

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
