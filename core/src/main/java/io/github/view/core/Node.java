package io.github.view.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	// TODO: Make a better add child

	public final void addChild(Node node) {
		this.children.add(node);
		node.parent = this;
	}

	// TODO: Make a better get children

	public final <T extends Node> Optional<T> getChild(Class<T> type) {
		return this.children.stream().filter(child -> child.getClass().isAssignableFrom(type)).findFirst().map(type::cast);
	}

	public final <T extends Node> List<T> getChildrenOfType(Class<T> type) {
		return this.children.stream().filter(child -> child.getClass().isAssignableFrom(type)).map(type::cast).toList();
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
