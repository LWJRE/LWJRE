package io.github.view.core;

public class Component {

	public final Entity entity;
	private volatile State processState = State.NEW;
	private volatile State renderingState = State.NEW;

	public Component(Entity entity) {
		this.entity = entity;
	}

	public void onStart() {
		this.processState = State.READY;
	}

	public void onUpdate() {

	}

	public void onExit() {
		this.processState = State.REMOVED;
	}

	public void prepareRendering() {
		this.renderingState = State.READY;
	}

	public void render() {

	}

	public void cleanUpRendering() {
		this.renderingState = State.REMOVED;
	}

	public final void queueRemove() {
		if(this.processState != State.REMOVED)
			this.processState = State.TO_BE_REMOVED;
		if(this.renderingState != State.REMOVED)
			this.renderingState = State.TO_BE_REMOVED;
	}

	public final State getProcessState() {
		return this.processState;
	}

	public final State getRenderingState() {
		return this.renderingState;
	}

	public final boolean wasRemoved() {
		return this.processState == State.REMOVED && this.renderingState == State.REMOVED;
	}

	public enum State {
		NEW,
		READY,
		TO_BE_REMOVED,
		REMOVED
	}
}
