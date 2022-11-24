package io.github.view.core;

import java.util.concurrent.CopyOnWriteArrayList;

public class TreeNode {

	private final CopyOnWriteArrayList<TreeNode> children = new CopyOnWriteArrayList<>();
	private TreeNode parent;

	private boolean markedToRemove = false;

	public void addChild(TreeNode node) {
		if(node.parent != null)
			throw new RuntimeException("Cannot add " + node + " as a child of " + this + " because it already has parent " + node.parent);
		this.children.add(node);
		node.parent = this;
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public void process() {
		this.children.forEach(TreeNode::process);
		this.children.removeIf(node -> node.markedToRemove);
	}

	public void render() {
		this.children.forEach(TreeNode::render);
		this.children.removeIf(node -> node.markedToRemove);
	}

	public final void queueRemove() {
		this.markedToRemove = true;
	}
}
