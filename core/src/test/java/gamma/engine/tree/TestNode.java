package gamma.engine.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestNode {

	@Test
	public void testAddChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		Assertions.assertEquals(child.getParent(), parent);
	}

	@Test
	public void testAddNullChild() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild(null));
	}

	@Test
	public void testAddChildAlreadyHasParent() {
		Node parent1 = new Node();
		Node parent2 = new Node();
		Node child = new Node();
		parent1.addChild(child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent2.addChild(child));
	}

	@Test
	public void testAddChildWithKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("Key", child);
		Assertions.assertTrue(parent.hasChild("Key"));
		Assertions.assertEquals(parent.getChild("Key"), child);
	}

	@Test
	public void testAddNullChildWithKey() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild("Key", null));
	}

	@Test
	public void testAddChildWithNullKey() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild(null, new Node()));
	}

	@Test
	public void testAddChildWithKeyAlreadyHasParent() {
		Node parent1 = new Node();
		Node parent2 = new Node();
		Node child = new Node();
		parent1.addChild("Key", child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent2.addChild("Key", child));
	}

	@Test
	public void testAddChildWithKeyAlreadyHasKey() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();
		parent.addChild("Key", child1);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.addChild("Key", child2));
	}

	@Test
	public void testQueueChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.queueChild(child);
		Assertions.assertNotEquals(child.getParent(), parent);
		parent.process(0.0f);
		Assertions.assertEquals(child.getParent(), parent);
	}

	@Test
	public void testQueueNullChild() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.queueChild(null));
	}

	@Test
	public void testRemoveChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		Assertions.assertEquals(child.getParent(), parent);
		parent.removeChild(child);
		Assertions.assertNotEquals(child.getParent(), parent);
	}

	@Test
	public void testRemoveNullChild() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.removeChild((Node) null));
	}

	@Test
	public void testRemoveChildOtherParent() {
		Node parent1 = new Node();
		Node parent2 = new Node();
		Node child = new Node();
		parent1.addChild(child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent2.removeChild(child));
	}

	@Test
	public void testRemoveChildWithKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("Key", child);
		Assertions.assertTrue(parent.hasChild("Key"));
		Assertions.assertEquals(parent.getChild("Key"), child);
		parent.removeChild("Key");
		Assertions.assertFalse(parent.hasChild("Key"));
		Assertions.assertNotEquals(parent.getChild("Key"), child);
	}

	@Test
	public void testRemoveChildWithOtherKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("Key", child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.removeChild("Other"));
	}

	@Test
	public void testQueueRemove() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		child.queueRemove();
		Assertions.assertEquals(child.getParent(), parent);
		parent.process(0.0f);
		Assertions.assertNotEquals(child.getParent(), parent);
	}

	@Test
	public void testChildCount() {
		Node parent = new Node();
		parent.addChild(new Node());
		parent.addChild(new Node());
		parent.addChild(new Node());
		Assertions.assertEquals(3, parent.getChildCount());
	}

	@Test
	public void testGetChildren() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();
		Node child3 = new Node();
		parent.addChild(child1);
		parent.addChild(child2);
		parent.addChild(child3);
		List<Node> children = parent.getChildren().toList();
		Assertions.assertTrue(children.contains(child1));
		Assertions.assertTrue(children.contains(child2));
		Assertions.assertTrue(children.contains(child3));
	}

	@Test
	public void testIsInsideTree() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		Assertions.assertFalse(child.isInsideTree());
		SceneTree.changeScene(parent);
		SceneTree.process();
		Assertions.assertTrue(child.isInsideTree());
	}

	private static class MockNode extends Node {

		private boolean calledEnter = false;
		private boolean calledUpdate = false;
		private boolean calledExit = false;
		private boolean calledEditorProcess = false;

		@Override
		protected void onEnter() {
			super.onEnter();
			this.calledEnter = true;
		}

		@Override
		protected void onUpdate(float delta) {
			super.onUpdate(delta);
			this.calledUpdate = true;
		}

		@Override
		protected void onExit() {
			super.onExit();
			this.calledExit = true;
		}

		@Override
		protected void onEditorProcess() {
			super.onEditorProcess();
			this.calledEditorProcess = true;
		}
	}

	@Test
	public void testOnEnter() {
		SceneTree.changeScene(new Node());
		SceneTree.process();
		MockNode mockNode = new MockNode();
		SceneTree.getRoot().addChild(mockNode);
		Assertions.assertTrue(mockNode.calledEnter);
	}

	@Test
	public void testNotOnEnter() {
		Node parent = new Node();
		MockNode child = new MockNode();
		parent.addChild(child);
		Assertions.assertFalse(child.calledEnter);
	}

	@Test
	public void testOnUpdate() {
		SceneTree.changeScene(new Node());
		SceneTree.process();
		MockNode mockNode = new MockNode();
		SceneTree.getRoot().addChild(mockNode);
		Assertions.assertFalse(mockNode.calledUpdate);
		SceneTree.process();
		Assertions.assertTrue(mockNode.calledUpdate);
	}

	@Test
	public void testNotOnUpdate() {
		Node parent = new Node();
		MockNode child = new MockNode();
		parent.addChild(child);
		SceneTree.process();
		Assertions.assertFalse(child.calledUpdate);
	}

	@Test
	public void testOnExit() {
		SceneTree.changeScene(new Node());
		SceneTree.process();
		MockNode mockNode = new MockNode();
		SceneTree.getRoot().addChild(mockNode);
		SceneTree.process();
		mockNode.queueRemove();
		Assertions.assertFalse(mockNode.calledExit);
		SceneTree.process();
		Assertions.assertTrue(mockNode.calledExit);
	}

	@Test
	public void testOnEditorProcess() {
		Node parent = new Node();
		MockNode child = new MockNode();
		parent.addChild(child);
		parent.editorProcess();
		Assertions.assertTrue(child.calledEditorProcess);
	}
}
