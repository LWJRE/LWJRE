package io.github.view.core;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class Node implements Iterable<Node> {

	private final CopyOnWriteArrayList<Node> children = new CopyOnWriteArrayList<>();
	private Node parent;

	public final void addChild(Node child) {
		if(child.parent == null) {
			child.parent = this;
			this.children.add(child);
		} else {
			throw new RuntimeException("Cannot add " + child + " as a child of " + this + " because it already has parent " + child.parent);
		}
	}

	public final Node removeChild(int index) {
		this.children.get(index).parent = null;
		return this.children.remove(index);
	}

	public final void removeChild(Node child) {
		if(child.parent == this) {
			child.parent = null;
			this.children.remove(child);
		} else {
			throw new RuntimeException("Node " + child + " is not a child of " + this);
		}
	}

	public final Node getParent() {
		return this.parent;
	}

	@Override
	public final Iterator<Node> iterator() {
		return this.children.iterator();
	}
}
