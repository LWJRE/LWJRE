package gamma.engine.tree;

import gamma.engine.input.InputEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public class Node {

	private Node parent = null;
	private final ArrayList<Node> queue = new ArrayList<>();
	private final HashMap<String, Node> children = new HashMap<>();
	private State state = State.OUTSIDE_TREE;

	protected void onEnter() {

	}

	protected void onUpdate(float delta) {

	}

	protected void onExit() {

	}

	protected final void process(float delta) {
		this.children.values().removeIf(child -> {
			if(child.state == State.REMOVING) {
				child.exit();
				child.parent = null;
				return true;
			}
			child.process(delta);
			return false;
		});
		this.queue.forEach(this::addChild);
		this.queue.clear();
		this.onUpdate(delta);
	}

	protected final void enter() {
		this.children.forEach((key, child) -> child.enter());
		this.state = State.INSIDE_TREE;
		this.onEnter();
	}

	public final void input(InputEvent event) {
		this.children.forEach((key, child) -> child.input(event));
		this.onInput(event);
	}

	protected void onInput(InputEvent event) {

	}

	public final void addChild(Node node) {
		if(node == null) {
			throw new IllegalArgumentException();
		} else if(node.parent != null) {
			throw new IllegalStateException();
		} else {
			String key = node.getClass().getSimpleName();
			if(this.children.containsKey(key)) {
				int index = 2;
				while(this.children.containsKey(key + index)) {
					index ++;
				}
				key = key + index;
			}
			this.children.put(key, node);
			node.parent = this;
			if(this.state == State.INSIDE_TREE) {
				node.enter();
			}
		}
	}

	public final void addChild(String key, Node node) {
		if(node == null) {
			throw new IllegalArgumentException();
		} else if(key == null) {
			throw new IllegalArgumentException();
		} else if(this.children.containsKey(key)) {
			throw new IllegalStateException();
		} else {
			this.children.put(key, node);
			node.parent = this;
			if(this.state == State.INSIDE_TREE) {
				node.enter();
			}
		}
	}

	public final void queueChild(Node node) {
		if(node == null) {
			throw new IllegalArgumentException();
		} else {
			this.queue.add(node);
		}
	}

	protected final void exit() {
		this.children.forEach((key, child) -> child.exit());
		this.onExit();
		this.state = State.OUTSIDE_TREE;
	}

	public final void removeChild(Node node) {
		if(node == null) {
			throw new IllegalArgumentException();
		} else if(node.parent != this) {
			throw new IllegalStateException();
		} else {
			node.exit();
			this.children.values().remove(node);
			node.parent = null;
		}
	}

	public final void removeChild(String name) {
		if(this.children.containsKey(name)) {
			Node node = this.children.get(name);
			node.exit();
			this.children.remove(name);
			node.parent = null;
		} else {
			throw new IllegalStateException();
		}
	}

	public final void queueRemove() {
		this.state = State.REMOVING;
	}

	public final Node getParent() {
		return this.parent;
	}

	public final int getChildCount() {
		return this.children.size();
	}

	public final Optional<Node> getChild(String name) {
		return Optional.ofNullable(this.children.get(name));
	}

	public final Stream<Node> getChildren() {
		return this.children.values().stream();
	}

	public final boolean isInsideTree() {
		return this.state != State.OUTSIDE_TREE;
	}

	public final void editorProcess() {
		this.children.forEach((key, child) -> child.editorProcess());
		this.onEditorProcess();
	}

	protected void onEditorProcess() {

	}

	private enum State {
		INSIDE_TREE,
		OUTSIDE_TREE,
		REMOVING
	}
}
