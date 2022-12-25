package io.github.view.core;

import java.util.ArrayList;

public class Node {

	private final ArrayList<Node> children = new ArrayList<>();
	private Node parent;

	private State currentState = State.NEW;

	public final void process(float delta) {
		this.children.removeIf(child -> child.currentState == State.REMOVED);
		this.children.forEach(child -> {
			if(child.parent == null)
				child.parent = this;
			child.process(delta);
		});
		switch(this.currentState) {
			case NEW -> {
				this.onStart();
				this.currentState = State.READY;
			}
			case READY -> this.onUpdate(delta);
			case TO_BE_REMOVED -> {
				this.onExit();
				this.currentState = State.REMOVED;
			}
		}
	}

	protected void onStart() {

	}

	protected void onUpdate(float delta) {

	}

	protected void onExit() {

	}

	public final Node getParent() {
		return this.parent;
	}

	public final void markToRemove() {
		this.currentState = State.TO_BE_REMOVED;
	}

	public enum State {
		NEW,
		READY,
		TO_BE_REMOVED,
		REMOVED
	}
}
