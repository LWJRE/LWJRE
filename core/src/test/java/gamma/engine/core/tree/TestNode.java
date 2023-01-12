package gamma.engine.core.tree;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

public class TestNode {

	@Test
	public void testAddChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		Assertions.assertEquals(parent, child.getParent());
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
	public void testAddChildKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("test", child);
		Assertions.assertEquals(parent, child.getParent());
		Assertions.assertEquals(child, parent.getChild("test").orElse(null));
	}

	@Test
	public void testAlreadyHasKey() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();
		parent.addChild("test", child1);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.addChild("test", child2));
	}

	@Test
	public void testHasChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("test", child);
		Assertions.assertTrue(parent.hasChild("test"));
	}

	@Test
	public void testRemoveChild() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild(child);
		parent.removeChild(child);
		Assertions.assertNull(child.getParent());
	}

	@Test
	public void testIsNotChild() {
		Node parent = new Node();
		Node child = new Node();
		Assertions.assertThrows(IllegalStateException.class, () -> parent.removeChild(child));
	}

	@Test
	public void testRemoveChildKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("test", child);
		parent.removeChild("test");
		Assertions.assertFalse(parent.hasChild("test"));
	}

	@Test
	public void testDoesNotHaveKey() {
		Node parent = new Node();
		Node child = new Node();
		parent.addChild("test", child);
		Assertions.assertThrows(IllegalStateException.class, () -> parent.removeChild("key"));
	}

	@Test
	public void testGetChildOfType() {
		Node parent = new Node();
		Node2 child = new Node2();
		parent.addChild(child);
		Assertions.assertEquals(child, parent.getChildOfType(Node1.class).orElse(null));
	}

	@Test
	public void testGetChildren() {
		Node parent = new Node();
		Node child1 = new Node();
		Node child2 = new Node();
		parent.addChild(child1);
		parent.addChild(child2);
		Assertions.assertEquals(Set.of(child1, child2), parent.getChildren().collect(Collectors.toSet()));
	}

	@Test
	public void testGetChildrenOfType() {
		Node parent = new Node();
		Node child1 = new Node();
		Node2 child2 = new Node2();
		Node1 child3 = new Node1();
		parent.addChild(child1);
		parent.addChild(child2);
		parent.addChild(child3);
		Assertions.assertEquals(Set.of(child2, child3), parent.getChildrenOfType(Node1.class).collect(Collectors.toSet()));
	}

	private static class Node1 extends Node {

	}

	private static class Node2 extends Node1 {

	}
}
