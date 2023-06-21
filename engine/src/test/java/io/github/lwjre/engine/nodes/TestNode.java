package io.github.lwjre.engine.nodes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestNode {

	@Test
	public void testAddChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		Assertions.assertEquals(parent, child.getParent());
	}

	@Test
	public void testNullChild() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild(null));
	}

	@Test
	public void testAlreadyHasParent() {
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
		Assertions.assertEquals(parent, child.getParent());
		Assertions.assertEquals(child, parent.getChild("Key"));
	}

	@Test
	public void testNullChildWithKey() {
		Node parent = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild("Key", null));
	}

	@Test
	public void testAddChildWithNullKey() {
		Node parent = new Node();
		Node child = new Node();
		Assertions.assertThrows(IllegalArgumentException.class, () -> parent.addChild(null, child));
	}

	@Test
	public void testAlreadyHasParentWithKey() {
		Node parent1 = new Node();
		Node parent2 = new Node();
		Node child = new Node();
		parent1.addChild(child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent2.addChild("Key", child));
	}

	@Test
	public void testAlreadyHasKey() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();
		parent.addChild("Key", child1);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.addChild("Key", child2));
	}

	@Test
	public void testHasChild() {
		Node parent = new Node();
		parent.addChild("Key", new Node());
		Assertions.assertTrue(parent.hasChild("Key"));
		Assertions.assertFalse(parent.hasChild("No"));
	}

	@Test
	public void testQueueChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.queueChild(child);
		Assertions.assertNull(child.getParent());
		parent.process(0.0f);
		Assertions.assertEquals(parent, child.getParent());
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
		Assertions.assertEquals(parent, child.getParent());
		parent.removeChild(child);
		Assertions.assertNull(child.getParent());
	}

	@Test
	public void testRemoveNullChild() {
		Node parent = new Node();
		parent.addChild(new Node());
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
		Assertions.assertEquals(parent, child.getParent());
		parent.removeChild("Key");
		Assertions.assertNull(child.getParent());
	}

	@Test
	public void testRemoveChildNoKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("Key", child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.removeChild("No"));
	}

	@Test
	public void testQueueRemove() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		child.queueRemove();
		Assertions.assertEquals(parent, child.getParent());
		parent.process(0.0f);
		Assertions.assertNull(child.getParent());
	}

	@Test
	public void testChildCount() {
		Node node = new Node();
		node.addChild(new Node());
		node.addChild(new Node());
		node.addChild(new Node());
		Assertions.assertEquals(3, node.getChildCount());
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
		List<Node> expected = List.of(child1, child2, child3);
		List<Node> actual = parent.getChildren().toList();
		Assertions.assertEquals(expected.size(), actual.size());
		Assertions.assertTrue(actual.containsAll(expected));
	}

	@Test
	public void testIsInsideTree() {
		Node parent = new Node();
		Node child = new Node();
		Assertions.assertFalse(child.isInsideTree());
		parent.addChild(child);
		SceneTree.changeScene(parent);
		SceneTree.process();
		Assertions.assertTrue(child.isInsideTree());
		parent.removeChild(child);
		Assertions.assertFalse(child.isInsideTree());
	}
}
