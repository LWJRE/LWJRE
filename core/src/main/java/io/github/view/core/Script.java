package io.github.view.core;

import io.github.view.scene.SceneObject;

public class Script {

	protected final SceneObject object;
	private State state = State.NEW;

	public Script(SceneObject object) {
		this.object = object;
	}

	public void onStart() {
		this.state = State.READY;
	}

	public void onUpdate(float time) {

	}

	public void onExit() {
		this.state = State.REMOVED;
	}

	public final void queueRemove() {
		if(this.state != State.REMOVED)
			this.state = State.TO_BE_REMOVED;
	}

	public final State getState() {
		return this.state;
	}

	public final boolean wasRemoved() {
		return this.state == State.REMOVED;
	}

	public enum State {
		NEW,
		READY,
		TO_BE_REMOVED,
		REMOVED
	}
}
